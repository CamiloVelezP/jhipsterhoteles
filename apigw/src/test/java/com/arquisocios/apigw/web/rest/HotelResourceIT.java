package com.arquisocios.apigw.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.arquisocios.apigw.IntegrationTest;
import com.arquisocios.apigw.domain.Hotel;
import com.arquisocios.apigw.repository.EntityManager;
import com.arquisocios.apigw.repository.HotelRepository;
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
 * Integration tests for the {@link HotelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HotelResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CADENA = "AAAAAAAAAA";
    private static final String UPDATED_CADENA = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hotels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Hotel hotel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createEntity(EntityManager em) {
        Hotel hotel = new Hotel().nombre(DEFAULT_NOMBRE).cadena(DEFAULT_CADENA).ciudad(DEFAULT_CIUDAD).direccion(DEFAULT_DIRECCION);
        return hotel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createUpdatedEntity(EntityManager em) {
        Hotel hotel = new Hotel().nombre(UPDATED_NOMBRE).cadena(UPDATED_CADENA).ciudad(UPDATED_CIUDAD).direccion(UPDATED_DIRECCION);
        return hotel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Hotel.class).block();
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
        hotel = createEntity(em);
    }

    @Test
    void createHotel() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().collectList().block().size();
        // Create the Hotel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate + 1);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHotel.getCadena()).isEqualTo(DEFAULT_CADENA);
        assertThat(testHotel.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testHotel.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    void createHotelWithExistingId() throws Exception {
        // Create the Hotel with an existing ID
        hotel.setId(1L);

        int databaseSizeBeforeCreate = hotelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().collectList().block().size();
        // set the field null
        hotel.setNombre(null);

        // Create the Hotel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCadenaIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().collectList().block().size();
        // set the field null
        hotel.setCadena(null);

        // Create the Hotel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCiudadIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().collectList().block().size();
        // set the field null
        hotel.setCiudad(null);

        // Create the Hotel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().collectList().block().size();
        // set the field null
        hotel.setDireccion(null);

        // Create the Hotel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllHotels() {
        // Initialize the database
        hotelRepository.save(hotel).block();

        // Get all the hotelList
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
            .value(hasItem(hotel.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].cadena")
            .value(hasItem(DEFAULT_CADENA))
            .jsonPath("$.[*].ciudad")
            .value(hasItem(DEFAULT_CIUDAD))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION));
    }

    @Test
    void getHotel() {
        // Initialize the database
        hotelRepository.save(hotel).block();

        // Get the hotel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, hotel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(hotel.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.cadena")
            .value(is(DEFAULT_CADENA))
            .jsonPath("$.ciudad")
            .value(is(DEFAULT_CIUDAD))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION));
    }

    @Test
    void getNonExistingHotel() {
        // Get the hotel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel).block();

        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();

        // Update the hotel
        Hotel updatedHotel = hotelRepository.findById(hotel.getId()).block();
        updatedHotel.nombre(UPDATED_NOMBRE).cadena(UPDATED_CADENA).ciudad(UPDATED_CIUDAD).direccion(UPDATED_DIRECCION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedHotel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedHotel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHotel.getCadena()).isEqualTo(UPDATED_CADENA);
        assertThat(testHotel.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testHotel.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    void putNonExistingHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, hotel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel).block();

        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHotel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHotel.getCadena()).isEqualTo(DEFAULT_CADENA);
        assertThat(testHotel.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testHotel.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    void fullUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel).block();

        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        partialUpdatedHotel.nombre(UPDATED_NOMBRE).cadena(UPDATED_CADENA).ciudad(UPDATED_CIUDAD).direccion(UPDATED_DIRECCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHotel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHotel.getCadena()).isEqualTo(UPDATED_CADENA);
        assertThat(testHotel.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testHotel.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    void patchNonExistingHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, hotel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().collectList().block().size();
        hotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hotel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHotel() {
        // Initialize the database
        hotelRepository.save(hotel).block();

        int databaseSizeBeforeDelete = hotelRepository.findAll().collectList().block().size();

        // Delete the hotel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, hotel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Hotel> hotelList = hotelRepository.findAll().collectList().block();
        assertThat(hotelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
