package com.banking.cqrs.core.producers;

import com.banking.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produceEvent(String topic, BaseEvent event);
}
