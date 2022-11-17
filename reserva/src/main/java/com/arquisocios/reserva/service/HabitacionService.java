package com.arquisocios.reserva.service;

import com.arquisocios.reserva.domain.Habitacion;
import com.arquisocios.reserva.repository.HabitacionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Habitacion}.
 */
@Service
@Transactional
public class HabitacionService {

    private final Logger log = LoggerFactory.getLogger(HabitacionService.class);

    private final HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    /**
     * Save a habitacion.
     *
     * @param habitacion the entity to save.
     * @return the persisted entity.
     */
    public Habitacion save(Habitacion habitacion) {
        log.debug("Request to save Habitacion : {}", habitacion);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Update a habitacion.
     *
     * @param habitacion the entity to save.
     * @return the persisted entity.
     */
    public Habitacion update(Habitacion habitacion) {
        log.debug("Request to update Habitacion : {}", habitacion);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Partially update a habitacion.
     *
     * @param habitacion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Habitacion> partialUpdate(Habitacion habitacion) {
        log.debug("Request to partially update Habitacion : {}", habitacion);

        return habitacionRepository
            .findById(habitacion.getId())
            .map(existingHabitacion -> {
                if (habitacion.getUbicacion() != null) {
                    existingHabitacion.setUbicacion(habitacion.getUbicacion());
                }
                if (habitacion.getCapacidad() != null) {
                    existingHabitacion.setCapacidad(habitacion.getCapacidad());
                }
                if (habitacion.getPrecio() != null) {
                    existingHabitacion.setPrecio(habitacion.getPrecio());
                }
                if (habitacion.getDisponible() != null) {
                    existingHabitacion.setDisponible(habitacion.getDisponible());
                }

                return existingHabitacion;
            })
            .map(habitacionRepository::save);
    }

    /**
     * Get all the habitacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Habitacion> findAll(Pageable pageable) {
        log.debug("Request to get all Habitacions");
        return habitacionRepository.findAll(pageable);
    }

    /**
     * Get one habitacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Habitacion> findOne(Long id) {
        log.debug("Request to get Habitacion : {}", id);
        return habitacionRepository.findById(id);
    }

    /**
     * Delete the habitacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Habitacion : {}", id);
        habitacionRepository.deleteById(id);
    }
}
