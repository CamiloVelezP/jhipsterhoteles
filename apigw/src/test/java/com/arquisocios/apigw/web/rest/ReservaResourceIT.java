package com.arquisocios.apigw.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.arquisocios.apigw.IntegrationTest;
import com.arquisocios.apigw.domain.Reserva;
import com.arquisocios.apigw.repository.EntityManager;
import com.arquisocios.apigw.repository.ReservaRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ReservaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReservaResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reservas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Reserva reserva;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createEntity(EntityManager em) {
        Reserva reserva = new Reserva().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN);
        return reserva;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createUpdatedEntity(EntityManager em) {
        Reserva reserva = new Reserva().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);
        return reserva;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Reserva.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        reserva = createEntity(em);
    }

    @Test
    void createReserva() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().collectList().block().size();
        // Create the Reserva
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate + 1);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    void createReservaWithExistingId() throws Exception {
        // Create the Reserva with an existing ID
        reserva.setId(1L);

        int databaseSizeBeforeCreate = reservaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().collectList().block().size();
        // set the field null
        reserva.setFechaInicio(null);

        // Create the Reserva, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().collectList().block().size();
        // set the field null
        reserva.setFechaFin(null);

        // Create the Reserva, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllReservas() {
        // Initialize the database
        reservaRepository.save(reserva).block();

        // Get all the reservaList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(reserva.getId().intValue()))
            .jsonPath("$.[*].fechaInicio")
            .value(hasItem(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.[*].fechaFin")
            .value(hasItem(DEFAULT_FECHA_FIN.toString()));
    }

    @Test
    void getReserva() {
        // Initialize the database
        reservaRepository.save(reserva).block();

        // Get the reserva
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reserva.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(reserva.getId().intValue()))
            .jsonPath("$.fechaInicio")
            .value(is(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.fechaFin")
            .value(is(DEFAULT_FECHA_FIN.toString()));
    }

    @Test
    void getNonExistingReserva() {
        // Get the reserva
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReserva() throws Exception {
        // Initialize the database
        reservaRepository.save(reserva).block();

        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).block();
        updatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedReserva.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedReserva))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    void putNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reserva.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.save(reserva).block();

        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    void fullUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.save(reserva).block();

        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    void patchNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reserva.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().collectList().block().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reserva))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReserva() {
        // Initialize the database
        reservaRepository.save(reserva).block();

        int databaseSizeBeforeDelete = reservaRepository.findAll().collectList().block().size();

        // Delete the reserva
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reserva.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Reserva> reservaList = reservaRepository.findAll().collectList().block();
        assertThat(reservaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
