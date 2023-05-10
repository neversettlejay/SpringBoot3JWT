package com.jaytech.security.models.payload.transfer;

import org.springframework.context.ApplicationEvent;
public class EntityCreatedEvent<T> extends ApplicationEvent {
    private T entity;
    public EntityCreatedEvent(Object source, T entity) {
        super(source);
        this.entity = entity;
    }
    public T getEntity() {
        return entity;
    }
}