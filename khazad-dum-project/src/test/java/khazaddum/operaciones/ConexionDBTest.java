package khazaddum.operaciones;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import khazaddum.modelo.RegistroUsuarios;
import khazaddum.modelo.ResultadoIdentificacion;

public class ConexionDBTest {

    @BeforeEach
    public void setup() throws Exception {
        // Indicar que la aplicación use una base de datos H2 en memoria
        // con el modo de compatibilidad MySQL y que no se cierre al terminar la conexión.
        System.setProperty("khazaddum.db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        System.setProperty("khazaddum.db.user", "sa");
        System.setProperty("khazaddum.db.password", "");

        // Conectar a la base de datos y crear el esquema mínimo necesario para las pruebas.
        Connection con = ConexionDB.conectar();
        try (Statement s = con.createStatement()) {
            // Crear tablas básicas que utiliza la aplicación: entidades, empleados, usuarios temporales y registros.
            s.execute("CREATE TABLE entidades (id_entidad INT AUTO_INCREMENT PRIMARY KEY, tipo_entidad VARCHAR(50))");
            s.execute("CREATE TABLE empleados (id_entidad INT PRIMARY KEY, nombre VARCHAR(100), apellido1 VARCHAR(100), apellido2 VARCHAR(100), dni VARCHAR(30), genero VARCHAR(20), puesto VARCHAR(50), email VARCHAR(100), nivel_acceso INT, foto BLOB, codigo_tag VARCHAR(100))");
            s.execute("CREATE TABLE usuarios_temporales (id_entidad INT PRIMARY KEY, nombre VARCHAR(100), apellido1 VARCHAR(100), apellido2 VARCHAR(100), dni VARCHAR(30), motivo_visita VARCHAR(255), foto BLOB, fecha_expiracion TIMESTAMP, codigo_tag VARCHAR(100))");
            s.execute("CREATE TABLE registros (id_registro INT AUTO_INCREMENT PRIMARY KEY, id_entidad INT, fecha_hora TIMESTAMP, tipo_registro VARCHAR(20))");

            // Insertar un empleado de ejemplo para las pruebas.
            s.execute("INSERT INTO entidades (id_entidad, tipo_entidad) VALUES (1, 'empleado')");
            s.execute("INSERT INTO empleados (id_entidad, nombre, apellido1, apellido2, dni, genero, puesto, email, nivel_acceso, codigo_tag) VALUES (1, 'Juan', 'Perez', 'Lopez', '12345678A', 'M', 'Dev', 'juan@example.com', 1, 'TAG123')");

            // Insertar un usuario temporal de ejemplo con fecha de expiración en 5 horas.
            s.execute("INSERT INTO entidades (id_entidad, tipo_entidad) VALUES (2, 'temporal')");
            s.execute("INSERT INTO usuarios_temporales (id_entidad, nombre, apellido1, apellido2, dni, motivo_visita, fecha_expiracion, codigo_tag) VALUES (2, 'Ana', 'Gomez', 'Ruiz', '87654321B', 'Visita', DATEADD('HOUR', 5, CURRENT_TIMESTAMP()), 'TAGTEMP')");
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        // Eliminar las tablas para limpiar la base de datos en memoria entre pruebas.
        Connection con = ConexionDB.conectar();
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS registros");
            s.execute("DROP TABLE IF EXISTS empleados");
            s.execute("DROP TABLE IF EXISTS usuarios_temporales");
            s.execute("DROP TABLE IF EXISTS entidades");
        }
    }

    @Test
    public void testBuscarEmpleadoPorTag() {
        // Verifica que buscar por código tag retorne el resultado esperado para un empleado.
        ConexionDB db = new ConexionDB();
        ResultadoIdentificacion res = db.buscarEmpleadoPorTag("TAG123");
        assertNotNull(res, "ResultadoIdentificacion no debe ser null");
        assertEquals(1, res.idEntidad());
        assertEquals("empleado", res.tipoEntidad());
    }

    @Test
    public void testBuscarTemporalPorTag() {
        // Verifica que buscar por código tag retorne el resultado para un usuario temporal.
        ConexionDB db = new ConexionDB();
        ResultadoIdentificacion res = db.buscarEmpleadoPorTag("TAGTEMP");
        assertNotNull(res);
        assertEquals(2, res.idEntidad());
        assertEquals("temporal", res.tipoEntidad());
    }

    @Test
    public void testBuscarEmpleadoPorNombredeTabla() {
        // Comprueba la búsqueda por nombre y primer apellido tanto en empleados como en temporales.
        ConexionDB db = new ConexionDB();
        ResultadoIdentificacion res = db.buscarEmpleadoPorNombredeTabla("Juan", "Perez");
        assertNotNull(res);
        assertEquals(1, res.idEntidad());
        assertEquals("empleado", res.tipoEntidad());

        // También testear la búsqueda de un usuario temporal por nombre.
        ResultadoIdentificacion resTemp = db.buscarEmpleadoPorNombredeTabla("Ana", "Gomez");
        assertNotNull(resTemp);
        assertEquals(2, resTemp.idEntidad());
        assertEquals("temporal", resTemp.tipoEntidad());
    }

    @Test
    public void testObtenerDatosCompletosEmpleadoAndTemporal() {
        // Comprueba que obtenerDatosCompletos devuelve los campos esperados para empleado y temporal.
        ConexionDB db = new ConexionDB();
        Object[] emp = db.obtenerDatosCompletos(1, "empleado");
        assertNotNull(emp);
        assertEquals(1, emp[0]);
        assertEquals("Juan", emp[1]);

        Object[] temp = db.obtenerDatosCompletos(2, "temporal");
        assertNotNull(temp);
        assertEquals(2, temp[0]);
        assertEquals("Ana", temp[1]);
    }

    @Test
    public void testObtenerEmpleadosAndTemporalLists() throws Exception {
        // Verifica que los métodos que retornan listas de empleados y temporales entregan colecciones no vacías.
        ConexionDB db = new ConexionDB();
        // obtenerEmpleados espera una consulta SELECT que devuelva las columnas de empleados.
        ArrayList<?> empleados = db.obtenerEmpleados("SELECT * FROM empleados");
        assertNotNull(empleados);
        assertFalse(empleados.isEmpty());

        ArrayList<?> temporales = db.obtenerTemporal("SELECT * FROM usuarios_temporales");
        assertNotNull(temporales);
        assertFalse(temporales.isEmpty());
    }

    @Test
    public void testRegistrarEntradaSalidaAndTipoRegistro() {
        // Prueba la lógica que alterna entre 'entrada' y 'salida' al registrar movimientos.
        ConexionDB db = new ConexionDB();

        // Inicialmente no hay registros, por lo que tipoRegistro debe ser 'entrada'.
        String inicial = db.tipoRegistro(1);
        assertEquals("entrada", inicial);

        // Registrar entrada -> debe insertar un registro 'entrada' y la siguiente llamada debe ser 'salida'.
        db.registrarEntradaSalida(1);
        String despues = db.tipoRegistro(1);
        assertEquals("salida", despues);

        // Registrar de nuevo -> vuelve a 'entrada'.
        db.registrarEntradaSalida(1);
        assertEquals("entrada", db.tipoRegistro(1));
    }

    @Test
    public void testObtenerRegistrosNombre() throws Exception {
        // Inserta registros para un id y verifica que obtenerRegistrosNombre devuelve objetos RegistroUsuarios con los campos esperados.
        ConexionDB db = new ConexionDB();
        Connection con = ConexionDB.conectar();
        try (Statement s = con.createStatement()) {
            // Añadir dos registros (entrada y salida) para la entidad 1 en la fecha actual.
            s.execute("INSERT INTO registros (id_entidad, fecha_hora, tipo_registro) VALUES (1, CURRENT_TIMESTAMP(), 'entrada')");
            s.execute("INSERT INTO registros (id_entidad, fecha_hora, tipo_registro) VALUES (1, CURRENT_TIMESTAMP(), 'salida')");
        }

        var registros = db.obtenerRegistrosNombre(1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertNotNull(registros);
        assertFalse(registros.isEmpty());
        // Cada RegistroUsuarios tiene fechaHora, tipoRegistro y nombreCompleto.
        RegistroUsuarios r = registros.get(0);
        assertNotNull(r.fechaHora());
        assertNotNull(r.tipoRegistro());
        assertNotNull(r.nombreCompleto());
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        // Crea un usuario adicional, lo elimina mediante eliminarUsuario y verifica que la entidad ya no existe en la tabla entidades.
        ConexionDB db = new ConexionDB();
        // Crear un usuario extra que luego será eliminado.
        Connection con = ConexionDB.conectar();
        try (Statement s = con.createStatement()) {
            s.execute("INSERT INTO entidades (id_entidad, tipo_entidad) VALUES (3, 'empleado')");
            s.execute("INSERT INTO empleados (id_entidad, nombre, apellido1, apellido2, dni, genero, puesto, email, nivel_acceso, codigo_tag) VALUES (3, 'Luis', 'Diaz', 'Marin', '11111111C', 'M', 'Ops', 'luis@example.com', 1, 'TAGDEL')");
            s.execute("INSERT INTO registros (id_entidad, fecha_hora, tipo_registro) VALUES (3, CURRENT_TIMESTAMP(), 'entrada')");
        }

        boolean eliminado = db.eliminarUsuario(3);
        assertTrue(eliminado);

        // Verificar que la fila fue eliminada de la tabla entidades.
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM entidades WHERE id_entidad = 3")) {
            if (rs.next()) {
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    public void testAñadirYComprobarLogin() {
        // Crear la tabla de login si no existe, insertar credenciales mediante añadirUsuariosLogin y comprobarlas.
        ConexionDB db = new ConexionDB();
        try (Connection con = ConexionDB.conectar(); Statement s = con.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS login_usuarios (usuario VARCHAR(100), password VARCHAR(100), nivel VARCHAR(50))");
        } catch (Exception e) {
            fail("No se pudo crear la tabla login_usuarios: " + e.getMessage());
        }

        String insertSql = "INSERT INTO login_usuarios (usuario, password, nivel) VALUES (?, ?, ?)";
        Object[] datos = new Object[] {"tester", "pwd123", "Gandalf"};
        boolean ok = ConexionDB.añadirUsuariosLogin(insertSql, datos);
        assertTrue(ok, "añadirUsuariosLogin debe retornar true");

        String nivel = ConexionDB.comprobarLogin("SELECT nivel FROM login_usuarios WHERE usuario = ? AND password = ?", "tester", "pwd123");
        assertNotNull(nivel);
        assertEquals("Gandalf", nivel);
    }

}