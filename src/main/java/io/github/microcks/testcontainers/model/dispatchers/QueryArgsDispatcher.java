package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;

import java.util.LinkedHashSet;
import java.util.Set;

public class QueryArgsDispatcher implements Dispatcher {

    private final Set<String> args;

    private QueryArgsDispatcher(Set<String> args) {
        this.args = args;
    }

    @Override
    public Type getType() {
        return Type.QUERY_ARG;
    }

    private boolean argsExist() {
        return args != null && !args.isEmpty();
    }

    @Override
    public String getRules() {
        if (argsExist()) {
            return String.join(" && ", args);
        } else {
            return "";
        }
    }

    public static Builder queryArg() {
        return new Builder();
    }

    public static class Builder {
        private Set<String> args = new LinkedHashSet<>();

        public Builder arg(String queryParam) {
            this.args.add(queryParam);
            return this;
        }

        public QueryArgsDispatcher build() {
            return new QueryArgsDispatcher(args);
        }
    }
}
