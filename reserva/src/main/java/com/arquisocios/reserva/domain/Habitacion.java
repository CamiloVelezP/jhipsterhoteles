package com.arquisocios.reserva.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Habitacion.
 */
@Entity
@Table(name = "habitacion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Habitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @NotNull
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Float precio;

    @NotNull
    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @OneToMany(mappedBy = "idHabitacion")
    @JsonIgnoreProperties(value = { "idHabitacion", "nroDoc" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "habitacions" }, allowSetters = true)
    private Hotel idHotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Habitacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Habitacion ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getCapacidad() {
        return this.capacidad;
    }

    public Habitacion capacidad(Integer capacidad) {
        this.setCapacidad(capacidad);
        return this;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Habitacion precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Boolean getDisponible() {
        return this.disponible;
    }

    public Habitacion disponible(Boolean disponible) {
        this.setDisponible(disponible);
        return this;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setIdHabitacion(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setIdHabitacion(this));
        }
        this.reservas = reservas;
    }

    public Habitacion reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Habitacion addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setIdHabitacion(this);
        return this;
    }

    public Habitacion removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setIdHabitacion(null);
        return this;
    }

    public Hotel getIdHotel() {
        return this.idHotel;
    }

    public void setIdHotel(Hotel hotel) {
        this.idHotel = hotel;
    }

    public Habitacion idHotel(Hotel hotel) {
        this.setIdHotel(hotel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Habitacion)) {
            return false;
        }
        return id != null && id.equals(((Habitacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Habitacion{" +
            "id=" + getId() +
            ", ubicacion='" + getUbicacion() + "'" +
            ", capacidad=" + getCapacidad() +
            ", precio=" + getPrecio() +
            ", disponible='" + getDisponible() + "'" +
            "}";
    }
}
