package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import org.junit.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class FallbackDispatcherTest {

    @Test
    public void testBuildsFallbackDispatcher() {
        Dispatcher innerDispatcher = Dispatcher.jsonBody("/key")
                .presenceOperator()
                .ifPresent("presentResponse")
                .ifAbsent("absentResponse");

        Dispatcher dispatcher = Dispatcher.fallback("fallbackResponse").dispatcher(innerDispatcher);

        assertEquals(Dispatcher.Type.FALLBACK, dispatcher.getType());
        assertJsonEquals(
                "{\"dispatcher\":\"JSON_BODY\",\"dispatcherRules\":\"{\\\"exp\\\":\\\"/key\\\",\\\"operator\\\":\\\"presence\\\",\\\"cases\\\":{\\\"found\\\":\\\"presentResponse\\\",\\\"default\\\":\\\"absentResponse\\\"}}\",\"fallback\":\"fallbackResponse\"}",
                dispatcher.getRules()
        );
    }

    private void assertJsonEquals(String expected, String actual) {
        try {
            assertEquals(mapper.readTree(expected), mapper.readTree(actual));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ObjectMapper mapper = new ObjectMapper();
}
