package khazaddum.modelo;

/**
 * Resultado de una operación de identificación.
 *
 * <p>Contiene el identificador y el tipo de la entidad identificada. Este
 * registro se utiliza para devolver resultados de identificación desde métodos
 * que realizan búsquedas de entidades (por ejemplo, a partir de comunicación
 * serial o consultas a bases de datos).</p>
 *
 * <p>El registro implementa automáticamente los métodos {@code equals},
 * {@code hashCode} y {@code toString}.</p>
 *
 * @param idEntidad   el identificador numérico de la entidad
 * @param tipoEntidad el tipo de la entidad identificada
 */
public record ResultadoIdentificacion(int idEntidad, String tipoEntidad) {
    /**
     * Crea una nueva instancia de {@code ResultadoIdentificacion}.
     *
     * @param idEntidad   el identificador numérico de la entidad
     * @param tipoEntidad el tipo de la entidad identificada
     */
    public ResultadoIdentificacion {
        // Se puede agregar validación adicional aquí si es necesario
    }
}