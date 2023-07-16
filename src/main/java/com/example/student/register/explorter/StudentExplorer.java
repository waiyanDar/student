package com.example.student.register.explorter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.student.register.entity.Student;
import com.example.student.register.service.StudentService;

import lombok.Data;

@Component
@Data
public class StudentExplorer {
	static final Logger logger = Logger.getLogger(StudentExplorer.class.getName());

	@Autowired
	private StudentService studentService;
	
	public void explortStudentToExcel() {
		
		List<Student> students = studentService.findAllStudent();
		
		try(Workbook workbook = WorkbookFactory.create(true)) {
			Sheet sheet = workbook.createSheet("Student Data");
			
			Row headerRow = sheet.createRow(0);
			
			CellStyle headerCellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerCellStyle.setFont(font);
			
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Student ID");
			headerRow.createCell(2).setCellValue("Name");
			headerRow.createCell(3).setCellValue("Date of Birth");
			headerRow.createCell(4).setCellValue("Phone");
			headerRow.createCell(5).setCellValue("Photo");
			headerRow.createCell(6).setCellValue("Gender");
			headerRow.createCell(7).setCellValue("Education");
			
			for(Cell cell : headerRow) {
				cell.setCellStyle(headerCellStyle);
			}
			
			int rowNumber = 1;
			
			for(Student student : students) {
				Row dataRow = sheet.createRow(rowNumber++);
				dataRow.createCell(0).setCellValue(student.getId());
				dataRow.createCell(1).setCellValue(student.getStudentId());
				dataRow.createCell(2).setCellValue(student.getName());
				dataRow.createCell(3).setCellValue(student.getDateOfBirth());
				dataRow.createCell(4).setCellValue(student.getPhone());
				dataRow.createCell(5).setCellValue(student.getPhoto().toString());
				dataRow.createCell(6).setCellValue(student.getGender().toString());
				dataRow.createCell(7).setCellValue(student.getEducation().toString());
			}
			
			for (int i = 0; i < 8; i++) {
				sheet.autoSizeColumn(i);
			}
			
			String fileName = "src/main/resources/static/excel/student_data.xlsx";

			File file = new File(fileName);
			if (file.exists()) {
				if (file.delete()) {
					logger.info("File successfully deleted");
				} else {
					logger.warning("Failed to delete the file.");
				}
			} else {
				logger.warning("File does not exist.");
			}
			try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
				logger.log(Level.FINE,"File saved");
				workbook.write(fileOutputStream);
			} catch (Exception e) {
				
			}
		} catch (Exception e) {
		}
		
	}

}
