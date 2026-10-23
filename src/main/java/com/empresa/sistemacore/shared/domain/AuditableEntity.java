package com.empresa.sistemacore.shared.domain;

import java.time.OffsetDateTime;

public abstract class AuditableEntity {
    private String createdBy;
    private OffsetDateTime createdAt;
    private String modifiedBy;
    private OffsetDateTime modifiedAt;

    public String getCreatedBy() { return createdBy; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public String getModifiedBy() { return modifiedBy; }
    public OffsetDateTime getModifiedAt() { return modifiedAt; }

    public void recordCreation(String user, OffsetDateTime time) {
        createdBy = user;
        createdAt = time;
        recordModification(user, time);
    }

    public void recordModification(String user, OffsetDateTime time) {
        modifiedBy = user;
        modifiedAt = time;
    }
}
