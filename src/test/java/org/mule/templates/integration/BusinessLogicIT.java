/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Tempalte that make calls to external systems.
 * 
 */
@SuppressWarnings("unchecked")
public class BusinessLogicIT extends AbstractTemplateTestCase {
	protected static final String TEMPLATE_NAME = "contact-migration";
	protected static final int TIMEOUT_SEC = 120;
	private static final Logger LOG = LogManager.getLogger(BusinessLogicIT.class);
	private BatchTestHelper helper;
	
	
	protected SubflowInterceptingChainLifecycleWrapper createContactSapFlow;
	protected SubflowInterceptingChainLifecycleWrapper deleteFromSapFlow;
	protected SubflowInterceptingChainLifecycleWrapper retrieveContactFromSalesforceFlow;
	protected SubflowInterceptingChainLifecycleWrapper deleteFromSalesforceFlow;
	
	private String existingContactFirstName = "Michael";
	private String existingContactLastName = "Schulz";
	Map<String, Object> sfContact;
	List<String> idsToDelete = new ArrayList<String>();
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("account.sync.policy", "syncAccount");
		
		helper = new BatchTestHelper(muleContext);
		
		createContactSapFlow = getSubFlow("createContactSapFlow");
		createContactSapFlow.initialise();
		
		deleteFromSapFlow = getSubFlow("deleteFromSapFlow");
		deleteFromSapFlow.initialise();
		
		retrieveContactFromSalesforceFlow = getSubFlow("retrieveContactFromSalesforceFlow");
		retrieveContactFromSalesforceFlow.initialise();
		
		deleteFromSalesforceFlow = getSubFlow("deleteFromSalesforceFlow");
		deleteFromSalesforceFlow.initialise();
	}

	@After
	public void tearDown() throws MuleException, Exception {
//		deleteSapTestData();
		deleteSalesforceTestData();
	}

	@Test
	public void testMainFlow() throws Exception {
		runFlow("triggerFlow");
		
		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();
		
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("FirstName", existingContactFirstName);
		payload.put("LastName", existingContactLastName);
		MuleEvent event = retrieveContactFromSalesforceFlow.process(getTestEvent(payload, MessageExchangePattern.REQUEST_RESPONSE));
		sfContact = (Map<String, Object>) event.getMessage().getPayload();
		LOG.info(sfContact);
		
		Assert.assertNotNull(sfContact);
		Assert.assertNotNull(sfContact.get("Account"));
		Assert.assertTrue("Becker Berlin".equals(((HashMap<String, Object>)sfContact.get("Account")).get("Name")));
		idsToDelete.add((String)sfContact.get("Id"));
	}
	
	private void deleteSalesforceTestData() throws MuleException, Exception{
		MuleEvent event = deleteFromSalesforceFlow.process(getTestEvent(idsToDelete, MessageExchangePattern.REQUEST_RESPONSE));
		LOG.info(event.getMessage().getPayload());
	}
	

}
