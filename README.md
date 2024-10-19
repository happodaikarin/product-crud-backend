# Product CRUD Backend

Este es el backend de un proyecto CRUD para la gestión de productos, construido con Spring Boot. Este servicio permite crear, leer, actualizar y eliminar productos en una base de datos MySQL.

## Requisitos Previos

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/get-started)
- MySQL Server (si no se usa el servicio embebido)

## Instalación

1. **Clonar el repositorio:**
   \`\`\`bash
   git clone <URL-del-repositorio>
   cd product-crud-backend
   \`\`\`

2. **Construir el proyecto:**
   \`\`\`bash
   mvn clean package
   \`\`\`

3. **Configurar la base de datos:**
   - Asegúrate de tener un servidor MySQL en ejecución.
   - Configura las propiedades de conexión en \`src/main/resources/application.properties\`.

4. **Ejecutar el proyecto:**
   \`\`\`bash
   mvn spring-boot:run
   \`\`\`

   O, si prefieres usar Docker:

   \`\`\`bash
   docker build -t product-crud-backend .
   docker run -p 8080:8080 product-crud-backend
   \`\`\`

## Endpoints

- \`GET /products\`: Obtener una lista de todos los productos.
- \`GET /products/{id}\`: Obtener un producto específico por ID.
- \`POST /products\`: Crear un nuevo producto.
- \`PUT /products/{id}\`: Actualizar un producto existente.
- \`DELETE /products/{id}\`: Eliminar un producto por ID.

