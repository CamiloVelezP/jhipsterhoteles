package com.arquisocios.apigw.service;

import com.arquisocios.apigw.domain.Reserva;
import com.arquisocios.apigw.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Reserva}.
 */
@Service
@Transactional
public class ReservaService {

    private final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    /**
     * Save a reserva.
     *
     * @param reserva the entity to save.
     * @return the persisted entity.
     */
    public Mono<Reserva> save(Reserva reserva) {
        log.debug("Request to save Reserva : {}", reserva);
        return reservaRepository.save(reserva);
    }

    /**
     * Update a reserva.
     *
     * @param reserva the entity to save.
     * @return the persisted entity.
     */
    public Mono<Reserva> update(Reserva reserva) {
        log.debug("Request to update Reserva : {}", reserva);
        return reservaRepository.save(reserva);
    }

    /**
     * Partially update a reserva.
     *
     * @param reserva the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Reserva> partialUpdate(Reserva reserva) {
        log.debug("Request to partially update Reserva : {}", reserva);

        return reservaRepository
            .findById(reserva.getId())
            .map(existingReserva -> {
                if (reserva.getFechaInicio() != null) {
                    existingReserva.setFechaInicio(reserva.getFechaInicio());
                }
                if (reserva.getFechaFin() != null) {
                    existingReserva.setFechaFin(reserva.getFechaFin());
                }

                return existingReserva;
            })
            .flatMap(reservaRepository::save);
    }

    /**
     * Get all the reservas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Reserva> findAll(Pageable pageable) {
        log.debug("Request to get all Reservas");
        return reservaRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of reservas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reservaRepository.count();
    }

    /**
     * Get one reserva by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Reserva> findOne(Long id) {
        log.debug("Request to get Reserva : {}", id);
        return reservaRepository.findById(id);
    }

    /**
     * Delete the reserva by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Reserva : {}", id);
        return reservaRepository.deleteById(id);
    }
}
