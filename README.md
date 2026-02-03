# Spring Boot API REST para el registro del inventario de estado de vacunación de empleados

API REST para llevar un registro del inventario del estado de vacunación de los empleados
en una empresa, con Java, Spring Boot y PostgreSQL.

Nota.- Se aplican prácticas básicas de organización por capas y el patrón DTO, con enfoque educativo (no productivo).

## Quick start

1. Levantar PostgreSQL con Docker Compose: `docker compose up -d`
2. Ejecutar la aplicación desde `src/main/java/com/alefiengo/inventariovacunacionempleados/InventarioVacunacionEmpleadosApplication.java`
3. Abrir la documentación en `http://localhost:8081/api/v1/swagger-ui/index.html`

### Ejemplo rápido

```
curl -i http://localhost:8081/api/v1/admin/empleados
```

## Alcance funcional

1. La aplicación cuenta con 2 roles: Administrador y Empleado.
2. Como Administrador se requiere registrar, editar, listar y eliminar a los empleados.
3. Como Empleado se requiere visualizar y actualizar la información.

Nota.- En esta versión educativa no se implementa autenticación/autorización; los roles se representan con rutas separadas para fines didácticos.

## Modelo de Datos

Con base en las condiciones anteriores se tiene la siguiente aproximación:

![](extras/img.png)

Aclaración inmediata: En el modelo se incluyen dos campos del tipo enum, dentro de las clases Empleado y Vacuna.

* Para la clase "Empleado" en el campo "estado_vacunacion" los valores permitidos son: [VACUNADO, NO_VACUNADO]
* Para la clase "vacuna" en el campo "tipo_vacuna" los valores permitidos son: [SPUTNIK_V, ASTRA_ZENECA, PFIZER, JHONSON_AND_JHONSON]

## Construcción del proyecto

### 0. Tecnologías y herramientas

Esta es la lista de todas las tecnologías y/o herramientas utilizadas en el proceso de construcción del proyecto.

* Java 21
* Maven 3.9 o superior
* Spring Boot 3.5.6
* Dependencias: Spring Web, Spring Boot DevTools, Lombok, Mapstruct, Validation, Springdoc OpenAPI UI, PostgreSQL JDBC Driver
* PostgreSQL 14.0
* IntelliJ IDEA (Community Edition)
* Plugin recomendado: Lombok
* Insomnia (opcional)

### 1. Clonar el repositorio

1. Descargar este proyecto e importarlo en `IntelliJ IDEA`.
```
git clone https://github.com/alefiengo/inventario-vacunacion-empleados.git
```
Nota.- Esperar hasta que se descarguen todas las dependencias con `Maven`.
Opcional: también puedes usar el wrapper incluido (`./mvnw`).

2. Crear una base de datos PostgreSQL con el nombre `vacunacion_db`.

3. Configurar credenciales en `src/main/resources/application.properties`. Variables: `spring.datasource.username` y `spring.datasource.password` (o usa `DB_USER` y `DB_PASSWORD`).

## Ejecución de la aplicación

1. Abrir `src/main/java/com/alefiengo/inventariovacunacionempleados/InventarioVacunacionEmpleadosApplication.java`.
2. Ejecutar la clase `InventarioVacunacionEmpleadosApplication`.

![](extras/img3.png)

3. Esperar a que la aplicación compile y se ejecute.
4. Se verá una respuesta similar en la barra de herramientas.

![](extras/img2.png)

## Base de datos con Docker Compose

1. Levantar PostgreSQL (imagen `postgres:14-alpine`):
```
docker compose up -d
```

2. Si necesitas credenciales distintas, define variables de entorno:
```
DB_USER=usuario DB_PASSWORD=clave docker compose up -d
```

3. Verificar que la base exista: `vacunacion_db`.

## Documentación OpenAPI

1. En un navegador web, ingresar a la siguiente URL:
```
http://localhost:8081/api/v1/swagger-ui/index.html
```

2. Explorar los `endpoints` para el rol `ADMINISTRADOR` y para el rol `EMPLEADO`.

## Notas de configuración (educativo)

* Por simplicidad, `spring.jpa.hibernate.ddl-auto` está configurado en `update`. En producción se recomienda un manejo de migraciones.
* `server.error.include-message=always` está pensado para facilitar el aprendizaje durante el desarrollo.
* Puedes sobreescribir credenciales con variables de entorno: `DB_USER`, `DB_PASSWORD` y `JPA_DDL_AUTO`.
* Para CORS, puedes ajustar `app.cors.allowed-origins` (lista separada por comas).

## Extras

* Dentro del directorio `inventario-vacunacion-empleados/extras` se tiene el archivo `Insomnia_2021-10-25.json`, el cual se puede importar desde `Insomnia` para explorar los endpoints.
