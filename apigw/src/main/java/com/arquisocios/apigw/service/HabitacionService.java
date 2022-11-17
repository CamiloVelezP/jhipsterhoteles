package com.arquisocios.apigw.service;

import com.arquisocios.apigw.domain.Habitacion;
import com.arquisocios.apigw.repository.HabitacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Habitacion> save(Habitacion habitacion) {
        log.debug("Request to save Habitacion : {}", habitacion);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Update a habitacion.
     *
     * @param habitacion the entity to save.
     * @return the persisted entity.
     */
    public Mono<Habitacion> update(Habitacion habitacion) {
        log.debug("Request to update Habitacion : {}", habitacion);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Partially update a habitacion.
     *
     * @param habitacion the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Habitacion> partialUpdate(Habitacion habitacion) {
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
            .flatMap(habitacionRepository::save);
    }

    /**
     * Get all the habitacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Habitacion> findAll(Pageable pageable) {
        log.debug("Request to get all Habitacions");
        return habitacionRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of habitacions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return habitacionRepository.count();
    }

    /**
     * Get one habitacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Habitacion> findOne(Long id) {
        log.debug("Request to get Habitacion : {}", id);
        return habitacionRepository.findById(id);
    }

    /**
     * Delete the habitacion by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Habitacion : {}", id);
        return habitacionRepository.deleteById(id);
    }
}
