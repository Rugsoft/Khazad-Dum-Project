package khazaddum.operaciones;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utilidad para exportar el contenido de un TableModel a un archivo Excel (.xlsx)
 * usando Apache POI.
 */
public class ExportarExcel {
	
	private TableModel modelo;
	
	/**
	 * Constructor que recibe el modelo de tabla a exportar.
	 *
	 * @param modelo instancia de {@link TableModel} que contiene los datos.
	 */
	public ExportarExcel(TableModel modelo) {
		this.modelo = modelo;
	}
	
	/**
	 * Abre un {@link JFileChooser} para seleccionar la ruta de guardado y
	 * crea un archivo Excel con los datos del modelo.
	 */
	public void exportar() {
		
		// --- 1. Crear el JFileChooser ---
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Guardar archivo Excel");
	    // Filtro para que solo muestre archivos .xlsx
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx");
	    fileChooser.setFileFilter(filter);

	    int userSelection = fileChooser.showSaveDialog(null);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File fileToSave = fileChooser.getSelectedFile();
	        
	        // Asegurarse de que el archivo tenga la extensión .xlsx
	        String filePath = fileToSave.getAbsolutePath();
	        if (!filePath.endsWith(".xlsx")) {
	            fileToSave = new File(filePath + ".xlsx");
	        }

	        // --- 2. Lógica de Apache POI ---
	        Workbook workbook = new XSSFWorkbook(); // Crea un libro de Excel
	        Sheet sheet = workbook.createSheet("Datos"); // Crea una hoja
	        
	 	   	TableModel model = this.modelo;
	        
	        // --- 3. Crear la fila de cabecera (Headers) ---
	        Row headerRow = sheet.createRow(0);
	        for (int j = 0; j < model.getColumnCount(); j++) {
	            Cell cell = headerRow.createCell(j);
	            cell.setCellValue(model.getColumnName(j));
	        }

	        // --- 4. Crear las filas de datos ---
	        for (int i = 0; i < model.getRowCount(); i++) {
	            Row dataRow = sheet.createRow(i + 1); // Empezamos en la fila 1
	            
	            for (int j = 0; j < model.getColumnCount(); j++) {
	                Object value = model.getValueAt(i, j);
	                String cellValue = (value == null) ? "" : String.valueOf(value);
	                
	                dataRow.createCell(j).setCellValue(cellValue);
	            }
	        }

	        // --- 5. Escribir el archivo ---
	        try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
	            workbook.write(outputStream);
	            workbook.close();
	            JOptionPane.showMessageDialog(null, "Exportado a excel con éxito", "Info", JOptionPane.INFORMATION_MESSAGE);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, "Error al exportar a Excel: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}
}