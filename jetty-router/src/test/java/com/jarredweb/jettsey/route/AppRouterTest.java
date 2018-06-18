package com.jarredweb.jettsey.route;

import org.eclipse.jetty.server.Request;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppRouterTest {

    private final AppRoutes router = new AppRoutes();

    public AppRouterTest() {
        router.addHandler(new AppRoute("/book", "post", "application/json", "application/json"), null);
        router.addHandler(new AppRoute("/book/:id", "get", "application/json", ""), null);
        router.addHandler(new AppRoute("/book", "get", "application/json", ""), null);
        router.addHandler(new AppRoute("/book/:id/", "put", "application/json", "application/json"), null);
        router.addHandler(new AppRoute("/book/:id/", "delete", "application/json", ""), null);
        router.addHandler(new AppRoute("/book/:id/author/:uid/name", "get", "application/json", ""), null);
        router.addHandler(new AppRoute("/book/:id/author/:uid/address/:city/", "get", "application/json", ""), null);
    }

    @Test
    public void testMatchBookAuthorNameRoute() {
        AppRoute incoming = new AppRoute("/book/1234/author/6789/name", "get", "application/json", "");
        Request request = new MockRoute(null, null, incoming);
        AppRoute match = router.match(request);
        assertTrue(match != null);
        assertEquals("Expecting '1234'", "1234", match.pathParams.get(":id"));
        assertEquals("Expecting '6789'", "6789", match.pathParams.get(":uid"));
    }

    @Test
    public void testMatchBookAuthorCityRoute() {
        AppRoute incoming = new AppRoute("/book/9876/author/5432/address/chicago/", "get", "application/json", "");
        Request request = new MockRoute(null, null, incoming);
        AppRoute match = router.match(request);
        assertTrue(match != null);
        assertEquals("Expecting '9876'", "9876", match.pathParams.get(":id"));
        assertEquals("Expecting '5432'", "5432", match.pathParams.get(":uid"));
        assertEquals("Expecting 'chicago'", "chicago", match.pathParams.get(":city"));
    }
}
