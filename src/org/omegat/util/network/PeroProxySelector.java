package org.omegat.util.network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rad1kal on 29.10.2014.
 */
public class PeroProxySelector extends ProxySelector {
    protected ProxySelector defaultSelector;
    protected Map<SocketAddress, InnerProxy> proxies;

    public PeroProxySelector(ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
        proxies = new HashMap<>();

//        InnerProxy i = new InnerProxy(new InetSocketAddress("webcache1.example.com", 8080));
//        proxies.put(i.address(), i);
//        i = new InnerProxy(new InetSocketAddress("webcache2.example.com", 8080));
//        proxies.put(i.address(), i);
//        i = new InnerProxy(new InetSocketAddress("webcache3.example.com", 8080));
//        proxies.put(i.address(), i);
    }
    
    public List<Proxy> select(String uri) throws URISyntaxException {
        return select(new URI(uri));
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri == null){
            throw new IllegalArgumentException("URI must not be null");
        }

        String protocol = uri.getScheme();
        if ("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol)) {
            ArrayList<Proxy> list = new ArrayList<>();
            for (InnerProxy p : proxies.values()) {
                list.add(p.toProxy());
            }
            return list;
        }

        /*
         * Not HTTP or HTTPS (could be SOCKS or FTP)
         * defer to the default selector.
         */
        if (defaultSelector != null) {
            return defaultSelector.select(uri);
        } else {
            ArrayList<Proxy> list = new ArrayList<>();
            list.add(Proxy.NO_PROXY);
            return list;
        }
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        if (uri == null || sa == null || ioe == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }
        InnerProxy proxy = proxies.get(sa);
        if (proxy != null) {
            if (proxy.failed() >= 3) {
                proxies.remove(sa);
            }
        } else if (defaultSelector != null) {
            defaultSelector.connectFailed(uri, sa, ioe);
        }
    }

    protected static class InnerProxy {
        protected Proxy proxy;
        protected SocketAddress addr;
        // How many times did we fail to reach this proxy?
        int failedCount = 0;

        InnerProxy(InetSocketAddress a) {
            addr = a;
            proxy = new Proxy(Proxy.Type.HTTP, a);
        }

        SocketAddress address() {
            return addr;
        }

        Proxy toProxy() {
            return proxy;
        }

        int failed() {
            return ++failedCount;
        }
    }
}
