package io.github.microcks.testcontainers.model;

public class OperationUpdateRequest {
    private final String dispatcher;
    private final String dispatcherRules;

    public OperationUpdateRequest(Dispatcher dispatcher) {
        this.dispatcher = dispatcher.getType().name();
        this.dispatcherRules = dispatcher.getRules();
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public String getDispatcherRules() {
        return dispatcherRules;
    }
}
