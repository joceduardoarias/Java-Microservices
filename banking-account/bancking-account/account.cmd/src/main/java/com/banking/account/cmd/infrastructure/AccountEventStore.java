package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.Infrastructure.EventStore;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import exceptions.AggregateNotFoundException;
import exceptions.ConcurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;
    @Override
    public void save(String aggregateId, Iterable<BaseEvent> events, long version) {
        var evenStream = eventStoreRepository.findByAggregateId(aggregateId);
        if (version != -1 && evenStream.get(evenStream.size() - 1).getVersion() != version) {
            throw new ConcurrencyException("Concurrency exception");
        }
        var versionNumber = version;
        for (var event : events) {
            versionNumber++;
            event.setVersion(versionNumber);
            var eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateId(aggregateId)
                    .aggregateType(event.getClass().getTypeName())
                    .version(versionNumber)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (persistedEvent != null) {
                System.out.println("Event persisted call kafka");
            }
        }

    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var stream = eventStoreRepository.findByAggregateId(aggregateId);
        if (stream == null || stream.isEmpty()) {
            throw new AggregateNotFoundException("Aggregate not found - La cuenta del banco es incorrecta");
        }

        return stream.stream().map(EventModel::getEventData).toList();
    }
}
