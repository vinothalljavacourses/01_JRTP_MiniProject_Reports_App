package com.vinothit.service;

import java.io.File;
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
import com.vinothit.requestdto.SearchRequestDTO;
import com.vinothit.util.EmailUtils;
import com.vinothit.util.ExcelGenerator;
import com.vinothit.util.PdfGenerator;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private CitizenPlanRepository citizenPlanRepo;
	
	@Autowired
	private ExcelGenerator excelGenerator;
	
	@Autowired
	private PdfGenerator pdfGenerator;
	
	@Autowired
	private EmailUtils emailUtils;

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
	public List<CitizenPlan> search(SearchRequestDTO searchRequest) {
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
		
		File f=new File("Plans.xlsx");
		List<CitizenPlan> allPlans = citizenPlanRepo.findAll();		
		
		// call ExcelGenerator Util class
		excelGenerator.exportExcel(response,allPlans);	
		
		// call EmailUtils to send email..
		String subject = "Test mail subject";
		String body = "<h1> Test mail Body</h1>";
		String to = "vinronric@gmail.com";
			
		emailUtils.sendEmail(subject, body, to,f);
		
		f.delete();
		
		return true;
	}

	@Override
	public boolean exportPdf(HttpServletResponse response) throws Exception {
		
		File f=new File("Plans.xlsx");
		List<CitizenPlan> allPlans = citizenPlanRepo.findAll();		
		
		// call pdfGenerator Util class
		pdfGenerator.exportPdf(response, allPlans,f);
		
		// call EmailUtils to send email..
		String subject = "Test mail subject";
		String body = "<h1> Test mail Body</h1>";
		String to = "vinronric@gmail.com";
			
		emailUtils.sendEmail(subject, body, to,f);
		
		f.delete();
		
		return true;
	}

    

}
