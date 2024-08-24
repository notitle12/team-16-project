package com.sparta.ordersystem.order.management.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created_at;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long created_by;

    @LastModifiedDate
    @Column
    private LocalDateTime updated_at;

    @LastModifiedBy
    @Column
    private Long updated_by;

    @Column
    private LocalDateTime deleted_at;

    @Column
    private Long deleted_by;
}