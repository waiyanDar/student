package com.example.student.register.explorter;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;

import lombok.Data;

@Component
@Data
public class UserExplorer {

    @Autowired
    private UserService userService;

    List<String> roleName;

    public void exportUserToExcel() {

        List<User> users = userService.findAllUser();

        try (Workbook workbook = WorkbookFactory.create(true)) {
            Sheet sheet = workbook.createSheet("User Data");

            Row headerRow = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerCellStyle.setFont(font);

            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("USER ID");
            headerRow.createCell(2).setCellValue("USERNAME");
            headerRow.createCell(3).setCellValue("EMAIL");
//			headerRow.createCell(4).setCellValue("ROLE");

            for (Cell cell : headerRow) {
                cell.setCellStyle(headerCellStyle);
            }

            int rowNumber = 1;

            for (User user : users) {
                Row dataRow = sheet.createRow(rowNumber++);
                dataRow.createCell(0).setCellValue(user.getId());
                dataRow.createCell(1).setCellValue(user.getUserId());
                dataRow.createCell(2).setCellValue(user.getUsername());
                dataRow.createCell(3).setCellValue(user.getEmail());

//				user.getRoles().forEach(r -> roleName.add(r.getName()));
//				Cell roleCell = dataRow.createCell(4);
//				roleCell.setCellValue(String.join(", ",roleName = user.getRoles().forEach(r -> r.getName());));
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "src/main/resources/static/excel/user_data.xlsx";


            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                workbook.write(fileOutputStream);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
