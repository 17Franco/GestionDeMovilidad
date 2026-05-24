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

## 🏗 Arquitectura del Sistema

El sistema se encuentra organizado mediante una arquitectura en modular.

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
    
## ⚙ Requisitos Previos

## 📥 Instalación

## ▶ Ejecutar el Proyecto

## 🌐 API / Endpoints

## 📦 Módulos del Sistema

### Módulo Cliente

### Módulo Carga

### Módulo Pago

## 📊 Diagrama del Sistema
