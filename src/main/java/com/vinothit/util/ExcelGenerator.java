package com.vinothit.util;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import com.vinothit.entity.CitizenPlan;

@Component
public class ExcelGenerator {
	
	public void exportExcel(HttpServletResponse response,List<CitizenPlan> allRecords) throws Exception {
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
		
		// to save file in local disk
		FileOutputStream fos=new FileOutputStream("Plans.xlsx");
		workbook.write(fos);
		fos.close();
		
		// to send the downloaded file as well in browser itself
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
	}

}
