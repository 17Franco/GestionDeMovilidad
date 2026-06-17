# 🚗 GestionDeMovilidad

## 📖 Descripción del Proyecto

GestionDeMovilidad es un sistema que permite administrar el proceso completo de carga de vehículos eléctricos. Desde el registro de usuarios y medios de pago hasta el uso de estaciones de carga y el procesamiento de pagos, el sistema busca facilitar y organizar las distintas operaciones relacionadas con la movilidad eléctrica.

## 🎯 Objetivo

El objetivo del proyecto es desarrollar una aplicación que permita gestionar de forma simple y organizada el proceso de carga de vehículos eléctricos, aplicando una estructura modular para mantener el sistema ordenado y facilitar futuras mejoras y ampliaciones.
También busca aplicar conceptos como inyección de dependencias, comunicación mediante eventos y separación de responsabilidades entre módulos.

## ✨ Funcionalidades

- Registrar clientes.
- Agregar medios de pago.
- Gestionar estaciones y cargadores.
- Iniciar y finalizar cargas.
- Consultar cargas realizadas.
- Procesar pagos.
- Consultar pagos.
- Realizar reclamos.
- Visualizar estaciones disponibles.

## 🛠 Tecnologías Utilizadas

- **Java**: lenguaje principal del proyecto.
- **Jakarta EE 10**: APIs utilizadas para el desarrollo de la aplicación.
- **CDI / Weld**: inyección de dependencias y manejo de eventos.
- **WildFly**: servidor de aplicaciones utilizado para desplegar el proyecto.
- **Maven**: gestión de dependencias, compilación y empaquetado del proyecto.
- **JUnit 5**: ejecución de pruebas unitarias.
- **Mockito**: creación de mocks para pruebas.
- **AssertJ**: assertions más expresivas en los tests.
- **Lombok**: generación automática de código repetitivo como getters, setters y constructores.
- **JBoss Logging**: registro de mensajes y errores de la aplicación.

## 🏗 Arquitectura del Sistema

El sistema se encuentra organizado mediante una arquitectura modular.

La estructura se divide principalmente en:

- Dominio: contiene las entidades y reglas de negocio.
- Aplicación: contiene los casos de uso y lógica de aplicación.
- Interfaces: permite la comunicación entre módulos y el manejo de eventos.
- Infraestructura: contiene configuraciones y mecanismos de persistencia.


## 📂 Estructura del Proyecto

    src/
    │
    ├── ModuloCliente/
    │   ├── dominio/
    │   │   └── repositorio/
    │   │
    │   ├── aplicacion/
    │   │
    │   ├── interface/
    │   │
    │   └── infraestructura/
    │       ├── configuracion/
    │       └── persistencia/
    │
    ├── ModuloCarga/
    │   ├── dominio/
    │   │   └── repositorio/
    │   │
    │   ├── aplicacion/
    │   │
    │   ├── interface/
    │   │
    │   └── infraestructura/
    │       ├── configuracion/
    │       └── persistencia/
    │
    ├── ModuloPago/
    │   ├── dominio/
    │   │   └── repositorio/
    │   │
    │   ├── aplicacion/
    │   │
    │   ├── interface/
    │   │
    │   └── infraestructura/
    │       ├── configuracion/
    │       └── persistencia/

## 📦 Módulos del Sistema

### 👤 Módulo Cliente
Responsable de la gestión de clientes registrados en el sistema, permitiendo el registro de usuarios, administración de medios de pago y realización de reclamos.

### ⚡ Módulo Carga
Responsable de administrar el proceso de carga de vehículos eléctricos, incluyendo el inicio y finalización de cargas, consulta de historial y gestión de estaciones y cargadores.

### 💳 Módulo Pago
Encargado de procesar y gestionar los pagos asociados a las cargas realizadas por los usuarios.
    
## ⚙ Requisitos Previos

Antes de ejecutar el proyecto es necesario tener instalado:

- Java JDK 21+
- Apache Maven
- MariaDB
- IDE compatible (visual studio code o IntelliJ IDEA)

## 📥 Instalación

## ▶ Ejecutar el Proyecto

## 🌐 API / Endpoints

### Endpoints de ModuloCliente

| Método | Endpoint | Descripción | Consumido Por |
|---|---|---|---|
| POST | movilidad/clientes  | Permite registrar un usuario  | app móvil | 
| POST | movilidad/clientes/reclamos  | Permite a un usuario realizar un reclamo | app móvil |
| POST | movilidad/clientes/metodoPago | Permite agregar un metodo de pago al cliente | app móvil |
| GET | movilidad/clientes/obtener | Permite listar a todos los usuarios | app movil |
| POST | movilidad/clientes/medioPago | Permite cargar nuevos medios de pago | app movil |
---
### Endpoints de ModuloCarga

| Método | Endpoint | Descripción | Consumido Por |
|---|---|---|---|
| POST | movilidad/cargas/iniciar | Permite generar una carga nueva | app móvil |
| GET | movilidad/cargas/verCarga | Retorna la carga actual del cliente consultado | app móvil |
| POST | movilidad/cargas/verHistorial | Retorna el historial de cargas del cliente consultado | app móvil |

---
### Endpoints de ModuloPago
| Método | Endpoint | Descripción | Consumido Por |
|---|---|---|---|
| | | | | 
---
## Autenticación

Los endpoints consumidos por la appMovil requieren autenticación mediante usuario y contraseña.

## 📊 Diagrama del Sistema

![Diagrama del Sistema](imgReadme/Diagrama%20De%20modulos.drawio.png)

## 🧪 Testing

El proyecto incluye pruebas para validar el correcto funcionamiento de los principales casos de uso implementados:

- **TestModuloCarga**
  - Verifica la creación de estaciones, cargadores, IniciarCarga, VerHistorial Carga, eventos, y FinalizarCarga.

- **TestModuloCliente**
  - Verifica la cracion del cliente y Medio de Pagos.
    

