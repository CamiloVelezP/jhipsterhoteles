package com.arquisocios.reserva.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.arquisocios.reserva.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HabitacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Habitacion.class);
        Habitacion habitacion1 = new Habitacion();
        habitacion1.setId(1L);
        Habitacion habitacion2 = new Habitacion();
        habitacion2.setId(habitacion1.getId());
        assertThat(habitacion1).isEqualTo(habitacion2);
        habitacion2.setId(2L);
        assertThat(habitacion1).isNotEqualTo(habitacion2);
        habitacion1.setId(null);
        assertThat(habitacion1).isNotEqualTo(habitacion2);
    }
}
