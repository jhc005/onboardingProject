package com.onboarding.shared.cqrs;

public interface CommandBus {
    <R> R dispatch(Command command);
}
