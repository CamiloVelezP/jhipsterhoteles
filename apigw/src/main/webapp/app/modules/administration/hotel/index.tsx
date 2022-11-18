import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Hotel from './hotel';
import HotelDetail from './hotel-detail';
import HotelUpdate from './hotel-update';
import HotelDeleteDialog from './hotel-delete-dialog';

const HotelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Hotel />} />
    <Route path="new" element={<HotelUpdate />} />
    <Route path=":id">
      <Route index element={<HotelDetail />} />
      <Route path="edit" element={<HotelUpdate />} />
      <Route path="delete" element={<HotelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HotelRoutes;
