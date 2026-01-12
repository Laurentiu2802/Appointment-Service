package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dto.AppointmentRequest;
import org.example.business.dto.AppointmentResponse;
import org.example.business.dto.StatusUpdateRequest;
import org.example.domain.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(
            @Valid @RequestBody AppointmentRequest request,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("Creating appointment for user: {} with roles: {}", userId, roles);

        if (!roles.contains("CAR_ENTHUSIAST")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only car enthusiasts can create appointments");
        }

        AppointmentResponse response = appointmentService.createAppointment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request,
            @RequestHeader("X-User-Id") String mechanicId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("Mechanic {} updating appointment {} status", mechanicId, id);

        if (!roles.contains("MECHANIC")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only mechanics can update appointment status");
        }

        AppointmentResponse response = appointmentService.updateStatus(id, request, mechanicId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<?> getMyAppointments(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("Fetching appointments for user: {} with roles: {}", userId, roles);

        if (!roles.contains("CAR_ENTHUSIAST")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only car enthusiasts can view their appointments");
        }

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsForCarEnthusiast(userId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/mechanic/all")
    public ResponseEntity<?> getAllMechanicAppointments(
            @RequestHeader("X-User-Id") String mechanicId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("Fetching all appointments for mechanic: {}", mechanicId);

        if (!roles.contains("MECHANIC")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only mechanics can view mechanic appointments");
        }

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsForMechanic(mechanicId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/mechanic/pending")
    public ResponseEntity<?> getPendingAppointments(
            @RequestHeader("X-User-Id") String mechanicId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("Fetching pending appointments for mechanic: {}", mechanicId);

        if (!roles.contains("MECHANIC")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only mechanics can view pending appointments");
        }

        List<AppointmentResponse> appointments = appointmentService.getPendingAppointmentsForMechanic(mechanicId);
        return ResponseEntity.ok(appointments);
    }
}