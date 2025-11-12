package org.example.persistance;

import org.example.persistence.entity.AppointmentEntity;
import org.example.persistence.entity.AppointmentEntity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByCarEnthusiastId(String carEnthusiastId);

    List<AppointmentEntity> findByMechanicId(String mechanicId);

    List<AppointmentEntity> findByMechanicIdAndStatus(String mechanicId, AppointmentStatus status);

    List<AppointmentEntity> findByCarEnthusiastIdAndStatus(String carEnthusiastId, AppointmentStatus status);
}