package com.banking.cqrs.core.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "eventStore")
public class EventModel {
    @Id
    private String id;
    private String aggregateId;
    private String aggregateType;
    private String eventType;
    private BaseEvent eventData;
    private Date timestamp;
    private long version;
}
