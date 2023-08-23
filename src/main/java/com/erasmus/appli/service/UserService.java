package com.erasmus.appli.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasmus.appli.entity.Application;
import com.erasmus.appli.entity.Users;
import com.erasmus.appli.exception.ErasmusAppException;
import com.erasmus.appli.payload.request.SignupRequest;
import com.erasmus.appli.repository.ApplicationRepository;
import com.erasmus.appli.repository.UserRepository;

@Service
public class UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ApplicationRepository applicationRepo;
	
	public boolean isTeacher(long id) {
		Optional<Users> user = userRepo.findById(id);
		return (user!=null && user.isPresent()) ? user.get().getRole().equals("teacher") && user.get().getEnabled().equals("Y") : false;
	}

	public Users getUser(String email) {
		Optional<Users> users = userRepo.findByEmail(email);
		return users.isPresent()?users.get():null;
	}

	public Users updateUser(long userId, SignupRequest request) {
		Optional<Users> userOptional = userRepo.findById(userId);
			if(userOptional.isPresent()) {
				Users user = userOptional.get();
				if(!user.getEmail().equals(request.getEmail().trim())) {
					if (userRepo.findByEmail(request.getEmail()).isPresent()) {
					    throw new ErasmusAppException("Email already taken!!");
					}
					user.setEmail(request.getEmail());
				}
				
				user.setFirstName(request.getFirstName());
				user.setLastName(request.getLastName());
				user.setPassword(request.getPassword());
				user.setRole(request.getRole());
				return userRepo.save(user);
			} else {
				throw new ErasmusAppException("Email not found!!");
			}
	}

	public List<Users> getAllUser() {
		return userRepo.findAllByRoleNotAndEnabled("admin","Y");
	}

	public Users deleteUser(long userId) {
		
		Optional<Users> userOptional = userRepo.findById(userId);
		if(userOptional.isPresent()) {
			List<Application> appList = applicationRepo.findAllByUserId(userId);
			applicationRepo.deleteAll(appList);
//			Users user = userOptional.get();
//			user.setEnabled("N");
//			return userRepo.save(user);
			userRepo.delete(userOptional.get());
		}
		return null;
	}
}
