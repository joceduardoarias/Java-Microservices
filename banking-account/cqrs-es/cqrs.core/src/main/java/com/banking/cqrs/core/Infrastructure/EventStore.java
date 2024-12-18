package com.banking.cqrs.core.Infrastructure;

import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;

import java.util.List;

public interface EventStore {

    void save(String aggregateId, Iterable<BaseEvent> events, long version);
    List<BaseEvent> getEvents(String aggregateId);
}
