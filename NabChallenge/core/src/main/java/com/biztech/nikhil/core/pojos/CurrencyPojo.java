package com.biztech.nikhil.core.pojos;

import com.adobe.cq.sightly.WCMUsePojo;

public class CurrencyPojo extends WCMUsePojo {
	
	private String currency;

	@Override
	public void activate() throws Exception {
		// TODO Auto-generated method stub
		
		currency = get("currency", String.class);
		
	}
	
	public String getCurrency() {
		
		return currency;
	}

}
