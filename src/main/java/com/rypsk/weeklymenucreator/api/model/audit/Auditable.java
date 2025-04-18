package com.rypsk.weeklymenucreator.api.model.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    private LocalDate createdAt;
    private String createdBy;
    private LocalDate modifyAt;
    private String modifyBy;
}
