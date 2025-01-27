package io.github.microcks.testcontainers.model;

import io.github.microcks.testcontainers.model.dispatchers.*;

public interface Dispatcher {

    Type getType();

    String getRules();

    static JsonBodyDispatcher.ExpressionBuilder jsonBody(String expression) {
        return new JsonBodyDispatcher.ExpressionBuilder(expression);
    }

    static FallbackDispatcher.FallbackDispatcherBuilder fallback(String response) {
        return new FallbackDispatcher.FallbackDispatcherBuilder(response);
    }

    static UriDispatcher.Builder uri() {
        return new UriDispatcher.Builder();
    }

    static ScriptDispatcher script(String script) {
        return ScriptDispatcher.script(script);
    }

    static QueryArgsDispatcher.Builder queryArgs() {
        return new QueryArgsDispatcher.Builder();
    }
    enum Type {
        FALLBACK,
        JSON_BODY,
        QUERY_ARG,
        SCRIPT,
        SEQUENCE,
        URI_ELEMENTS,
        URI_PARAMS,
        URI_PARTS,
        EMPTY
    }
}
