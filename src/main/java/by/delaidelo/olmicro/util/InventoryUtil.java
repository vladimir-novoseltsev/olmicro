package by.delaidelo.olmicro.util;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
public class InventoryUtil {

    private static final int PORT = 9080;
    private static final String PROTOCOL = "http";
    private static final String SYSTEM_PROPERTIES = "/system/properties";

    public static JsonObject getProperties(String hostname) {
        Client client = ClientBuilder.newClient();
        URI propURI = InventoryUtil.buildUri(hostname);
        return client.target(propURI).request().get(JsonObject.class);
    }

    public static boolean responseOk(String hostname) {
        try {
            URL target = new URL(buildUri(hostname).toString());
            HttpURLConnection http = (HttpURLConnection) target.openConnection();
            http.setConnectTimeout(50);
            int response = http.getResponseCode();
            return response == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static URI buildUri(String hostname) {
        return UriBuilder.fromUri(SYSTEM_PROPERTIES)
                .host(hostname)
                .port(PORT)
                .scheme(PROTOCOL)
                .build();
    }

}