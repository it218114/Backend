package com.erasmus.appli.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erasmus.appli.entity.Application;
import com.erasmus.appli.payload.request.ApplicationApproveRequest;
import com.erasmus.appli.service.ApplicationService;

@RestController
@CrossOrigin
@RequestMapping("/api/application")
public class ApplicationController {
	
	@Autowired
	ApplicationService appService;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveApplication(
    		@RequestPart("data") String data, @RequestPart(name = "grade", required = false) MultipartFile doc) throws SerialException, SQLException, IOException {
        Application application = appService.saveApplication(data, doc);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/approve")
    public ResponseEntity<?> approveApplication(
    		@RequestPart("data") String data, @RequestPart(name = "grade", required = false) MultipartFile doc) throws SerialException, SQLException, IOException {
        Application application = appService.approveApplication(data, doc);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@PostMapping("/decline")
    public ResponseEntity<?> declineApplication(
    		@RequestBody ApplicationApproveRequest data) throws SerialException, SQLException, IOException {
        Application application = appService.declineApplication(data.getAppId(), data.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@GetMapping("/pending/{userId}")
    public ResponseEntity<?> getAllPendingApplicationByUser(
    		@PathVariable("userId") Long userId) throws SerialException, SQLException, IOException {
        List<Application> application = appService.getAllPendingApplicationByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@GetMapping("/non-pending/{userId}")
    public ResponseEntity<?> getAllNonPendingApplicationByUser(
    		@PathVariable("userId") Long userId) throws SerialException, SQLException, IOException {
        List<Application> application = appService.getAllApplicationNotPendingByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@GetMapping("/{appId}")
    public ResponseEntity<?> getApplicationById(
    		@PathVariable("appId") Long appId) throws SerialException, SQLException, IOException {
        Application application = appService.getApplicationById(appId);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@DeleteMapping("/{appId}")
    public ResponseEntity<?> deleteApplicationById(
    		@PathVariable("appId") Long appId) throws SerialException, SQLException, IOException {
        Application application = appService.deleteApplicationById(appId);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@GetMapping()
    public ResponseEntity<?> getAllApplication() throws SerialException, SQLException, IOException {
        List<Application> application = appService.getAllApplication();
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
}
