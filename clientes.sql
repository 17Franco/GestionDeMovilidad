START TRANSACTION;

SET @cedula_comun = '1111111-1';
SET @cedula_profesional = '2222222-2';

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM MCliente_Cliente_Grupo
WHERE MCliente_Cliente_cedula IN (@cedula_comun, @cedula_profesional);

DELETE FROM MCliente_ClienteComun
WHERE cedula IN (@cedula_comun, @cedula_profesional);

DELETE FROM MCliente_ClienteProfesional
WHERE cedula IN (@cedula_comun, @cedula_profesional);

DELETE FROM MCliente_Cliente
WHERE cedula IN (@cedula_comun, @cedula_profesional);

SET FOREIGN_KEY_CHECKS = 1;

INSERT IGNORE INTO Grupo (nombre)
VALUES ('appMovil');

-- Cliente común
INSERT INTO MCliente_Cliente
    (cedula, nombre, apellido, numTel, contra)
VALUES
    (@cedula_comun, 'Juan', 'Comun', '099111111', '1234');

INSERT INTO MCliente_ClienteComun
    (cedula)
VALUES
    (@cedula_comun);

-- Cliente profesional
INSERT INTO MCliente_Cliente
    (cedula, nombre, apellido, numTel, contra)
VALUES
    (@cedula_profesional, 'Pedro', 'Profesional', '099222222', '1234');

INSERT INTO MCliente_ClienteProfesional
    (cedula, tipo, porcentajeDescuento)
VALUES
    (@cedula_profesional, 'BASICO', 15);

-- Roles / grupos
INSERT INTO MCliente_Cliente_Grupo
    (MCliente_Cliente_cedula, grupos_nombre)
VALUES
    (@cedula_comun, 'appMovil');

INSERT INTO MCliente_Cliente_Grupo
    (MCliente_Cliente_cedula, grupos_nombre)
VALUES
    (@cedula_profesional, 'appMovil');

-- Para que ande la carga de de metodo de pago en modulo de carga se necesitan los clientes en ese modulo tamb.
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    ('1111111-1', 'Cliente', 'Prueba', '099111111', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    ('1111111-1', NULL);

COMMIT;