package com.arquisocios.reserva.web.rest;

import com.arquisocios.reserva.domain.Habitacion;
import com.arquisocios.reserva.repository.HabitacionRepository;
import com.arquisocios.reserva.service.HabitacionService;
import com.arquisocios.reserva.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.arquisocios.reserva.domain.Habitacion}.
 */
@RestController
@RequestMapping("/api")
public class HabitacionResource {

    private final Logger log = LoggerFactory.getLogger(HabitacionResource.class);

    private static final String ENTITY_NAME = "reservaHabitacion";

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
    public ResponseEntity<Habitacion> createHabitacion(@Valid @RequestBody Habitacion habitacion) throws URISyntaxException {
        log.debug("REST request to save Habitacion : {}", habitacion);
        if (habitacion.getId() != null) {
            throw new BadRequestAlertException("A new habitacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Habitacion result = habitacionService.save(habitacion);
        return ResponseEntity
            .created(new URI("/api/habitacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<Habitacion> updateHabitacion(
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

        if (!habitacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Habitacion result = habitacionService.update(habitacion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, habitacion.getId().toString()))
            .body(result);
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
    public ResponseEntity<Habitacion> partialUpdateHabitacion(
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

        if (!habitacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Habitacion> result = habitacionService.partialUpdate(habitacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, habitacion.getId().toString())
        );
    }

    /**
     * {@code GET  /habitacions} : get all the habitacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of habitacions in body.
     */
    @GetMapping("/habitacions")
    public ResponseEntity<List<Habitacion>> getAllHabitacions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Habitacions");
        Page<Habitacion> page = habitacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /habitacions/:id} : get the "id" habitacion.
     *
     * @param id the id of the habitacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the habitacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/habitacions/{id}")
    public ResponseEntity<Habitacion> getHabitacion(@PathVariable Long id) {
        log.debug("REST request to get Habitacion : {}", id);
        Optional<Habitacion> habitacion = habitacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(habitacion);
    }

    /**
     * {@code DELETE  /habitacions/:id} : delete the "id" habitacion.
     *
     * @param id the id of the habitacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/habitacions/{id}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable Long id) {
        log.debug("REST request to delete Habitacion : {}", id);
        habitacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
