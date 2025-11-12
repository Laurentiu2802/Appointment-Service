package org.example.domain;

import org.example.business.dto.AppointmentRequest;
import org.example.business.dto.AppointmentResponse;
import org.example.business.dto.StatusUpdateRequest;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request, String carEnthusiastId);

    AppointmentResponse updateStatus(Long appointmentId, StatusUpdateRequest request, String mechanicId);

    List<AppointmentResponse> getAppointmentsForCarEnthusiast(String carEnthusiastId);

    List<AppointmentResponse> getAppointmentsForMechanic(String mechanicId);

    List<AppointmentResponse> getPendingAppointmentsForMechanic(String mechanicId);
}