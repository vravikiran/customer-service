package com.app.service.customer.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;

import com.app.service.customer.enums.CustomerCSVFileHeaders;

public abstract class PatchableObject {

	public PatchableObject updateValues(PatchableObject requestToUpdate, Map<String, String> valuesToUpdate) throws NoSuchElementException {
		valuesToUpdate.forEach((key, value) -> {
			try {
				BeanUtils.getProperty(requestToUpdate, key);
				if(key.equals(CustomerCSVFileHeaders.dataimportfk.name())) {
					BeanUtils.setProperty(requestToUpdate, key,UUID.fromString(value));
				} else {
				BeanUtils.setProperty(requestToUpdate, key, value);
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				throw new NoSuchElementException();
			}
		});
		return requestToUpdate;
	}

}
