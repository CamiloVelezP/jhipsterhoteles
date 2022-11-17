import { IReserva } from 'app/shared/model/reserva.model';

export interface IUsuario {
  id?: number;
  nroDoc?: string;
  nombre?: string;
  email?: string;
  reservas?: IReserva[] | null;
}

export const defaultValue: Readonly<IUsuario> = {};
