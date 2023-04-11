package com.vinothit.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.vinothit.entity.CitizenPlan;
import com.vinothit.request.SearchRequest;

public interface ReportService {
	
	public List<String> getPlanNames();
	public List<String> getPlanStatuses();
	public List<CitizenPlan> search(SearchRequest searchRequest);
	public boolean exportExcel(HttpServletResponse response) throws Exception;
	public boolean exportPdf(HttpServletResponse response) throws Exception;
	

}
