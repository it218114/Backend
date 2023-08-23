package com.erasmus.appli.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "application_form")
@AllArgsConstructor
@NoArgsConstructor
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private Users user;

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
	@Size(max = 10)
	private String phone;
	
	@Size(max = 1000)
	private String street;

	@Lob
	private Blob grades;

	@NotBlank
	@Size(max = 500)
	private String university;
	
	@NotBlank
	@Size(max = 20)
	private String status;
	
	@Lob
	private Blob approvalDoc;
	
	private String gradeFileExt;
	
	private String approvalDocExt;
	
	@ManyToOne
	private Users approvedBy;
	
	private LocalDateTime appliedOn;
	
	private LocalDateTime modifiedOn;
	
	private String enabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	

	public void setGrades(Blob grades) {
		this.grades = grades;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setApprovalDoc(Blob approvalDoc) {
		this.approvalDoc = approvalDoc;
	}
	
	public String getGrades() {
		try {
			String data = null;
			if(gradeFileExt != null && !gradeFileExt.equals("")) {
				if(gradeFileExt.equals("pdf")) {
					data = "data:application/pdf;base64,";
				} else if(gradeFileExt.equals("png")) {
					data = "data:image/png;base64,";
				} else if(gradeFileExt.equals("jpg")) {
					data = "data:image/jpg;base64,";
				} else if(gradeFileExt.equals("jpeg")) {
					data = "data:image/jpeg;base64,";
				}
			}
			
			if(grades != null && grades.getBinaryStream().readAllBytes() != null && !Base64.getEncoder().encodeToString(grades.getBinaryStream().readAllBytes()).equals("")) {
				return data+Base64.getEncoder().encodeToString(grades.getBinaryStream().readAllBytes());
			}
			return "";
			} catch (Exception e) {
				return "";
			}
	}
	
	public String getApprovalDoc() {
		try {
		String data = null;
		if(approvalDocExt != null && !approvalDocExt.equals("")) {
			if(approvalDocExt.equals("pdf")) {
				data = "data:application/pdf;base64,";
			} else if(approvalDocExt.equals("png")) {
				data = "data:image/png;base64,";
			} else if(approvalDocExt.equals("jpg")) {
				data = "data:image/jpg;base64,";
			} else if(approvalDocExt.equals("jpeg")) {
				data = "data:image/jpeg;base64,";
			}
		}
		
		if(approvalDoc != null && approvalDoc.getBinaryStream().readAllBytes() != null && !Base64.getEncoder().encodeToString(approvalDoc.getBinaryStream().readAllBytes()).equals("")) {
			return data+Base64.getEncoder().encodeToString(approvalDoc.getBinaryStream().readAllBytes());
		}
		return "";
		} catch (Exception e) {
			return "";
		}
	}


	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getAppliedOn() {
		return appliedOn;
	}

	public void setAppliedOn(LocalDateTime appliedOn) {
		this.appliedOn = appliedOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getGradeFileExt() {
		return gradeFileExt;
	}

	public void setGradeFileExt(String gradeFileExt) {
		this.gradeFileExt = gradeFileExt;
	}

	public String getApprovalDocExt() {
		return approvalDocExt;
	}

	public void setApprovalDocExt(String approvalDocExt) {
		this.approvalDocExt = approvalDocExt;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
}
