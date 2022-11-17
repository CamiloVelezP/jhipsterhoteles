import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHotel } from 'app/shared/model/hotel.model';
import { getEntities as getHotels } from 'app/entities/hotel/hotel.reducer';
import { IHabitacion } from 'app/shared/model/habitacion.model';
import { getEntity, updateEntity, createEntity, reset } from './habitacion.reducer';

export const HabitacionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hotels = useAppSelector(state => state.apigw.hotel.entities);
  const habitacionEntity = useAppSelector(state => state.apigw.habitacion.entity);
  const loading = useAppSelector(state => state.apigw.habitacion.loading);
  const updating = useAppSelector(state => state.apigw.habitacion.updating);
  const updateSuccess = useAppSelector(state => state.apigw.habitacion.updateSuccess);

  const handleClose = () => {
    navigate('/habitacion' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHotels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...habitacionEntity,
      ...values,
      idHotel: hotels.find(it => it.id.toString() === values.idHotel.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...habitacionEntity,
          idHotel: habitacionEntity?.idHotel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apigwApp.habitacion.home.createOrEditLabel" data-cy="HabitacionCreateUpdateHeading">
            Crear o editar Habitacion
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="habitacion-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Ubicacion"
                id="habitacion-ubicacion"
                name="ubicacion"
                data-cy="ubicacion"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Capacidad"
                id="habitacion-capacidad"
                name="capacidad"
                data-cy="capacidad"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                  validate: v => isNumber(v) || 'Este campo debe ser un número.',
                }}
              />
              <ValidatedField
                label="Precio"
                id="habitacion-precio"
                name="precio"
                data-cy="precio"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                  validate: v => isNumber(v) || 'Este campo debe ser un número.',
                }}
              />
              <ValidatedField label="Disponible" id="habitacion-disponible" name="disponible" data-cy="disponible" check type="checkbox" />
              <ValidatedField id="habitacion-idHotel" name="idHotel" data-cy="idHotel" label="Id Hotel" type="select">
                <option value="" key="0" />
                {hotels
                  ? hotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/habitacion" replace color="info">
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

export default HabitacionUpdate;
