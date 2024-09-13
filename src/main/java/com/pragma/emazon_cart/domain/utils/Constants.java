package com.pragma.emazon_cart.domain.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /* --- SECURITY EXCEPTIONS --- */

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access: ";

    /* --- OPENAPI CONSTANTS --- */

    public static final String ADD_TO_CART = "Add item to cart";
    public static final String ITEM_ADDED = "Item added to cart";
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
    public static final String INVALID_TOKEN = "Token invalid, not Authorized";

    /* --- CART REQUEST CONSTANTS --- */

    public static final String CART_URL = "/cart/";
    public static final String CART_BUY_URL = "/cart/buy";

    /* --- ROLES --- */

    public static final String ROLE_CLIENT = "CLIENT";
}
