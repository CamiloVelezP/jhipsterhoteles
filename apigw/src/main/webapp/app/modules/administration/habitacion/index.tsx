import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Habitacion from './habitacion';
import HabitacionDetail from './habitacion-detail';
import HabitacionUpdate from './habitacion-update';
import HabitacionDeleteDialog from './habitacion-delete-dialog';

const HabitacionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Habitacion />} />
    <Route path="new" element={<HabitacionUpdate />} />
    <Route path=":id">
      <Route index element={<HabitacionDetail />} />
      <Route path="edit" element={<HabitacionUpdate />} />
      <Route path="delete" element={<HabitacionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HabitacionRoutes;
