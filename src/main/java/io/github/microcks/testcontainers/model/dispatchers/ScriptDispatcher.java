package io.github.microcks.testcontainers.model.dispatchers;

import io.github.microcks.testcontainers.model.Dispatcher;

public class ScriptDispatcher implements Dispatcher {

    private final String script;

    private ScriptDispatcher(String script) {
        this.script = script;
    }

    @Override
    public Type getType() {
        return Type.SCRIPT;
    }

    @Override
    public String getRules() {
        return script;
    }

    public static ScriptDispatcher script(String script) {
        return new ScriptDispatcher(script);
    }
}
