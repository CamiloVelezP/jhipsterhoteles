import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHabitacion } from 'app/shared/model/habitacion.model';
import { getEntities as getHabitacions } from 'app/entities/habitacion/habitacion.reducer';
import { IUsuario } from 'app/shared/model/usuario.model';
import { getEntities as getUsuarios } from 'app/entities/usuario/usuario.reducer';
import { IReserva } from 'app/shared/model/reserva.model';
import { getEntity, updateEntity, createEntity, reset } from './reserva.reducer';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const habitacions = useAppSelector(state => state.apigw.habitacion.entities);
  const usuarios = useAppSelector(state => state.apigw.usuario.entities);
  const reservaEntity = useAppSelector(state => state.apigw.reserva.entity);
  const loading = useAppSelector(state => state.apigw.reserva.loading);
  const updating = useAppSelector(state => state.apigw.reserva.updating);
  const updateSuccess = useAppSelector(state => state.apigw.reserva.updateSuccess);

  const handleClose = () => {
    navigate('/reserva' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHabitacions({}));
    dispatch(getUsuarios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaInicio = convertDateTimeToServer(values.fechaInicio);
    values.fechaFin = convertDateTimeToServer(values.fechaFin);

    const entity = {
      ...reservaEntity,
      ...values,
      idHabitacion: habitacions.find(it => it.id.toString() === values.idHabitacion.toString()),
      nroDoc: usuarios.find(it => it.id.toString() === values.nroDoc.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaInicio: displayDefaultDateTime(),
          fechaFin: displayDefaultDateTime(),
        }
      : {
          ...reservaEntity,
          fechaInicio: convertDateTimeFromServer(reservaEntity.fechaInicio),
          fechaFin: convertDateTimeFromServer(reservaEntity.fechaFin),
          idHabitacion: reservaEntity?.idHabitacion?.id,
          nroDoc: reservaEntity?.nroDoc?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apigwApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
            Crear o editar Reserva
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="reserva-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Fecha Inicio"
                id="reserva-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Fecha Fin"
                id="reserva-fechaFin"
                name="fechaFin"
                data-cy="fechaFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField id="reserva-idHabitacion" name="idHabitacion" data-cy="idHabitacion" label="Id Habitacion" type="select">
                <option value="" key="0" />
                {habitacions
                  ? habitacions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="reserva-nroDoc" name="nroDoc" data-cy="nroDoc" label="Nro Doc" type="select">
                <option value="" key="0" />
                {usuarios
                  ? usuarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reserva" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Volver</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Guardar
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservaUpdate;
