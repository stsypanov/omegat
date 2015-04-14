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
	private String httpsURL = "https://www.mail.ru";
	private String httpURL = "http://lenta.ru";


	@Test
	public void testHttpsConnection() throws Exception {
		URL google = new URL(httpsURL);
		List<Proxy> proxies = ProxyUtils.getProxySelector(httpsURL);

		for (Proxy proxy : proxies) {
			int responseCode = getResponseCode(google, proxy);

			logger.info(proxy.toString() + ' ' + responseCode);
		}
	}

	@Test
	public void testHttpConnection() throws Exception {
		URL google = new URL(httpURL);
		List<Proxy> proxies = ProxyUtils.getProxySelector(httpURL);

		for (Proxy proxy : proxies) {
			int responseCode = getResponseCode(google, proxy);

			logger.info(proxy.toString() + ' ' + responseCode);
		}
	}

	@Test
	public void testProxySelector() throws Exception {
		Proxy proxy = ProxyUtils.getProxy(httpsURL);

		logger.info(proxy.toString() + ' ' + getResponseCode(new URL(httpsURL), proxy));
	}

	private static int getResponseCode(URL google, Proxy proxy) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) google.openConnection(proxy);
		return urlConnection.getResponseCode();
	}
}
