import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getUser, getRoles, updateUser, createUser, reset } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = values => {
    if (isNew) {
      dispatch(createUser(values));
    } else {
      dispatch(updateUser(values));
    }
    handleClose();
  };

  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Crear o editar un usuario</h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              {user.id ? <ValidatedField type="text" name="id" required readOnly label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                type="text"
                name="login"
                label="Login"
                validate={{
                  required: {
                    value: true,
                    message: 'Su nombre de usuario es obligatorio.',
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: 'Su nombre de usuario no es válido.',
                  },
                  minLength: {
                    value: 1,
                    message: 'Su nombre de usuario debe tener al menos 1 caracter.',
                  },
                  maxLength: {
                    value: 50,
                    message: 'Su nombre de usuario no puede tener más de 50 caracteres.',
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label="Nombre"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'Este campo no puede superar más de 50 caracteres.',
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                label="Apellidos"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'Este campo no puede superar más de 50 caracteres.',
                  },
                }}
              />
              <FormText>This field cannot be longer than 50 characters.</FormText>
              <ValidatedField
                name="email"
                label="Correo electrónico"
                placeholder="Su correo electrónico"
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: 'Se requiere un correo electrónico.',
                  },
                  minLength: {
                    value: 5,
                    message: 'Se requiere que su correo electrónico tenga por lo menos 5 caracteres',
                  },
                  maxLength: {
                    value: 254,
                    message: 'Su correo electrónico no puede tener más de 50 caracteres',
                  },
                  validate: v => isEmail(v) || 'Su correo electrónico no es válido.',
                }}
              />
              <ValidatedField type="checkbox" name="activated" check value={true} disabled={!user.id} label="Activado" />
              <ValidatedField type="select" name="authorities" multiple label="Perfiles">
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Volver</span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
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

export default UserManagementUpdate;
