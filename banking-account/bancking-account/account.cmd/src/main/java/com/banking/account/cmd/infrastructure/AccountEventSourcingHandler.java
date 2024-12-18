package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.Infrastructure.EventStore;
import com.banking.cqrs.core.domain.AggregateRoot;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    @Autowired
    private EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.save(aggregateRoot.getId(),aggregateRoot.getUncommitedChanges(),aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if( events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var lastVersion = events.stream().map(e -> e.getVersion()).max(Comparator.naturalOrder()).get();
            aggregate.setVersion(lastVersion);
        }
        return aggregate;
    }
}
