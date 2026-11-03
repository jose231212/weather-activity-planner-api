# Weather Activity Planner API

[English](README.md) | [Español](README.es.md)

Una API REST para gestionar actividades planificadas y evaluar las condiciones meteorológicas que podrían afectarlas.

Desarrollé este proyecto para practicar la creación de aplicaciones backend con Java y Spring Boot. Integra la gestión de eventos, el consumo de datos meteorológicos externos, un servicio de toma de decisiones, persistencia en base de datos, documentación de la API y pruebas automatizadas.

La aplicación utiliza Open-Meteo para obtener información de ubicación y pronósticos meteorológicos. Su lógica de evaluación compara las condiciones del clima con los límites definidos para cada actividad y determina si puede mantenerse como estaba planificada, si debe reprogramarse o si debe cancelarse.

## Funcionalidades

- Crear, consultar, actualizar y eliminar eventos.
- Almacenar los datos de los eventos y sus límites meteorológicos en MySQL.
- Consultar información de ubicación de ciudades mediante el servicio de geocodificación de Open-Meteo.
- Obtener datos de pronósticos por hora mediante el servicio de pronósticos de Open-Meteo.
- Evaluar la temperatura, la probabilidad de precipitación y la velocidad del viento según los límites de cada actividad.
- Generar una de tres decisiones: `MAINTAIN`, `RESCHEDULE` o `CANCEL`.
- Consultar eventos mediante paginación y ordenamiento.
- Validar los datos recibidos y los rangos de temperatura de los eventos.
- Gestionar errores mediante un manejador centralizado de excepciones.
- Documentar la API REST con Swagger UI y OpenAPI.
- Incluir pruebas unitarias, de persistencia y de integración de controladores.

## Tecnologías utilizadas

- **Java 21** — lenguaje de programación.
- **Spring Boot 4.1.2** — framework principal.
- **Spring Web MVC** — endpoints REST y manejo de solicitudes HTTP.
- **Spring Data JPA** — acceso y persistencia de datos.
- **Hibernate** — mapeo objeto-relacional.
- **MySQL** — base de datos relacional.
- **Maven** — gestión de dependencias y construcción del proyecto.
- **Lombok** — reducción de código repetitivo.
- **Open-Meteo** — geocodificación y datos de pronósticos meteorológicos.
- **JUnit 5 y Mockito** — pruebas automatizadas.
- **Springdoc OpenAPI / Swagger UI** — documentación de la API.
- **Docker y Docker Compose** — contenerización y ejecución de la aplicación junto con MySQL.

## Requisitos

Antes de ejecutar la aplicación, asegúrate de tener:

- JDK 21.
- MySQL instalado y en ejecución, o acceso a un servidor MySQL.
- Git, si deseas clonar el repositorio.

El proyecto incluye Maven Wrapper, por lo que no necesitas instalar Maven por separado para compilar y ejecutar la aplicación.

Para ejecutar la aplicación con Docker, también necesitas Docker Desktop (o Docker Engine) y Docker Compose.

## Configuración

Crea una base de datos MySQL llamada `apiweather`:

```sql
CREATE DATABASE apiweather;
```

La aplicación obtiene la contraseña de la base de datos mediante la variable de entorno `DB_PASSWORD`. Configúrala antes de iniciar la aplicación. No incluyas credenciales reales en el repositorio.

En PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

La configuración local predeterminada de la base de datos es:

- **Base de datos:** `apiweather`
- **URL:** `jdbc:mysql://localhost:3306/apiweather`
- **Usuario:** `root`

Revisa `src/main/resources/application.properties` si tu host, puerto, nombre de base de datos o usuario de MySQL son diferentes.

Las URL base de los servicios de Open-Meteo también están configuradas en `application.properties`. La aplicación utiliza las API públicas de geocodificación y pronósticos de Open-Meteo.

## Ejecutar la aplicación

### Ejecución local con Maven

Desde la raíz del proyecto, inicia la aplicación utilizando Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

También puedes ejecutar la clase principal de la aplicación desde tu IDE.

Una vez iniciada, la URL local predeterminada es:

`http://localhost:8080`

### Ejecución con Docker

La raíz del proyecto incluye un archivo `Dockerfile` y un archivo `compose.yaml` para ejecutar la API y MySQL mediante Docker Compose.

Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:

```dotenv
MYSQL_ROOT_PASSWORD=your_mysql_password
MYSQL_DATABASE=apiweather
DB_PASSWORD=your_mysql_password
```

Utiliza una contraseña segura y asegúrate de que `MYSQL_ROOT_PASSWORD` y `DB_PASSWORD` tengan el mismo valor. El archivo `.env` contiene credenciales y no debe subirse al repositorio.

Construye la imagen y levanta los servicios:

```bash
docker compose up -d --build
```

Docker Compose inicia MySQL y espera a que su comprobación de salud indique que está listo antes de iniciar la API.

Para consultar el estado de los contenedores:

```bash
docker compose ps
```

Para consultar los registros de la API:

```bash
docker compose logs -f api
```

La API estará disponible en `http://localhost:8080`.

Para detener los servicios:

```bash
docker compose down
```

Los datos de MySQL se almacenan en un volumen de Docker, por lo que permanecen disponibles aunque elimines y vuelvas a crear los contenedores. `docker compose down` no elimina ese volumen.

Si deseas eliminar también el volumen y todos los datos almacenados en la base de datos, ejecuta:

```bash
docker compose down -v
```

**Importante:** este último comando elimina permanentemente los datos guardados en el volumen.

## Documentación de la API

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Documento OpenAPI:

`http://localhost:8080/v3/api-docs`

Puedes utilizar Swagger UI para consultar las operaciones disponibles, los parámetros de las solicitudes, los formatos de respuesta y los errores documentados.

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/events` | Crear un evento |
| `GET` | `/api/events` | Consultar eventos con paginación y ordenamiento |
| `GET` | `/api/events/{id}` | Consultar un evento por su ID |
| `PUT` | `/api/events/{id}` | Actualizar un evento existente |
| `DELETE` | `/api/events/{id}` | Eliminar un evento |

## Lógica de evaluación meteorológica

El servicio de evaluación compara los datos del pronóstico por hora con los límites configurados para una actividad.

Tiene en cuenta:

- Temperatura mínima y máxima.
- Probabilidad máxima de precipitación.
- Velocidad máxima del viento.
- Si la actividad permite ser reprogramada.

El servicio devuelve una de las siguientes decisiones:

| Decisión | Significado |
|---|---|
| `MAINTAIN` | Las condiciones meteorológicas evaluadas cumplen los límites configurados para la actividad. |
| `RESCHEDULE` | Al menos una condición está fuera de los límites configurados y la actividad puede reprogramarse. |
| `CANCEL` | Al menos una condición está fuera de los límites configurados y la actividad no puede reprogramarse. |

Cuando las condiciones son desfavorables, el resultado de la evaluación también puede incluir una sugerencia de alternativa en un espacio cerrado.

## Manejo de errores

La aplicación utiliza un manejador centralizado de excepciones y devuelve respuestas de error estructuradas. Dependiendo de la situación, las respuestas pueden incluir:

- `400 Bad Request` — datos inválidos o incumplimiento de una regla de negocio.
- `404 Not Found` — el recurso solicitado no existe.
- `503 Service Unavailable` — no es posible contactar al proveedor meteorológico externo o este devuelve un error.
- `500 Internal Server Error` — error inesperado de la aplicación.

Los errores de validación pueden incluir detalles para identificar los campos que necesitan corregirse.

## Pruebas

La suite de pruebas se ejecutó correctamente durante la validación del proyecto:

```text
Tests run: 48, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd clean test
```

El proyecto incluye pruebas unitarias para los clientes meteorológicos y la lógica de evaluación, pruebas de persistencia para el repositorio de eventos, pruebas de servicios y pruebas de integración de controladores que realizan solicitudes HTTP contra la aplicación y la base de datos.

## Estado del proyecto

La API de gestión de eventos, la integración con Open-Meteo, la lógica de evaluación meteorológica, la capa de persistencia, la documentación Swagger y las pruebas automatizadas han sido implementadas y validadas.

Este proyecto forma parte de mi proceso de aprendizaje y práctica en el desarrollo backend con Java. Me permitió trabajar con API externas, aplicar reglas de negocio a datos meteorológicos reales, persistir información y verificar el comportamiento de una aplicación Spring Boot mediante pruebas automatizadas.
