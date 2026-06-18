# 🐾 Delivery Mascotero API

API REST desarrollada con Spring Boot para la gestión integral de un negocio de productos para mascotas.

El proyecto permite administrar clientes, productos, proveedores, compras, pedidos, pagos y ofertas, implementando buenas prácticas de desarrollo backend, autenticación mediante JWT y arquitectura por capas.

---

## 🚀 Tecnologías utilizadas

- Java 21+
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT Authentication
- Hibernate
- MySQL
- Maven
- MapStruct
- Lombok
- Jakarta Validation
- Swagger / OpenAPI

---

## 📌 Funcionalidades

### Productos

- Alta, baja lógica y modificación de productos
- Gestión de Alimentos y Antipulgas mediante herencia JPA
- Control de stock
- Productos activos/inactivos
- Aplicación de ofertas

### Clientes

- Alta y modificación de clientes
- Asociación de múltiples direcciones
- Validaciones de datos

### Proveedores

- Gestión completa de proveedores
- Asociación con productos

### Compras

- Registro de compras a proveedores
- Detalle de compra
- Actualización automática de stock al recibir una compra
- Estados de compra

### Pedidos

- Creación de pedidos
- Control automático de stock disponible
- Descuento de stock
- Gestión de estados del pedido
- Gestión del estado de pago

### Pagos

- Registro de pagos
- Pago parcial
- Pago total
- Actualización automática del estado del pedido

### Ofertas

- Creación de ofertas
- Asociación con múltiples productos
- Cálculo automático del precio con descuento

### Reportes

- Pedidos pendientes
- Pedidos por dirección
- Resumen de ventas por mes
- Consultas operativas para la gestión diaria

---

## 🔐 Seguridad

El proyecto implementa autenticación mediante JWT.

Características:

- Registro de usuarios
- Login
- Generación de Token JWT
- Protección de endpoints
- Roles de usuario
- Spring Security

---

## ✅ Validaciones

Se implementaron validaciones utilizando Jakarta Validation:

- Campos obligatorios
- Longitud máxima
- Valores positivos
- Validación de formatos
- Validación personalizada para Enums
- Manejo centralizado de excepciones

---

## 📁 Arquitectura

El proyecto sigue una arquitectura por capas.

```
Controller
│
├── Service
│
├── Repository
│
├── Entity
│
├── DTO
│
├── Mapper
│
├── Security
│
├── Exception
│
└── Config
```

---

## 📦 Principales entidades

- Cliente
- Dirección
- Producto
- Alimento
- Antipulgas
- Proveedor
- Compra
- DetalleCompra
- Pedido
- DetallePedido
- Pago
- Oferta
- Usuario
- Rol

---

## 👨‍💻 Autores

Proyecto desarrollado por Juan Pablo Sommacal, Juan Jose Stachelski, Matias Vega y Matias Garcia como práctica integral de desarrollo Backend utilizando Spring Boot.

Link al DER
https://miro.com/app/board/uXjVHZoWnwM=/?share_link_id=764926157389

Flujo para probar la App

-Primero registrar un usuario
-Login de ese usuario
-Crear productos, clientes o proveedores
-Para asociar una direccion a un cliente, el cliente ya debe estar creado
-Para hacer un pedido ya tiene que haber productos cargados y minimo un cliente con su direccion asociada
-Para hacer un pedido a proveedor tiene que haber productos cargados
-Los pedidos solo se pueden cancelar si no estan entregados ni pagados
-Los pedidos se pueden entregar y el cliente pagarlo despues. (Osea es posible tener clientes con deuda)
