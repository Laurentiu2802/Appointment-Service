package org.example.business.dto;

import org.example.persistence.entity.AppointmentEntity.AppointmentStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponse {
    private Long id;
    private String carEnthusiastId;
    private String mechanicId;
    private LocalDateTime appointmentDate;
    private String description;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}