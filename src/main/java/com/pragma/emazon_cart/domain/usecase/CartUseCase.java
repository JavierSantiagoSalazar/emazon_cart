package com.pragma.emazon_cart.domain.usecase;

import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.exceptions.ArticleAlreadyExistsInCartException;
import com.pragma.emazon_cart.domain.exceptions.ArticleNotFoundException;
import com.pragma.emazon_cart.domain.exceptions.CartNotFoundException;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class CartUseCase implements CartServicePort {

    private static final Short CATEGORY_LIMIT = 3;
    private static final int START_INDEX = 0;
    private static final int REMOVAL_INDEX = -1;
    private static final int ARTICLE_ID_INDEX = 0;

    private final CartPersistencePort cartPersistencePort;
    private final TokenServicePort tokenServicePort;
    private final FeignClientPort feignClientPort;

    @Override
    public void addItemsToCart(AddArticles addArticles) {

        List<Article> newArticles = fetchNewArticles(addArticles.getArticleIds());

        List<Integer> outOfStockArticleIds = validateArticleStock(newArticles, addArticles.getArticleAmounts());

        handleOutOfStockArticles(outOfStockArticleIds);

        Integer userId = tokenServicePort.extractUserIdFromToken();

        Optional<Cart> optionalCart = cartPersistencePort.findCartByUserId(userId);

        Cart cart = optionalCart.orElseGet(() -> createNewCart(userId));

        validateArticlesByCategory(fetchExistingArticles(cart), newArticles);

        checkIfArticlesAlreadyInCart(cart, newArticles);

        updateCartWithNewArticles(cart, newArticles, addArticles.getArticleAmounts(), optionalCart.isPresent());

        cartPersistencePort.saveCart(cart);
    }

    @Override
    public void deleteItemsFromCart(List<Integer> articlesIds) {

        Integer userId = tokenServicePort.extractUserIdFromToken();

        Cart cart = findUserCart(userId);

        validateArticlesExistInCart(cart, articlesIds);

        removeArticlesAndAmounts(cart, articlesIds);
        updateCartLastUpdateDate(cart);

        cartPersistencePort.saveCart(cart);
    }

    private void updateCartLastUpdateDate(Cart cart) {
        cart.setCartLastUpdateDate(LocalDate.now());
    }

    private void validateArticlesExistInCart(Cart cart, List<Integer> articlesIds) {
        Set<Integer> articleIdSet = cart.getCartArticleList()
                .stream()
                .map(Article::getArticleId)
                .collect(Collectors.toSet());

        if (!articleIdSet.containsAll(articlesIds)) {
            throw new ArticleNotFoundException(Constants.ARTICLES_NOT_FOUND);
        }
    }

    private Cart findUserCart(Integer userId) {
        return cartPersistencePort.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(Constants.CART_NOT_FOUND_ERROR_MESSAGE + userId));
    }

    private void removeArticlesAndAmounts(Cart cart, List<Integer> articleIdsToRemove) {

        List<Integer> articleIdList = cart.getCartArticleList()
                .stream()
                .map(Article::getArticleId)
                .toList();

        List<Article> updatedArticleList = new ArrayList<>(cart.getCartArticleList());
        List<Integer> updatedAmountList = new ArrayList<>(cart.getCartAmountList());

        for (int i = articleIdsToRemove.size() - 1; i >= START_INDEX; i--) {
            Integer articleId = articleIdsToRemove.get(i);
            int index = articleIdList.indexOf(articleId);
            if (index != REMOVAL_INDEX) {
                updatedArticleList.remove(index);
                updatedAmountList.remove(index);
            }
        }

        cart.setCartArticleList(updatedArticleList);
        cart.setCartAmountList(updatedAmountList);
    }

    private List<Article> fetchExistingArticles(Cart cart) {

        List<Integer> existingArticleIds = cart.getCartArticleList()
                .stream()
                .map(Article::getArticleId)
                .toList();
        return feignClientPort.getArticlesByIds(existingArticleIds);
    }

    private void checkIfArticlesAlreadyInCart(Cart cart, List<Article> newArticles) {

        boolean articleAlreadyExistsInCart = newArticles.stream()
                .anyMatch(article -> cart.getCartArticleList().stream()
                        .anyMatch(cartArticle -> cartArticle.getArticleId().equals(article.getArticleId())));

        if (articleAlreadyExistsInCart) {
            throw new ArticleAlreadyExistsInCartException(Constants.ARTICLE_ALREADY_EXISTS_ERROR_MESSAGE);
        }
    }

    private void updateCartWithNewArticles(
            Cart cart,
            List<Article> newArticles,
            List<Integer> newArticleAmounts,
            boolean cartExists
    ) {

        if (cartExists) {
            cart.getCartArticleList().addAll(newArticles);
            cart.getCartAmountList().addAll(newArticleAmounts);
        } else {
            cart.setCartArticleList(new ArrayList<>(newArticles));
            cart.setCartAmountList(new ArrayList<>(newArticleAmounts));
        }
        cart.setCartLastUpdateDate(LocalDate.now());
    }

    private List<Article> fetchNewArticles(List<Integer> articleIds) {
        return feignClientPort.getArticlesByIds(articleIds);
    }

    private void handleOutOfStockArticles(List<Integer> outOfStockArticleIds) {

        if (!outOfStockArticleIds.isEmpty()) {
            List<LocalDate> restockDates = feignClientPort.getRestockDates(outOfStockArticleIds);
            handleOutOfStockArticles(outOfStockArticleIds, restockDates);
        }
    }

    private Cart createNewCart(Integer userId) {
        return Cart.builder()
                .cartUserId(userId)
                .cartArticleList(new ArrayList<>())
                .cartAmountList(new ArrayList<>())
                .cartCreationDate(LocalDate.now())
                .cartLastUpdateDate(LocalDate.now())
                .build();
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
