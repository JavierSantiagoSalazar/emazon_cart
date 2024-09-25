package com.pragma.emazon_cart.domain.utils;

public class Constants {



    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /* --- VALIDATION CONSTANTS: CART --- */

    public static final String DATA_LIST_CANNOT_BE_NULL = "The data send cannot be null";
    public static final String ARTICLE_ID_MUST_NOT_BE_NULL = "Article ID cannot be null";
    public static final String ARTICLE_ID_MUST_BE_POSITIVE = "Article ID must be a positive number";
    public static final String ARTICLE_AMOUNT_MUST_NOT_BE_NULL = "Article amount cannot be null";
    public static final String ARTICLE_AMOUNT_MUST_BE_POSITIVE = "Article amount must be a positive number";
    public static final String TRANSACTION_SERVICE_UNAVAILABLE = "The TransactionService is unavailable";
    public static final String STOCK_SERVICE_UNAVAILABLE = "The StockService is unavailable";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    /* --- EXCEPTIONS CONSTANTS --- */

    public static final String CATEGORY_LIMIT_EXCEEDED_PREFIX_ERROR_MESSAGE = "The limit of articles in the category '";
    public static final String CATEGORY_LIMIT_EXCEEDED_SUFFIX_ERROR_MESSAGE = "' has been exceeded. No more than 3 items can be added.";
    public static final String ERROR_PROCESSING_RESPONSE_BODY = "Error processing response body";
    public static final String INVALID_REQUEST_ERROR = "Invalid request to the endpoint: ";
    public static final String ARTICLE_ALREADY_EXISTS_ERROR_MESSAGE = "The item already exists in the cart";

    /* --- FEIGN CONSTANTS --- */

    public static final String STOCK_MICROSERVICE_NAME = "emazon-stock";
    public static final String TRANSACTION_MICROSERVICE_NAME = "emazon-transaction";
    public static final String JWT_IS_EMPTY_ERROR = "The JWT is empty";
    public static final String UNAUTHORIZED_ERROR = "Unauthorized for the resource: ";
    public static final String COMMUNICATING_SERVER_ERROR = "Error trying to communicate the server";

    /* --- MAPSTRUCT CONSTANTS --- */

    public static final String SPRING_COMPONENT_MODEL = "spring";

    /* --- SECURITY EXCEPTIONS --- */

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access: ";

    /* --- OPENAPI CONSTANTS --- */

    public static final String ADD_TO_CART = "Add Items to Shopping Cart";

    public static final String ARTICLE_NOT_FOUND = "Article not found";
    public static final String ITEM_ADDED = "Item added to cart successfully";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String CATEGORY_LIMIT_EXCEEDED = "Category limit exceeded";
    public static final String DELETE_FROM_CART = "Delete item from cart";
    public static final String ITEM_DELETED = "Item deleted from cart";
    public static final String BUY_FROM_CART = "Buy item from cart";
    public static final String ITEMS_PURCHASED = "Items purchased from cart";

    /* --- OPENAPI CONSTANTS --- */

    public static final String OPEN_API_TITLE = "Emazon Cart API";
    public static final String OPEN_API_TERMS = "http://swagger.io/terms/";
    public static final String OPEN_API_LICENCE_NAME = "Apache 2.0";
    public static final String OPEN_API_LICENCE_URL = "http://springdoc.org";
    public static final String OPEN_API_APP_DESCRIPTION = "${appDescription}";
    public static final String OPEN_API_APP_VERSION = "${appVersion}";
    public static final String OPEN_API_SWAGGER_UI_HTML = "/swagger-ui/**";
    public static final String OPEN_API_SWAGGER_UI = "/swagger-ui/";
    public static final String OPEN_API_V3_API_DOCS = "/v3/api-docs/**";

    /* --- JWT CONSTANTS --- */

    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_USER_ID = "userId";
    public static final String INVALID_TOKEN = "Token invalid, not Authorized";

    /* --- CART REQUEST CONSTANTS --- */

    public static final String CART_URL = "/cart/";
    public static final String CART_BUY_URL = "/cart/buy";


    public static final String WHITE_SPACE = " ";
    public static final String CART_OUT_OF_STOCK_ARTICLES = "The following items are out of stock: ";
    public static final String CART_RESTOCKING_DATE = ", Restocking date: ";
    public static final String CART_ARTICLE_ID = "Article Id ";
    public static final String CART_NO_RESTOCKING_DATE = ", No restocking date available";

    /* --- ROLES --- */

    public static final String ROLE_CLIENT = "CLIENT";
}
