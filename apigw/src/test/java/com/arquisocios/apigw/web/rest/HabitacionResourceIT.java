package com.arquisocios.apigw.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.arquisocios.apigw.IntegrationTest;
import com.arquisocios.apigw.domain.Habitacion;
import com.arquisocios.apigw.repository.EntityManager;
import com.arquisocios.apigw.repository.HabitacionRepository;
import java.time.Duration;
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
 * Integration tests for the {@link HabitacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HabitacionResourceIT {

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDAD = 1;
    private static final Integer UPDATED_CAPACIDAD = 2;

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final Boolean DEFAULT_DISPONIBLE = false;
    private static final Boolean UPDATED_DISPONIBLE = true;

    private static final String ENTITY_API_URL = "/api/habitacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Habitacion habitacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habitacion createEntity(EntityManager em) {
        Habitacion habitacion = new Habitacion()
            .ubicacion(DEFAULT_UBICACION)
            .capacidad(DEFAULT_CAPACIDAD)
            .precio(DEFAULT_PRECIO)
            .disponible(DEFAULT_DISPONIBLE);
        return habitacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habitacion createUpdatedEntity(EntityManager em) {
        Habitacion habitacion = new Habitacion()
            .ubicacion(UPDATED_UBICACION)
            .capacidad(UPDATED_CAPACIDAD)
            .precio(UPDATED_PRECIO)
            .disponible(UPDATED_DISPONIBLE);
        return habitacion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Habitacion.class).block();
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
        habitacion = createEntity(em);
    }

    @Test
    void createHabitacion() throws Exception {
        int databaseSizeBeforeCreate = habitacionRepository.findAll().collectList().block().size();
        // Create the Habitacion
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeCreate + 1);
        Habitacion testHabitacion = habitacionList.get(habitacionList.size() - 1);
        assertThat(testHabitacion.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testHabitacion.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testHabitacion.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testHabitacion.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    void createHabitacionWithExistingId() throws Exception {
        // Create the Habitacion with an existing ID
        habitacion.setId(1L);

        int databaseSizeBeforeCreate = habitacionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUbicacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitacionRepository.findAll().collectList().block().size();
        // set the field null
        habitacion.setUbicacion(null);

        // Create the Habitacion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCapacidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitacionRepository.findAll().collectList().block().size();
        // set the field null
        habitacion.setCapacidad(null);

        // Create the Habitacion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitacionRepository.findAll().collectList().block().size();
        // set the field null
        habitacion.setPrecio(null);

        // Create the Habitacion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDisponibleIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitacionRepository.findAll().collectList().block().size();
        // set the field null
        habitacion.setDisponible(null);

        // Create the Habitacion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllHabitacions() {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        // Get all the habitacionList
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
            .value(hasItem(habitacion.getId().intValue()))
            .jsonPath("$.[*].ubicacion")
            .value(hasItem(DEFAULT_UBICACION))
            .jsonPath("$.[*].capacidad")
            .value(hasItem(DEFAULT_CAPACIDAD))
            .jsonPath("$.[*].precio")
            .value(hasItem(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.[*].disponible")
            .value(hasItem(DEFAULT_DISPONIBLE.booleanValue()));
    }

    @Test
    void getHabitacion() {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        // Get the habitacion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, habitacion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(habitacion.getId().intValue()))
            .jsonPath("$.ubicacion")
            .value(is(DEFAULT_UBICACION))
            .jsonPath("$.capacidad")
            .value(is(DEFAULT_CAPACIDAD))
            .jsonPath("$.precio")
            .value(is(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.disponible")
            .value(is(DEFAULT_DISPONIBLE.booleanValue()));
    }

    @Test
    void getNonExistingHabitacion() {
        // Get the habitacion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHabitacion() throws Exception {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();

        // Update the habitacion
        Habitacion updatedHabitacion = habitacionRepository.findById(habitacion.getId()).block();
        updatedHabitacion.ubicacion(UPDATED_UBICACION).capacidad(UPDATED_CAPACIDAD).precio(UPDATED_PRECIO).disponible(UPDATED_DISPONIBLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedHabitacion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedHabitacion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
        Habitacion testHabitacion = habitacionList.get(habitacionList.size() - 1);
        assertThat(testHabitacion.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testHabitacion.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testHabitacion.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testHabitacion.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    void putNonExistingHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, habitacion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHabitacionWithPatch() throws Exception {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();

        // Update the habitacion using partial update
        Habitacion partialUpdatedHabitacion = new Habitacion();
        partialUpdatedHabitacion.setId(habitacion.getId());

        partialUpdatedHabitacion.ubicacion(UPDATED_UBICACION).capacidad(UPDATED_CAPACIDAD).disponible(UPDATED_DISPONIBLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHabitacion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHabitacion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
        Habitacion testHabitacion = habitacionList.get(habitacionList.size() - 1);
        assertThat(testHabitacion.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testHabitacion.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testHabitacion.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testHabitacion.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    void fullUpdateHabitacionWithPatch() throws Exception {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();

        // Update the habitacion using partial update
        Habitacion partialUpdatedHabitacion = new Habitacion();
        partialUpdatedHabitacion.setId(habitacion.getId());

        partialUpdatedHabitacion
            .ubicacion(UPDATED_UBICACION)
            .capacidad(UPDATED_CAPACIDAD)
            .precio(UPDATED_PRECIO)
            .disponible(UPDATED_DISPONIBLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHabitacion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHabitacion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
        Habitacion testHabitacion = habitacionList.get(habitacionList.size() - 1);
        assertThat(testHabitacion.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testHabitacion.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testHabitacion.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testHabitacion.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    void patchNonExistingHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, habitacion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHabitacion() throws Exception {
        int databaseSizeBeforeUpdate = habitacionRepository.findAll().collectList().block().size();
        habitacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(habitacion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Habitacion in the database
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHabitacion() {
        // Initialize the database
        habitacionRepository.save(habitacion).block();

        int databaseSizeBeforeDelete = habitacionRepository.findAll().collectList().block().size();

        // Delete the habitacion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, habitacion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Habitacion> habitacionList = habitacionRepository.findAll().collectList().block();
        assertThat(habitacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
