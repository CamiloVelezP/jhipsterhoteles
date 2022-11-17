import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Usuario from './usuario';
import Reserva from './reserva';
import Hotel from './hotel';
import Habitacion from './habitacion';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('apigw', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="usuario/*" element={<Usuario />} />
        <Route path="reserva/*" element={<Reserva />} />
        <Route path="hotel/*" element={<Hotel />} />
        <Route path="habitacion/*" element={<Habitacion />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
