package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import io.github.microcks.testcontainers.model.dispatchers.QueryArgsDispatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryArgsDispatcherTest {

    @Test
    public void testCreatesQueryArgsDispatcher() {
        QueryArgsDispatcher queryArgsDispatcher = Dispatcher.queryArgs().arg("id").arg("goose").build();
        assertEquals(Dispatcher.Type.QUERY_ARG, queryArgsDispatcher.getType());
        assertEquals("id && goose", queryArgsDispatcher.getRules());
    }

    @Test
    public void testCreatesSingleQueryArgsDispatcher() {
        QueryArgsDispatcher queryArgsDispatcher = Dispatcher.queryArgs().arg("id").build();
        assertEquals(Dispatcher.Type.QUERY_ARG, queryArgsDispatcher.getType());
        assertEquals("id", queryArgsDispatcher.getRules());
    }
}
