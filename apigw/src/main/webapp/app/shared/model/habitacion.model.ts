import { IReserva } from 'app/shared/model/reserva.model';
import { IHotel } from 'app/shared/model/hotel.model';

export interface IHabitacion {
  id?: number;
  ubicacion?: string;
  capacidad?: number;
  precio?: number;
  disponible?: boolean;
  reservas?: IReserva[] | null;
  idHotel?: IHotel | null;
}

export const defaultValue: Readonly<IHabitacion> = {
  disponible: false,
};
