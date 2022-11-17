package com.arquisocios.apigw.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.arquisocios.apigw.IntegrationTest;
import com.arquisocios.apigw.domain.Usuario;
import com.arquisocios.apigw.repository.EntityManager;
import com.arquisocios.apigw.repository.UsuarioRepository;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NRO_DOC = "AAAAAAAAAA";
    private static final String UPDATED_NRO_DOC = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario().nroDoc(DEFAULT_NRO_DOC).nombre(DEFAULT_NOMBRE).email(DEFAULT_EMAIL);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario().nroDoc(UPDATED_NRO_DOC).nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL);
        return usuario;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Usuario.class).block();
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
        usuario = createEntity(em);
    }

    @Test
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().collectList().block().size();
        // Create the Usuario
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNroDoc()).isEqualTo(DEFAULT_NRO_DOC);
        assertThat(testUsuario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNroDocIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setNroDoc(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setNombre(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setEmail(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUsuarios() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        // Get all the usuarioList
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
            .value(hasItem(usuario.getId().intValue()))
            .jsonPath("$.[*].nroDoc")
            .value(hasItem(DEFAULT_NRO_DOC))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @Test
    void getUsuario() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        // Get the usuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(usuario.getId().intValue()))
            .jsonPath("$.nroDoc")
            .value(is(DEFAULT_NRO_DOC))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingUsuario() {
        // Get the usuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).block();
        updatedUsuario.nroDoc(UPDATED_NRO_DOC).nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUsuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNroDoc()).isEqualTo(UPDATED_NRO_DOC);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nroDoc(UPDATED_NRO_DOC).nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNroDoc()).isEqualTo(UPDATED_NRO_DOC);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nroDoc(UPDATED_NRO_DOC).nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNroDoc()).isEqualTo(UPDATED_NRO_DOC);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUsuario() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeDelete = usuarioRepository.findAll().collectList().block().size();

        // Delete the usuario
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
