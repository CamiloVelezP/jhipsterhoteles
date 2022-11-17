import { IHabitacion } from 'app/shared/model/habitacion.model';

export interface IHotel {
  id?: number;
  nombre?: string;
  cadena?: string;
  ciudad?: string;
  direccion?: string;
  habitacions?: IHabitacion[] | null;
}

export const defaultValue: Readonly<IHotel> = {};
