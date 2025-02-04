package com.banking.account.query.infrastructure;

import com.banking.cqrs.core.Infrastructure.QueryDispatcher;
import com.banking.cqrs.core.domain.BaseEntity;
import com.banking.cqrs.core.queries.BaseQuery;
import com.banking.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> QueryType, QueryHandlerMethod<T> handler) {
        // Aquí se registra el handler que manejará la consulta
        var handlers = routes.computeIfAbsent(QueryType, c -> new ArrayList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No handler registered");
        }
        if (handlers.size() > 1) {
            throw new RuntimeException("Multiple handlers registered");
        }
        return handlers.get(0).handle(query);
    }
}
