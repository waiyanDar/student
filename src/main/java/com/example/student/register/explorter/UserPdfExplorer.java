package com.example.student.register.explorter;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.student.register.config.ConfigForSmb;
import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.Data;

@Component
@Data
public class UserPdfExplorer {

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigForSmb configForSmb;

	public void writePdf(String fileName, DiskShare diskShare) {

		Document document = new Document();

		try {
			
			File file = diskShare.openFile(fileName, EnumSet.of(AccessMask.GENERIC_WRITE), null, SMB2ShareAccess.ALL,
					SMB2CreateDisposition.FILE_OVERWRITE_IF, null);
			
			PdfWriter writer = PdfWriter.getInstance(document, file.getOutputStream());
	        writer.setEncryption("test".getBytes(), "owner".getBytes(),
	                PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_256);

//			PdfWriter.getInstance(document, file.getOutputStream());
			document.open();

			PdfPTable table = new PdfPTable(4);

			addTableTitle(table, "User List");
			addTableHeaderRow(table);
			addRows(userService.findAllUser(), table);

			document.add(table);
			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addTableHeaderRow(PdfPTable table) {

		Stream.of("Id", "User Id", "Username", "Email").forEach(columnTitle -> {

			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setPhrase(new Phrase(columnTitle));
			header.setBorderWidth(1);
			header.setFixedHeight(20f);
			table.addCell(header);

		});

		try {
			table.setWidths(new float[] { 25f, 50f, 75f, 100f });

		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	private void addRows(List<User> listUser, PdfPTable table) {
		listUser.forEach(u -> {

			addCell(u.getId().toString(), table);
			addCell(u.getUserId(), table);
			addCell(u.getUsername(), table);
			addCell(u.getEmail(), table);

		});
	}

	private void addCell(String data, PdfPTable table) {
		PdfPCell cell = new PdfPCell(new Phrase(data));
		cell.setFixedHeight(20f);
		table.addCell(cell);
	}

	private void addTableTitle(PdfPTable table, String title) {

		Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		Phrase phrase = new Phrase(title);
		phrase.setFont(font);

		PdfPCell titleCell = new PdfPCell(phrase);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorderWidth(0);
		titleCell.setColspan(4);
		titleCell.setPadding(10f);
		titleCell.setFixedHeight(30f);

		table.addCell(titleCell);

	}

}
