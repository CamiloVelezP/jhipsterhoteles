package com.arquisocios.reserva.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arquisocios.reserva.IntegrationTest;
import com.arquisocios.reserva.domain.Reserva;
import com.arquisocios.reserva.repository.ReservaRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restReservaMockMvc;

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

    @BeforeEach
    public void initTest() {
        reserva = createEntity(em);
    }

    @Test
    @Transactional
    void createReserva() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();
        // Create the Reserva
        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isCreated());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate + 1);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void createReservaWithExistingId() throws Exception {
        // Create the Reserva with an existing ID
        reserva.setId(1L);

        int databaseSizeBeforeCreate = reservaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().size();
        // set the field null
        reserva.setFechaInicio(null);

        // Create the Reserva, which fails.

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().size();
        // set the field null
        reserva.setFechaFin(null);

        // Create the Reserva, which fails.

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservas() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())));
    }

    @Test
    @Transactional
    void getReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get the reserva
        restReservaMockMvc
            .perform(get(ENTITY_API_URL_ID, reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reserva.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReserva() throws Exception {
        // Get the reserva
        restReservaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).get();
        // Disconnect from session so that the updates on updatedReserva are not directly saved in db
        em.detach(updatedReserva);
        updatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReserva.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reserva.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReserva.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeDelete = reservaRepository.findAll().size();

        // Delete the reserva
        restReservaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reserva.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
