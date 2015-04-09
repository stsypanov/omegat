package org.omegat.util.network;

import com.btr.proxy.search.ProxySearch;
import com.btr.proxy.util.PlatformUtil;
import org.omegat.util.Preferences;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyUtils {
	private static final Logger logger = Logger.getLogger(ProxyUtils.class.getName());

	public static final String URL = "http://www.mail.ru";
	private static List<Proxy> proxies;

	public static List<Proxy> getProxySelector(String url) throws URISyntaxException {
		if (proxies == null) {
			ProxySearch proxySearch = new ProxySearch();

			if (PlatformUtil.getCurrentPlattform() == PlatformUtil.Platform.WIN) {
				proxySearch.addStrategy(ProxySearch.Strategy.IE);
				proxySearch.addStrategy(ProxySearch.Strategy.FIREFOX);
				proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
			} else if (PlatformUtil.getCurrentPlattform() == PlatformUtil.Platform.LINUX) {
				proxySearch.addStrategy(ProxySearch.Strategy.GNOME);
				proxySearch.addStrategy(ProxySearch.Strategy.KDE);
				proxySearch.addStrategy(ProxySearch.Strategy.FIREFOX);
			} else {
				proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
			}

			ProxySelector proxySelector = proxySearch.getProxySelector();

//            ProxySelector.setDefault(proxySelector);
			proxies = proxySelector.select(new URI(url));
		}
		return proxies;
	}

	public static Proxy getProxy(String url) throws URISyntaxException {
		if (!isProxySettingsDefined()) {
			return Proxy.NO_PROXY;
		} else {
			return pickProxy(url);
		}

	}

	private static Proxy pickProxy(String url) throws URISyntaxException {
		List<Proxy> proxyList = getProxySelector(url);
		for (Proxy proxy : proxyList) {
			if (testConnection(proxy)) {
				return proxy;
			}
		}
		return Proxy.NO_PROXY;
	}

	private static boolean testConnection(Proxy proxy) {
		try {
			URL url = new URL(URL);
			Proxy.Type type = proxy.type();
			switch (type) {
				case HTTP: {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
					int responseCode = connection.getResponseCode();
					return responseCode >= 200 && responseCode <= 400;
				}
				case SOCKS: {

				}
				case DIRECT: {
					return true;
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to get connection", e);
			return false;
		}
		return false;
	}

	private static boolean isProxySettingsDefined() {
		try {
			List<Proxy> proxyList = getProxySelector(URL);
			return !proxyList.isEmpty() && !proxyList.get(0).equals(Proxy.NO_PROXY);
		} catch (final URISyntaxException e) {
			logger.log(Level.SEVERE, "Bad URI", e);
			return false;
		}
	}

	public static void applyProxyPreferences() {
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", Preferences.getPreference(Preferences.HTTP_PROXY_HOST));
		System.setProperty("http.proxyPort", Preferences.getPreference(Preferences.HTTP_PROXY_PORT));

		System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", Preferences.getPreference(Preferences.HTTP_PROXY_HOST));
		System.setProperty("https.proxyPort", Preferences.getPreference(Preferences.HTTP_PROXY_PORT));

	}
}
