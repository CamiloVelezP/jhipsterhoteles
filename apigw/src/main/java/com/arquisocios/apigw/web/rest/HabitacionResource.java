package com.arquisocios.apigw.web.rest;

import com.arquisocios.apigw.domain.Habitacion;
import com.arquisocios.apigw.repository.HabitacionRepository;
import com.arquisocios.apigw.service.HabitacionService;
import com.arquisocios.apigw.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.arquisocios.apigw.domain.Habitacion}.
 */
@RestController
@RequestMapping("/api")
public class HabitacionResource {

    private final Logger log = LoggerFactory.getLogger(HabitacionResource.class);

    private static final String ENTITY_NAME = "habitacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HabitacionService habitacionService;

    private final HabitacionRepository habitacionRepository;

    public HabitacionResource(HabitacionService habitacionService, HabitacionRepository habitacionRepository) {
        this.habitacionService = habitacionService;
        this.habitacionRepository = habitacionRepository;
    }

    /**
     * {@code POST  /habitacions} : Create a new habitacion.
     *
     * @param habitacion the habitacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new habitacion, or with status {@code 400 (Bad Request)} if the habitacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/habitacions")
    public Mono<ResponseEntity<Habitacion>> createHabitacion(@Valid @RequestBody Habitacion habitacion) throws URISyntaxException {
        log.debug("REST request to save Habitacion : {}", habitacion);
        if (habitacion.getId() != null) {
            throw new BadRequestAlertException("A new habitacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return habitacionService
            .save(habitacion)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/habitacions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /habitacions/:id} : Updates an existing habitacion.
     *
     * @param id the id of the habitacion to save.
     * @param habitacion the habitacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habitacion,
     * or with status {@code 400 (Bad Request)} if the habitacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the habitacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/habitacions/{id}")
    public Mono<ResponseEntity<Habitacion>> updateHabitacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Habitacion habitacion
    ) throws URISyntaxException {
        log.debug("REST request to update Habitacion : {}, {}", id, habitacion);
        if (habitacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habitacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return habitacionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return habitacionService
                    .update(habitacion)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /habitacions/:id} : Partial updates given fields of an existing habitacion, field will ignore if it is null
     *
     * @param id the id of the habitacion to save.
     * @param habitacion the habitacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habitacion,
     * or with status {@code 400 (Bad Request)} if the habitacion is not valid,
     * or with status {@code 404 (Not Found)} if the habitacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the habitacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/habitacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Habitacion>> partialUpdateHabitacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Habitacion habitacion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Habitacion partially : {}, {}", id, habitacion);
        if (habitacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habitacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return habitacionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Habitacion> result = habitacionService.partialUpdate(habitacion);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /habitacions} : get all the habitacions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of habitacions in body.
     */
    @GetMapping("/habitacions")
    public Mono<ResponseEntity<List<Habitacion>>> getAllHabitacions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Habitacions");
        return habitacionService
            .countAll()
            .zipWith(habitacionService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /habitacions/:id} : get the "id" habitacion.
     *
     * @param id the id of the habitacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the habitacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/habitacions/{id}")
    public Mono<ResponseEntity<Habitacion>> getHabitacion(@PathVariable Long id) {
        log.debug("REST request to get Habitacion : {}", id);
        Mono<Habitacion> habitacion = habitacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(habitacion);
    }

    /**
     * {@code DELETE  /habitacions/:id} : delete the "id" habitacion.
     *
     * @param id the id of the habitacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/habitacions/{id}")
    public Mono<ResponseEntity<Void>> deleteHabitacion(@PathVariable Long id) {
        log.debug("REST request to delete Habitacion : {}", id);
        return habitacionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
