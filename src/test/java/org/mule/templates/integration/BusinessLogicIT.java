package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	protected static final String TEMPLATE_NAME = "contact-broadcast";
	protected static final int TIMEOUT_SEC = 120;
	private BatchTestHelper helper;
	
	protected SubflowInterceptingChainLifecycleWrapper createContactSapFlow;
	protected SubflowInterceptingChainLifecycleWrapper deleteFromSapFlow;
	protected SubflowInterceptingChainLifecycleWrapper retrieveContactFromSalesforceFlow;
	protected SubflowInterceptingChainLifecycleWrapper deleteFromSalesforceFlow;
	
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
		runFlow("mainFlow", buildIDocRequest());
		
		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();
		
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("FirstName", "Jozko 41");
		payload.put("LastName", "Pucik 41");
		MuleEvent event = retrieveContactFromSalesforceFlow.process(getTestEvent(payload, MessageExchangePattern.REQUEST_RESPONSE));
		sfContact = (Map<String, Object>) event.getMessage().getPayload();
		System.err.println(sfContact);
		
		Assert.assertNotNull(sfContact);
		Assert.assertNotNull(sfContact.get("Account"));
		Assert.assertTrue("Janko Hrasko".equals(((HashMap<String, Object>)sfContact.get("Account")).get("Name")));
		idsToDelete.add((String)sfContact.get("Id"));
	}
	
	private void deleteSalesforceTestData() throws MuleException, Exception{
		MuleEvent event = deleteFromSalesforceFlow.process(getTestEvent(idsToDelete, MessageExchangePattern.REQUEST_RESPONSE));
		System.err.println(event.getMessage().getPayload());
	}
	
	private String buildIDocRequest(){
		StringBuilder request = new StringBuilder();
		request.append("<?xml version=\"1.0\"?>");
		request.append("<DEBMAS01>");
		request.append("	<IDOC BEGIN=\"1\">");
		request.append("		<EDI_DC40 SEGMENT=\"1\">");
		request.append("			<TABNAM>EDI_DC40</TABNAM>");
		request.append("			<MANDT>800</MANDT>");
		request.append("			<DOCNUM>0000000001086095</DOCNUM>");
		request.append("			<DOCREL>731</DOCREL>");
		request.append("			<STATUS>30</STATUS>");
		request.append("			<DIRECT>1</DIRECT>");
		request.append("			<OUTMOD>2</OUTMOD>");
		request.append("			<IDOCTYP>DEBMAS01</IDOCTYP>");
		request.append("			<MESTYP>DEBMAS</MESTYP>");
		request.append("			<SNDPOR>SAPEH6</SNDPOR>");
		request.append("			<SNDPRT>LS</SNDPRT>");
		request.append("			<SNDPRN>EH6CLNT800</SNDPRN>");
		request.append("			<RCVPOR>MULESOFT</RCVPOR>");
		request.append("			<RCVPRT>LS</RCVPRT>");
		request.append("			<RCVPRN>MULESOFT</RCVPRN>");
		request.append("			<CREDAT>20140716</CREDAT>");
		request.append("			<CRETIM>125035</CRETIM>");
		request.append("			<SERIAL>20140716125035</SERIAL>");
		request.append("		</EDI_DC40>");
		request.append("		<E1KNA1M SEGMENT=\"1\">");
		request.append("			<MSGFN>005</MSGFN>");
		request.append("			<KUNNR>0000099009</KUNNR>");
		request.append("			<ANRED>Mr.</ANRED>");
		request.append("			<BBBNR>0000000</BBBNR>");
		request.append("			<BBSNR>00000</BBSNR>");
		request.append("			<BUBKZ>0</BUBKZ>");
		request.append("			<KTOKD>ZAG2</KTOKD>");
		request.append("			<LAND1>SK</LAND1>");
		request.append("			<NAME1>Janko Hrasko</NAME1>");
		request.append("			<NAME2>Hrasko 2</NAME2>");
		request.append("			<SPRAS>E</SPRAS>");
		request.append("			<UMJAH>0000</UMJAH>");
		request.append("			<JMZAH>000000</JMZAH>");
		request.append("			<JMJAH>0000</JMJAH>");
		request.append("			<UMSA1>0</UMSA1>");
		request.append("			<HZUOR>00</HZUOR>");
		request.append("			<CIVVE>X</CIVVE>");
		request.append("			<SPRAS_ISO>EN</SPRAS_ISO>");
		request.append("			<E1KNVKM SEGMENT=\"1\">");
		request.append("				<MSGFN>005</MSGFN>");
		request.append("				<PARNR>0000152955</PARNR>");
		request.append("				<NAMEV>Jozko 41</NAMEV>");
		request.append("				<NAME1>Pucik 41</NAME1>");
		request.append("				<ABTNR>0002</ABTNR>");
		request.append("				<UEPAR>0000000000</UEPAR>");
		request.append("				<PAFKT>11</PAFKT>");
		request.append("				<GBDAT>00000000</GBDAT>");
		request.append("				<VRTNR>0000000000</VRTNR>");
		request.append("				<MOAB1>000000</MOAB1>");
		request.append("				<MOBI1>000000</MOBI1>");
		request.append("				<MOAB2>000000</MOAB2>");
		request.append("				<MOBI2>000000</MOBI2>");
		request.append("				<DIAB1>000000</DIAB1>");
		request.append("				<DIBI1>000000</DIBI1>");
		request.append("				<DIAB2>000000</DIAB2>");
		request.append("				<DIBI2>000000</DIBI2>");
		request.append("				<MIAB1>000000</MIAB1>");
		request.append("				<MIBI1>000000</MIBI1>");
		request.append("				<MIAB2>000000</MIAB2>");
		request.append("				<MIBI2>000000</MIBI2>");
		request.append("				<DOAB1>000000</DOAB1>");
		request.append("				<DOBI1>000000</DOBI1>");
		request.append("				<DOAB2>000000</DOAB2>");
		request.append("				<DOBI2>000000</DOBI2>");
		request.append("				<FRAB1>000000</FRAB1>");
		request.append("				<FRBI1>000000</FRBI1>");
		request.append("				<FRAB2>000000</FRAB2>");
		request.append("				<FRBI2>000000</FRBI2>");
		request.append("				<SAAB1>000000</SAAB1>");
		request.append("				<SABI1>000000</SABI1>");
		request.append("				<SAAB2>000000</SAAB2>");
		request.append("				<SABI2>000000</SABI2>");
		request.append("				<SOAB1>000000</SOAB1>");
		request.append("				<SOBI1>000000</SOBI1>");
		request.append("				<SOAB2>000000</SOAB2>");
		request.append("				<SOBI2>000000</SOBI2>");
		request.append("			</E1KNVKM>");
		request.append("		</E1KNA1M>");
		request.append("	</IDOC>");
		request.append("</DEBMAS01>");
		return request.toString();
	}

}
