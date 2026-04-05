package com.siceb.shared;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * UUID-based entity identifier. No auto-increment sequences — IDs can be
 * generated offline on the client and remain globally unique without
 * server coordination.
 */
public record EntityId(@JsonValue UUID value) {

    public EntityId {
        Objects.requireNonNull(value, "EntityId value must not be null");
    }

    public static EntityId generate() {
        return new EntityId(UUID.randomUUID());
    }

    @JsonCreator
    public static EntityId of(UUID uuid) {
        return new EntityId(uuid);
    }

    public static EntityId of(String uuid) {
        return new EntityId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
