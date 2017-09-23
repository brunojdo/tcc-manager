package com.unifacisa.tcc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.unifacisa.tcc.domain.Pessoa;

import com.unifacisa.tcc.repository.PessoaRepository;
import com.unifacisa.tcc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Pessoa.
 */
@RestController
@RequestMapping("/api")
public class PessoaResource {

    private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

    private static final String ENTITY_NAME = "pessoa";

    private final PessoaRepository pessoaRepository;
    public PessoaResource(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * POST  /pessoas : Create a new pessoa.
     *
     * @param pessoa the pessoa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pessoa, or with status 400 (Bad Request) if the pessoa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pessoas")
    @Timed
    public ResponseEntity<Pessoa> createPessoa(@Valid @RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to save Pessoa : {}", pessoa);
        if (pessoa.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pessoa cannot already have an ID")).body(null);
        }
        Pessoa result = pessoaRepository.save(pessoa);
        return ResponseEntity.created(new URI("/api/pessoas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pessoas : Updates an existing pessoa.
     *
     * @param pessoa the pessoa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pessoa,
     * or with status 400 (Bad Request) if the pessoa is not valid,
     * or with status 500 (Internal Server Error) if the pessoa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pessoas")
    @Timed
    public ResponseEntity<Pessoa> updatePessoa(@Valid @RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to update Pessoa : {}", pessoa);
        if (pessoa.getId() == null) {
            return createPessoa(pessoa);
        }
        Pessoa result = pessoaRepository.save(pessoa);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pessoa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pessoas : get all the pessoas.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of pessoas in body
     */
    @GetMapping("/pessoas")
    @Timed
    public List<Pessoa> getAllPessoas(@RequestParam(required = false) String filter) {
        if ("usuario-is-null".equals(filter)) {
            log.debug("REST request to get all Pessoas where usuario is null");
            return StreamSupport
                .stream(pessoaRepository.findAll().spliterator(), false)
                .filter(pessoa -> pessoa.getUsuario() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Pessoas");
        return pessoaRepository.findAll();
        }

    /**
     * GET  /pessoas/:id : get the "id" pessoa.
     *
     * @param id the id of the pessoa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pessoa, or with status 404 (Not Found)
     */
    @GetMapping("/pessoas/{id}")
    @Timed
    public ResponseEntity<Pessoa> getPessoa(@PathVariable Long id) {
        log.debug("REST request to get Pessoa : {}", id);
        Pessoa pessoa = pessoaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pessoa));
    }

    /**
     * DELETE  /pessoas/:id : delete the "id" pessoa.
     *
     * @param id the id of the pessoa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pessoas/{id}")
    @Timed
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        log.debug("REST request to delete Pessoa : {}", id);
        pessoaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
