package com.app.service.customer.entities;

import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID customerpk;
	private UUID parentcustomerfk;
	private UUID dataimportfk;
	@Nonnull
	private UUID customertypefk;
	@Nonnull
	private String customercode;
	@Nonnull
	private String customername;
	@Nonnull
	private String customeralias;
	@Nonnull
	private UUID supplystatefk;
	@Nonnull
	private UUID gsttypefk;
	@Nonnull
	private boolean istaxexempt;
	@Nonnull
	private UUID greetingfk;
	@Nonnull
	private UUID creditstatusfk;
	@Nonnull
	private UUID ratingfk;
	@Nonnull
	private boolean allowduplicategstin;
	private String customergstin;
	private String supplygstin;
	@Nonnull
	private long phoneno;
	@Nonnull
	private long mobileno;
	@Nonnull
	private long faxnumber;
	@Nonnull
	private String email;
	@Nonnull
	private String website;
	private String tanno;
	private String panno;
	@Nonnull
	private UUID brandfk;
	@Nonnull
	private boolean isactive;

	/*
	 * private UUID cbyfk; private LocalDate cdate; private UUID mbyfk; private
	 * LocalDate mdate;
	 */
	public UUID getCustomerpk() {
		return customerpk;
	}

	public void setCustomerpk(UUID customerpk) {
		this.customerpk = customerpk;
	}

	public UUID getParentcustomerfk() {
		return parentcustomerfk;
	}

	public void setParentcustomerfk(UUID parentcustomerfk) {
		this.parentcustomerfk = parentcustomerfk;
	}

	public UUID getDataimportfk() {
		return dataimportfk;
	}

	public void setDataimportfk(UUID dataimportfk) {
		this.dataimportfk = dataimportfk;
	}

	public UUID getCustomertypefk() {
		return customertypefk;
	}

	public void setCustomertypefk(UUID customertypefk) {
		this.customertypefk = customertypefk;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomeralias() {
		return customeralias;
	}

	public void setCustomeralias(String customeralias) {
		this.customeralias = customeralias;
	}

	public UUID getSupplystatefk() {
		return supplystatefk;
	}

	public void setSupplystatefk(UUID supplystatefk) {
		this.supplystatefk = supplystatefk;
	}

	public UUID getGsttypefk() {
		return gsttypefk;
	}

	public void setGsttypefk(UUID gsttypefk) {
		this.gsttypefk = gsttypefk;
	}

	public boolean isIstaxexempt() {
		return istaxexempt;
	}

	public void setIstaxexempt(boolean istaxexempt) {
		this.istaxexempt = istaxexempt;
	}

	public UUID getGreetingfk() {
		return greetingfk;
	}

	public void setGreetingfk(UUID greetingfk) {
		this.greetingfk = greetingfk;
	}

	public UUID getCreditstatusfk() {
		return creditstatusfk;
	}

	public void setCreditstatusfk(UUID creditstatusfk) {
		this.creditstatusfk = creditstatusfk;
	}

	public UUID getRatingfk() {
		return ratingfk;
	}

	public void setRatingfk(UUID ratingfk) {
		this.ratingfk = ratingfk;
	}

	public boolean isAllowduplicategstin() {
		return allowduplicategstin;
	}

	public void setAllowduplicategstin(boolean allowduplicategstin) {
		this.allowduplicategstin = allowduplicategstin;
	}

	public String getCustomergstin() {
		return customergstin;
	}

	public void setCustomergstin(String customergstin) {
		this.customergstin = customergstin;
	}

	public String getSupplygstin() {
		return supplygstin;
	}

	public void setSupplygstin(String supplygstin) {
		this.supplygstin = supplygstin;
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
	
	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public UUID getBrandfk() {
		return brandfk;
	}

	public void setBrandfk(UUID brandfk) {
		this.brandfk = brandfk;
	}
}
