package com.app.service.customer.entities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.app.service.customer.validators.IsValidPhoneNumber;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID customeremppk;
	@NotNull(message="Invalid customer")
	private UUID customerfk;
	private String designation;
	private String empname;
	private String department;
	@IsValidPhoneNumber(message="Invalid phone number")
	private Long phoneno;
	@IsValidPhoneNumber(message="Invalid mobile number")
	private Long mobileno;
	@Email(message="email is not valid")
	private String email;
	private LocalDate dob;
	private LocalDate anniversarydate;
	private LocalDate cdate;
	private boolean isactive;
	@Transient
	private int slNo;

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public UUID getCustomeremppk() {
		return customeremppk;
	}

	public void setCustomeremppk(UUID customeremppk) {
		this.customeremppk = customeremppk;
	}

	public UUID getCustomerfk() {
		return customerfk;
	}

	public void setCustomerfk(UUID customerfk) {
		this.customerfk = customerfk;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(Long phoneno) {
		this.phoneno = phoneno;
	}

	public Long getMobileno() {
		return mobileno;
	}

	public void setMobileno(Long mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public LocalDate getAnniversarydate() {
		return anniversarydate;
	}

	public void setAnniversarydate(LocalDate anniversarydate) {
		this.anniversarydate = anniversarydate;
	}

	public LocalDate getCdate() {
		return cdate;
	}

	public void setCdate(LocalDate cdate) {
		this.cdate = cdate;
	}

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anniversarydate, cdate, customeremppk, customerfk, department, designation, dob, email,
				empname, isactive, mobileno, phoneno);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(anniversarydate, other.anniversarydate) && Objects.equals(cdate, other.cdate)
				&& Objects.equals(customeremppk, other.customeremppk) && Objects.equals(customerfk, other.customerfk)
				&& Objects.equals(department, other.department) && Objects.equals(designation, other.designation)
				&& Objects.equals(dob, other.dob) && Objects.equals(email, other.email)
				&& Objects.equals(empname, other.empname) && isactive == other.isactive
				&& Objects.equals(mobileno, other.mobileno) && Objects.equals(phoneno, other.phoneno);
	}

	public Employee() {
	}
}
