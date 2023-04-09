package com.vinothit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

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
	public boolean exportExcel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exportPdf() {
		// TODO Auto-generated method stub
		return false;
	}

}
