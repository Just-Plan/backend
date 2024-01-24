package com.jyp.justplan.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    @PrePersist
    public void onPrePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
