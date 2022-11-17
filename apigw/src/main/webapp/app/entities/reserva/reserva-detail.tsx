import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserva.reducer';

export const ReservaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservaEntity = useAppSelector(state => state.apigw.reserva.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservaDetailsHeading">Reserva</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{reservaEntity.id}</dd>
          <dt>
            <span id="fechaInicio">Fecha Inicio</span>
          </dt>
          <dd>
            {reservaEntity.fechaInicio ? <TextFormat value={reservaEntity.fechaInicio} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="fechaFin">Fecha Fin</span>
          </dt>
          <dd>{reservaEntity.fechaFin ? <TextFormat value={reservaEntity.fechaFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Id Habitacion</dt>
          <dd>{reservaEntity.idHabitacion ? reservaEntity.idHabitacion.id : ''}</dd>
          <dt>Nro Doc</dt>
          <dd>{reservaEntity.nroDoc ? reservaEntity.nroDoc.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reserva" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserva/${reservaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservaDetail;
