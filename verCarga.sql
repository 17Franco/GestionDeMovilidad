START TRANSACTION;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM MCarga_Cargas
WHERE cliente_cedula IN ('7654321-0', '1111111-1');

DELETE FROM MCarga_ClienteComun
WHERE cedula IN ('7654321-0', '1111111-1');

DELETE FROM MCarga_Cliente
WHERE cedula IN ('7654321-0', '1111111-1');

SET FOREIGN_KEY_CHECKS = 1;

-- Cliente CON carga actual: debería devolver 200 OK
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    ('7654321-0', 'Santiago', 'Prueba', '099123456', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    ('7654321-0', NULL);

INSERT INTO MCarga_Cargas
    (
        fecha,
        hora_inicio,
        hora_fin,
        importe_total,
        recargo_por_demora,
        porcentaje_avance,
        hora_estimada_fin,
        estado,
        cliente_cedula,
        cargador_id
    )
VALUES
    (
        CURDATE(),
        NOW(),
        NULL,
        250.50,
        0,
        45,
        DATE_ADD(NOW(), INTERVAL 35 MINUTE),
        'ENPROGRESO',
        '7654321-0',
        NULL
    );

SET @id_carga_actual = LAST_INSERT_ID();

UPDATE MCarga_Cliente
SET carga_actual_id = @id_carga_actual
WHERE cedula = '7654321-0';

-- Cliente SIN carga actual: debería devolver 404 NOT FOUND
INSERT INTO MCarga_Cliente
    (cedula, nombre, apellido, numTel, contra, carga_actual_id)
VALUES
    ('1111111-1', 'Cliente', 'SinCarga', '099000000', '1234', NULL);

INSERT INTO MCarga_ClienteComun
    (cedula, forma_pago_id)
VALUES
    ('1111111-1', NULL);

COMMIT;
