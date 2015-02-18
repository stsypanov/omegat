package org.omegat.util.network;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by stsypanov on 29.10.2014.
 */
public class ProxyUtilsTest {
	private static final Logger logger = Logger.getLogger(ProxyUtilsTest.class.getName());
	private String url = "https://www.mail.ru";


	@Test
	public void testConnection() throws Exception {
		URL google = new URL(url);
		List<Proxy> proxies = ProxyUtils.getProxySelector(url);

		for (Proxy proxy : proxies) {
			int responseCode = getResponseCode(google, proxy);

			logger.info(proxy.toString() + ' ' + responseCode);
		}

	}

	@Test
	public void testProxySelector() throws Exception {
		Proxy proxy = ProxyUtils.getProxy(url);

		logger.info(proxy.toString() + ' ' + getResponseCode(new URL(url), proxy));
	}

	private static int getResponseCode(URL google, Proxy proxy) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) google.openConnection(proxy);
		return urlConnection.getResponseCode();
	}
}
