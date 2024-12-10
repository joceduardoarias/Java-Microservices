package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // Esta anotación es necesaria para que Spring pueda inyectar esta clase en otras clases
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List <CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> commandType, CommandHandlerMethod<T> handler) {
        // Aquí se registra el handler para el comando
        var handlers = routes.computeIfAbsent(commandType, c -> List.of());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        // Aquí se envía el comando a los handlers
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("No handler registered");
        }
        if (handlers.size() > 1) {
            throw new IllegalArgumentException("Multiple handlers registered");
        }
        handlers.forEach(h -> h.handle(command));
    }

}
