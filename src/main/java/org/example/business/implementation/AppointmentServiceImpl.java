package org.example.business.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dto.AppointmentRequest;
import org.example.business.dto.AppointmentResponse;
import org.example.business.dto.StatusUpdateRequest;
import org.example.domain.AppointmentService;
import org.example.persistance.AppointmentRepository;
import org.example.persistence.entity.AppointmentEntity;
import org.example.persistence.entity.AppointmentEntity.AppointmentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request, String carEnthusiastId) {
        log.info("Creating appointment for car enthusiast: {} with mechanic: {}",
                carEnthusiastId, request.getMechanicId());

        AppointmentEntity appointment = AppointmentEntity.builder()
                .carEnthusiastId(carEnthusiastId)
                .mechanicId(request.getMechanicId())
                .appointmentDate(request.getAppointmentDate())
                .description(request.getDescription())
                .status(AppointmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        AppointmentEntity savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with ID: {}", savedAppointment.getId());

        return mapToResponse(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse updateStatus(Long appointmentId, StatusUpdateRequest request, String mechanicId) {
        log.info("Updating appointment {} status to {} by mechanic: {}",
                appointmentId, request.getStatus(), mechanicId);

        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        if (!appointment.getMechanicId().equals(mechanicId)) {
            throw new RuntimeException("Unauthorized: You can only update your own appointments");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException("Can only update status of pending appointments");
        }

        appointment.setStatus(request.getStatus());
        appointment.setUpdatedAt(LocalDateTime.now());

        AppointmentEntity updatedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment status updated successfully");

        return mapToResponse(updatedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsForCarEnthusiast(String carEnthusiastId) {
        log.info("Fetching appointments for car enthusiast: {}", carEnthusiastId);

        List<AppointmentEntity> appointments = appointmentRepository.findByCarEnthusiastId(carEnthusiastId);

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsForMechanic(String mechanicId) {
        log.info("Fetching all appointments for mechanic: {}", mechanicId);

        List<AppointmentEntity> appointments = appointmentRepository.findByMechanicId(mechanicId);

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getPendingAppointmentsForMechanic(String mechanicId) {
        log.info("Fetching pending appointments for mechanic: {}", mechanicId);

        List<AppointmentEntity> appointments = appointmentRepository
                .findByMechanicIdAndStatus(mechanicId, AppointmentStatus.PENDING);

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapToResponse(AppointmentEntity entity) {
        return AppointmentResponse.builder()
                .id(entity.getId())
                .carEnthusiastId(entity.getCarEnthusiastId())
                .mechanicId(entity.getMechanicId())
                .appointmentDate(entity.getAppointmentDate())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}