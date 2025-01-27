package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class FallbackDispatcher implements Dispatcher {

    private final String response;

    private final Dispatcher dispatcher;

    private FallbackDispatcher(Dispatcher dispatcher, String response) {
        this.dispatcher = dispatcher;
        this.response = response;
    }

    @Override
    public Type getType() {
        return Type.FALLBACK;
    }

    @Override
    public String getRules() {
        LinkedHashMap<String, String> innerRules = new LinkedHashMap<>();
        innerRules.put("dispatcher", dispatcher.getType().name());
        innerRules.put("dispatcherRules", dispatcher.getRules());
        innerRules.put("fallback", response);
        return new ObjectMapper().valueToTree(innerRules).toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    private static class Builder {

        private String response;
        private Dispatcher dispatcher;

        Builder response(String response) {
            this.response = response;
            return this;
        }

        Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        FallbackDispatcher build() {
            if (response == null || dispatcher == null) {
                throw new IllegalStateException("FallbackDispatcher must have a response and a dispatcher");
            }

            return new FallbackDispatcher(dispatcher, response);
        }
    }

    public static class FallbackDispatcherBuilder {

        private final Builder builder;
        public FallbackDispatcherBuilder(String response) {
            builder = new Builder();
            builder.response(response);
        }

        public Dispatcher dispatcher(Dispatcher dispatcher) {
            builder.dispatcher(dispatcher);
            return builder.build();
        }
    }
}
