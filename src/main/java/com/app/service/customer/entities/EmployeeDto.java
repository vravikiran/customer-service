package com.app.service.customer.entities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class EmployeeDto {
	private UUID customeremppk;
	private String customerName;
	private String designation;
	private String empname;
	private String department;
	private Long phoneno;
	private Long mobileno;
	private String email;
	private LocalDate dob;
	private LocalDate anniversarydate;
	private LocalDate cdate;
	private boolean isactive;

	public UUID getCustomeremppk() {
		return customeremppk;
	}

	public void setCustomeremppk(UUID customeremppk) {
		this.customeremppk = customeremppk;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
		return Objects.hash(anniversarydate, cdate, customerName, customeremppk, department, designation, dob, email,
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
		EmployeeDto other = (EmployeeDto) obj;
		return Objects.equals(anniversarydate, other.anniversarydate) && Objects.equals(cdate, other.cdate)
				&& Objects.equals(customerName, other.customerName)
				&& Objects.equals(customeremppk, other.customeremppk) && Objects.equals(department, other.department)
				&& Objects.equals(designation, other.designation) && Objects.equals(dob, other.dob)
				&& Objects.equals(email, other.email) && Objects.equals(empname, other.empname)
				&& isactive == other.isactive && Objects.equals(mobileno, other.mobileno)
				&& Objects.equals(phoneno, other.phoneno);
	}

	@Override
	public String toString() {
		return "EmployeeDto [customeremppk=" + customeremppk + ", customerName=" + customerName + ", designation="
				+ designation + ", empname=" + empname + ", department=" + department + ", phoneno=" + phoneno
				+ ", mobileno=" + mobileno + ", email=" + email + ", dob=" + dob + ", anniversarydate="
				+ anniversarydate + ", cdate=" + cdate + ", isactive=" + isactive + "]";
	}

	public EmployeeDto() {
		super();
	}

}
