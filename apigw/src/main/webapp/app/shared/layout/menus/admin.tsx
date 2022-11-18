import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavDropdown } from './menu-components';

const adminMenuItems = () => (
  <>
    <MenuItem icon="road" to="/admin/gateway">
      Gateway
    </MenuItem>
    <MenuItem icon="users" to="/admin/user-management">
      Gestión de usuarios
    </MenuItem>
    <MenuItem icon="tachometer-alt" to="/admin/metrics">
      Métricas
    </MenuItem>
    <MenuItem icon="heart" to="/admin/health">
      Salud
    </MenuItem>
    <MenuItem icon="cogs" to="/admin/configuration">
      Configuración
    </MenuItem>
    <MenuItem icon="tasks" to="/admin/logs">
      Logs
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/habitacion">
      Habitacion
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/hotel">
      Hotel
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/usuario">
      Usuario
    </MenuItem>
    {/* jhipster-needle-add-element-to-admin-menu - JHipster will add entities to the admin menu here */}
  </>
);

const openAPIItem = () => (
  <MenuItem icon="book" to="/admin/docs">
    API
  </MenuItem>
);

const databaseItem = () => (
  <DropdownItem tag="a" href="http://localhost:8092/" target="_tab">
    <FontAwesomeIcon icon="database" fixedWidth /> Base de datos
  </DropdownItem>
);

export const AdminMenu = ({ showOpenAPI, showDatabase }) => (
  <NavDropdown icon="users-cog" name="Administración" id="admin-menu" data-cy="adminMenu">
    {adminMenuItems()}
    {showOpenAPI && openAPIItem()}

    {showDatabase && databaseItem()}
  </NavDropdown>
);

export default AdminMenu;
