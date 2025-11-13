package khazaddum.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;

public class ModelosUnitTest {

    @Test
    public void testEmpleadoCrearAndAccessors() {
        Empleado e = new Empleado(10, "Alice", "Smith", "Jones", "99999999X", "F", "Dev", "alice@example.com", 3, null, "TAGX");

        // verificar getters
        assertEquals(10, e.getIdEmpleado());
        assertEquals("Alice", e.getName());
        assertEquals("Smith", e.getLastName1());
        assertEquals("Jones", e.getLastName2());
        assertEquals("99999999X", e.getDni());
        assertEquals("F", e.getGenero());
        assertEquals("Dev", e.getPuesto());
        assertEquals("alice@example.com", e.getEmail());
        assertEquals(3, e.getNivelAcceso());
        assertNull(e.getFoto());
        assertEquals("TAGX", e.getTag());

        // verificar crear() produce el array con los valores en orden esperado
        Object[] arr = e.crear();
        assertNotNull(arr);
        assertEquals(11, arr.length);
        assertEquals(10, arr[0]);
        assertEquals("Alice", arr[1]);
        assertEquals("Smith", arr[2]);
        assertEquals("Jones", arr[3]);
        assertEquals("99999999X", arr[4]);
        assertEquals("F", arr[5]);
        assertEquals("Dev", arr[6]);
        assertEquals("alice@example.com", arr[7]);
        assertEquals(3, arr[8]);
        assertNull(arr[9]);
        assertEquals("TAGX", arr[10]);
    }

    @Test
    public void testEmpleadoSetters() {
        Empleado e = new Empleado(1, "Bob", "A", "B", "11111111A", "M", "Ops", "bob@example.com", 1, null, "TAG1");
        e.setName("Robert");
        e.setLastName1("Alpha");
        e.setLastName2("Beta");
        e.setDni("22222222B");
        e.setGenero("M");
        e.setPuesto("Admin");
        e.setEmail("robert@example.com");
        e.setNivelAcceso(5);
        File f = new File("/tmp/foto.jpg");
        e.setFoto(f);
        e.setTag("NEWTAG");

        assertEquals("Robert", e.getName());
        assertEquals("Alpha", e.getLastName1());
        assertEquals("Beta", e.getLastName2());
        assertEquals("22222222B", e.getDni());
        assertEquals("Admin", e.getPuesto());
        assertEquals("robert@example.com", e.getEmail());
        assertEquals(5, e.getNivelAcceso());
        assertSame(f, e.getFoto());
        assertEquals("NEWTAG", e.getTag());
    }

    @Test
    public void testUserLoginCrearAndAccessors() {
        UserLogin u = new UserLogin("Gandalf", "TheGrey", "gandalf", "mellon", "gandalf@middleearth", "Wizard");

        Object[] arr = u.crear();
        assertNotNull(arr);
        assertEquals(6, arr.length);
        assertEquals("Gandalf", arr[0]);
        assertEquals("TheGrey", arr[1]);
        assertEquals("gandalf", arr[2]);
        assertEquals("mellon", arr[3]);
        assertEquals("gandalf@middleearth", arr[4]);
        assertEquals("Wizard", arr[5]);

        // comprobar getters
        assertEquals("Gandalf", u.getName());
        assertEquals("TheGrey", u.getLastName());
        assertEquals("gandalf", u.getUser());
        assertEquals("mellon", u.getPassword());
        assertEquals("gandalf@middleearth", u.getEmail());
        assertEquals("Wizard", u.getRole());

        // setters
        u.setPassword("youShallNotPass");
        assertEquals("youShallNotPass", u.getPassword());
    }

    @Test
    public void testRegistroUsuariosEqualsAndFields() {
        RegistroUsuarios r1 = new RegistroUsuarios("2025-11-12 10:00", "entrada", "Alice Smith");
        RegistroUsuarios r2 = new RegistroUsuarios("2025-11-12 10:00", "entrada", "Alice Smith");
        RegistroUsuarios r3 = new RegistroUsuarios("2025-11-12 11:00", "salida", "Alice Smith");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);

        assertEquals("2025-11-12 10:00", r1.fechaHora());
        assertEquals("entrada", r1.tipoRegistro());
        assertEquals("Alice Smith", r1.nombreCompleto());
    }

    @Test
    public void testResultadoIdentificacionRecord() {
        ResultadoIdentificacion res = new ResultadoIdentificacion(42, "temporal");
        assertEquals(42, res.idEntidad());
        assertEquals("temporal", res.tipoEntidad());

        ResultadoIdentificacion same = new ResultadoIdentificacion(42, "temporal");
        ResultadoIdentificacion diff = new ResultadoIdentificacion(43, "empleado");

        assertEquals(res, same);
        assertNotEquals(res, diff);
    }

}
