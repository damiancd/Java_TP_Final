# E-commerce REST API - Spring Boot

## 📖 Descripción del Proyecto
Este proyecto es una API RESTful desarrollada en Java y Spring Boot para la gestión del backend de un e-commerce. Evolucionó de una aplicación clásica de consola a una arquitectura orientada a la web, implementando persistencia de datos relacional y separación de responsabilidades por capas (Controller, Service, Repository, Model).

**Características principales:**
- **Operaciones CRUD:** Gestión completa de Productos y Pedidos mediante peticiones HTTP (GET, POST, PUT, DELETE).
- **Persistencia de Datos:** Utiliza Spring Data JPA e Hibernate para interactuar con una base de datos MySQL.
- **Relaciones JPA:** Implementación de relaciones `@ManyToOne` y `@OneToMany` (ej. Productos pertenecientes a Categorías, Detalles de Pedidos vinculados a un Pedido central).
- **Validaciones:** Integridad de datos asegurada mediante Hibernate Validator (`@NotBlank`, `@Min`, `@NotNull`).
- **Manejo de Errores:** Excepciones personalizadas (`StockInsuficienteException`, `ProductoNoEncontradoException`) gestionadas globalmente a través de un `@ControllerAdvice`.
- **CORS Configurado:** Listo para ser consumido..
- **Código Limpio:** Uso de Lombok para reducir el código repetitivo.

---

## 🚀 Instrucciones para ejecutar la aplicación

**Requisitos previos:**
- Java 17 o superior.
- XAMPP (Para el servidor MySQL).
- El IDE de tu preferencia.

**Paso a paso:**
1. **Preparar la Base de Datos:**
   * Abre XAMPP e inicia el servicio de **MySQL**.
   * Ingresa a phpMyAdmin (`http://localhost/phpmyadmin`) y crea una base de datos vacía llamada `ecommerce1`. No es necesario crear las tablas, Hibernate lo hará automáticamente.

2. **Configurar la Aplicación:**
   * Abre la carpeta del proyecto en Visual Studio Code.
   * Verifica que en el archivo `src/main/resources/application.properties` las credenciales coincidan con las de tu entorno local (por defecto en XAMPP el usuario es `root` sin contraseña).

3. **Ejecutar el Servidor:**
   * Abre una nueva terminal integrada en VS Code.
   * Ejecuta el proyecto utilizando Maven Wrapper con el siguiente comando:
     ```bash
     .\mvnw spring-boot:run
     ```
   * La API estará disponible y escuchando peticiones en `http://localhost:8080`.

---

## 🧪 Ejemplos de uso o datos de prueba

### 1. Poblar la base de datos (Datos iniciales)
Una vez que la aplicación se haya ejecutado por primera vez y las tablas estén creadas, puedes insertar estos datos de prueba ejecutando el siguiente script SQL en tu phpMyAdmin:

```sql
USE ecommerce1;

-- Insertar Categorías
INSERT INTO categoria (nombre) VALUES ('Tecnología'), ('Alimentos');

-- Insertar Productos
INSERT INTO producto (nombre, precio, stock, es_importado, categoria_id) VALUES 
('Laptop Gamer', 1500000.00, 5, 1, 1),
('Café Premium', 5000.00, 20, 0, 2),
('Auriculares Bluetooth', 45000.00, 15, 1, 1);

## 🌐 Endpoints de la API

### 📦 Productos
Gestionan todo el inventario de la tienda.

| Método HTTP | Endpoint | Descripción | Body (JSON) Requerido |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/productos` | Lista todos los productos disponibles. 
| **GET** | `/api/productos/{id}` | Obtiene los detalles de un producto específico usando su ID.
| **POST** | `/api/productos` | Crea un nuevo producto en la base de datos. | Sí (Datos del producto y categoría) |
| **PUT** | `/api/productos/{id}` | Actualiza el precio, stock o datos de un producto existente. | Sí (Datos actualizados del producto) |
| **DELETE** | `/api/productos/{id}` | Elimina un producto por completo del sistema.|

---

### 🏷️ Categorías
Gestionan las agrupaciones y familias de productos.

| Método HTTP | Endpoint | Descripción | Body (JSON) Requerido |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/categorias` | Lista todas las categorías. | *Ninguno* |
| **GET** | `/api/categorias/{id}` | Obtiene una categoría específica por su ID. | *Ninguno* |
| **POST** | `/api/categorias` | Crea una nueva categoría. | Sí (`{"nombre": "Nueva Categoría"}`) |
| **PUT** | `/api/categorias/{id}` | Actualiza el nombre de una categoría existente. | Sí (`{"nombre": "Nombre Actualizado"}`) |
| **DELETE** | `/api/categorias/{id}` | Elimina una categoría del sistema. | *Ninguno* |

---

### 🛒 Pedidos
Gestionan las compras y el historial de transacciones.

| Método HTTP | Endpoint | Descripción | Body (JSON) Requerido |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/pedidos` | Lista todos los pedidos registrados y sus totales calculados.
| **POST** | `/api/pedidos` | Confirma una compra, calcula recargos (importados) y descuenta el stock. | Sí (IDs de productos y cantidades) |

Pruebas con Postman (Endpoints principales)
A. Crear un nuevo producto (POST)
URL: http://localhost:8080/api/productos

Body (JSON):
{
  "nombre": "Monitor Curvo 24 pulgadas",
  "precio": 250000.00,
  "stock": 10,
  "esImportado": true,
  "categoria": {
    "id": 1
  }
}

B. Listar todos los productos (GET)
URL: http://localhost:8080/api/productos

Respuesta esperada: Un arreglo JSON con todos los productos disponibles y sus datos relacionados.

C. Simular una compra / Crear un pedido (POST)
URL: http://localhost:8080/api/pedidos

Body (JSON):
{
  "lineas": [
    {
      "producto": { "id": 1 },
      "cantidad": 1
    },
    {
      "producto": { "id": 2 },
      "cantidad": 3
    }
  ]
}

Nota: Si se intenta comprar una cantidad mayor al stock disponible en la base de datos, la API devolverá automáticamente un error 400 Bad Request con el mensaje de validación correspondiente.

Estructura:
src/
 └── main/
      ├── java/
      │    └── com/
      │         └── ecommerce/
      │              └── api/
      │                   ├── config/
      │                   │    └── CorsConfig.java
      │                   ├── controller/
      │                   │    ├── CategoriaController.java      
      │                   │    ├── PedidoController.java
      │                   │    └── ProductoController.java
      │                   ├── exception/
      │                   │    ├── CategoriaNoEncontradaException.java
      │                   │    ├── GlobalExceptionHandler.java
      │                   │    ├── ProductoNoEncontradoException.java
      │                   │    └── StockInsuficienteException.java
      │                   ├── model/
      │                   │    ├── Categoria.java
      │                   │    ├── DetallePedido.java
      │                   │    ├── Pedido.java
      │                   │    └── Producto.java
      │                   ├── repository/
      │                   │    ├── CategoriaRepository.java
      │                   │    ├── PedidoRepository.java
      │                   │    └── ProductoRepository.java
      │                   ├── service/
      │                   │    ├── CategoriaService.java
      │                   │    ├── PedidoService.java
      │                   │    └── ProductoService.java
      │                   └── EcommerceApplication.java
      └── resources/
           └── application.properties
