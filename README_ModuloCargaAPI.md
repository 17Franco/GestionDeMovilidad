# ModuloCargaAPI

Guia rapida para probar los endpoints de `ModuloCargaAPI.java`.

Base URL:

```text
http://localhost:8080/GestionDeMovilidad/movilidad/cargas
```

Antes de probar, conviene cargar los datos de prueba de [casosCarga.sql](casosCarga.sql). Ese script crea:

- Cliente con carga actual: `7654321-0`
- Cliente sin carga actual: `1111111-1`
- Cliente para iniciar carga con Cuenta UTE: `2222222-2`
- Cliente para iniciar carga con Tarjeta: `3333333-3`
- Cargador de prueba: `8001`
- Tarjeta de prueba: `12345678`

Formato esperado de cedula: `1234567-8`.

Formato esperado de tarjeta: 8 digitos, por ejemplo `12345678`.

## 1. Iniciar Carga

Endpoint:

```text
POST /iniciar
```

URL completa:

```text
http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar
```

Requisitos:

- Debe existir el cliente.
- Debe existir el cargador.
- Si `metodoPago` es `CUENTA_UTE`, el cliente debe ser comun y tener una Cuenta UTE asociada.
- Si `metodoPago` es `TARJETA`, la tarjeta debe existir y estar asociada al cliente.

### OK con Cuenta UTE

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"2222222-2","cargadorID":8001,"metodoPago":"CUENTA_UTE"}'
```

Respuesta esperada:

```text
HTTP/1.1 201 Created
```

```json
{"mensaje":"Carga iniciada correctamente con Cuenta de UTE"}
```

### OK con Tarjeta

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"3333333-3","cargadorID":8001,"metodoPago":"TARJETA","numeroTarjeta":"12345678"}'
```

Respuesta esperada:

```text
HTTP/1.1 201 Created
```

```json
{"mensaje":"Carga iniciada correctamente con Tarjeta"}
```

### Error: cliente no existe

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"9999999-9","cargadorID":8001,"metodoPago":"CUENTA_UTE"}'
```

Respuesta esperada:

```text
HTTP/1.1 404 Not Found
```

### Error: cargador no existe

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"2222222-2","cargadorID":9999,"metodoPago":"CUENTA_UTE"}'
```

Respuesta esperada:

```text
HTTP/1.1 404 Not Found
```

### Error: cedula con formato invalido

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"22222222","cargadorID":8001,"metodoPago":"CUENTA_UTE"}'
```

Respuesta esperada:

```text
HTTP/1.1 400 Bad Request
```

### Error: tarjeta no asociada al cliente

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/iniciar" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"3333333-3","cargadorID":8001,"metodoPago":"TARJETA","numeroTarjeta":"87654321"}'
```

Respuesta esperada:

```text
HTTP/1.1 400 Bad Request
```

## 2. Ver Carga Actual

Endpoint:

```text
GET /verCarga
```

URL completa:

```text
http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga
```

Requisitos:

- Debe existir el cliente.
- Para una respuesta OK, el cliente debe tener una carga actual asociada.

Nota: este endpoint esta implementado como `GET`, pero recibe JSON en el body.

### OK: cliente con carga actual

```bash
curl -i -X GET "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"7654321-0"}'
```

Respuesta esperada:

```text
HTTP/1.1 200 OK
```

### Error: cliente sin carga actual

```bash
curl -i -X GET "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1111111-1"}'
```

Respuesta esperada:

```text
HTTP/1.1 404 Not Found
```

### Error: cliente no existe

```bash
curl -i -X GET "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"9999999-9"}'
```

Respuesta esperada:

```text
HTTP/1.1 404 Not Found
```

### Error: cedula con formato invalido

```bash
curl -i -X GET "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verCarga" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"76543210"}'
```

Respuesta esperada:

```text
HTTP/1.1 400 Bad Request
```

## 3. Ver Historial

Endpoint:

```text
POST /verHistorial
```

URL completa:

```text
http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial
```

Requisitos:

- Debe existir el cliente.
- Para obtener una lista de cargas, el cliente debe tener historial asociado.

### OK: cliente con historial

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"7654321-0"}'
```

Respuesta esperada:

```text
HTTP/1.1 200 OK
```

### OK: cliente sin historial

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1111111-1"}'
```

Respuesta esperada:

```text
HTTP/1.1 200 OK
```

```json
{"mensaje":"El cliente no tiene cargas en el historial"}
```

### Error: cliente no existe

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"9999999-9"}'
```

Respuesta esperada:

```text
HTTP/1.1 404 Not Found
```

### Error: cedula con formato invalido

```bash
curl -i -X POST "http://localhost:8080/GestionDeMovilidad/movilidad/cargas/verHistorial" \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"76543210"}'
```

Respuesta esperada:

```text
HTTP/1.1 400 Bad Request
```
