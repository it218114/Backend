package com.erasmus.appli.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

		@JsonProperty("message")
		private String message = null;

		@JsonProperty("status")
		private String status = null;
		
		
}
