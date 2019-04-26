package com.biztech.nikhil.services.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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


@Component(service=CurrencyInfoProvider.class)
public class CurrencyInfoProviderImpl implements CurrencyInfoProvider{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyInfoProviderImpl.class);
	
	@Reference
	private SlingRepository repository;

	@Override
	public String getCurrencyInfo(String currency) {
		
		JSONParser parser = new JSONParser();
		JSONArray currencyArray;
		try {
			currencyArray = (JSONArray) parser.parse(new FileReader("/Users/nikhilverma/git/challenge1/data/20180507.json"));

			for (Object currrency : currencyArray) {
				JSONObject currencyJSON = (JSONObject) currrency;

				String cName = (String) currencyJSON.get("currency");
				
				if(currency.equals(cName)) {
					
					return currencyJSON.toJSONString();
					
				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currency;
	}

}
