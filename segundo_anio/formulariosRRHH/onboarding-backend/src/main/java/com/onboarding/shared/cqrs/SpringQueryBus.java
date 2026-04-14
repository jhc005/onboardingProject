package com.onboarding.shared.cqrs;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public SpringQueryBus(List<QueryHandler> handlerList) {
        for (QueryHandler handler : handlerList) {
            Class<?> queryType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (queryType != null) {
                handlers.put(queryType, handler);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R ask(Query<R> query) {
        QueryHandler<Query<R>, R> handler =
                (QueryHandler<Query<R>, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No se encontró handler para la query: " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}
