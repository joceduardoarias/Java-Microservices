package com.banking.cqrs.core.queries;

import com.banking.cqrs.core.messages.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseQuery extends Message {
    public BaseQuery(String id) {
        super(id);
    }
}
