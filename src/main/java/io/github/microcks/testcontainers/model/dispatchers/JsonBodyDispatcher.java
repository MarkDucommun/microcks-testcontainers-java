package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonBodyDispatcher implements Dispatcher {

    private JsonBodyDispatcher(
            String expression,
            Operator operator,
            Map<String, String> cases,
            String defaultCase
    ) {
        this.expression = expression;
        this.operator = operator;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    private final String expression;
    private final Operator operator;
    private final Map<String, String> cases;
    private final String defaultCase;

    @Override
    public Type getType() {
        return Type.JSON_BODY;
    }

    @Override
    public String getRules() {
        cases.put("default", defaultCase);
        HashMap<String, Object> innerRules = new LinkedHashMap<>();
        innerRules.put("exp", expression);
        innerRules.put("operator", operator.name().toLowerCase());
        innerRules.put("cases", cases);
        return new ObjectMapper().valueToTree(innerRules).toString();
    }

    public enum Operator {
        EQUALS,
        RANGE,
        SIZE,
        REGEXP,
        PRESENCE;
    }

    private static class Builder {
        private String expression;
        private Operator operator;
        private Map<String, String> cases = new LinkedHashMap<>();
        private String defaultCase;

        protected Builder() {
        }

        public Builder expression(String expression) {
            this.expression = expression;
            return this;
        }

        public Builder operator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public Builder addCase(String key, String response) {
            cases.put(key, response);
            return this;
        }

        public Builder defaultCase(String defaultCase) {
            this.defaultCase = defaultCase;
            return this;
        }

        public JsonBodyDispatcher build() {
            if (expression == null || operator == null || defaultCase == null) {
                throw new IllegalArgumentException("Expression, operator and default case are mandatory.");
            }
            return new JsonBodyDispatcher(expression, operator, cases, defaultCase);
        }
    }

    public static class ExpressionBuilder {

        private final Builder builder;

        public ExpressionBuilder(String expression) {
            builder = new Builder();
            builder.expression(expression);
        }

        public OperatorBuilder equalsOperator() {
            builder.operator(Operator.EQUALS);
            return new OperatorBuilder(builder);
        }

        public FromRangeOperatorBuilder rangeOperator() {
            builder.operator(Operator.RANGE);
            return new FromRangeOperatorBuilder(builder);
        }

        public SizeOperatorBuilder sizeOperator() {
            builder.operator(Operator.SIZE);
            return new SizeOperatorBuilder(builder);
        }

        public OperatorBuilder regexpOperator() {
            builder.operator(Operator.REGEXP);
            return new OperatorBuilder(builder);
        }

        public PresenceBuilder presenceOperator() {
            builder.operator(Operator.PRESENCE);
            return new PresenceBuilder(builder);
        }
    }

    public static class PresenceBuilder {

        private final Builder builder;

        private PresenceBuilder(Builder builder) {
            this.builder = builder;
        }

        public AbsenceBuilder ifPresent(String response) {
            builder.addCase("found", response);
            return new AbsenceBuilder(builder);
        }
    }

    public static class AbsenceBuilder {

        private final Builder builder;

        private AbsenceBuilder(Builder builder) {
            this.builder = builder;
        }

        public Dispatcher ifAbsent(String response) {
            builder.defaultCase(response);
            return builder.build();
        }
    }

    public static class OperatorBuilder {

        private final Builder builder;

        private OperatorBuilder(Builder builder) {
            this.builder = builder;
        }

        public OperatorBuilder addCase(String key, String value) {
            builder.addCase(key, value);
            return this;
        }

        public Dispatcher defaultCase(String response) {
            builder.defaultCase(response);
            return builder.build();
        }
    }

    public static class FromRangeOperatorBuilder {

        private final Builder builder;

        private FromRangeOperatorBuilder(Builder builder) {
            this.builder = builder;
        }

        public ToRangeOperatorBuilder inclusiveFrom(String from) {
            return new ToRangeOperatorBuilder(builder, "[" + from + ";");
        }

        public ToRangeOperatorBuilder exclusiveFrom(String from) {
            return new ToRangeOperatorBuilder(builder, "]" + from + ";");
        }
    }

    public static class ToRangeOperatorBuilder {

        private final Builder builder;
        private final String fromCase;

        private ToRangeOperatorBuilder(Builder builder, String fromCase) {
            this.builder = builder;
            this.fromCase = fromCase;
        }

        public ToRangeOperatorBuilder inclusiveTo(String to, String response) {
            builder.addCase(fromCase + to + "]", response);
            return this;
        }

        public ToRangeOperatorBuilder exclusiveTo(String to, String response) {
            builder.addCase(fromCase + to + "[", response);
            return this;
        }

        Dispatcher defaultCase(String response) {
            builder.defaultCase(response);
            return builder.build();
        }
    }

    public static class SizeOperatorBuilder {

        private final Builder builder;

        private SizeOperatorBuilder(Builder builder) {
            this.builder = builder;
        }

        public SizeOperatorBuilder addCase(Integer from, Integer to, String response) {
            builder.addCase("[" + from + ";" + to + "]", response);
            return this;
        }

        Dispatcher defaultCase(String response) {
            builder.defaultCase(response);
            return builder.build();
        }
    }
}
