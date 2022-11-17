import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
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
          data-cy="resetPassword"
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
          data-cy="confirmResetPassword"
        />
        <Button color="success" type="submit" data-cy="submit">
          Validar la nueva contraseña
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="4">
          <h1>Restablecer la contraseña</h1>
          <div>{key ? getResetForm() : null}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;
