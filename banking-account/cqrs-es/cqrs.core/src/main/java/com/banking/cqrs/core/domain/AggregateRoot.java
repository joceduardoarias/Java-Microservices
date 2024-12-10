package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.messages.Message;
import lombok.Data;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public abstract class AggregateRoot {

    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommitedChanges() {
        return this.changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, boolean isNewEvent) {

        try {
            this.getClass()
                .getMethod("apply", event.getClass())
                .invoke(this, event);

        }catch(NoSuchMethodError e) {
            logger.log(Level.WARNING, MessageFormat.format("No apply method found for event: ", event.getClass().getName()));
        }
        catch (Exception e) {
            logger.severe("Error applying event: " + e.getMessage());
        }finally {
            if(isNewEvent) {
                this.changes.add(event);
            }
        }
        this.applyChange(event, isNewEvent);
    }

    public void raiseEvent(BaseEvent event) {
        this.applyChange(event, true);
    }

    public void replayEvents(List<BaseEvent> events) {
        for(BaseEvent event : events) {
            this.applyChange(event, false);
        }
    }
}
