import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
            Contraseña de [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label="Contraseña actual"
              placeholder="Contraseña actual"
              type="password"
              validate={{
                required: { value: true, message: 'Se requiere que ingrese una contraseña.' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label="Nueva contraseña"
              placeholder="Nueva contraseña"
              type="password"
              validate={{
                required: { value: true, message: 'Se requiere que ingrese una contraseña.' },
                minLength: { value: 4, message: 'Se requiere que su contraseña tenga por lo menos 4 caracteres' },
                maxLength: { value: 50, message: 'Su contraseña no puede tener más de 50 caracteres' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label="Confirmación de la nueva contraseña"
              placeholder="Confirmación de la nueva contraseña"
              type="password"
              validate={{
                required: { value: true, message: 'Se requiere que confirme la contraseña.' },
                minLength: { value: 4, message: 'Se requiere que su contraseña de confirmación tenga por lo menos 4 caracteres' },
                maxLength: { value: 50, message: 'Su contraseña de confirmación no puede tener más de 50 caracteres' },
                validate: v => v === password || '¡La contraseña y la confirmación de contraseña no coinciden!',
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              Guardar
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;
