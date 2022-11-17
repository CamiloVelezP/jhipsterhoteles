package com.arquisocios.reserva.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "hotel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "cadena", nullable = false)
    private String cadena;

    @NotNull
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "idHotel")
    @JsonIgnoreProperties(value = { "reservas", "idHotel" }, allowSetters = true)
    private Set<Habitacion> habitacions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hotel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Hotel nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCadena() {
        return this.cadena;
    }

    public Hotel cadena(String cadena) {
        this.setCadena(cadena);
        return this;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Hotel ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Hotel direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<Habitacion> getHabitacions() {
        return this.habitacions;
    }

    public void setHabitacions(Set<Habitacion> habitacions) {
        if (this.habitacions != null) {
            this.habitacions.forEach(i -> i.setIdHotel(null));
        }
        if (habitacions != null) {
            habitacions.forEach(i -> i.setIdHotel(this));
        }
        this.habitacions = habitacions;
    }

    public Hotel habitacions(Set<Habitacion> habitacions) {
        this.setHabitacions(habitacions);
        return this;
    }

    public Hotel addHabitacion(Habitacion habitacion) {
        this.habitacions.add(habitacion);
        habitacion.setIdHotel(this);
        return this;
    }

    public Hotel removeHabitacion(Habitacion habitacion) {
        this.habitacions.remove(habitacion);
        habitacion.setIdHotel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hotel)) {
            return false;
        }
        return id != null && id.equals(((Hotel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hotel{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cadena='" + getCadena() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", direccion='" + getDireccion() + "'" +
            "}";
    }
}
