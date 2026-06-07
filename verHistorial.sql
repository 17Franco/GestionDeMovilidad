START TRANSACTION;

SET @cedula_cliente = '7654321-0';
SET @medio_pago_id = 9001;

-- Aseguro que exista el cliente base
INSERT IGNORE INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    (@cedula_cliente, 'Santiago', 'Prueba', '099123456', '1234', NULL);

INSERT IGNORE INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    (@cedula_cliente, NULL);

-- Medio de pago de prueba: Cuenta UTE
INSERT IGNORE INTO MCarga_MedioPago
    (id, fechaCreacion)
VALUES
    (@medio_pago_id, CURDATE());

INSERT IGNORE INTO MCarga_CuentUte
    (id, numeroCuenta)
VALUES
    (@medio_pago_id, 'UTE-7654321');

UPDATE MCarga_ClienteComun
SET forma_pago_id = @medio_pago_id
WHERE cedula = @cedula_cliente;

-- Historial del cliente
INSERT INTO MCarga_HistorialesDeCargas
    (cliente_cedula)
VALUES
    (@cedula_cliente)
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id);

SET @historial_id = LAST_INSERT_ID();

-- Carga 1
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-01', '2026-05-01 08:10:00', '2026-05-01 09:05:00', 320.50, 0, 100, '2026-05-01 09:00:00', 'TERMINADO', @cedula_cliente, NULL);

SET @carga_1 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_1, @medio_pago_id, @historial_id);

-- Carga 2
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-08', '2026-05-08 14:30:00', '2026-05-08 15:20:00', 410.00, 25.00, 100, '2026-05-08 15:10:00', 'TERMINADO', @cedula_cliente, NULL);

SET @carga_2 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_2, @medio_pago_id, @historial_id);

-- Carga 3
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-05-15', '2026-05-15 18:00:00', '2026-05-15 18:45:00', 280.75, 0, 100, '2026-05-15 18:50:00', 'TERMINADO', @cedula_cliente, NULL);

SET @carga_3 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_3, @medio_pago_id, @historial_id);

-- Carga 4
INSERT INTO MCarga_Cargas
    (fecha, hora_inicio, hora_fin, importe_total, recargo_por_demora, porcentaje_avance, hora_estimada_fin, estado, cliente_cedula, cargador_id)
VALUES
    ('2026-06-02', '2026-06-02 10:15:00', '2026-06-02 11:25:00', 560.00, 40.00, 100, '2026-06-02 11:00:00', 'TERMINADO', @cedula_cliente, NULL);

SET @carga_4 = LAST_INSERT_ID();

INSERT INTO MCarga_ElementoHistorial
    (carga_id, medio_pago_id, historial_id)
VALUES
    (@carga_4, @medio_pago_id, @historial_id);

COMMIT;
