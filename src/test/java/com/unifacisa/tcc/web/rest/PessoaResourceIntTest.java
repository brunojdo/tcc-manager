package com.unifacisa.tcc.web.rest;

import com.unifacisa.tcc.TccmanagerApp;

import com.unifacisa.tcc.domain.Pessoa;
import com.unifacisa.tcc.repository.PessoaRepository;
import com.unifacisa.tcc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.unifacisa.tcc.domain.enumeration.TipoDeUsuario;
/**
 * Test class for the PessoaResource REST controller.
 *
 * @see PessoaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TccmanagerApp.class)
public class PessoaResourceIntTest {

    private static final String DEFAULT_PRIMEIRO_NOME = "AAAAAAAAAA";
    private static final String UPDATED_PRIMEIRO_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOME = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOME = "BBBBBBBBBB";

    private static final Long DEFAULT_MATRICULA = 1L;
    private static final Long UPDATED_MATRICULA = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMERO_CONTATO = 1L;
    private static final Long UPDATED_NUMERO_CONTATO = 2L;

    private static final Boolean DEFAULT_DIPONIBILIDADE = false;
    private static final Boolean UPDATED_DIPONIBILIDADE = true;

    private static final TipoDeUsuario DEFAULT_TIPO_DE_USUARIO = TipoDeUsuario.ALUNO;
    private static final TipoDeUsuario UPDATED_TIPO_DE_USUARIO = TipoDeUsuario.PROFESSOR;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPessoaMockMvc;

    private Pessoa pessoa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PessoaResource pessoaResource = new PessoaResource(pessoaRepository);
        this.restPessoaMockMvc = MockMvcBuilders.standaloneSetup(pessoaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa()
            .primeiroNome(DEFAULT_PRIMEIRO_NOME)
            .segundoNome(DEFAULT_SEGUNDO_NOME)
            .matricula(DEFAULT_MATRICULA)
            .email(DEFAULT_EMAIL)
            .numeroContato(DEFAULT_NUMERO_CONTATO)
            .diponibilidade(DEFAULT_DIPONIBILIDADE)
            .tipoDeUsuario(DEFAULT_TIPO_DE_USUARIO);
        return pessoa;
    }

    @Before
    public void initTest() {
        pessoa = createEntity(em);
    }

    @Test
    @Transactional
    public void createPessoa() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        // Create the Pessoa
        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isCreated());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate + 1);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getPrimeiroNome()).isEqualTo(DEFAULT_PRIMEIRO_NOME);
        assertThat(testPessoa.getSegundoNome()).isEqualTo(DEFAULT_SEGUNDO_NOME);
        assertThat(testPessoa.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testPessoa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPessoa.getNumeroContato()).isEqualTo(DEFAULT_NUMERO_CONTATO);
        assertThat(testPessoa.isDiponibilidade()).isEqualTo(DEFAULT_DIPONIBILIDADE);
        assertThat(testPessoa.getTipoDeUsuario()).isEqualTo(DEFAULT_TIPO_DE_USUARIO);
    }

    @Test
    @Transactional
    public void createPessoaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        // Create the Pessoa with an existing ID
        pessoa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPrimeiroNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setPrimeiroNome(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSegundoNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setSegundoNome(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMatriculaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setMatricula(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setEmail(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroContatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setNumeroContato(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoDeUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setTipoDeUsuario(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc.perform(post("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPessoas() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList
        restPessoaMockMvc.perform(get("/api/pessoas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].primeiroNome").value(hasItem(DEFAULT_PRIMEIRO_NOME.toString())))
            .andExpect(jsonPath("$.[*].segundoNome").value(hasItem(DEFAULT_SEGUNDO_NOME.toString())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].numeroContato").value(hasItem(DEFAULT_NUMERO_CONTATO.intValue())))
            .andExpect(jsonPath("$.[*].diponibilidade").value(hasItem(DEFAULT_DIPONIBILIDADE.booleanValue())))
            .andExpect(jsonPath("$.[*].tipoDeUsuario").value(hasItem(DEFAULT_TIPO_DE_USUARIO.toString())));
    }

    @Test
    @Transactional
    public void getPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get the pessoa
        restPessoaMockMvc.perform(get("/api/pessoas/{id}", pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pessoa.getId().intValue()))
            .andExpect(jsonPath("$.primeiroNome").value(DEFAULT_PRIMEIRO_NOME.toString()))
            .andExpect(jsonPath("$.segundoNome").value(DEFAULT_SEGUNDO_NOME.toString()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.numeroContato").value(DEFAULT_NUMERO_CONTATO.intValue()))
            .andExpect(jsonPath("$.diponibilidade").value(DEFAULT_DIPONIBILIDADE.booleanValue()))
            .andExpect(jsonPath("$.tipoDeUsuario").value(DEFAULT_TIPO_DE_USUARIO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPessoa() throws Exception {
        // Get the pessoa
        restPessoaMockMvc.perform(get("/api/pessoas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa
        Pessoa updatedPessoa = pessoaRepository.findOne(pessoa.getId());
        updatedPessoa
            .primeiroNome(UPDATED_PRIMEIRO_NOME)
            .segundoNome(UPDATED_SEGUNDO_NOME)
            .matricula(UPDATED_MATRICULA)
            .email(UPDATED_EMAIL)
            .numeroContato(UPDATED_NUMERO_CONTATO)
            .diponibilidade(UPDATED_DIPONIBILIDADE)
            .tipoDeUsuario(UPDATED_TIPO_DE_USUARIO);

        restPessoaMockMvc.perform(put("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPessoa)))
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getPrimeiroNome()).isEqualTo(UPDATED_PRIMEIRO_NOME);
        assertThat(testPessoa.getSegundoNome()).isEqualTo(UPDATED_SEGUNDO_NOME);
        assertThat(testPessoa.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testPessoa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPessoa.getNumeroContato()).isEqualTo(UPDATED_NUMERO_CONTATO);
        assertThat(testPessoa.isDiponibilidade()).isEqualTo(UPDATED_DIPONIBILIDADE);
        assertThat(testPessoa.getTipoDeUsuario()).isEqualTo(UPDATED_TIPO_DE_USUARIO);
    }

    @Test
    @Transactional
    public void updateNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Create the Pessoa

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPessoaMockMvc.perform(put("/api/pessoas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isCreated());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        int databaseSizeBeforeDelete = pessoaRepository.findAll().size();

        // Get the pessoa
        restPessoaMockMvc.perform(delete("/api/pessoas/{id}", pessoa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pessoa.class);
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(pessoa1.getId());
        assertThat(pessoa1).isEqualTo(pessoa2);
        pessoa2.setId(2L);
        assertThat(pessoa1).isNotEqualTo(pessoa2);
        pessoa1.setId(null);
        assertThat(pessoa1).isNotEqualTo(pessoa2);
    }
}
