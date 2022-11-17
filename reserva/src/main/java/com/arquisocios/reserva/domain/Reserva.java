package com.arquisocios.reserva.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private Instant fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private Instant fechaFin;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas", "idHotel" }, allowSetters = true)
    private Habitacion idHabitacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas" }, allowSetters = true)
    private Usuario nroDoc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reserva id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Reserva fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Reserva fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Habitacion getIdHabitacion() {
        return this.idHabitacion;
    }

    public void setIdHabitacion(Habitacion habitacion) {
        this.idHabitacion = habitacion;
    }

    public Reserva idHabitacion(Habitacion habitacion) {
        this.setIdHabitacion(habitacion);
        return this;
    }

    public Usuario getNroDoc() {
        return this.nroDoc;
    }

    public void setNroDoc(Usuario usuario) {
        this.nroDoc = usuario;
    }

    public Reserva nroDoc(Usuario usuario) {
        this.setNroDoc(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        return id != null && id.equals(((Reserva) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
