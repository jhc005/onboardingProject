package com.onboarding.shared.cqrs;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringCommandBus implements CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public SpringCommandBus(List<CommandHandler> handlerList) {
        for (CommandHandler handler : handlerList) {
            Class<?> commandType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (commandType != null) {
                handlers.put(commandType, handler);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command command) {
        CommandHandler<Command, R> handler =
                (CommandHandler<Command, R>) handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No se encontró handler para el comando: " + command.getClass().getName());
        }

        return handler.handle(command);
    }
}
