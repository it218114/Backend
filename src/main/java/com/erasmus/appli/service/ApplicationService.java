package com.erasmus.appli.service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.erasmus.appli.entity.Application;
import com.erasmus.appli.entity.Users;
import com.erasmus.appli.exception.ErasmusAppException;
import com.erasmus.appli.payload.request.ApplicationApproveRequest;
import com.erasmus.appli.payload.request.ApplicationRequest;
import com.erasmus.appli.repository.ApplicationRepository;
import com.erasmus.appli.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApplicationService {
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	ApplicationRepository applicationRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;

	public Application saveApplication(String data, MultipartFile doc) throws SerialException, SQLException, IOException {
		
		ApplicationRequest applicationRequest = mapper.readValue(data, ApplicationRequest.class);
    	if(userService.isTeacher(applicationRequest.getUserId())) {
    		throw new ErasmusAppException("User dont have permission to apply");
    	}
    	Optional<Users> userOptional = userRepo.findById(applicationRequest.getUserId());
    	if(!userOptional.isPresent() && (userOptional != null && userOptional.get() != null && !userOptional.get().getEnabled().equals("Y"))) {
    		throw new ErasmusAppException("User not found");
    	}
    	
    	Application app = new Application();
    	app.setEnabled("Y");
    	app.setFirstName(applicationRequest.getFirstName());
    	app.setEmail(applicationRequest.getEmail());
    	app.setStreet(applicationRequest.getStreet());
    	Users users = userOptional.get();
    	app.setUser(users);
    	app.setLastName(applicationRequest.getLastName());
    	app.setPhone(applicationRequest.getPhone());
    	app.setUniversity(applicationRequest.getUniversity());
    	app.setAppliedOn(LocalDateTime.now());
    	app.setStatus("pending");
    	if(doc!=null) {
			Blob blob = new SerialBlob(doc.getBytes());
			app.setGrades(blob);
			app.setGradeFileExt(StringUtils.getFilenameExtension(doc.getOriginalFilename()));
		}
    	
		return applicationRepo.save(app);
	}
	
	public List<Application> getAllPendingApplicationByUser(long userId) throws SerialException, SQLException, IOException {
		Optional<Users> userOptional = userRepo.findById(userId);
    	if(!userOptional.isPresent() && (userOptional != null && userOptional.get() != null && userOptional.get().getEnabled().equals("Y"))) {
    		throw new ErasmusAppException("User not found");
    	}
    	
    	if(userService.isTeacher(userId)) {
    		return applicationRepo.findAllByStatusAndEnabled("pending","Y");
    	}
		return applicationRepo.findAllByUserIdAndStatusAndEnabled(userId, "pending","Y");
	}
	
	public List<Application> getAllApplicationNotPendingByUser(long userId) throws SerialException, SQLException, IOException {
		Optional<Users> userOptional = userRepo.findById(userId);
    	if(!userOptional.isPresent() && (userOptional != null && userOptional.get() != null && userOptional.get().getEnabled().equals("Y"))) {
    		throw new ErasmusAppException("User not found");
    	}
    	
    	if(userService.isTeacher(userId)) {
    		return applicationRepo.findAllByStatusNotAndEnabled("pending","Y");
    	}
    	
		return applicationRepo.findAllByUserIdAndEnabledAndStatusNot(userId,"Y", "pending");
	}
	
	public List<Application> getAllApplication() throws SerialException, SQLException, IOException {
		return applicationRepo.findAllByUserEnabledAndEnabled("Y","Y");
	}
	
	public Application getApplicationById(long appId) throws SerialException, SQLException, IOException {
		Optional<Application> findById = applicationRepo.findByIdAndEnabled(appId,"Y");
		if(findById.isPresent()) {
			return findById.get();
		} 
		return null;
	}
	
	public Application deleteApplicationById(long appId) throws SerialException, SQLException, IOException {
		Optional<Application> findById = applicationRepo.findByIdAndEnabled(appId,"Y");
		if(findById.isPresent()) {
			Application application = findById.get();
			application.setEnabled("N");
			return applicationRepo.save(application);
		} 
		return null;
	}
	
	public Application approveApplication(String data, MultipartFile doc) throws SerialException, SQLException, IOException {
		//long appId, long userId, String status
		ApplicationApproveRequest applicationRequest = mapper.readValue(data, ApplicationApproveRequest.class);
		Optional<Application> findById = applicationRepo.findByIdAndEnabled(applicationRequest.getAppId(),"Y");
		
		if(!userService.isTeacher(applicationRequest.getUserId())) {
    		throw new ErasmusAppException("User dont have permission to apply");
    	}
		
		Optional<Users> userOptional = userRepo.findById(applicationRequest.getUserId());
    	if(!userOptional.isPresent() && (userOptional != null && userOptional.get() != null && userOptional.get().getEnabled().equals("Y"))) {
    		throw new ErasmusAppException("User not found");
    	}
    	
		if(findById.isPresent()) {
			Application application = findById.get();
			application.setStatus("approved");
			application.setModifiedOn(LocalDateTime.now());
			application.setApprovedBy(userOptional.get());
			if(doc!=null) {
				Blob blob = new SerialBlob(doc.getBytes());
				application.setApprovalDoc(blob);
				application.setApprovalDocExt(StringUtils.getFilenameExtension(doc.getOriginalFilename()));
			}
			sendEmail(application);
			return applicationRepo.save(application);
		} 
		return null;
	}
	
	public Application declineApplication(long appId, long userId) throws SerialException, SQLException, IOException {
		//
		Optional<Application> findById = applicationRepo.findById(appId);
		
		if(!userService.isTeacher(userId)) {
    		throw new ErasmusAppException("User dont have permission to decline");
    	}
		
		Optional<Users> userOptional = userRepo.findById(userId);
    	if(!userOptional.isPresent() && (userOptional != null && userOptional.get() != null && userOptional.get().getEnabled().equals("Y"))) {
    		throw new ErasmusAppException("User not found");
    	}
    	
		if(findById.isPresent()) {
			Application application = findById.get();
			application.setStatus("declined");
			application.setModifiedOn(LocalDateTime.now());
			application.setApprovedBy(userOptional.get());
			sendEmail(application);
			return applicationRepo.save(application);
		} 
		return null;
	}
	
	public void sendEmail(Application application) {
		String subject = "";
		Map<Object, Object> emailMap = new LinkedHashMap<>();
		subject = "Your Erasmus application form status - " + application.getStatus().toUpperCase();
		emailMap.put("Application ID", application.getId());
		emailMap.put("First Name", application.getFirstName());
		emailMap.put("Last Name", application.getLastName());
		emailMap.put("University", application.getUniversity());
		emailMap.put("Applied on", application.getAppliedOn());
		emailMap.put("Status", application.getStatus().toUpperCase());
		try {
			emailService.sendEmail(new String[] { application.getEmail() }, subject, emailMap);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		}
}
