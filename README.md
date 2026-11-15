Diseño y Consulta de un Modelo ORM


Descripción de la actividad: 

En esta actividad, los estudiantes deberán diseñar un modelo de datos utilizando un enfoque de mapeo objeto-relacional (ORM) con JPA

Se realizó una aplicación de escritorio desarrollada en Java que permite administrar productos y registrar pedidos de una empresa, en esta ocasión de las tiendas TUTI. El proyecto utiliza Hibernate y JPA para gestionar la información mediante un modelo ORM, además de consultas JPQL para buscar y organizar los datos.


Tecnologías utilizadas

-Java 25

-Java Swing

-Hibernate 6

-Jakarta Persistence (JPA)

-Base de datos H2

-Maven

-JPQL


Funcionalidades

-Registrar, consultar, buscar, editar y eliminar productos.

-Administrar categorías y sucursales.

-Registrar pedidos con sus respectivos productos.

-Calcular automáticamente subtotales y totales.

-Verificar la disponibilidad de stock.

-Consultar el historial de pedidos.

-Eliminar pedidos junto con sus detalles.

-Ejecutar consultas JPQL con filtros, ordenamiento y paginación.


Modelo ORM

-El proyecto cuenta con cinco entidades principales:

-Producto

-Categoria

-Sucursal

-Pedido

-DetallePedido

Las entidades están relacionadas mediante asociaciones @OneToMany y @ManyToOne. La entidad DetallePedido permite relacionar los productos con los pedidos, almacenando también su cantidad y subtotal.


Consultas JPQL

-Se implementaron tres consultas principales:

-Búsqueda de productos por categoría.

-Búsqueda de productos con stock bajo.

-Búsqueda de productos por rango de precio.

-Las consultas incluyen filtros, ordenamiento y paginación.


Ejecución del proyecto

-Clonar o descargar el repositorio.

-Abrir el proyecto en NetBeans.

-Esperar a que Maven descargue las dependencias.

-Ejecutar la clase Main.java.

-La base de datos H2 se gestiona mediante Hibernate.
