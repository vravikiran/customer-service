package com.app.service.customer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import com.app.service.customer.entities.Brand;
import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.enums.CustomerCSVFileHeaders;
import com.app.service.customer.enums.GSTNTypeEnum;
import com.app.service.customer.exceptions.EmptyDataException;
import com.app.service.customer.repositories.BrandRepository;
import com.app.service.customer.repositories.CustomerRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

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
	@Autowired
	private Validator validator;

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
		customerDto.setParentCompany(csvRecord.get(CustomerCSVFileHeaders.parentCompany.name().toUpperCase()));
		if (!csvRecord.get(CustomerCSVFileHeaders.dataimportfk.name().toUpperCase()).equals("0")) {
			customerDto.setDataimportfk(UUID.fromString(csvRecord.get(CustomerCSVFileHeaders.dataimportfk.name().toUpperCase())));
		}
		customerDto.setCustomerCode(csvRecord.get(CustomerCSVFileHeaders.customerCode.name().toUpperCase()));
		customerDto.setCustomerName(csvRecord.get(CustomerCSVFileHeaders.customerName.name().toUpperCase()));
		customerDto.setCustomerAlias(csvRecord.get(CustomerCSVFileHeaders.customerAlias.name().toUpperCase()));
		customerDto.setCustomerType(csvRecord.get(CustomerCSVFileHeaders.customerType.name().toUpperCase()));
		customerDto.setBrand(csvRecord.get(CustomerCSVFileHeaders.brand.name().toUpperCase()));
		customerDto.setSupplyState(csvRecord.get(CustomerCSVFileHeaders.supplyState.name().toUpperCase()));
		customerDto.setGstType(csvRecord.get(CustomerCSVFileHeaders.gstType.name().toUpperCase()));
		customerDto
				.setTaxexempt(Boolean.valueOf(csvRecord.get(CustomerCSVFileHeaders.istaxexempt.name().toUpperCase())));
		customerDto.setGreeting(csvRecord.get(CustomerCSVFileHeaders.greeting.name().toUpperCase()));
		customerDto.setCreditStatus(csvRecord.get(CustomerCSVFileHeaders.creditStatus.name().toUpperCase()));
		customerDto.setRating(csvRecord.get(CustomerCSVFileHeaders.rating.name().toUpperCase()));
		boolean isAllowed = Integer
				.valueOf(csvRecord.get(CustomerCSVFileHeaders.allowDuplicateGSTIN.name().toUpperCase())) == 0 ? false
						: true;
		customerDto.setAllowDuplicateGSTIN(isAllowed);
		customerDto.setCustomerGstIn(csvRecord.get(CustomerCSVFileHeaders.customerGstIn.name().toUpperCase()));
		customerDto.setSupplyGstIn(csvRecord.get(CustomerCSVFileHeaders.supplyGstIn.name().toUpperCase()));
		if (!csvRecord.get(CustomerCSVFileHeaders.phoneno.name().toUpperCase()).isBlank()
				|| !csvRecord.get(CustomerCSVFileHeaders.phoneno.name().toUpperCase()).isEmpty())
			customerDto.setPhoneno(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.phoneno.name().toUpperCase())));
		if (!csvRecord.get(CustomerCSVFileHeaders.mobileno.name().toUpperCase()).isEmpty())
			customerDto.setMobileno(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.mobileno.name().toUpperCase())));
		if (!csvRecord.get(CustomerCSVFileHeaders.faxnumber.name().toUpperCase()).isEmpty())
			customerDto
					.setFaxnumber(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.faxnumber.name().toUpperCase())));
		customerDto.setEmail(csvRecord.get(CustomerCSVFileHeaders.email.name().toUpperCase()));
		customerDto.setWebsite(csvRecord.get(CustomerCSVFileHeaders.website.name().toUpperCase()));
		customerDto.setTanno(csvRecord.get(CustomerCSVFileHeaders.tanno.name().toUpperCase()));
		customerDto.setPanno(csvRecord.get(CustomerCSVFileHeaders.panno.name().toUpperCase()));
		customerDto.setSlNo(Integer.valueOf(csvRecord.get(CustomerCSVFileHeaders.SlNo.name().toUpperCase())));
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
		customer.setPanno(customerDto.getPanno());
		customer.setSupplygstin(customerDto.getSupplyGstIn());
		customer.setCustomergstin(customerDto.getCustomerGstIn());
		customer.setCustomeralias(customerDto.getCustomerAlias());
		customer.setSupplystatefk(dbConfig.getStatesMap().get(customerDto.getSupplyState().toUpperCase()));
		customer.setCustomercode(customerDto.getCustomerCode());
		customer.setCustomertypefk(dbConfig.getCustomerTypes().get(customerDto.getCustomerType().toUpperCase()));
		customer.setCustomername(customerDto.getCustomerName());
		customer.setDataimportfk(customerDto.getDataimportfk());
		customer.setBrandfk(brandRepository.fetchByBrandName(customerDto.getBrand()));
		customer.setIstaxexempt(customerDto.isTaxexempt());
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setPhoneno(Long.valueOf(customerDto.getPhoneno()));
		customer.setMobileno(customerDto.getMobileno());
		customer.setFaxnumber(customerDto.getFaxnumber());
		customer.setEmail(customerDto.getEmail());
		customer.setWebsite(customerDto.getWebsite());
		customer.setTanno(customerDto.getTanno());
		customer.setGreetingfk(dbConfig.getGreetings().get(customerDto.getGreeting().toUpperCase()));
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setIstaxexempt(customerDto.isTaxexempt());
		customer.setGsttypefk(dbConfig.getGstnTypes().get(customerDto.getGstType().toUpperCase()));
		customer.setCreditstatusfk(dbConfig.getCreditStatuses().get(customerDto.getCreditStatus().toUpperCase()));
		customer.setRatingfk(dbConfig.getRatings().get(customerDto.getRating().toUpperCase()));
		customer.setIsactive(customerDto.isIsactive());
		return customer;
	}

	/**
	 * validates the provided customerDto and creates new Customer if there are no
	 * errors
	 * 
	 * @param customerDto
	 * @return Customer
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws Exception            if there are validation errors
	 */
	public CustomerDto createCustomer(CustomerDto customerDto)
			throws ValidationException, InterruptedException, ExecutionException {
		Customer customer = null;
		Customer updatedCustomer = null;
		Map<String, String> errors = customerFieldsValidator.validateCustomerDto(customerDto).get();
		if (errors.isEmpty()) {
			customer = convertcustomerDtoToObj(customerDto);
			updatedCustomer = customerRepository.save(customer);
		} else {
			StringBuffer errorMsg = new StringBuffer();
			errors.forEach((k,v)->{
				errorMsg.append(k+"-"+v);
				errorMsg.append(";");
			});
			throw new ValidationException(errorMsg.toString());
		}
		return convertCustomertoCustomerDto(updatedCustomer);
	}

	/**
	 * deactivates the customer by customerId if exists
	 * 
	 * @param customerId
	 * @throws Exception if customer doesn't exist
	 */
	public void deactivateCustomer(UUID customerId) throws NoSuchElementException {
		Customer customer = customerRepository.findById(customerId).get();
		if (customer != null) {
			customer.setIsactive(false);
			customerRepository.save(customer);
		}
	}

	/**
	 * Fetches customerInfo by customerId if exists
	 * 
	 * @param customerId
	 * @return
	 * @throws Exception if customer doesn't exists
	 */
	public CustomerDto getCustomerInfo(UUID customerId) throws NoSuchElementException {
		Customer customer = customerRepository.findById(customerId).get();
		return convertCustomertoCustomerDto(customer);
	}

	/**
	 * 
	 * @param customerId
	 * @param customerDto
	 * @return
	 * @throws EmptyDataException
	 * @throws Exception
	 */
	public CustomerDto updateCustomer(UUID customerId, Map<String, String> valuesToUpdate) throws EmptyDataException {
		Customer customer = customerRepository.findById(customerId).get();
		if (valuesToUpdate == null || (valuesToUpdate != null && valuesToUpdate.isEmpty())) {
			throw new EmptyDataException("No data to update");
		} else {
			StringBuffer errorMsg = new StringBuffer();
			if (valuesToUpdate.containsKey((CustomerCSVFileHeaders.customerType.name()))) {
				customer.setCustomertypefk(dbConfig.getCustomerTypes()
						.get(valuesToUpdate.get(CustomerCSVFileHeaders.customerType.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.customerType.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.creditStatus.name())) {
				customer.setCreditstatusfk(dbConfig.getCreditStatuses()
						.get(valuesToUpdate.get(CustomerCSVFileHeaders.creditStatus.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.creditStatus.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.supplyState.name())) {
				customer.setSupplystatefk(
						dbConfig.getStatesMap().get(valuesToUpdate.get(CustomerCSVFileHeaders.supplyState.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.supplyState.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.greeting.name())) {
				customer.setGreetingfk(
						dbConfig.getGreetings().get(valuesToUpdate.get(CustomerCSVFileHeaders.greeting.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.greeting.name());
			}

			String gstnType = getKeyByValue(dbConfig.getGstnTypes(), customer.getGsttypefk());
			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.gstType.name())) {
				gstnType = valuesToUpdate.get(CustomerCSVFileHeaders.gstType.name());
				if (dbConfig.getGstnTypes().get(gstnType) != null) {
					customer.setGsttypefk(dbConfig.getGstnTypes().get(gstnType));
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.gstType.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.rating.name())) {
				customer.setRatingfk(
						dbConfig.getRatings().get(valuesToUpdate.get(CustomerCSVFileHeaders.rating.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.rating.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.parentCompany.name())) {
				if (valuesToUpdate.get(CustomerCSVFileHeaders.parentCompany.name()) != null) {
					UUID parentCustomerId = customerRepository
							.getParentCustomerId(valuesToUpdate.get(CustomerCSVFileHeaders.parentCompany.name()));
					if (parentCustomerId != null) {
						customer.setParentcustomerfk(parentCustomerId);
					} else {
						customer.setParentcustomerfk(null);
					}
				} else {
					customer.setParentcustomerfk(null);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.parentCompany.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.brand.name())) {
				if (valuesToUpdate.get(CustomerCSVFileHeaders.brand.name()) != null) {
					UUID brandUUID = brandRepository
							.fetchByBrandName(valuesToUpdate.get(CustomerCSVFileHeaders.brand.name()));
					if (brandUUID != null) {
						customer.setBrandfk(brandUUID);
					} else {
						throw new NoSuchElementException("Given brand doesn't exist");
					}
				} else {
					customer.setBrandfk(null);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.brand.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.customerName.name())) {
				String name = valuesToUpdate.get(CustomerCSVFileHeaders.customerName.name());
				if (name != null && customerRepository.findCustomerName(name) != null) {
					errorMsg.append(CustomerCSVFileHeaders.customerName.name().toUpperCase() + "-"
							+ "Customer Name already exists");
				} else {
					customer.setCustomername(name);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.customerName.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.customerAlias.name())) {
				String name = valuesToUpdate.get(CustomerCSVFileHeaders.customerAlias.name());
				if (name != null && customerRepository.findCustomerAlias(name) != null) {
					errorMsg.append(CustomerCSVFileHeaders.customerAlias.name().toUpperCase() + "-"
							+ "Customer Alias already exists");
				} else {
					customer.setCustomeralias(name);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.customerAlias.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.customerCode.name())) {
				String name = valuesToUpdate.get(CustomerCSVFileHeaders.customerCode.name());
				if (name != null && customerRepository.findCustomerCode(name) != null) {
					errorMsg.append(CustomerCSVFileHeaders.customerCode.name().toUpperCase() + "-"
							+ "Customer Code already exists");
				} else {
					customer.setCustomercode(name);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.customerCode.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.allowDuplicateGSTIN.name())
					&& ((valuesToUpdate.get(CustomerCSVFileHeaders.allowDuplicateGSTIN.name()) != null))) {
				customer.setAllowduplicategstin(
						Boolean.valueOf(valuesToUpdate.get(CustomerCSVFileHeaders.allowDuplicateGSTIN.name())));
				valuesToUpdate.remove(CustomerCSVFileHeaders.allowDuplicateGSTIN.name());
			}
			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.customerGstIn.name())) {
				String customerGstIn = valuesToUpdate.get(CustomerCSVFileHeaders.customerGstIn.name());
				if (!customer.isAllowduplicategstin() && customerGstIn != null
						&& customerRepository.findCustomerGSTIN(customerGstIn).size() > 0) {
					errorMsg.append(CustomerCSVFileHeaders.customerGstIn.name().toUpperCase() + "-"
							+ "Customer GSTIN already exists ");
				} else {
					customer.setCustomergstin(customerGstIn);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.customerGstIn.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.supplyGstIn.name())) {
				String supplyGstIn = valuesToUpdate.get(CustomerCSVFileHeaders.supplyGstIn.name());
				if (!customer.isAllowduplicategstin() && supplyGstIn != null
						&& customerRepository.findSupplyGSTIN(supplyGstIn).size() > 0) {
					errorMsg.append(CustomerCSVFileHeaders.supplyGstIn.name().toUpperCase() + "-"
							+ "Supplier GSTIN already exists ");
				} else {
					customer.setSupplygstin(supplyGstIn);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.supplyGstIn.name());
			}

			if (valuesToUpdate.containsKey(CustomerCSVFileHeaders.panno.name())) {
				String panNo = valuesToUpdate.get(CustomerCSVFileHeaders.panno.name());
				if (!customer.isAllowduplicategstin() && panNo != null
						&& customerRepository.findPanNo(panNo).size() > 0) {
					errorMsg.append(
							CustomerCSVFileHeaders.panno.name().toUpperCase() + "-" + "PAN number already exists");
				} else {
					customer.setPanno(panNo);
				}
				valuesToUpdate.remove(CustomerCSVFileHeaders.panno.name());
			}

			if (gstnType != null && gstnType.equalsIgnoreCase(GSTNTypeEnum.Registered.name().toUpperCase())
					&& customer.getCustomergstin() == null) {
				errorMsg.append(CustomerCSVFileHeaders.customerGstIn.name().toUpperCase() + "-"
						+ "CustomerGSTIN is not available for " + GSTNTypeEnum.Registered.name() + " Customer "
						+ customer.getCustomername());
			}

			if (gstnType != null && gstnType.equalsIgnoreCase(GSTNTypeEnum.Unregistered.name())
					&& customer.getPanno() == null) {
				errorMsg.append(
						CustomerCSVFileHeaders.panno.name().toUpperCase() + "-" + "PAN number is not available for "
								+ GSTNTypeEnum.Unregistered.name() + customer.getCustomername());
			}

			if (!valuesToUpdate.isEmpty()) {
				customer.updateValues(customer, valuesToUpdate);
			}

			CustomerDto customerDto = convertCustomertoCustomerDto(customer);
			Set<ConstraintViolation<CustomerDto>> validationErrors = validator.validate(customerDto);
			if (!validationErrors.isEmpty()) {
				for (ConstraintViolation<CustomerDto> c : validationErrors) {
					errorMsg.append(c.getPropertyPath().toString() + "-" + c.getMessage());
					errorMsg.append(";");
				}
			}
			if (!errorMsg.isEmpty()) {
				throw new ValidationException(errorMsg.toString());
			} else {
				Customer updatedCustomer = customerRepository.save(customer);
				return convertCustomertoCustomerDto(updatedCustomer);
			}
		}
	}

	/**
	 * convert Customer object to CustomerDto
	 * 
	 * @param customer
	 * @return
	 */
	private CustomerDto convertCustomertoCustomerDto(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerpk(customer.getCustomerpk());
		customerDto.setCustomerAlias(customer.getCustomeralias());
		customerDto.setCustomerCode(customer.getCustomercode());
		customerDto.setCustomerName(customer.getCustomername());
		customerDto.setAllowDuplicateGSTIN(customer.isAllowduplicategstin());
		customerDto.setCustomerGstIn(customer.getCustomergstin());
		if (customer.getBrandfk() != null) {
			Optional<Brand> brand = brandRepository.findById(customer.getBrandfk());
			if (brand != null) {
				customerDto.setBrand(brand.get().getBrandname());
			}
		}
		customerDto.setEmail(customer.getEmail());
		customerDto.setIsactive(customer.isIsactive());
		customerDto.setMobileno(customer.getMobileno());
		customerDto.setPanno(customer.getPanno());
		if (customer.getParentcustomerfk() != null) {
			Optional<Customer> parentCompany = customerRepository.findById(customer.getParentcustomerfk());
			if (parentCompany != null) {
				customerDto.setParentCompany(parentCompany.get().getCustomername());
			}
		}
		customerDto.setPhoneno(customer.getPhoneno());
		customerDto.setSupplyGstIn(customer.getSupplygstin());
		customerDto.setTanno(customer.getTanno());
		customerDto.setTaxexempt(customer.isIstaxexempt());
		customerDto.setWebsite(customer.getWebsite());
		customerDto.setCreditStatus(getKeyByValue(dbConfig.getCreditStatuses(), customer.getCreditstatusfk()));
		customerDto.setSupplyState(getKeyByValue(dbConfig.getStatesMap(), customer.getSupplystatefk()));
		customerDto.setCustomerType(getKeyByValue(dbConfig.getCustomerTypes(), customer.getCustomertypefk()));
		customerDto.setGreeting(getKeyByValue(dbConfig.getGreetings(), customer.getGreetingfk()));
		customerDto.setGstType(getKeyByValue(dbConfig.getGstnTypes(), customer.getGsttypefk()));
		customerDto.setRating(getKeyByValue(dbConfig.getRatings(), customer.getRatingfk()));
		customerDto.setDataimportfk(customer.getDataimportfk());
		customerDto.setFaxnumber(customer.getFaxnumber());
		return customerDto;
	}

	/**
	 * fetch key by value for
	 * state,customertype,greeting,rating,gsttype,creditstatus
	 * 
	 * @param map
	 * @param value
	 * @return
	 */
	private String getKeyByValue(Map<String, UUID> map, UUID value) {
		for (Map.Entry<String, UUID> entry : map.entrySet()) {
			if (value != null && value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
