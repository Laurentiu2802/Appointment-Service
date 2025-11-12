package org.example.business.dto;

import org.example.persistence.entity.AppointmentEntity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @NotNull(message = "Status is required")
    private AppointmentStatus status;
}