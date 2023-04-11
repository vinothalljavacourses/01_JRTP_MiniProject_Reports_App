package com.vinothit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vinothit.entity.CitizenPlan;
import com.vinothit.repository.CitizenPlanRepository;
import com.vinothit.request.SearchRequest;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private CitizenPlanRepository citizenPlanRepo;

	@Override
	public List<String> getPlanNames() {
		List<String> planNames = citizenPlanRepo.getPlanNames();
		return planNames;
	}

	@Override
	public List<String> getPlanStatuses() {
		List<String> planAllStatus = citizenPlanRepo.getPlanAllStatus();
		return planAllStatus;
	}

	@Override
	public List<CitizenPlan> search(SearchRequest searchRequest) {
		CitizenPlan entity=new CitizenPlan();
		
		//BeanUtils.copyProperties(searchRequest, entity);
		
		if(searchRequest.getPlanName() != null && searchRequest.getPlanName() != "") {
			entity.setPlanName(searchRequest.getPlanName());
		}
		
		if(searchRequest.getPlanStatus() != null && searchRequest.getPlanStatus() != "") {
			entity.setPlanStatus(searchRequest.getPlanStatus());
		}
		
		if(searchRequest.getGender() != null && searchRequest.getGender() != "") {
			entity.setGender(searchRequest.getGender());
		}
		
		if(searchRequest.getPlanStartDate() != null && searchRequest.getPlanStartDate() != "") {
			String planStartDate = searchRequest.getPlanStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate localDate = LocalDate.parse(planStartDate, formatter);
	        entity.setPlanStartDate(localDate);	
		}
		
		if(searchRequest.getPlanEndDate() != null && searchRequest.getPlanEndDate() != "") {
			String planEndDate = searchRequest.getPlanEndDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate localDate = LocalDate.parse(planEndDate, formatter);
	        entity.setPlanEndDate(localDate);
		}
		
		return citizenPlanRepo.findAll(Example.of(entity));
	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception{
		
		Workbook workbook=new XSSFWorkbook(); // both .xlsx and .xls extensions
		//Workbook workbook=new HSSFWorkbook(); // .xls extension only
		
		Sheet sheet = workbook.createSheet("plan-data");
		Row headerRow = sheet.createRow(0);
		
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Citizen Name");
		headerRow.createCell(2).setCellValue("Plan Name");
		headerRow.createCell(3).setCellValue("Plan Status");
		headerRow.createCell(4).setCellValue("Plan Start Date");
		headerRow.createCell(5).setCellValue("Plan End Date");
		headerRow.createCell(6).setCellValue("Benefit Amount");
		
		
		List<CitizenPlan> allRecords = citizenPlanRepo.findAll();
		
		int rowIndex=1;
		
		for(CitizenPlan plan : allRecords) {
		  Row dataRow = sheet.createRow(rowIndex);
		  dataRow.createCell(0).setCellValue(plan.getCitizenId());
		  dataRow.createCell(1).setCellValue(plan.getCitizenName());
		  dataRow.createCell(2).setCellValue(plan.getPlanName());
		  dataRow.createCell(3).setCellValue(plan.getPlanStatus());
		  if(plan.getPlanStartDate() !=null) {
		     dataRow.createCell(4).setCellValue(plan.getPlanStartDate()+"");
		  }else {
			 dataRow.createCell(4).setCellValue("N/A");  
		  }
		  if(plan.getPlanEndDate() != null) {
		     dataRow.createCell(5).setCellValue(plan.getPlanEndDate()+"");
		  }else {
			 dataRow.createCell(5).setCellValue("");
		  }
		  if(plan.getBenefitAmount() != null) {
		    dataRow.createCell(6).setCellValue(plan.getBenefitAmount());
		  } else {
			dataRow.createCell(6).setCellValue("N/A");  
		  }
		  
		  rowIndex++;
		  
		}
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
   
		return true;
	}

	@Override
	public boolean exportPdf(HttpServletResponse response) throws Exception {
		
		Document document=new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		// Creating font
		// Setting font style and size
		Font fontTitle= FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTitle.setSize(20);
		
		// Creating paragraph
		Paragraph paragraph=new Paragraph("Citizen Plans Info",fontTitle);
		
		// Aligning the paragraph in document
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		// Adding the created paragraph in document
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(7);
		table.setSpacingBefore(5);
		table.addCell("ID");
		table.addCell("Citizen Name");
		table.addCell("Plan Name");
		table.addCell("Plan Status");
		table.addCell("Plan Start Date");
		table.addCell("Plan End Date");
		table.addCell("Benefit Amount");
		
        List<CitizenPlan> allRecords = citizenPlanRepo.findAll();
		
		for(CitizenPlan plan : allRecords) {
			table.addCell(String.valueOf(plan.getCitizenId()));
			table.addCell(plan.getCitizenName());
			table.addCell(plan.getPlanName());
			table.addCell(plan.getPlanStatus());
			if(plan.getPlanStartDate() !=null) {
			  table.addCell(plan.getPlanStartDate()+"");
			}else {
			  table.addCell("N/A");	
			}
			if(plan.getPlanEndDate() != null) {
			  table.addCell(plan.getPlanEndDate()+"");
			}else {
			  table.addCell("N/A");		
			}	
			table.addCell(String.valueOf(plan.getBenefitAmount()));
		}
		
		document.add(table);
		document.close();

		return true;
	}

    

}
