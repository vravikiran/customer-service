package com.app.service.customer.entities;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/**
 * DataImport entity
 */
@Entity
public class Dataimport {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID dataimportpk;
	private String filepath;
	private byte[] filedata;
	private UUID doctypefk;
	private LocalDate importdate;

	public UUID getDataimportpk() {
		return dataimportpk;
	}

	public void setDataimportpk(UUID dataimportpk) {
		this.dataimportpk = dataimportpk;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public byte[] getFiledata() {
		return filedata;
	}

	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}

	public UUID getDoctypefk() {
		return doctypefk;
	}

	public void setDoctypefk(UUID doctypefk) {
		this.doctypefk = doctypefk;
	}

	public LocalDate getImportdate() {
		return importdate;
	}

	public void setImportdate(LocalDate importdate) {
		this.importdate = importdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(filedata);
		result = prime * result + Objects.hash(dataimportpk, doctypefk, filepath, importdate);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dataimport other = (Dataimport) obj;
		return Objects.equals(dataimportpk, other.dataimportpk) && Objects.equals(doctypefk, other.doctypefk)
				&& Arrays.equals(filedata, other.filedata) && Objects.equals(filepath, other.filepath)
				&& Objects.equals(importdate, other.importdate);
	}

	@Override
	public String toString() {
		return "DataImport [dataimportpk=" + dataimportpk + ", filepath=" + filepath + ", filedata="
				+ Arrays.toString(filedata) + ", doctypefk=" + doctypefk + ", importdate=" + importdate + "]";
	}
}
