package khazaddum.operaciones;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias básicas para el método comprobarLogin de ConexionDB.
 *
 * Configura una base de datos H2 en memoria antes de cargar la clase ConexionDB
 * (se definen las propiedades de conexión en @BeforeAll) y crea la tabla
 * login_usuarios con dos usuarios de prueba.
 */
public class ConexionDBLoginTest {

    @BeforeAll
    public static void setup() throws Exception {
        // Establecemos las propiedades antes de que se cargue ConexionDB
        System.setProperty("khazaddum.db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        System.setProperty("khazaddum.db.user", "sa");
        System.setProperty("khazaddum.db.password", "");

        // Ahora abrimos la conexión y creamos la tabla e inserciones de prueba
        try (Connection conn = ConexionDB.conectar()) {
            assertNotNull(conn, "La conexión no debe ser nula");

            try (Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS login_usuarios (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255), password VARCHAR(255), nivel VARCHAR(255))");

                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO login_usuarios (username, password, nivel) VALUES (?, ?, ?)")) {
                    ps.setString(1, "gandalf");
                    ps.setString(2, "mellon");
                    ps.setString(3, "Gandalf");
                    ps.executeUpdate();

                    ps.setString(1, "balrog");
                    ps.setString(2, "flame");
                    ps.setString(3, "Balrog");
                    ps.executeUpdate();
                }

            }
        }
    }

    @AfterAll
    public static void teardown() throws SQLException {
        // Opcional: limpiar la base de datos
        try (Connection conn = ConexionDB.conectar()) {
            try (Statement st = conn.createStatement()) {
                st.execute("DROP TABLE IF EXISTS login_usuarios");
            }
        }
    }

    @Test
    public void testValidLogin() {
        String sql = "SELECT nivel FROM login_usuarios WHERE username = ? AND password = ?";
        String nivel = ConexionDB.comprobarLogin(sql, "gandalf", "mellon");
        assertEquals("Gandalf", nivel);
    }

    @Test
    public void testInvalidPassword() {
        String sql = "SELECT nivel FROM login_usuarios WHERE username = ? AND password = ?";
        String nivel = ConexionDB.comprobarLogin(sql, "gandalf", "wrong");
        assertNull(nivel);
    }

    @Test
    public void testNonExistingUser() {
        String sql = "SELECT nivel FROM login_usuarios WHERE username = ? AND password = ?";
        String nivel = ConexionDB.comprobarLogin(sql, "unknown", "nope");
        assertNull(nivel);
    }
}
