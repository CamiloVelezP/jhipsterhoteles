package com.arquisocios.apigw.repository;

import com.arquisocios.apigw.domain.Reserva;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Reserva entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservaRepository extends ReactiveCrudRepository<Reserva, Long>, ReservaRepositoryInternal {
    Flux<Reserva> findAllBy(Pageable pageable);

    @Query("SELECT * FROM reserva entity WHERE entity.id_habitacion_id = :id")
    Flux<Reserva> findByIdHabitacion(Long id);

    @Query("SELECT * FROM reserva entity WHERE entity.id_habitacion_id IS NULL")
    Flux<Reserva> findAllWhereIdHabitacionIsNull();

    @Query("SELECT * FROM reserva entity WHERE entity.nro_doc_id = :id")
    Flux<Reserva> findByNroDoc(Long id);

    @Query("SELECT * FROM reserva entity WHERE entity.nro_doc_id IS NULL")
    Flux<Reserva> findAllWhereNroDocIsNull();

    @Override
    <S extends Reserva> Mono<S> save(S entity);

    @Override
    Flux<Reserva> findAll();

    @Override
    Mono<Reserva> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ReservaRepositoryInternal {
    <S extends Reserva> Mono<S> save(S entity);

    Flux<Reserva> findAllBy(Pageable pageable);

    Flux<Reserva> findAll();

    Mono<Reserva> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Reserva> findAllBy(Pageable pageable, Criteria criteria);

}
