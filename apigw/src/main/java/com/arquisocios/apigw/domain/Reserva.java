package com.arquisocios.apigw.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Reserva.
 */
@Table("reserva")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha_inicio")
    private Instant fechaInicio;

    @NotNull(message = "must not be null")
    @Column("fecha_fin")
    private Instant fechaFin;

    @Transient
    @JsonIgnoreProperties(value = { "reservas", "idHotel" }, allowSetters = true)
    private Habitacion idHabitacion;

    @Transient
    @JsonIgnoreProperties(value = { "reservas" }, allowSetters = true)
    private Usuario nroDoc;

    @Column("id_habitacion_id")
    private Long idHabitacionId;

    @Column("nro_doc_id")
    private Long nroDocId;

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
        this.idHabitacionId = habitacion != null ? habitacion.getId() : null;
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
        this.nroDocId = usuario != null ? usuario.getId() : null;
    }

    public Reserva nroDoc(Usuario usuario) {
        this.setNroDoc(usuario);
        return this;
    }

    public Long getIdHabitacionId() {
        return this.idHabitacionId;
    }

    public void setIdHabitacionId(Long habitacion) {
        this.idHabitacionId = habitacion;
    }

    public Long getNroDocId() {
        return this.nroDocId;
    }

    public void setNroDocId(Long usuario) {
        this.nroDocId = usuario;
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
