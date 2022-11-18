import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './usuario.reducer';

export const UsuarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usuarioEntity = useAppSelector(state => state.apigw.usuario.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usuarioDetailsHeading">Usuario</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{usuarioEntity.id}</dd>
          <dt>
            <span id="nroDoc">Nro Doc</span>
          </dt>
          <dd>{usuarioEntity.nroDoc}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{usuarioEntity.nombre}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{usuarioEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/usuario" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/usuario/${usuarioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsuarioDetail;
