package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import io.github.microcks.testcontainers.model.dispatchers.UriDispatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UriDispatcherTest {

    @Test
    public void testUriParamsType() {
        UriDispatcher uriDispatcher = Dispatcher.uri().pathParam("id").pathParam("goose").build();
        assertEquals(Dispatcher.Type.URI_PARTS, uriDispatcher.getType());
        assertEquals("id && goose", uriDispatcher.getRules());
    }

    @Test
    public void testUriPartsType() {
        UriDispatcher uriDispatcher = Dispatcher.uri().queryParam("id").queryParam("goose").build();
        assertEquals(Dispatcher.Type.URI_PARAMS, uriDispatcher.getType());
        assertEquals("id && goose", uriDispatcher.getRules());
    }

    @Test
    public void testUriElementsType() {
        UriDispatcher uriDispatcher = Dispatcher.uri().queryParam("id").queryParam("goose").pathParam("name").pathParam("duck").build();
        assertEquals(Dispatcher.Type.URI_ELEMENTS, uriDispatcher.getType());
        assertEquals("name && duck ?? id && goose", uriDispatcher.getRules());
    }
}
