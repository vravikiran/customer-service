package com.app.service.customer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.EmptyFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.service.customer.config.DBConfig;
import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.enums.CustomerCSVFileHeaders;
import com.app.service.customer.repositories.BrandRepository;
import com.app.service.customer.repositories.CustomerRepository;

/**
 * Reads the contents of uploaded csv file and converts the csv records into
 * customerDtos
 */
@Service
public class CustomerService {
	Logger logger = LoggerFactory.getLogger(CustomerService.class);
	@Autowired
	CustomerFieldsValidator customerFieldsValidator;
	@Autowired
	ErrorLogService errorLogService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	DBConfig dbConfig;
	@Autowired
	BrandRepository brandRepository;

	/**
	 * Reads the rows of csv file and converts them to customerDtos
	 * 
	 * @param in
	 * @throws Exception
	 */
	public boolean uploadCustomerInfo(InputStream in) throws EmptyFileException, IOException {
		List<CustomerDto> customerDtos = null;
		boolean isAllValid = false;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		try {
			CSVParser csvParser = CSVParser.parse(bufferedReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));
			List<CSVRecord> csvRecords = csvParser.getRecords();
			if (csvRecords.isEmpty()) {
				throw new EmptyFileException();
			} else {
				customerDtos = csvRecords.stream().map(customer -> convertCsvRecordToCustomerRecord(customer))
						.collect(Collectors.toList());
				isAllValid = validateListOfCustomers(customerDtos);
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Exception occured while validating customers", "CustomerService :: uploadCustomerInfo - {}",
					e.getMessage());
		}
		return isAllValid;
	}

	/**
	 * converts csvRecord to CustomerDto
	 * 
	 * @param csvRecord
	 * @return
	 */
	private CustomerDto convertCsvRecordToCustomerRecord(CSVRecord csvRecord) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setParentCompany(csvRecord.get(CustomerCSVFileHeaders.ParentCompany));
		customerDto.setDataImportFk(csvRecord.get(CustomerCSVFileHeaders.DataImportfk));
		customerDto.setCustomerCode(csvRecord.get(CustomerCSVFileHeaders.CustomerCode));
		customerDto.setCustomerName(csvRecord.get(CustomerCSVFileHeaders.CustomerName));
		customerDto.setCustomerAlias(csvRecord.get(CustomerCSVFileHeaders.CustomerAlias));
		customerDto.setCustomerType(csvRecord.get(CustomerCSVFileHeaders.CustomerType));
		customerDto.setBrand(csvRecord.get(CustomerCSVFileHeaders.Brand));
		customerDto.setSupplyState(csvRecord.get(CustomerCSVFileHeaders.SupplyState));
		customerDto.setGstType(csvRecord.get(CustomerCSVFileHeaders.GSTType));
		customerDto.setTaxExempt(Boolean.valueOf(csvRecord.get(CustomerCSVFileHeaders.IsTaxExempt)));
		customerDto.setGreeting(csvRecord.get(CustomerCSVFileHeaders.Greeting));
		customerDto.setCreditStatus(csvRecord.get(CustomerCSVFileHeaders.CreditStatus));
		customerDto.setRating(csvRecord.get(CustomerCSVFileHeaders.Rating));
		boolean isAllowed = Integer.valueOf(csvRecord.get(CustomerCSVFileHeaders.AllowDuplicateGSTIN.name())) == 0
				? false
				: true;
		customerDto.setAllowDuplicateGSTIN(isAllowed);
		customerDto.setCustomerGstIn(csvRecord.get(CustomerCSVFileHeaders.CustomerGSTIN));
		customerDto.setSupplyGstIn(csvRecord.get(CustomerCSVFileHeaders.SupplyGSTIN));
		if (!csvRecord.get(CustomerCSVFileHeaders.PhoneNo).isBlank()
				|| !csvRecord.get(CustomerCSVFileHeaders.PhoneNo).isEmpty())
			customerDto.setPhoneNo(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.PhoneNo)));
		if (!csvRecord.get(CustomerCSVFileHeaders.MobileNo).isEmpty())
			customerDto.setMobileNo(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.MobileNo)));
		if (!csvRecord.get(CustomerCSVFileHeaders.FaxNumber).isEmpty())
			customerDto.setFaxNumber(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.FaxNumber)));
		customerDto.setEmail(csvRecord.get(CustomerCSVFileHeaders.Email));
		customerDto.setWebsite(csvRecord.get(CustomerCSVFileHeaders.Website));
		customerDto.setTanNo(csvRecord.get(CustomerCSVFileHeaders.TANNo));
		customerDto.setPanNo(csvRecord.get(CustomerCSVFileHeaders.PANNo));
		customerDto.setSlNo(Integer.valueOf(csvRecord.get(CustomerCSVFileHeaders.SlNo)));
		return customerDto;
	}

	/**
	 * validates the list of customerDtos and returns a map of errors if there are
	 * no errors saves all the records into database writes the errors associated
	 * with the records into a log file valid records are persisted into database,
	 * invalid records are written to error log file
	 * 
	 * @param customerDtos
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean validateListOfCustomers(List<CustomerDto> customerDtos)
			throws InterruptedException, ExecutionException {
		logger.info("CustomerService:: validateListOfCustomers - {}", "validating uploaded customer data started");
		boolean isValid = false;
		Map<Integer, Map<String, String>> errorsMap = new HashMap<>();
		Set<CustomerDto> validCustomerDtos = new HashSet<>();
		customerDtos.parallelStream().forEach(customerDto -> {
			try {
				Map<String, String> errors = customerFieldsValidator.validateCustomerDto(customerDto).get();
				if (!errors.isEmpty()) {
					errorsMap.put(customerDto.getSlNo(), errors);
				} else {
					if (!validCustomerDtos.contains(customerDto)) {
						validCustomerDtos.add(customerDto);
					} else {
						logger.error(
								"CustomerService :: validateListOfCustomers,duplicate customer row in csv row - {}",
								customerDto.getSlNo());
						errors.put(CustomerCSVFileHeaders.Duplicate.name(), "duplicate customer record");
						errorsMap.put(customerDto.getSlNo(), errors);
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				logger.error(
						"CustomerService :: validateListOfCustomers, exception occurred while validating customer in the row of uploaded csv file - {}",
						customerDto.getSlNo() + " " + e.getMessage());
			}
		});
		if (errorsMap.isEmpty()) {
			isValid = true;
		} else {
			errorLogService.generateErrorLogExcelFile(errorsMap);
		}
		if (!validCustomerDtos.isEmpty()) {
			saveCustomerDtos(validCustomerDtos);
		}
		logger.info("CustomerService:: validateListOfCustomers - {}", "validating uploaded customer data completed");
		return isValid;
	}

	/**
	 * Saves the customer records to database
	 * 
	 * @param customerDtos
	 */
	@Async
	public void saveCustomerDtos(Set<CustomerDto> customerDtos) {
		logger.info("CustomerService:: saveCustomerDtos - {}", "persiting customers started");
		List<CustomerDto> parentCustomerDtos = customerDtos.stream().filter(
				customerDto -> ((customerDto.getParentCompany() != null && customerDto.getParentCompany().isBlank())
						|| customerDto.getParentCompany() == null))
				.collect(Collectors.toList());
		customerDtos.removeAll(parentCustomerDtos);
		List<Customer> parentCustomers = parentCustomerDtos.parallelStream()
				.map(customerDto -> convertcustomerDtoToObj(customerDto)).collect(Collectors.toList());
		customerRepository.saveAll(parentCustomers);
		List<Customer> childCustomers = customerDtos.parallelStream()
				.map(customerDto -> convertcustomerDtoToObj(customerDto)).collect(Collectors.toList());
		customerRepository.saveAll(childCustomers);
		logger.info("CustomerService:: saveCustomerDtos - {}", "persiting customers completed");
	}

	/**
	 * converts CustomerDto to Customer object
	 * 
	 * @param customerDto
	 * @return
	 */
	public Customer convertcustomerDtoToObj(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setParentcustomerfk(customerRepository.getParentCustomerId(customerDto.getParentCompany()));
		customer.setPanno(customerDto.getPanNo());
		customer.setSupplygstin(customerDto.getSupplyGstIn());
		customer.setCustomergstin(customerDto.getCustomerGstIn());
		customer.setCustomeralias(customerDto.getCustomerAlias());
		customer.setSupplystatefk(dbConfig.getStatesMap().get(customerDto.getSupplyState().toUpperCase()));
		customer.setCustomercode(customerDto.getCustomerCode());
		customer.setCustomertypefk(dbConfig.getCustomerTypes().get(customerDto.getCustomerType().toUpperCase()));
		customer.setCustomername(customerDto.getCustomerName());
		if (!customerDto.getDataImportFk().equals("0")) {
			customer.setDataimportfk(UUID.fromString(customerDto.getDataImportFk()));
		}
		customer.setBrandfk(brandRepository.fetchByBrandName(customerDto.getBrand()));
		customer.setIstaxexempt(customerDto.isTaxExempt());
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setPhoneno(Long.valueOf(customerDto.getPhoneNo()));
		customer.setMobileno(customerDto.getMobileNo());
		customer.setFaxnumber(customerDto.getFaxNumber());
		customer.setEmail(customerDto.getEmail());
		customer.setWebsite(customerDto.getWebsite());
		customer.setTanno(customerDto.getTanNo());
		customer.setGreetingfk(dbConfig.getGreetings().get(customerDto.getGreeting().toUpperCase()));
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setIstaxexempt(customerDto.isTaxExempt());
		customer.setGsttypefk(dbConfig.getGstnTypes().get(customerDto.getGstType().toUpperCase()));
		customer.setCreditstatusfk(dbConfig.getCreditStatuses().get(customerDto.getCreditStatus().toUpperCase()));
		customer.setRatingfk(dbConfig.getRatings().get(customerDto.getRating().toUpperCase()));
		return customer;
	}
}
