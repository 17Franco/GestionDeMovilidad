# 🚗 GestionDeMovilidad

## 📖 Descripción del Proyecto

GestionDeMovilidad es un sistema que permite administrar el proceso completo de carga de vehículos eléctricos. Desde el registro de usuarios y medios de pago hasta el uso de estaciones de carga y el procesamiento de pagos, el sistema busca facilitar y organizar las distintas operaciones relacionadas con la movilidad eléctrica.

---

## 🎯 Objetivo

El objetivo del proyecto es desarrollar una aplicación que permita gestionar de forma simple y organizada el proceso de carga de vehículos eléctricos, aplicando una estructura modular para mantener el sistema ordenado y facilitar futuras mejoras y ampliaciones.
También busca aplicar conceptos como inyección de dependencias, comunicación mediante eventos y separación de responsabilidades entre módulos.

---

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

---

## 🛠 Tecnologías Utilizadas

- **Java**: lenguaje principal del proyecto.
- **Jakarta EE 10**: APIs utilizadas para el desarrollo de la aplicación.
- **WildFly**: servidor de aplicaciones utilizado para desplegar el proyecto.
- **Maven**: gestión de dependencias, compilación y empaquetado del proyecto.
- **JUnit 5**: ejecución de pruebas unitarias.
- **Mockito**: creación de mocks para pruebas.
- **AssertJ**: assertions más expresivas en los tests.
- **Lombok**: generación automática de código repetitivo como getters, setters y constructores.
- **JBoss Logging**: registro de mensajes y errores de la aplicación.
- **Bucket4j**: librería utilizada para implementar rate limiting, lo que permite limitar y controlar la cantidad de peticiones que recibe tu API para evitar sobrecargas o abusos.
- **Micrometer**: (InfluxDB): fachada de registro de métricas de la aplicación.

---

## 🏗 Arquitectura del Sistema

El sistema se encuentra organizado mediante una arquitectura modular.

La estructura se divide principalmente en:

- Dominio: contiene las entidades y reglas de negocio.
- Aplicación: contiene los casos de uso y lógica de aplicación.
- Interfaces: permite la comunicación entre módulos y el manejo de eventos.
- Infraestructura: contiene configuraciones y mecanismos de persistencia.


## 📂 Estructura del Proyecto
    
    src/main/java/
    │
    ├── FuncionalidadCargadorMOCK.aplicacion/
    │
    ├── infraestructura/
    │   ├── inicio/
    │   └── seguridad/
    │
    ├── moduloCarga/
    │   ├── aplicacion/
    │   ├── dominio/
    │   ├── infraestructura/
    │   └── interfaz/
    │
    ├── moduloCliente/
    │   ├── aplicacion/
    │   ├── dominio/
    │   ├── exepciones/
    │   ├── infraestructura/
    │   ├── interfaz/
    │   └── mapper/
    │
    ├── moduloMonitoreo/
    │   ├── infraestructura/
    │   └── interfaz.evento.in/
    │
    └── moduloPago/
        ├── aplicacion/
        ├── dominio/
        ├── infraestructura/
        └── interfaz/

---

## 📦 Módulos del Sistema

### 👤 Módulo Cliente
Responsable de la gestión de clientes registrados en el sistema, permitiendo el registro de usuarios, administración de medios de pago y realización de reclamos.

### ⚡ Módulo Carga
Responsable de administrar el proceso de carga de vehículos eléctricos, incluyendo el inicio y finalización de cargas, consulta de historial y gestión de estaciones y cargadores.

### 💳 Módulo Pago
Encargado de procesar y gestionar los pagos asociados a las cargas realizadas por los usuarios.

### 📊 Módulo Monitoreo
Encargado de la observabilidad del sistema. Recolecta métricas, procesa eventos de entrada (como telemetría o alertas) y facilita la integración con herramientas externas (como InfluxDB y Grafana) para supervisar la salud de la aplicación.

### 🛠️ Infraestructura Transversal (Inicio y Seguridad)
Gestiona configuraciones globales que afectan a toda la aplicación. Incluye la orquestación de arranque del sistema y el manejo de la seguridad, autenticación y autorización (utilizando Jakarta Security) para proteger los endpoints.

### 🔌 Funcionalidad Cargador MOCK
Módulo de apoyo utilizado para simular el comportamiento del hardware físico de los cargadores de vehículos eléctricos.

---
    
## 🚀 Manual de Despliegue y Ejecución

### 1. ⚙️ Requisitos Previos

Antes de ejecutar el proyecto es necesario tener instalado:
- Java JDK 21+
- Apache Maven
- MariaDB o MySQL (con la base de datos `Movilidad` creada)
- IDE compatible (visual studio code o IntelliJ IDEA)
- Docker
- Grafana e InfluxDB
- ollama en Contenedor Ollama

### 2. 🐳 Levantando la Infraestructura (Docker)
> ⚠️ **Nota:** Esta sección se ejecuta **solo la primera vez**, si todavía no existen los contenedores `docker-influxdb-grafana` y `ollama`. Si los contenedores ya existen en tu sistema, puedes saltar directamente al **Punto 3**.

1. Ver los contenedores existentes en el sistema:

        sudo docker ps -a
   
3. Crear el contenedor conjunto de InfluxDB + Grafana (Puertos: 3003 para Grafana, 8086 para InfluxDB y 3004/8083 para la interfaz administrativa):

       sudo docker run -d --name docker-influxdb-grafana -p 3003:3003 -p 8086:8086 -p 3004:8083 philhawthorne/docker-influxdb-grafana:latest
   
5. Crear el contenedor de Ollama:

       sudo docker run -d --name ollama -p 11434:11434 ollama/ollama

7. Descargar dentro del contenedor el modelo de lenguaje específico utilizado

       sudo docker exec ollama ollama pull qwen2.5:0.5b
   
10. Verificar que ambos contenedores quedaron creados correctamente:

        docker ps -a
   
        **Salida esperada (similar a esto):**
        
        CONTAINER ID   IMAGE                                          COMMAND               CREATED       STATUS       PORTS                                                                                                                                      NAMES
        b72d6975c156   ollama/ollama                                  "/bin/ollama serve"   3 hours ago   Up 3 hours   0.0.0.0:11434->11434/tcp, [::]:11434->11434/tcp                                                                                            ollama
        66d4427b9de8   philhawthorne/docker-influxdb-grafana:latest   "/run.sh"             3 hours ago   Up 3 hours   0.0.0.0:3003->3003/tcp, [::]:3003->3003/tcp, 0.0.0.0:8086->8086/tcp, [::]:8086->8086/tcp, 0.0.0.0:3004->8083/tcp, [::]:3004->8083/tcp   docker-influxdb-grafana    

### 3. ▶️ Arranque de la Infraestructura Docker
Si ya realizaste la instalación inicial, cada vez que vayas a trabajar en el proyecto debes asegurarte de iniciar los servicios con los siguientes comandos:

#### Paso A: Levantar InfluxDB y Grafana
1. Verificar el estado de los contenedores:
   ```bash
   docker ps -a
   ```
2. Iniciar el contenedor utilizado por el proyecto:
   ```bash
   docker start docker-influxdb-grafana
   ```
3. Verificar que esté corriendo (debe figurar con STATUS "Up"):
   ```bash
   docker ps
   ```

#### Paso B: Levantar Ollama
1. Iniciar el contenedor de Ollama:
   ```bash
   docker start ollama
   ```
2. Verificar que esté levantado y con el puerto 11434 publicado:
   ```bash
   docker ps
   ```
3. Comprobar que Ollama responda correctamente en el puerto esperado por el código de la aplicación:
   ```bash
   curl http://localhost:11434/api/tags
   ```
   **Salida esperada (similar a esto):**
   ```json
   {
     "models": [
       {
         "name": "qwen2.5:0.5b",
         "model": "qwen2.5:0.5b",
         "modified_at": "2026-07-02T17:43:26.150082696Z",
         "size": 397821319,
         "digest": "a8b0c51577010a279d933d14c2a8ab4b268079d44c5c8830c0a93900f1827c67",
         "details": {
           "parent_model": "",
           "format": "gguf",
           "family": "qwen2",
           "families": [
             "qwen2"
           ],
           "parameter_size": "494.03M",
           "quantization_level": "Q4_K_M",
           "context_length": 32768,
           "embedding_length": 896
         },
         "capabilities": [
           "completion",
           "tools"
         ]
       }
     ]
   }
   ```

---

### 4. 🗄️ Configuración de la Base de Datos


### 5. 🔧 Configuración del Entorno (Variables y Credenciales)
*(Indica si hay que configurar algún archivo `application.properties`, variables de entorno del sistema, o si el script `config.cli` de WildFly ya inyecta todo lo necesario).*

### 6. 🏗️ Compilación y Pruebas
*(Comandos para compilar el código fuente y verificar que los tests pasan antes de intentar ejecutar).*

### 7. ▶️ Ejecución del Proyecto
*(Aquí va tu comando `mvn clean package wildfly:dev -DskipTests` y cualquier instrucción adicional sobre el tiempo de arranque).*

### 8. ✅ Verificación del Despliegue (Puntos de Acceso)
*(Una lista con las URLs donde el desarrollador puede comprobar que todo levantó bien).*
- **API Principal:** `http://localhost:8080/GestionDeMovilidad/api/...`
- **Panel de Grafana:** `http://localhost:3000`
- **Ollama API:** `http://localhost:11434`

---

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
    
