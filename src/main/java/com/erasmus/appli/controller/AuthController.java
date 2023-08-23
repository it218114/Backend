package com.erasmus.appli.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasmus.appli.entity.Users;
import com.erasmus.appli.exception.ErasmusAppException;
import com.erasmus.appli.payload.request.LoginRequest;
import com.erasmus.appli.payload.request.SignupRequest;
import com.erasmus.appli.payload.response.MessageResponse;
import com.erasmus.appli.payload.response.UserInfoResponse;
import com.erasmus.appli.repository.UserRepository;
import com.erasmus.appli.security.jwt.JwtUtils;
import com.erasmus.appli.security.services.UserDetailsImpl;
import com.erasmus.appli.security.services.UserDetailsServiceImpl;



@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  JwtUtils jwtUtils;
  
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

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
                                   userDetails.getEmail(),enabled.getPhone(), jwtToken, userDetails.getRole()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
      throw new ErasmusAppException("Email already taken!!");
    }
    // Create new user's account
    Users user = new Users(signUpRequest.getFirstName(), signUpRequest.getLastName(), 
                         signUpRequest.getEmail(),
                         signUpRequest.getPassword(),
                         signUpRequest.getPhone(),
                         signUpRequest.getRole());
    user.setEnabled("Y");
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
