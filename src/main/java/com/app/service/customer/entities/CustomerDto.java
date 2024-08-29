package com.app.service.customer.entities;

import java.util.Objects;
import java.util.UUID;

import com.app.service.customer.validators.IsValidCreditStatus;
import com.app.service.customer.validators.IsValidCustomerType;
import com.app.service.customer.validators.IsValidGreetingType;
import com.app.service.customer.validators.IsValidGstnType;
import com.app.service.customer.validators.IsValidPhoneNumber;
import com.app.service.customer.validators.IsValidRatingType;
import com.app.service.customer.validators.IsValidState;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO POJO for customer
 */

public class CustomerDto extends PatchableObject {
	private UUID customerpk;
	@JsonIgnore
	private int slNo;
	private String parentCompany;
	private UUID dataimportfk;
	@NotBlank(message = "customer type cannot be blank")
	@IsValidCustomerType(message = "Invalid Customer Type")
	private String customerType;
	@NotBlank(message = "customer code cannot be blank")
	@Size(max = 50, message = "customer code cannot have more than 50 characters")
	private String customerCode;
	@NotNull(message = "customer name cannot be blank")
	@Size(max = 200, message = "Customer name length is more than 200 characters")
	private String customerName;
	@NotBlank
	@Size(max = 200, message = "Customer alias length is more than 200 characters")
	private String customerAlias;
	@NotBlank(message = "brand cannot be blank")
	private String brand;
	@NotBlank(message = "Supply state cannot be blank")
	@IsValidState(message = "Invalid supply state")
	private String supplyState;
	@NotBlank(message = "GST Type cannot be blank")
	@IsValidGstnType(message = "Invalid Gstn Type")
	private String gstType;
	private boolean isTaxexempt;
	@NotBlank(message = "Greeting cannot be blank")
	@IsValidGreetingType(message = "Invalid Greeting Type")
	private String greeting;
	@IsValidCreditStatus(message = "Invalid Credit Status Type")
	@NotBlank(message = "Credit status cannot be blank")
	private String creditStatus;
	@NotBlank(message = "Rating cannot be blank")
	@IsValidRatingType(message = "Invalid Rating Type")
	private String rating;
	private boolean allowDuplicateGSTIN = false;
	private String customerGstIn;
	private String supplyGstIn;
	@IsValidPhoneNumber(message = "Invalid Phone Number")
	private long phoneno;
	@IsValidPhoneNumber(message = "Invalid Mobile Number")
	private long mobileno;
	@IsValidPhoneNumber(message = "Invalid Fax Number")
	private long faxnumber;
	@NotBlank(message = "Email cannot be blank")
	@Email
	private String email;
	@NotBlank(message = "Website cannot be blank")
	private String website;
	private String tanno;
	private String panno;
	private boolean isactive = true;

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(String parentCompany) {
		this.parentCompany = parentCompany;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAlias() {
		return customerAlias;
	}

	public void setCustomerAlias(String customerAlias) {
		this.customerAlias = customerAlias;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSupplyState() {
		return supplyState;
	}

	public void setSupplyState(String supplyState) {
		this.supplyState = supplyState;
	}

	public String getGstType() {
		return gstType;
	}

	public void setGstType(String gstType) {
		this.gstType = gstType;
	}


	public boolean isTaxexempt() {
		return isTaxexempt;
	}

	public void setTaxexempt(boolean isTaxexempt) {
		this.isTaxexempt = isTaxexempt;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public boolean isAllowDuplicateGSTIN() {
		return allowDuplicateGSTIN;
	}

	public void setAllowDuplicateGSTIN(boolean allowDuplicateGSTIN) {
		this.allowDuplicateGSTIN = allowDuplicateGSTIN;
	}

	public String getCustomerGstIn() {
		return customerGstIn;
	}

	public void setCustomerGstIn(String customerGstIn) {
		this.customerGstIn = customerGstIn;
	}

	public String getSupplyGstIn() {
		return supplyGstIn;
	}

	public void setSupplyGstIn(String supplyGstIn) {
		this.supplyGstIn = supplyGstIn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public UUID getCustomerpk() {
		return customerpk;
	}

	public void setCustomerpk(UUID customerpk) {
		this.customerpk = customerpk;
	}

	public UUID getDataimportfk() {
		return dataimportfk;
	}

	public void setDataimportfk(UUID dataimportfk) {
		this.dataimportfk = dataimportfk;
	}

	public long getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(long phoneno) {
		this.phoneno = phoneno;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public long getFaxnumber() {
		return faxnumber;
	}

	public void setFaxnumber(long faxnumber) {
		this.faxnumber = faxnumber;
	}

	public String getTanno() {
		return tanno;
	}

	public void setTanno(String tanno) {
		this.tanno = tanno;
	}

	public String getPanno() {
		return panno;
	}

	public void setPanno(String panno) {
		this.panno = panno;
	}

	@Override
	public String toString() {
		return "CustomerDto [customerpk=" + customerpk + ", slNo=" + slNo + ", parentCompany=" + parentCompany
				+ ", dataimportfk=" + dataimportfk + ", customerType=" + customerType + ", customerCode=" + customerCode
				+ ", customerName=" + customerName + ", customerAlias=" + customerAlias + ", brand=" + brand
				+ ", supplyState=" + supplyState + ", gstType=" + gstType + ", istaxexempt=" + isTaxexempt
				+ ", greeting=" + greeting + ", creditStatus=" + creditStatus + ", rating=" + rating
				+ ", allowDuplicateGSTIN=" + allowDuplicateGSTIN + ", customerGstIn=" + customerGstIn + ", supplyGstIn="
				+ supplyGstIn + ", phoneno=" + phoneno + ", mobileno=" + mobileno + ", faxnumber=" + faxnumber
				+ ", email=" + email + ", website=" + website + ", tanno=" + tanno + ", panno=" + panno + ", isactive="
				+ isactive + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowDuplicateGSTIN, brand, creditStatus, customerAlias, customerCode, customerGstIn,
				customerName, customerType, customerpk, dataimportfk, email, faxnumber, greeting, gstType, isTaxexempt,
				isactive, mobileno, panno, parentCompany, phoneno, rating, slNo, supplyGstIn, supplyState, tanno,
				website);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDto other = (CustomerDto) obj;
		return allowDuplicateGSTIN == other.allowDuplicateGSTIN && Objects.equals(brand, other.brand)
				&& Objects.equals(creditStatus, other.creditStatus)
				&& Objects.equals(customerAlias, other.customerAlias)
				&& Objects.equals(customerCode, other.customerCode)
				&& Objects.equals(customerGstIn, other.customerGstIn)
				&& Objects.equals(customerName, other.customerName) && Objects.equals(customerType, other.customerType)
				&& Objects.equals(customerpk, other.customerpk) && Objects.equals(dataimportfk, other.dataimportfk)
				&& Objects.equals(email, other.email) && faxnumber == other.faxnumber
				&& Objects.equals(greeting, other.greeting) && Objects.equals(gstType, other.gstType)
				&& isTaxexempt == other.isTaxexempt && isactive == other.isactive && mobileno == other.mobileno
				&& Objects.equals(panno, other.panno) && Objects.equals(parentCompany, other.parentCompany)
				&& phoneno == other.phoneno && Objects.equals(rating, other.rating) && slNo == other.slNo
				&& Objects.equals(supplyGstIn, other.supplyGstIn) && Objects.equals(supplyState, other.supplyState)
				&& Objects.equals(tanno, other.tanno) && Objects.equals(website, other.website);
	}

	public CustomerDto() {
		super();
	}
}
