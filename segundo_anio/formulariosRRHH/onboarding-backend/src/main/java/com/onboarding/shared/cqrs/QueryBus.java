package com.onboarding.shared.cqrs;

public interface QueryBus {
    <R> R ask(Query<R> query);
}
