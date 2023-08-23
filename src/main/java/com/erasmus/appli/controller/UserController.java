package com.erasmus.appli.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasmus.appli.entity.Users;
import com.erasmus.appli.exception.ErasmusAppException;
import com.erasmus.appli.payload.request.SignupRequest;
import com.erasmus.appli.payload.response.UserInfoResponse;
import com.erasmus.appli.repository.UserRepository;
import com.erasmus.appli.security.jwt.JwtUtils;
import com.erasmus.appli.security.services.UserDetailsImpl;
import com.erasmus.appli.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userService;
	
	 @Autowired
	  UserRepository userRepository;
	
	 @Autowired
	 JwtUtils jwtUtils;
	
	@GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable("email") String email) {
        Users users = userService.getUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
	
	@GetMapping
    public ResponseEntity<?> getAllUser() {
        List<Users> users = userService.getAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
	
	@DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Success");
    }
	
	@PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") long userId, @RequestBody SignupRequest request) {
        Users users = userService.updateUser(userId, request);
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Users enabled = userRepository.findByIdAndEnabled(userDetails.getId(), "Y");
            if(enabled==null) {
            	throw new ErasmusAppException("User is inactive state");
            }
            
            String jwtToken = jwtUtils.generateJwtCookie(userDetails);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtToken.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                                           userDetails.getFirstName(),userDetails.getLastName(),
                                           userDetails.getEmail(), enabled.getPhone(), jwtToken, userDetails.getRole()));
        //return ResponseEntity.status(HttpStatus.OK).body(users);
    }

}
