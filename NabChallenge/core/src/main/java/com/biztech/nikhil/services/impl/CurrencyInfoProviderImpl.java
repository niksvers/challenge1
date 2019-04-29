package com.biztech.nikhil.services.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.jcr.api.SlingRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biztech.nikhil.services.CurrencyInfoProvider;
import com.fasterxml.jackson.databind.ObjectMapper;



@Designate(ocd = CurrencyInfoProviderImpl.Config.class)
@Component(service=CurrencyInfoProvider.class)
public class CurrencyInfoProviderImpl implements CurrencyInfoProvider{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyInfoProviderImpl.class);
	
	@Reference
	private SlingRepository repository;
	
	private String jsonPath;
	
	@ObjectClassDefinition(name = "NAB Challenge Configuration")
    public static @interface Config {

        @AttributeDefinition(name = "Data JSON path")
        String getjsonPath();

    }

	@Activate
    protected void activate(final CurrencyInfoProviderImpl.Config config) {
		jsonPath = config.getjsonPath();
    }
	
	@Override
	public Map<String, Object> getCurrencyInfo(String currency) {
		
		JSONParser parser = new JSONParser();
		JSONArray currencyArray;
		try {
			// reading JSON file
			currencyArray = (JSONArray) parser.parse(new FileReader(jsonPath));
			
			for (Object currrency : currencyArray) {
				JSONObject currencyJSON = (JSONObject) currrency;

				String cName = (String) currencyJSON.get("currency");
				
				// depending upon cq page the JSON data is returned
				if(currency.equals(cName)) {
					
					HashMap<String,Object> currencyMap = new ObjectMapper().readValue(currencyJSON.toJSONString(), HashMap.class);
					
					return currencyMap;
					
				}

			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
