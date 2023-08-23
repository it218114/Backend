package com.erasmus.appli.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {

	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  @Size(max = 20)
  private String firstName;
  
  @NotBlank
  @Size(max = 20)
  private String lastName;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;
  
  @NotBlank
  @Size(max = 10)
  private String phone;
  
  @NotBlank
  private String role;
  
  @NotBlank
  private String enabled;
  
  public Users(String firstName, String lastName, String email, String password, String phone, String role) {
		this.firstName= firstName;
		this.lastName= lastName;
		this.email=email;
		this.phone = phone;
		this.password=password;
		this.role=role;
  }

}
