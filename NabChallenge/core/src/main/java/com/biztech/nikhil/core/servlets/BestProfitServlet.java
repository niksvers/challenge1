/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.biztech.nikhil.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.FileReader;
import java.io.IOException;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
		"sling.servlet.paths=" + "/bin/bestProfit", "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class BestProfitServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		final Resource resource = req.getResource();
		resp.setContentType("text/plain");
		resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));

		JSONParser parser = new JSONParser();
		JSONArray currencyArray;
		try {
			currencyArray = (JSONArray) parser.parse(new FileReader("/Users/nikhilverma/git/challenge1/data/20180507.json"));

			for (Object currrency : currencyArray) {
				JSONObject currencyJSON = (JSONObject) currrency;

				String cName = (String) currencyJSON.get("currency");
				resp.getWriter().write(cName);
				resp.getWriter().write("\n\n");

				String cDate = (String) currencyJSON.get("date");
				resp.getWriter().write(cDate);
				resp.getWriter().write("\n\n");

				JSONArray quotes = (JSONArray) currencyJSON.get("quotes");

				for (Object quote : quotes) {
					JSONObject quoteJSON = (JSONObject) quote;
					resp.getWriter().write((String)quoteJSON.get("time"));
					resp.getWriter().write("\n\n");
					resp.getWriter().write((String)quoteJSON.get("price"));
					resp.getWriter().write("\n\n");
				} 
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
