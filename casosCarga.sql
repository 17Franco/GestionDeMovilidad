START TRANSACTION;

SET FOREIGN_KEY_CHECKS = 0;

SET @cedula_cliente = '7654321-0';
SET @cedula_sin_carga = '1111111-1';
SET @cedula_iniciar_ute = '2222222-2';
SET @cedula_iniciar_tarjeta = '3333333-3';

SET @estacion_id = 8001;
SET @cargador_id = 8001;
SET @medio_pago_historial_ute_id = 9001;
SET @medio_pago_tarjeta_id = 9002;
SET @medio_pago_iniciar_ute_id = 9003;
SET @numero_tarjeta = '12345678';

DELETE FROM MCarga_ElementoHistorial
WHERE historial_id IN (
    SELECT id
    FROM MCarga_HistorialesDeCargas
    WHERE cliente_cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta)
)
OR carga_id IN (
    SELECT id
    FROM MCarga_Cargas
    WHERE cliente_cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta)
)
OR medio_pago_id IN (@medio_pago_historial_ute_id, @medio_pago_tarjeta_id, @medio_pago_iniciar_ute_id);

DELETE FROM MCarga_HistorialesDeCargas
WHERE cliente_cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta);

UPDATE MCarga_Cliente
SET carga_actual_id = NULL
WHERE cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta);

DELETE FROM MCarga_Cargas
WHERE cliente_cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta)
   OR cargador_id = @cargador_id;

DELETE FROM MCarga_Tarjeta
WHERE id IN (@medio_pago_tarjeta_id)
   OR cliente_id IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta)
   OR numero = @numero_tarjeta;

DELETE FROM MCarga_CuentUte
WHERE id IN (@medio_pago_historial_ute_id, @medio_pago_iniciar_ute_id);

DELETE FROM MCarga_MedioPago
WHERE id IN (@medio_pago_historial_ute_id, @medio_pago_tarjeta_id, @medio_pago_iniciar_ute_id);

DELETE FROM MCarga_ClienteComun
WHERE cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta);

DELETE FROM MCarga_Cliente
WHERE cedula IN (@cedula_cliente, @cedula_sin_carga, @cedula_iniciar_ute, @cedula_iniciar_tarjeta);

DELETE FROM MCarga_Cargadores
WHERE id = @cargador_id;

DELETE FROM MCarga_EstacionCarga
WHERE id = @estacion_id;

SET FOREIGN_KEY_CHECKS = 1;

-- Estacion y cargador para probar iniciarCarga.
INSERT INTO MCarga_EstacionCarga
    (id, descripcion, calle, departamento, longitud, latitud)
VALUES
    (@estacion_id, 'Estacion de prueba', 'Av. Italia 1234', 'Montevideo', -56, -34);

INSERT INTO MCarga_Cargadores
    (id, tipo_cargador, tiene_cable, tipo_conector, estado_cargador, fecha_estimada_finalizacion, potencia_minima, estacion_carga_id)
VALUES
    (@cargador_id, 'NORMAL', 1, 'GRANDE', 'OPERATIVO', NULL, 22, @estacion_id);

-- Cliente CON carga actual: verCarga deberia devolver 200 OK.
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    (@cedula_cliente, 'Santiago', 'Prueba', '099123456', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    (@cedula_cliente, NULL);

INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    (CURDATE(), NOW(), NULL, 250.50, 0, 45, DATE_ADD(NOW(), INTERVAL 35 MINUTE), 'ENPROGRESO', @cedula_cliente, @cargador_id);

SET @id_carga_actual = LAST_INSERT_ID();

UPDATE MCarga_Cliente
SET carga_actual_id = @id_carga_actual
WHERE cedula = @cedula_cliente;

-- Cliente SIN carga actual: verCarga deberia devolver 404 NOT FOUND.
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    (@cedula_sin_carga, 'Cliente', 'SinCarga', '099000000', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    (@cedula_sin_carga, NULL);

-- Medio de pago de prueba para verHistorial e iniciarCarga con Cuenta UTE.
INSERT INTO MCarga_MedioPago
    (id, fechaCreacion)
VALUES
    (@medio_pago_historial_ute_id, CURDATE());

INSERT INTO MCarga_CuentUte
    (id, numeroCuenta)
VALUES
    (@medio_pago_historial_ute_id, 'UTE-7654321');

UPDATE MCarga_ClienteComun
SET forma_pago_id = @medio_pago_historial_ute_id
WHERE cedula = @cedula_cliente;

-- Historial del cliente.
INSERT INTO MCarga_HistorialesDeCargas
    (cliente_cedula)
VALUES
    (@cedula_cliente);

SET @historial_id = LAST_INSERT_ID();

-- Carga 1 del historial.
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-01', '2026-05-01 08:10:00', '2026-05-01 09:05:00', 320.50, 0, 100, '2026-05-01 09:00:00', 'TERMINADO', @cedula_cliente, @cargador_id);

SET @carga_1 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_1, @medio_pago_historial_ute_id, @historial_id);

-- Carga 2 del historial.
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-08', '2026-05-08 14:30:00', '2026-05-08 15:20:00', 410.00, 25.00, 100, '2026-05-08 15:10:00', 'TERMINADO', @cedula_cliente, @cargador_id);

SET @carga_2 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_2, @medio_pago_historial_ute_id, @historial_id);

-- Carga 3 del historial.
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-15', '2026-05-15 18:00:00', '2026-05-15 18:45:00', 280.75, 0, 100, '2026-05-15 18:50:00', 'TERMINADO', @cedula_cliente, @cargador_id);

SET @carga_3 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_3, @medio_pago_historial_ute_id, @historial_id);

-- Carga 4 del historial.
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-06-02', '2026-06-02 10:15:00', '2026-06-02 11:25:00', 560.00, 40.00, 100, '2026-06-02 11:00:00', 'TERMINADO', @cedula_cliente, @cargador_id);

SET @carga_4 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_4, @medio_pago_historial_ute_id, @historial_id);

-- Cliente para iniciarCarga con CUENTA_UTE: deberia devolver 201 CREATED.
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    (@cedula_iniciar_ute, 'Cliente', 'IniciaUTE', '099222222', '1234', NULL);

INSERT INTO MCarga_MedioPago
    (id, fechaCreacion)
VALUES
    (@medio_pago_iniciar_ute_id, CURDATE());

INSERT INTO MCarga_CuentUte
    (id, numeroCuenta)
VALUES
    (@medio_pago_iniciar_ute_id, 'UTE-2222222');

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    (@cedula_iniciar_ute, @medio_pago_iniciar_ute_id);

-- Cliente para iniciarCarga con TARJETA: deberia devolver 201 CREATED si el DTO recibe el numero.
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    (@cedula_iniciar_tarjeta, 'Cliente', 'IniciaTarjeta', '099333333', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    (@cedula_iniciar_tarjeta, NULL);

INSERT INTO MCarga_MedioPago
    (id, fechaCreacion)
VALUES
    (@medio_pago_tarjeta_id, CURDATE());

INSERT INTO MCarga_Tarjeta
    (id, numero, fechaVencimiento, digitoVerificadocion, tipo, cliente_id)
VALUES
    (@medio_pago_tarjeta_id, @numero_tarjeta, '2030-12-31', '123', NULL, @cedula_iniciar_tarjeta);

COMMIT;

-- Requests utiles:
--
-- verCarga 200:
-- GET http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga
-- { "cedulaCliente": "7654321-0" }
--
-- verCarga 404:
-- GET http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga
-- { "cedulaCliente": "1111111-1" }
--
-- verHistorial 200:
-- POST http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial
-- { "cedulaCliente": "7654321-0" }
--
-- iniciarCarga con CUENTA_UTE 201:
-- POST http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
-- { "cedulaCliente": "2222222-2", "cargadorID": 8001, "metodoPago": "CUENTA_UTE" }
--
-- iniciarCarga con TARJETA 201:
-- POST http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
-- { "cedulaCliente": "3333333-3", "cargadorID": 8001, "metodoPago": "TARJETA", "numeroTarjeta": "12345678" }
