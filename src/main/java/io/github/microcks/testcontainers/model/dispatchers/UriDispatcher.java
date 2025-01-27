package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;

import java.util.*;

public class UriDispatcher implements Dispatcher {

    private final Set<String> queryParams;

    private final Set<String> pathParams;

    private UriDispatcher(Set<String> queryParams, Set<String> pathParams) {
        this.queryParams = queryParams;
        this.pathParams = pathParams;
    }

    @Override
    public Type getType() {
        boolean queryEmpty = !queryParamsExist();
        boolean pathEmpty = !pathParamsExist();

        if (queryEmpty && pathEmpty) {
            return Type.EMPTY;
        } else if (queryEmpty) {
            return Type.URI_PARTS;
        } else if (pathEmpty) {
            return Type.URI_PARAMS;
        } else {
            return Type.URI_ELEMENTS;
        }
    }

    private boolean queryParamsExist() {
        return queryParams != null && !queryParams.isEmpty();
    }

    private boolean pathParamsExist() {
        return pathParams != null && !pathParams.isEmpty();
    }

    @Override
    public String getRules() {
        List<String> list = new ArrayList<>();
        if (pathParamsExist()) {
            list.add(String.join(" && ", pathParams));
        }
        if (queryParamsExist()) {
            list.add(String.join(" && ", queryParams));
        }

        return String.join(" ?? ", list);
    }

    public static class Builder {
        private Set<String> queryParams = new LinkedHashSet<>();
        private Set<String> pathParams = new LinkedHashSet<>();

        public Builder queryParam(String queryParam) {
            this.queryParams.add(queryParam);
            return this;
        }

        public Builder pathParam(String pathParam) {
            this.pathParams.add(pathParam);
            return this;
        }

        public UriDispatcher build() {
            return new UriDispatcher(queryParams, pathParams);
        }
    }
}
