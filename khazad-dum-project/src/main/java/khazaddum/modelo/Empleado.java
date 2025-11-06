package khazaddum.modelo;

import java.io.File;

/**
 * Representa un empleado del sistema.
 * <p>
 * Contiene los datos básicos de un empleado (identificador, nombre, apellidos,
 * DNI, género, puesto, email, nivel de acceso, foto y código de tag RFID).
 * </p>
 */
public class Empleado {

     private int idEmpleado;
     private String name;
     private String lastName1;
     private String lastName2;
     private String dni;
     private String genero;
     private String puesto;
     private String email;
     private int nivelAcceso;
     private File foto;
     private String tag;
     
     /**
      * Construye un nuevo objeto Empleado con todos sus campos.
      *
      * @param idEmpleado identificador de la entidad/empleado
      * @param name nombre del empleado
      * @param lastName1 primer apellido
      * @param lastName2 segundo apellido
      * @param dni documento nacional de identidad
      * @param genero género del empleado
      * @param puesto puesto de trabajo
      * @param email correo electrónico
      * @param nivelAcceso nivel de acceso (entero)
      * @param foto archivo que contiene la foto (puede ser null)
      * @param tag código RFID asociado (puede ser null)
      */
     public Empleado(int idEmpleado, String name, String lastName1, String lastName2, String dni, String genero, String puesto,
             String email, int nivelAcceso, File foto, String tag) {

         this.idEmpleado = idEmpleado;
         this.name = name;
         this.lastName1 = lastName1;
         this.lastName2 = lastName2;
         this.dni = dni;
         this.genero = genero;
         this.puesto = puesto;
         this.email = email;
         this.nivelAcceso = nivelAcceso;
         this.foto = foto;
         this.tag = tag;
     }
     
     /**
      * Devuelve una representación en forma de array de objetos con los campos
      * del empleado, útil para insertar en modelos de tablas.
      *
      * @return array de objetos con los datos del empleado.
      */
     public Object[] crear() {
             return new Object[] {idEmpleado, name, lastName1, lastName2, dni, genero, puesto, email, nivelAcceso, foto, tag};
         }
     

     /** @return el identificador del empleado */
     public int getIdEmpleado() {
         return idEmpleado;
     }

     /** @param idEmpleado establece el identificador del empleado */
     public void setIdEmpleado(int idEmpleado) {
         this.idEmpleado = idEmpleado;
     }

     /** @return el código/tag RFID asociado */
     public String getTag() {
         return tag;
     }

     /** @param tag establece el código/tag RFID */
     public void setTag(String tag) {
         this.tag = tag;
     }

     /** @return el nombre del empleado */
     public String getName() {
         return name;
     }

     /** @param name establece el nombre del empleado */
     public void setName(String name) {
         this.name = name;
     }

     /** @return el primer apellido */
     public String getLastName1() {
         return lastName1;
     }

     /** @param lastName1 establece el primer apellido */
     public void setLastName1(String lastName1) {
         this.lastName1 = lastName1;
     }

     /** @return el segundo apellido */
     public String getLastName2() {
         return lastName2;
     }

     /** @param lastName2 establece el segundo apellido */
     public void setLastName2(String lastName2) {
         this.lastName2 = lastName2;
     }

     /** @return el DNI */
     public String getDni() {
         return dni;
     }

     /** @param dni establece el DNI */
     public void setDni(String dni) {
         this.dni = dni;
     }

     /** @return el género */
     public String getGenero() {
         return genero;
     }

     /** @param genero establece el género */
     public void setGenero(String genero) {
         this.genero = genero;
     }

     /** @return el puesto */
     public String getPuesto() {
         return puesto;
     }

     /** @param puesto establece el puesto */
     public void setPuesto(String puesto) {
         this.puesto = puesto;
     }

     /** @return el email */
     public String getEmail() {
         return email;
     }

     /** @param email establece el email */
     public void setEmail(String email) {
         this.email = email;
     }

     /** @return el nivel de acceso */
     public int getNivelAcceso() {
         return nivelAcceso;
     }

     /** @param nivelAcceso establece el nivel de acceso */
     public void setNivelAcceso(int nivelAcceso) {
         this.nivelAcceso = nivelAcceso;
     }

     /** @return el archivo con la foto */
     public File getFoto() {
         return foto;
     }

     /** @param foto establece la foto del empleado */
     public void setFoto(File foto) {
         this.foto = foto;
     }
     
}