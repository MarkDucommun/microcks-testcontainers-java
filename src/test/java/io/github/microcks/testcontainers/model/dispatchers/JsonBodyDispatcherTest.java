package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import org.junit.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class JsonBodyDispatcherTest {

    @Test
    public void testBuildsEqualsDispatcherRules() {
        Dispatcher dispatcher = Dispatcher
                .jsonBody("/key/subkey")
                .equalsOperator()
                .addCase("first", "firstResponse")
                .addCase("second", "secondResponse")
                .defaultCase("defaultResponse");

        assertEquals(Dispatcher.Type.JSON_BODY, dispatcher.getType());
        assertJsonEquals(
                "{\"exp\":\"/key/subkey\",\"operator\":\"equals\",\"cases\":{\"first\":\"firstResponse\",\"second\":\"secondResponse\",\"default\":\"defaultResponse\"}}",
                dispatcher.getRules()
        );
    }

    @Test
    public void testBuildsSizeDispatcherRules() {
        Dispatcher dispatcher = Dispatcher
                .jsonBody("/key/subkey")
                .sizeOperator()
                .addCase(0, 1, "firstResponse")
                .addCase(2, 3, "secondResponse")
                .defaultCase("defaultResponse");

        assertEquals(Dispatcher.Type.JSON_BODY, dispatcher.getType());
        assertJsonEquals(
                "{\"exp\":\"/key/subkey\",\"operator\":\"size\",\"cases\":{\"[0;1]\":\"firstResponse\",\"[2;3]\":\"secondResponse\",\"default\":\"defaultResponse\"}}",
                dispatcher.getRules()
        );
    }

    @Test
    public void testBuildsRangeDispatcherRules() {
        Dispatcher dispatcher = Dispatcher
                .jsonBody("/key/subkey")
                .rangeOperator()
                .exclusiveFrom("0.1").inclusiveTo("0.2", "firstResponse")
                .defaultCase("defaultResponse");

        assertEquals(Dispatcher.Type.JSON_BODY, dispatcher.getType());
        assertJsonEquals(
                "{\"exp\":\"/key/subkey\",\"operator\":\"range\",\"cases\":{\"]0.1;0.2]\":\"firstResponse\",\"default\":\"defaultResponse\"}}",
                dispatcher.getRules()
        );
    }

    @Test
    public void testBuildsRegexpDispatcherRules() {
        Dispatcher dispatcher = Dispatcher
                .jsonBody("/key/subkey")
                .regexpOperator()
                .addCase(".*", "firstResponse")
                .defaultCase("defaultResponse");

        assertEquals(Dispatcher.Type.JSON_BODY, dispatcher.getType());
        assertJsonEquals(
                "{\"exp\":\"/key/subkey\",\"operator\":\"regexp\",\"cases\":{\".*\":\"firstResponse\",\"default\":\"defaultResponse\"}}",
                dispatcher.getRules()
        );
    }

    @Test
    public void testBuildsPresenceDispatcherRules() {
        Dispatcher dispatcher = Dispatcher
                .jsonBody("/key/subkey")
                .presenceOperator()
                .ifPresent("presentResponse")
                .ifAbsent("absentResponse");

        assertEquals(Dispatcher.Type.JSON_BODY, dispatcher.getType());
        assertJsonEquals(
                "{\"exp\":\"/key/subkey\",\"operator\":\"presence\",\"cases\":{\"found\":\"presentResponse\",\"default\":\"absentResponse\"}}",
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
