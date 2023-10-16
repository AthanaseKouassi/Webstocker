package com.webstocker.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 */
public class HeaderUtil {

    private static String PARAMS="X-webstockerApp-params";
    private static String ALERT="X-webstockerApp-alert";

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(ALERT, message);
        headers.add(PARAMS, param);
        headers.add("Access-Control-Expose-Headers",ALERT);
        headers.add("Access-Control-Expose-Headers",PARAMS);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("webstockerApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("webstockerApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("webstockerApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-webstockerApp-error", "error." + errorKey);
        headers.add("X-webstockerApp-params", entityName);
        return headers;
    }
}
