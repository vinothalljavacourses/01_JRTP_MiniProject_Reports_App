package com.vinothit.requestdto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


@Data
public class SearchRequestDTO {
	
	private String planName;
	private String planStatus;
	private String gender;
	private String planStartDate; // yyyy-MM-dd
	private String planEndDate;   // yyyy-MM-dd

}
