package com.vinothit.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


@Data
public class SearchRequest {
	
	private String planName;
	private String planStatus;
	private String gender;
	private String planStartDate; // yyyy-MM-dd
	private String planEndDate;   // yyyy-MM-dd

}
