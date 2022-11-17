import usuario from 'app/entities/usuario/usuario.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import hotel from 'app/entities/hotel/hotel.reducer';
import habitacion from 'app/entities/habitacion/habitacion.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  usuario,
  reserva,
  hotel,
  habitacion,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
