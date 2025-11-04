package khazaddum.operaciones;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.openpdf.text.Document;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public class ExportarPDF {

	private TableModel modelo;
	
	public ExportarPDF(TableModel modelo) {
		this.modelo = modelo;
	}
	
	public void exportar() {

		// --- 1. Crear el JFileChooser ---
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Guardar archivo PDF");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
	    fileChooser.setFileFilter(filter);

	    int userSelection = fileChooser.showSaveDialog(null);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File fileToSave = fileChooser.getSelectedFile();
	        
	        // Asegurarse de que el archivo tenga la extensión .pdf
	        String filePath = fileToSave.getAbsolutePath();
	        if (!filePath.endsWith(".pdf")) {
	            fileToSave = new File(filePath + ".pdf");
	        }

	        // --- 2. Lógica de OpenPDF (idéntica a iText 5) ---
	        Document document = new Document();
	        
	        try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
	            PdfWriter.getInstance(document, outputStream);
	            document.open();
	            
	            TableModel model = this.modelo;
	            
	            // --- 3. Crear la PdfPTable ---
	            PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
	            
	            // --- 4. Añadir las cabeceras (Headers) ---
	            for (int j = 0; j < model.getColumnCount(); j++) {
	                PdfPCell headerCell = new PdfPCell(new Phrase(model.getColumnName(j)));
	                pdfTable.addCell(headerCell);
	            }
	            
	            // --- 5. Añadir los datos ---
	            for (int i = 0; i < model.getRowCount(); i++) {
	                for (int j = 0; j < model.getColumnCount(); j++) {
	                    Object value = model.getValueAt(i, j);
	                    String cellValue = (value == null) ? "" : String.valueOf(value);
	                    
	                    pdfTable.addCell(cellValue);
	                }
	            }
	            
	            // --- 6. Añadir la tabla al documento ---
	            document.add(pdfTable);
	            document.close();
	            
	            JOptionPane.showMessageDialog(null, "¡Exportado a PDF con éxito!");

	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}
}
