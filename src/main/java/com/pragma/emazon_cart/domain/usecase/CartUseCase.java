package com.pragma.emazon_cart.domain.usecase;

import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.exceptions.ArticleAlreadyExistsInCartException;
import com.pragma.emazon_cart.domain.exceptions.CategoryLimitExceededException;
import com.pragma.emazon_cart.domain.exceptions.OutOfStockException;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.model.stock.Category;
import com.pragma.emazon_cart.domain.spi.CartPersistencePort;
import com.pragma.emazon_cart.domain.spi.FeignClientPort;
import com.pragma.emazon_cart.domain.model.Cart;
import com.pragma.emazon_cart.domain.spi.TokenServicePort;
import com.pragma.emazon_cart.domain.utils.Constants;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
    public class CartUseCase implements CartServicePort {

        private static final Short CATEGORY_LIMIT = 3;

        private final CartPersistencePort cartPersistencePort;
        private final TokenServicePort tokenServicePort;
        private final FeignClientPort feignClientPort;

        @Override
        public void addItemsToCart(AddArticles addArticles) {

            List<Article> newArticles = feignClientPort.getArticlesByIds(addArticles.getArticleIds());

            List<Integer> outOfStockArticleIds = validateArticleStock(newArticles, addArticles.getArticleAmounts());

            if (!outOfStockArticleIds.isEmpty()) {
                List<LocalDate> restockDates = feignClientPort.getRestockDates(outOfStockArticleIds);
                handleOutOfStockArticles(outOfStockArticleIds, restockDates);
            }

            Integer userId = tokenServicePort.extractUserIdFromToken();

            Optional<Cart> optionalCart = cartPersistencePort.findCartByUserId(userId);

            Cart cart = optionalCart.orElseGet(() -> Cart.builder()
                    .cartUserId(userId)
                    .cartArticleList(new ArrayList<>())
                    .cartAmountList(new ArrayList<>())
                    .cartCreationDate(LocalDate.now())
                    .cartLastUpdateDate(LocalDate.now())
                    .build());

            List<Integer> existingArticleIds = cart.getCartArticleList()
                    .stream()
                    .map(Article::getArticleId)
                    .toList();

            List<Article> existingArticles = feignClientPort.getArticlesByIds(existingArticleIds);

            validateArticlesByCategory(existingArticles, newArticles);

            boolean articleAlreadyExistsInCart = newArticles.stream()
                    .anyMatch(article -> cart.getCartArticleList().stream()
                            .anyMatch(cartArticle -> cartArticle.getArticleId().equals(article.getArticleId())));

            if (articleAlreadyExistsInCart) {
                throw new ArticleAlreadyExistsInCartException(Constants.ARTICLE_ALREADY_EXISTS_ERROR_MESSAGE);
            }

            if (optionalCart.isPresent()) {
                cart.getCartArticleList().addAll(newArticles);
                cart.getCartAmountList().addAll(addArticles.getArticleAmounts());
                cart.setCartLastUpdateDate(LocalDate.now());
            } else {
                cart.setCartArticleList(new ArrayList<>(newArticles));
                cart.setCartAmountList(new ArrayList<>(addArticles.getArticleAmounts()));
            }

            cartPersistencePort.saveCart(cart);
        }

    private List<Integer> validateArticleStock(List<Article> articleList, List<Integer> requestedAmounts) {

        List<Integer> outOfStockArticleIds = new ArrayList<>();

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Integer requestedAmount = requestedAmounts.get(i);

            if (requestedAmount > article.getArticleAmount()) {
                outOfStockArticleIds.add(article.getArticleId());
            }
        }

        return outOfStockArticleIds;
    }

    private void handleOutOfStockArticles(List<Integer> outOfStockArticleIds, List<LocalDate> restockDates) {

        StringBuilder message = new StringBuilder(Constants.CART_OUT_OF_STOCK_ARTICLES);

        for (int i = 0; i < outOfStockArticleIds.size(); i++) {
            Integer articleId = outOfStockArticleIds.get(i);
            LocalDate restockDate = restockDates.get(i);

            if (restockDate != null) {
                message.append(Constants.WHITE_SPACE)
                        .append(articleId)
                        .append(Constants.CART_RESTOCKING_DATE)
                        .append(restockDate);
            } else {
                message.append(Constants.CART_ARTICLE_ID)
                        .append(articleId)
                        .append(Constants.CART_NO_RESTOCKING_DATE);
            }
        }
        
        throw new OutOfStockException(message.toString());
    }


    private void validateArticlesByCategory(List<Article> existingArticles, List<Article> newArticles) {

        List<Article> allArticles = Stream.concat(existingArticles.stream(), newArticles.stream())
                .toList();

        Map<String, Long> categoryCountMap = allArticles.stream()
                .flatMap(article -> article.getArticleCategories().stream())
                .collect(Collectors.groupingBy(Category::getName, Collectors.counting()));

        categoryCountMap.forEach((category, count) -> {
            if (count > CATEGORY_LIMIT) {
                throw new CategoryLimitExceededException(category);
            }
        });
    }

}
