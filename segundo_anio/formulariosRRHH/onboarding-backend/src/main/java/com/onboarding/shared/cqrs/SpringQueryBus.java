package com.onboarding.shared.cqrs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpringQueryBus implements QueryBus {

    private final ApplicationContext context;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <R> R ask(Query<R> query) {
        Map<String, QueryHandler> handlers = context.getBeansOfType(QueryHandler.class);

        for (QueryHandler handler : handlers.values()) {
            ResolvableType handlerType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class);
            Class<?> queryType = handlerType.getGeneric(0).resolve();

            if (queryType != null && queryType.isAssignableFrom(query.getClass())) {
                return (R) handler.handle(query);
            }
        }

        throw new IllegalStateException("No se encontró handler para la query: " + query.getClass().getName());
    }
}
