package org.omegat.util.network;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * Created by rad1kal on 29.10.2014.
 */
public class ProxyUtilsTest {
    protected String googleURL = "https://www.google.com";

    @Test
    public void testConnection() throws Exception{
        URL google = new URL(googleURL);
        List<Proxy> proxies = ProxyUtils.getProxySelector(googleURL);

        for (Proxy proxy : proxies){
            int responseCode = getResponseCode(google, proxy);

            System.out.println(proxy.toString() + " " + responseCode);
        }

    }

    @Test
    public void testProxySelector() throws Exception {
        Proxy proxy = ProxyUtils.getProxy(googleURL);

        System.out.println(proxy.toString() + " " + getResponseCode(new URL(googleURL), proxy));
    }

    private int getResponseCode(URL google, Proxy proxy) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) google.openConnection(proxy);
        return urlConnection.getResponseCode();
    }
}
