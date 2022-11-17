import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hotel.reducer';

export const HotelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hotelEntity = useAppSelector(state => state.apigw.hotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hotelDetailsHeading">Hotel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{hotelEntity.id}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{hotelEntity.nombre}</dd>
          <dt>
            <span id="cadena">Cadena</span>
          </dt>
          <dd>{hotelEntity.cadena}</dd>
          <dt>
            <span id="ciudad">Ciudad</span>
          </dt>
          <dd>{hotelEntity.ciudad}</dd>
          <dt>
            <span id="direccion">Direccion</span>
          </dt>
          <dd>{hotelEntity.direccion}</dd>
        </dl>
        <Button tag={Link} to="/hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hotel/${hotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HotelDetail;
