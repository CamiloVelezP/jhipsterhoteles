import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './habitacion.reducer';

export const HabitacionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const habitacionEntity = useAppSelector(state => state.apigw.habitacion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="habitacionDetailsHeading">Habitacion</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{habitacionEntity.id}</dd>
          <dt>
            <span id="ubicacion">Ubicacion</span>
          </dt>
          <dd>{habitacionEntity.ubicacion}</dd>
          <dt>
            <span id="capacidad">Capacidad</span>
          </dt>
          <dd>{habitacionEntity.capacidad}</dd>
          <dt>
            <span id="precio">Precio</span>
          </dt>
          <dd>{habitacionEntity.precio}</dd>
          <dt>
            <span id="disponible">Disponible</span>
          </dt>
          <dd>{habitacionEntity.disponible ? 'true' : 'false'}</dd>
          <dt>Id Hotel</dt>
          <dd>{habitacionEntity.idHotel ? habitacionEntity.idHotel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/habitacion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/habitacion/${habitacionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HabitacionDetail;
