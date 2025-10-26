Proyecto Base Implementando Clean Architecture
Este proyecto implementa una API RESTfull reactivo en Java 21 con Spring Boot WebFlux, siguiendo los principios de Arquitectura Hexagonal (Clean Architecture) propuestos por Bancolombia. El objetivo es construir un servicio que gestione una lista de franquicias, sus sucursales y los productos que ofrece cada una

Para el desarrollo de esta prueba tecnica se utilizaron los siguientes programas
JAVA Version 21

Gradle Version 9.1.0

Docker Desktop

PostgreSQL Version 17.5 Para Ambiente local

Amazon RDS PostgreSQL Ambiente prod (Lo tengo apagado por temas de costos me avisan y levanto el servicio para probarlo)

JUnit & Mockito

Lombok Version 1.18.38

Scalfold Version 3.26.1

SpringBoot Version 3.5.4

PgAdmin Version 4

Principios aplicados
Principios aplicados

Separación de capas: Dominio, Aplicación, Infraestructura.

Programación reactiva: Uso de Mono y Flux con operadores map, flatMap, switchIfEmpty

Inyección de dependencias para desacoplar componentes

Logging controlado con SLF4J

Pruebas unitarias con cobertura superior al 60%

Endpoints Disponibles
Método Endpoint Descripción

POST /api/franchises Crea una nueva franquicia

POST /api/franchises/{franchiseId}/branches Agrega una nueva sucursal a una franquicia existente

POST /api/franchises/{franchiseId}/branches/{branchId}/products Agrega un producto a una sucursal

DELETE /api/franchises/{franchiseId}/branches/{branchId}/products/{productId} Elimina un producto de una sucursal

PUT /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock Actualiza el stock de un producto

GET /api/franchises/{franchiseId}/top-products Obtiene el producto con mayor stock por sucursal de una franquicia

Despliegue Local
Requisitos previos

Tener instalados:

Java 21

Gradle 9.1

Docker Desktop

PostgreSQL 17.5

🔧 Configuración del entorno

Clonar el repositorio:

git clone https://github.com/michaelrove901/franquiciasScalfold cd franquiciasScaffold

Configurar las variables en application-local.yml (usuario, contraseña, puerto DB)

Compilar y ejecutar:

gradle clean build gradle bootRun

Acceder a la API:

colleccionPostman.json Importar en Postman y probar EndPoints

Despliegue con Docker
Construir la imagen:

gradle clean bootJar

cd deployment

docker-compose build

docker-compose up --build

docker ps

API disponible en:

http://localhost:8080

☁️ Despliegue en Producción

En ambiente productivo se utiliza Amazon RDS PostgreSQL, el cual se encuentra apagado por costos En caso de pruebas, avisar para habilitar la instancia temporalmente

Consideraciones de Diseño
Durante el desarrollo de este proyecto utilize una Arquitectura Hexagonal gracias al scalfold de bancolombia con el fin de mantener una clara separación de responsabilidades entre las capas de dominio, aplicación e infraestructura gracias a esto facilitamos la mantenibilidad del código y permite reemplazar componentes sin afectar la lógica de negocio

Se priorizó el uso de programación reactiva para garantizar una aplicación eficiente, escalable y no bloqueante, aprovechando las capacidades de Spring WebFlux para la construcción de APIs reactivas

El manejo de logs se realizó mediante SLF4J lo que me permitio un registro estructurado y una trazabilidad clara de los eventos dentro del sistema

Se incluyeron pruebas unitarias enfocadas en validar la lógica del dominio y el correcto funcionamiento de los flujos reactivos, asegurando una buena cobertura y confiabilidad en los resultados

El proyecto fue contenedorizado con Docker lo que me facilito su despliegue, ejecución y portabilidad en diferentes entornos
