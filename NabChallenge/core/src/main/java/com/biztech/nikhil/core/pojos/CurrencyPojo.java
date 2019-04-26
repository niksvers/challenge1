package com.biztech.nikhil.core.pojos;

import java.util.HashMap;
import java.util.Map;

import com.adobe.cq.sightly.WCMUsePojo;
import com.biztech.nikhil.services.CurrencyInfoProvider;

public class CurrencyPojo extends WCMUsePojo {
	
	
	private String currency;
	public Map<String, Object> currencyInfo = new HashMap<String, Object>();

	@Override
	public void activate() throws Exception {
		
		currency = get("currency", String.class);
		CurrencyInfoProvider currencyInfoProvider = getSlingScriptHelper().getService(CurrencyInfoProvider.class);
		currencyInfo = currencyInfoProvider.getCurrencyInfo(currency);
		
	}
	
	public String getCurrency() {
		return currency;
	}

}
