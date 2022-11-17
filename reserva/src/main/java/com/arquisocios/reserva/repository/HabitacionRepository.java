package com.arquisocios.reserva.repository;

import com.arquisocios.reserva.domain.Habitacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Habitacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {}
