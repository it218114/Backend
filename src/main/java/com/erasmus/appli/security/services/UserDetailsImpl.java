package com.erasmus.appli.security.services;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.erasmus.appli.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private long id;

  private String firstName;
  
  private String lastName;

  private String email;
  
  private String role;

  @JsonIgnore
  private String password;

  public UserDetailsImpl(long id, String firstName, String lastName, String email, String password, String role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public static UserDetailsImpl build(Users user) {

    return new UserDetailsImpl(
        user.getId(), 
        user.getFirstName(), 
        user.getLastName(), 
        user.getEmail(),
        user.getPassword(),
        user.getRole());
  }

  public long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
  
  
  public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

@Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }
  
  public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

@Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
	// TODO Auto-generated method stub
	return null;
}
}
