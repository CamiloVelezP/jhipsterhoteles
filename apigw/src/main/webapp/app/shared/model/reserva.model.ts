import dayjs from 'dayjs';
import { IHabitacion } from 'app/shared/model/habitacion.model';
import { IUsuario } from 'app/shared/model/usuario.model';

export interface IReserva {
  id?: number;
  fechaInicio?: string;
  fechaFin?: string;
  idHabitacion?: IHabitacion | null;
  nroDoc?: IUsuario | null;
}

export const defaultValue: Readonly<IReserva> = {};
