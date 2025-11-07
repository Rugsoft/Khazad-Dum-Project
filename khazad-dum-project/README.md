Proyecto Khazad-Dum
=====================

Descripción general
-------------------
Khazad-Dum es una aplicación de escritorio Java para gestionar empleados y visitantes temporales, registrar eventos de entrada/salida e integrar con etiquetas RFID y almacenamiento local (MySQL). La base de código usa Java puro (se recomienda JDK 11+), Maven para compilación y dependencias, Swing para la interfaz gráfica y una base de datos MySQL para persistencia.

Características principales
--------------------------
- Registrar y gestionar empleados y visitantes temporales
- Almacenar fotografías de usuarios en la base de datos (BLOB)
- Búsqueda por código RFID para entidades (empleados y temporales)
- Registrar eventos de entrada/salida e inferir el tipo de evento siguiente (entrada o salida)
- Funcionalidades de búsqueda y exportación (ver clases de operaciones)

Estructura del proyecto
-----------------------
- src/main/java/khazaddum/operaciones: lógica de base de datos y operaciones (ConexionDB, comunicación serial, auxiliares de exportación)
- src/main/java/khazaddum/modelo: modelos de dominio (Empleado, VisitaTemporal, ResultadoIdentificacion, RegistroUsuarios, UserLogin)
- src/main/java/khazaddum/gui: formularios y ventanas Swing
- doc/: Javadoc generado en HTML para referencia
- pom.xml: descriptor del proyecto Maven
- legal/: licencias y avisos de terceros

Stack tecnológico
-----------------
- Java (se recomienda JDK 11+)
- Maven (3.6+)
- MySQL 8 (o compatible)
- JDBC MySQL Connector (mysql-connector-java)
- Swing para la interfaz de usuario

Base de datos
-------------
El proyecto usa una base de datos MySQL local. La conexión por defecto está configurada en:

  src/main/java/khazaddum/operaciones/ConexionDB.java

Valores por defecto en el código:
- URL: jdbc:mysql://127.0.0.1:3306/khazad-dum-db
- Usuario: root
- Contraseña: (cadena vacía)

Esquema mínimo sugerido (inferido del código) — adaptar según sea necesario:

CREATE DATABASE khazad_dum_db;
USE khazad_dum_db;

CREATE TABLE entidades (
  id_entidad INT AUTO_INCREMENT PRIMARY KEY,
  tipo_entidad VARCHAR(32) NOT NULL
);

CREATE TABLE empleados (
  id_entidad INT PRIMARY KEY,
  nombre VARCHAR(100),
  apellido1 VARCHAR(100),
  apellido2 VARCHAR(100),
  dni VARCHAR(32),
  genero VARCHAR(32),
  puesto VARCHAR(100),
  email VARCHAR(150),
  nivel_acceso INT,
  foto LONGBLOB,
  codigo_tag VARCHAR(128),
  FOREIGN KEY (id_entidad) REFERENCES entidades(id_entidad) ON DELETE CASCADE
);

CREATE TABLE usuarios_temporales (
  id_entidad INT PRIMARY KEY,
  nombre VARCHAR(100),
  apellido1 VARCHAR(100),
  apellido2 VARCHAR(100),
  dni VARCHAR(32),
  motivo_visita VARCHAR(255),
  foto LONGBLOB,
  fecha_expiracion TIMESTAMP,
  codigo_tag VARCHAR(128),
  FOREIGN KEY (id_entidad) REFERENCES entidades(id_entidad) ON DELETE CASCADE
);

CREATE TABLE registros (
  id_registro INT AUTO_INCREMENT PRIMARY KEY,
  id_entidad INT,
  fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  tipo_registro VARCHAR(16),
  FOREIGN KEY (id_entidad) REFERENCES entidades(id_entidad) ON DELETE CASCADE
);

CREATE TABLE login_usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario VARCHAR(100) UNIQUE,
  password VARCHAR(255),
  nivel VARCHAR(64)
);

Configuración e instalación
---------------------------
1. Instalar Java JDK 11+ y Maven.
2. Instalar MySQL y crear la base de datos y las tablas. Ajustar las credenciales si es necesario.
3. Opción A — Ejecutar en un IDE: importar el proyecto Maven (pom.xml) y ejecutar la clase principal.
4. Opción B — Compilar y ejecutar desde la línea de comandos:
   - mvn clean package
   - Ejecutar el JAR generado o ejecutar desde el IDE. (El proyecto genera clases bajo target/)

Notas
-----
- Las credenciales de la base de datos están codificadas en ConexionDB.java. Actualice ese archivo o refactorice para usar variables de entorno en producción.
- La aplicación almacena imágenes como BLOBs y crea archivos temporales al recuperar imágenes para mostrarlas.
- El Javadoc está disponible en la carpeta doc/ para referencia de la API.

Contribuir
----------
- Haz un fork del repositorio, crea ramas por característica y abre pull requests.
- Mantén coordinados los cambios en la interfaz y en la base de datos. Añade migraciones o scripts SQL para cambios en el esquema.

Licencia
--------
Consulta legal/LICENSE en el repositorio para detalles de licencia y atribuciones de terceros.

Contacto
-------
Para consultas o incidencias, abre un issue en el repositorio con una descripción clara y pasos para reproducir el problema.