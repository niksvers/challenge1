package com.biztech.nikhil.core.pojos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adobe.cq.sightly.WCMUsePojo;
import com.biztech.nikhil.services.CurrencyInfoProvider;

public class CurrencyPojo extends WCMUsePojo {
	
	
	private String currency;
	private BigDecimal bestProfit;
	
	public Map<String, Object> currencyInfo = new HashMap<String, Object>();
	public Map<String, String> currencyQuoteBuy = new HashMap<String, String>();
	public Map<String, String> currencyQuoteSell = new HashMap<String, String>();
	

	@Override
	public void activate() throws Exception {
		
		currency = get("currency", String.class);
		CurrencyInfoProvider currencyInfoProvider = getSlingScriptHelper().getService(CurrencyInfoProvider.class);
		currencyInfo = currencyInfoProvider.getCurrencyInfo(currency);
		
		
		currencyInfo.replace("date", new SimpleDateFormat("d MMM yyyy").format( new SimpleDateFormat("yyyyMMdd").parse("" + currencyInfo.get("date")) ));
		
		calculateBestProfitQuote();
		
	}
	
	
	
	private void calculateBestProfitQuote() throws ParseException {
		
		List<HashMap<String, String>> quotesList = (List<HashMap<String, String>>) currencyInfo.get("quotes");
		
		setTimeFormat(quotesList);
		
		
		int buyIndex=0, sellIndex=0;
		
		for(int i=1; i<quotesList.size(); i++) {
			
			BigDecimal buyPrice = new BigDecimal(quotesList.get(buyIndex).get("price"));
			BigDecimal sellPrice = new BigDecimal(quotesList.get(sellIndex).get("price"));
			BigDecimal iPrice = new BigDecimal(quotesList.get(i).get("price"));
			
			if( sellPrice.compareTo(iPrice) < 0 ) {
				sellIndex = i;
			}
			
			//if( Float.compare(Float.parseFloat(quotesList.get(buyIndex).get("price")), Float.parseFloat(quotesList.get(i).get("price"))) > 0 && i<sellIndex) {
			if(buyPrice.compareTo(iPrice) > 0 && i<sellIndex) {
				buyIndex = i;
			}
			
		}
		
		
		if(buyIndex != sellIndex) {
			currencyQuoteBuy = ((List<HashMap<String, String>>)currencyInfo.get("quotes")).get(buyIndex);
			currencyQuoteSell = ((List<HashMap<String, String>>)currencyInfo.get("quotes")).get(sellIndex);
			
			bestProfit = new BigDecimal(quotesList.get(sellIndex).get("price")).subtract(new BigDecimal(quotesList.get(buyIndex).get("price")));
		}
		
	}

	private void setTimeFormat(List<HashMap<String, String>> quotesList) throws ParseException {
		
		for(Map<String, String> item : quotesList) {
			
			item.replace("time", new SimpleDateFormat("HH:mm a").format( new SimpleDateFormat("HHmm").parse("" + item.get("time")) ));
			
		}
		
	}



	public String getCurrency() {
		return currency;
	}
	
	public String getBestProfit() {
		return bestProfit.toString();
	}

}
