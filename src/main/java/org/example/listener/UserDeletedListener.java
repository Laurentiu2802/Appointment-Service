package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.RabbitMQConfig;
import org.example.persistance.AppointmentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeletedListener {

    private final AppointmentRepository appointmentRepository;

    @RabbitListener(queues = RabbitMQConfig.APPOINTMENT_QUEUE)
    @Transactional
    public void handleUserDeleted(String userId) {
        log.info("Received user deleted event for userId: {}", userId);

        try {
            // Delete appointments where user is car enthusiast
            var enthusiastAppointments = appointmentRepository.findByCarEnthusiastId(userId);
            appointmentRepository.deleteAll(enthusiastAppointments);
            log.info("Deleted {} appointments for car enthusiast: {}", enthusiastAppointments.size(), userId);

            // Delete appointments where user is mechanic
            var mechanicAppointments = appointmentRepository.findByMechanicId(userId);
            appointmentRepository.deleteAll(mechanicAppointments);
            log.info("Deleted {} appointments for mechanic: {}", mechanicAppointments.size(), userId);

        } catch (Exception e) {
            log.error("Error deleting appointments for user: {}", userId, e);
            throw e;
        }
    }
}