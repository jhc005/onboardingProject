package com.onboarding.shared.cqrs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpringCommandBus implements CommandBus {

    private final ApplicationContext context;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <R> R dispatch(Command command) {
        Map<String, CommandHandler> handlers = context.getBeansOfType(CommandHandler.class);

        for (CommandHandler handler : handlers.values()) {
            ResolvableType handlerType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class);
            Class<?> commandType = handlerType.getGeneric(0).resolve();

            if (commandType != null && commandType.isAssignableFrom(command.getClass())) {
                return (R) handler.handle(command);
            }
        }

        throw new IllegalStateException("No se encontró handler para el comando: " + command.getClass().getName());
    }
}
