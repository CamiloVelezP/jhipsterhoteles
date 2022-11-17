package com.arquisocios.apigw.repository;

import com.arquisocios.apigw.domain.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Hotel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotelRepository extends ReactiveCrudRepository<Hotel, Long>, HotelRepositoryInternal {
    Flux<Hotel> findAllBy(Pageable pageable);

    @Override
    <S extends Hotel> Mono<S> save(S entity);

    @Override
    Flux<Hotel> findAll();

    @Override
    Mono<Hotel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HotelRepositoryInternal {
    <S extends Hotel> Mono<S> save(S entity);

    Flux<Hotel> findAllBy(Pageable pageable);

    Flux<Hotel> findAll();

    Mono<Hotel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Hotel> findAllBy(Pageable pageable, Criteria criteria);

}
