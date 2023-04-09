package com.vinothit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.vinothit.entity.CitizenPlan;
import com.vinothit.request.SearchRequest;
import com.vinothit.service.ReportService;

@Controller
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/")
	public String loadIndexPage(Model model) {
		
		System.out.println("ReportController :: getPlanNames() ");	
	
		model.addAttribute("searchRequest", new SearchRequest());
		
		formInitBinding(model);
		
		return "index";
	}

	private void formInitBinding(Model model) {
		
		model.addAttribute("planNames", reportService.getPlanNames());
		model.addAttribute("planStatuses", reportService.getPlanStatuses());
	}
	
	@PostMapping("/searchReport")
	public String searchReport(@ModelAttribute("searchRequest") SearchRequest searchRequest, Model model) {
		
		System.out.println("ReportController :: searchReport");
		System.out.println("searchRequest :: " + searchRequest);
		System.out.println("model :: " + model);
		
		List<CitizenPlan> plans = reportService.search(searchRequest);
		model.addAttribute("plans", plans);
		
		formInitBinding(model);
		
		
		model.addAttribute("msg", "Data successfully saved...");
		
		return "index";
	}

}
