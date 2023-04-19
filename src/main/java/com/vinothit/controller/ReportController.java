package com.vinothit.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.vinothit.entity.CitizenPlan;
import com.vinothit.requestdto.SearchRequestDTO;
import com.vinothit.service.ReportService;

@Controller
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/pdf")
	public void excelPdf(HttpServletResponse response, Model model) throws Exception {	
		
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment;filename=plans.pdf");	
	    reportService.exportPdf(response);
		
	}
	
	@GetMapping("/excel")
	public void excelExport(HttpServletResponse response,Model model) throws Exception {	
		
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "attachment;filename=plans.xlsx");	
		reportService.exportExcel(response);
		
	}

	@GetMapping("/")
	public String loadIndexPage(Model model) {
		
		model.addAttribute("searchRequest", new SearchRequestDTO());		
		formInitBinding(model);
		
		return "index";
	}

	private void formInitBinding(Model model) {
		
		model.addAttribute("planNames", reportService.getPlanNames());
		model.addAttribute("planStatuses", reportService.getPlanStatuses());
	}
	
	@PostMapping("/searchReport")
	public String searchReport(@ModelAttribute("searchRequest") SearchRequestDTO searchRequest, Model model) {
				
		List<CitizenPlan> plans = reportService.search(searchRequest);
		model.addAttribute("plans", plans);
		
		formInitBinding(model);
			
		//model.addAttribute("msg", "Data successfully saved...");
		
		return "index";
	}

}
