package com.app.service.customer.entities;

import java.util.Objects;

import com.app.service.customer.validators.IsValidCreditStatus;
import com.app.service.customer.validators.IsValidCustomerType;
import com.app.service.customer.validators.IsValidGreetingType;
import com.app.service.customer.validators.IsValidGstnType;
import com.app.service.customer.validators.IsValidPhoneNumber;
import com.app.service.customer.validators.IsValidRatingType;
import com.app.service.customer.validators.IsValidState;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO POJO for customer
 */

public class CustomerDto {
	private int slNo;
	private String parentCompany;
	private String dataImportFk;
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
	@NotBlank(message = "brand cannot be null")
	private String brand;
	@NotBlank(message = "Supply state cannot be blank")
	@IsValidState(message = "Invalid supply state")
	private String supplyState;
	@NotBlank(message = "GST Type cannot be blank")
	@IsValidGstnType(message = "Invalid Gstn Type")
	private String gstType;
	private boolean isTaxExempt;
	@NotBlank(message = "Greeting cannot be blank")
	@IsValidGreetingType(message = "Invalid Greeting Type")
	private String greeting;
	@IsValidCreditStatus(message = "Invalid Credit Status Type")
	@NotBlank(message = "Credit status cannot be blank")
	private String creditStatus;
	@NotBlank(message = "Rating cannot be blank")
	@IsValidRatingType(message = "Invalid Rating Type")
	private String rating;
	private boolean allowDuplicateGSTIN;
	private String customerGstIn;
	private String supplyGstIn;
	@IsValidPhoneNumber(message = "Invalid Phone Number")
	private long phoneNo;
	@IsValidPhoneNumber(message = "Invalid Mobile Number")
	private long mobileNo;
	@IsValidPhoneNumber(message = "Invalid Fax Number")
	private long faxNumber;
	@NotBlank(message = "Email cannot be blank")
	@Email
	private String email;
	private String website;
	private String tanNo;
	private String panNo;

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

	public String getDataImportFk() {
		return dataImportFk;
	}

	public void setDataImportFk(String dataImportFk) {
		this.dataImportFk = dataImportFk;
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

	public boolean isTaxExempt() {
		return isTaxExempt;
	}

	public void setTaxExempt(boolean isTaxExempt) {
		this.isTaxExempt = isTaxExempt;
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

	public long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public long getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(long faxNumber) {
		this.faxNumber = faxNumber;
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

	public String getTanNo() {
		return tanNo;
	}

	public void setTanNo(String tanNo) {
		this.tanNo = tanNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	@Override
	public String toString() {
		return "CustomerDto [sNo=" + slNo + ", parentCompany=" + parentCompany + ", dataImportFk=" + dataImportFk
				+ ", customerType=" + customerType + ", customerCode=" + customerCode + ", customerName=" + customerName
				+ ", customerAlias=" + customerAlias + ", brand=" + brand + ", supplyState=" + supplyState
				+ ", gstType=" + gstType + ", isTaxExempt=" + isTaxExempt + ", greeting=" + greeting + ", creditStatus="
				+ creditStatus + ", rating=" + rating + ", allowDuplicateGSTIN=" + allowDuplicateGSTIN
				+ ", customerGstIn=" + customerGstIn + ", supplyGstIn=" + supplyGstIn + ", phoneNo=" + phoneNo
				+ ", mobileNo=" + mobileNo + ", faxNumber=" + faxNumber + ", email=" + email + ", website=" + website
				+ ", tanNo=" + tanNo + ", panNo=" + panNo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerAlias, customerCode, customerName);
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
		return Objects.equals(customerAlias, other.customerAlias) || Objects.equals(customerCode, other.customerCode)
				|| Objects.equals(customerName, other.customerName);
	}

}
