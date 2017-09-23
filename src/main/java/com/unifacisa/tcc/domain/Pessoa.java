package com.unifacisa.tcc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.unifacisa.tcc.domain.enumeration.TipoDeUsuario;

/**
 * A Pessoa.
 */
@Entity
@Table(name = "pessoa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "primeiro_nome", nullable = false)
    private String primeiroNome;

    @NotNull
    @Size(min = 3)
    @Column(name = "segundo_nome", nullable = false)
    private String segundoNome;

    @NotNull
    @Column(name = "matricula", nullable = false)
    private Long matricula;

    @NotNull
    @Size(min = 3)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "numero_contato", nullable = false)
    private Long numeroContato;

    @Column(name = "diponibilidade")
    private Boolean diponibilidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_usuario", nullable = false)
    private TipoDeUsuario tipoDeUsuario;

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pessoa> alunos = new HashSet<>();

    @OneToOne(mappedBy = "pessoa")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    private Pessoa professor;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public Pessoa primeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
        return this;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSegundoNome() {
        return segundoNome;
    }

    public Pessoa segundoNome(String segundoNome) {
        this.segundoNome = segundoNome;
        return this;
    }

    public void setSegundoNome(String segundoNome) {
        this.segundoNome = segundoNome;
    }

    public Long getMatricula() {
        return matricula;
    }

    public Pessoa matricula(Long matricula) {
        this.matricula = matricula;
        return this;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public Pessoa email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNumeroContato() {
        return numeroContato;
    }

    public Pessoa numeroContato(Long numeroContato) {
        this.numeroContato = numeroContato;
        return this;
    }

    public void setNumeroContato(Long numeroContato) {
        this.numeroContato = numeroContato;
    }

    public Boolean isDiponibilidade() {
        return diponibilidade;
    }

    public Pessoa diponibilidade(Boolean diponibilidade) {
        this.diponibilidade = diponibilidade;
        return this;
    }

    public void setDiponibilidade(Boolean diponibilidade) {
        this.diponibilidade = diponibilidade;
    }

    public TipoDeUsuario getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public Pessoa tipoDeUsuario(TipoDeUsuario tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
        return this;
    }

    public void setTipoDeUsuario(TipoDeUsuario tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
    }

    public Set<Pessoa> getAlunos() {
        return alunos;
    }

    public Pessoa alunos(Set<Pessoa> pessoas) {
        this.alunos = pessoas;
        return this;
    }

    public Pessoa addAluno(Pessoa pessoa) {
        this.alunos.add(pessoa);
        pessoa.setProfessor(this);
        return this;
    }

    public Pessoa removeAluno(Pessoa pessoa) {
        this.alunos.remove(pessoa);
        pessoa.setProfessor(null);
        return this;
    }

    public void setAlunos(Set<Pessoa> pessoas) {
        this.alunos = pessoas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Pessoa usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pessoa getProfessor() {
        return professor;
    }

    public Pessoa professor(Pessoa pessoa) {
        this.professor = pessoa;
        return this;
    }

    public void setProfessor(Pessoa pessoa) {
        this.professor = pessoa;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pessoa pessoa = (Pessoa) o;
        if (pessoa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pessoa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pessoa{" +
            "id=" + getId() +
            ", primeiroNome='" + getPrimeiroNome() + "'" +
            ", segundoNome='" + getSegundoNome() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroContato='" + getNumeroContato() + "'" +
            ", diponibilidade='" + isDiponibilidade() + "'" +
            ", tipoDeUsuario='" + getTipoDeUsuario() + "'" +
            "}";
    }
}
