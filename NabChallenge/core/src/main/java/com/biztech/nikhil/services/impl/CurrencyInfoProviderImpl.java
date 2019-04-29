package com.biztech.nikhil.services.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.jcr.api.SlingRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biztech.nikhil.services.CurrencyInfoProvider;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component(service=CurrencyInfoProvider.class)
public class CurrencyInfoProviderImpl implements CurrencyInfoProvider{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyInfoProviderImpl.class);
	
	@Reference
	private SlingRepository repository;

	@Override
	public Map<String, Object> getCurrencyInfo(String currency) {
		
		JSONParser parser = new JSONParser();
		JSONArray currencyArray;
		try {
			currencyArray = (JSONArray) parser.parse(new FileReader("/Users/nikhilverma/git/challenge1/data/20180507.json"));

			for (Object currrency : currencyArray) {
				JSONObject currencyJSON = (JSONObject) currrency;

				String cName = (String) currencyJSON.get("currency");
				
				if(currency.equals(cName)) {
					
					HashMap<String,Object> currencyMap = new ObjectMapper().readValue(currencyJSON.toJSONString(), HashMap.class);
					
					return currencyMap;
					
				}

			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getMessage());
		}
		
		return null;
	}

}
