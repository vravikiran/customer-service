package com.app.service.customer.config;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.service.customer.entities.Creditstatus;
import com.app.service.customer.entities.Customertype;
import com.app.service.customer.entities.Greeting;
import com.app.service.customer.entities.Gstntype;
import com.app.service.customer.entities.Rating;
import com.app.service.customer.entities.State;
import com.app.service.customer.repositories.CreditStatusRepository;
import com.app.service.customer.repositories.CustomerTypeRepository;
import com.app.service.customer.repositories.GreetingRepository;
import com.app.service.customer.repositories.GstnTypeRepository;
import com.app.service.customer.repositories.RatingRepository;
import com.app.service.customer.repositories.StateRepository;

import jakarta.annotation.PostConstruct;
/**
 * fetches the states,customerTypes, gstnTypes,creditStatuses,ratings,greetings on bean initialization
 */
@Configuration
public class DBConfig {
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CustomerTypeRepository customerTypeRepository;
	@Autowired
	private GstnTypeRepository gstnTypeRepository;
	@Autowired
	private CreditStatusRepository creditStatusRepository;
	@Autowired
	private RatingRepository ratingRepository;
	@Autowired
	private GreetingRepository greetingRepository;
	private Map<String, UUID> gstnTypes;
	private Map<String, UUID> creditStatuses;
	private Map<String, UUID> ratings;
	private Map<String, UUID> statesMap;
	private Map<String, UUID> customerTypes;

	public Map<String, UUID> getGstnTypes() {
		return gstnTypes;
	}

	public Map<String, UUID> getCreditStatuses() {
		return creditStatuses;
	}

	public Map<String, UUID> getRatings() {
		return ratings;
	}

	public Map<String, UUID> getStatesMap() {
		return statesMap;
	}

	public Map<String, UUID> getCustomerTypes() {
		return customerTypes;
	}

	public Map<String, UUID> getGreetings() {
		return greetings;
	}

	private Map<String, UUID> greetings;
	
	/**
	 * On bean initialization fetches customerTypes,states,creditstatuses,gstnTypes,greetings,ratings and creates a map of names and Ids
	 */
	@PostConstruct
	public void init() {
		statesMap = stateRepository.findAll().stream()
				.collect(Collectors.toMap(State::getStatename, State::getStateid));
		customerTypes = customerTypeRepository.findAll().stream()
				.collect(Collectors.toMap(Customertype::getBizcontacttypedesc, Customertype::getBizcontacttypeid));
		creditStatuses = creditStatusRepository.findAll().stream()
				.collect(Collectors.toMap(Creditstatus::getCreditstatus, Creditstatus::getCreditstatusid));
		gstnTypes = gstnTypeRepository.findAll().stream()
				.collect(Collectors.toMap(Gstntype::getGsttypedesc, Gstntype::getGstntypeid));
		ratings = ratingRepository.findAll().stream().collect(Collectors.toMap(Rating::getRating, Rating::getRatingid));
		greetings = greetingRepository.findAll().stream()
				.collect(Collectors.toMap(Greeting::getGreetingcode, Greeting::getGreetingid));
	}
}
