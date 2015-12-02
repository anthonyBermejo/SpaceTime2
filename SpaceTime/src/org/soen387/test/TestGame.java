package org.soen387.test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestGame {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	public final String BASE_URL = FieldMap.current.get().get("BASE_URL");
	XPath xPath =  XPathFactory.newInstance().newXPath();
		
	@BeforeClass
	public static void before() {
		try {
			Teardown.main(null);
			Setup.main(null);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testLogIn() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = login("fred", "fred");
		
		//Should fail, since I haven't registered them yet
		assertFailure(d);
		
		register("fred", "fred", "fred", "fredson", "fred@fred.com");
		
		//I have now registered this user, so it should pass
		d = login("fred", "fred");
		assertSuccess(d);
	}
	
	@Test
	public void testLogOut() throws SAXException, IOException, XpathException, XPathExpressionException {
		login("fred", "fred");
		//A full test might then try to challenge a user that one has not challenged to confirmed that one is not allowed
		//to do so when not logged in.
		Document d = logout();
		assertSuccess(d);
	}
	
	@Test
	public void testRegister() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("bob", "bob", "bob", "bobson", "bob@fred.com");
		long id = getPlayerId(d);
		XMLAssert.assertXpathExists("/game/player[@firstName='bob']", d);
		XMLAssert.assertXpathExists("/game/player[@lastName='bobson']", d);
		XMLAssert.assertXpathExists("/game/player[@email='bob@fred.com']", d);
		XMLAssert.assertXpathExists("/game/player/user[@username='bob']", d);
		d = login("bob", "bob");
		assertSuccess(d);
		d = listPlayers();
		XMLAssert.assertXpathExists("/game/players/player[@id='" + id + "']", d);
	}
	
	@Test
	public void testListPlayers() throws SAXException, IOException, XpathException, XPathExpressionException {
		register("alice", "alice", "alice", "aliceson", "alice@fred.com");
		register("dora", "dora", "dora", "dorason", "dora@fred.com");
		Document d = listPlayers();
		XMLAssert.assertXpathExists("/game/players/player[@firstName='alice']", d);
		XMLAssert.assertXpathExists("/game/players/player[@firstName='dora']", d);
		assertSuccess(d);
	}

	
	@Test
	public void testRecruitPilot() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("hal", "hal", "hal", "halson", "hal@fred.com");
		login("hal", "hal");
		
		d = recruitPilot();
		long pilotId = getPilotId(d);

		d = listPilots();
		
		XMLAssert.assertXpathExists("/game/pilots/pilot[@id='" + pilotId + "'] ", d);

		assertSuccess(d);
	}
	
	@Test
	public void testFirePilot() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("igor", "igor", "igor", "igorson", "igor@fred.com");
		login("igor", "igor");
		
		d = recruitPilot();
		long pilotId = getPilotId(d);
		firePilot(pilotId);
		
		d = listPilots();
		
		XMLAssert.assertXpathNotExists("/game/pilots/pilot[@id='" + pilotId + "'] ", d);
		
		
		assertSuccess(d);
	}

	@Test
	public void testListPilots() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("elsa", "elsa", "elsa", "elsason", "elsa@fred.com");
		login("elsa", "elsa");

		d = recruitPilot();
		long pilotId1 = getPilotId(d);
		d = recruitPilot();
		long pilotId2 = getPilotId(d);
		d = recruitPilot();
		long pilotId3 = getPilotId(d);
		
		d = listPilots();
		
		XMLAssert.assertXpathExists("/game/pilots/pilot[@id='" + pilotId1 + "'] ", d);
		XMLAssert.assertXpathExists("/game/pilots/pilot[@id='" + pilotId2 + "'] ", d);
		XMLAssert.assertXpathExists("/game/pilots/pilot[@id='" + pilotId3 + "'] ", d);
		
		assertSuccess(d);
	}
	

	
	@Test
	public void testAddPilotToTeam() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("hal2", "hal2", "hal2", "halson2", "hal2@fred.com");
		login("hal2", "hal2");
		
		d = recruitPilot();
		long pilotId = getPilotId(d);

		d = addTeam("TeamA");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		d = addPilotToTeam(pilotId, teamId, teamVersion);
		
		assertSuccess(d);
		
		d = viewTeam(teamId);
		
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId + "'] ", d);

		assertSuccess(d);
	}
	
	@Test
	public void testRemovePilotFromTeam() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse", "jesse", "jesse", "jesseson", "jesse@fred.com");
		login("jesse", "jesse");
		

		d = recruitPilot();
		long pilotId = getPilotId(d);

		d = addTeam("TeamB");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		d = addPilotToTeam(pilotId, teamId, teamVersion);
		
		d = removePilotFromTeam(pilotId, teamId, teamVersion);
		
		assertSuccess(d);
		
		d = viewTeam(teamId);
		
		XMLAssert.assertXpathNotExists("/game/team/pilot[@id='" + pilotId + "'] ", d);
	}
	
	@Test
	public void testRemovePilotFromTeamLostUpdate() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse3", "jesse3", "jesse3", "jesseson3", "jesse3@fred.com");
		login("jesse3", "jesse3");
		

		d = recruitPilot();
		long pilotId = getPilotId(d);

		d = addTeam("TeamB3");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		d = addPilotToTeam(pilotId, teamId, teamVersion);
		System.out.println("Viewing Team!");
		d = viewTeam(teamId);
		teamVersion = getTeamVersion(d);
		
		System.out.println("Removing Pilot From Team!");
		d = removePilotFromTeam(pilotId, teamId, teamVersion);
		
		assertSuccess(d);
		
		d = viewTeam(teamId);
		
		XMLAssert.assertXpathNotExists("/game/team/pilot[@id='" + pilotId + "'] ", d);
	}
	
	@Test
	public void testViewTeam() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("nancy", "nancy", "nancy", "nancyson", "nancy@fred.com");
		login("nancy", "nancy");
		
		d = recruitPilot();
		long pilotId1 = getPilotId(d);
		d = recruitPilot();
		long pilotId2 = getPilotId(d);
		d = recruitPilot();
		long pilotId3 = getPilotId(d);
		
		d = addTeam("TeamC");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		addPilotToTeam(pilotId1, teamId, teamVersion);
		addPilotToTeam(pilotId2, teamId, teamVersion);
		addPilotToTeam(pilotId3, teamId, teamVersion);
		
		d = viewTeam(teamId);
		assertSuccess(d);
		
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId1 + "'] ", d);
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId2 + "'] ", d);
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId3 + "'] ", d);
		
		
	}
	
	@Test
	public void testViewTeamLostUpdates() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("nancy2", "nancy2", "nancy2", "nancyson2", "nancy2@fred.com");
		login("nancy2", "nancy2");
		
		d = recruitPilot();
		long pilotId1 = getPilotId(d);
		d = recruitPilot();
		long pilotId2 = getPilotId(d);
		d = recruitPilot();
		long pilotId3 = getPilotId(d);
		
		d = addTeam("TeamC2");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		addPilotToTeam(pilotId1, teamId, teamVersion);
		d = viewTeam(teamId);
		teamVersion = getTeamVersion(d);
		addPilotToTeam(pilotId2, teamId, teamVersion);
		d = viewTeam(teamId);
		teamVersion = getTeamVersion(d);
		addPilotToTeam(pilotId3, teamId, teamVersion);
		
		d = viewTeam(teamId);
		assertSuccess(d);
		
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId1 + "'] ", d);
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId2 + "'] ", d);
		XMLAssert.assertXpathExists("/game/team/pilot[@id='" + pilotId3 + "'] ", d);
		
		
	}
	
	public final String LOGIN = BASE_URL+FieldMap.current.get().get("LOGIN_PATH");
	public Document login(String user, String pass) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LOGIN);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("USERNAME_PARAM"), user));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PASSWORD_PARAM"), pass));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}

	public final String LOGOUT = BASE_URL+FieldMap.current.get().get("LOGOUT_PATH");
	public Document logout() throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LOGOUT);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String REGISTER = BASE_URL+FieldMap.current.get().get("REGISTER_PATH");
	public Document register(String user, String pass, String first, String last, String email) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(REGISTER);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("USERNAME_PARAM"), user));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PASSWORD_PARAM"), pass));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("FIRSTNAME_PARAM"), first));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("LASTNAME_PARAM"), last));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("EMAIL_PARAM"), email));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_PLAYERS = BASE_URL+FieldMap.current.get().get("LIST_PLAYERS_PATH");
	public Document listPlayers() throws ParseException, ClientProtocolException, IOException, SAXException {
		
		String query = LIST_PLAYERS+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String RECRUIT_PILOT = BASE_URL+FieldMap.current.get().get("RECRUIT_PILOT_PATH");
	public Document recruitPilot() throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(RECRUIT_PILOT);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String FIRE_PILOT = BASE_URL+FieldMap.current.get().get("FIRE_PILOT_PATH");
	public Document firePilot(long id) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(FIRE_PILOT);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PILOT_ID"), ""+id));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_PILOTS = BASE_URL+FieldMap.current.get().get("LIST_PILOTS_PATH");
	public Document listPilots() throws ParseException, ClientProtocolException, IOException, SAXException {
		
		String query = LIST_PILOTS+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String ADD_TEAM = BASE_URL+FieldMap.current.get().get("ADD_TEAM_PATH");
	public Document addTeam(String name) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(ADD_TEAM);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_NAME"), name));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String REMOVE_TEAM = BASE_URL+FieldMap.current.get().get("REMOVE_TEAM_PATH");
	public Document removeTeam(long id) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(REMOVE_TEAM);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_ID"), ""+id));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String VIEW_TEAM = BASE_URL+FieldMap.current.get().get("VIEW_TEAM_PATH");
	public Document viewTeam(long id) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		String query = VIEW_TEAM+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE")+"&"
		  + FieldMap.current.get().get("TEAM_ID") + "=" + id;
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String ADD_PILOT_TO_TEAM = BASE_URL+FieldMap.current.get().get("ADD_PILOT_TO_TEAM_PATH");
	public Document addPilotToTeam(long pilot, long team, int version) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(ADD_PILOT_TO_TEAM);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PILOT_ID"), ""+pilot));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_ID"), ""+team));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String REMOVE_PILOT_FROM_TEAM = BASE_URL+FieldMap.current.get().get("REMOVE_PILOT_FROM_TEAM_PATH");
	public Document removePilotFromTeam(long pilot, long team, int version) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(REMOVE_PILOT_FROM_TEAM);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PILOT_ID"), ""+pilot));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_ID"), ""+team));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	private String prettyPrintPost(HttpPost httpPost, List<NameValuePair> nvps,
			String response) {
		StringBuilder sb = new StringBuilder();
		sb.append("******Request"+"\n");
		sb.append(httpPost+"\n");
		for(NameValuePair nvp: nvps) {
			sb.append("\t" + nvp.toString()+"\n");
		}
		
		sb.append("******Response"+"\n");
		sb.append(response);
		return  FieldMap.current.get().get("DEBUG").equals("true")?sb.toString():"";
	}
	
	private String prettyPrintGet(String query, String response) {

		StringBuilder sb = new StringBuilder();
		sb.append("******Request"+"\n");
		sb.append("GET " + query+"\n");

		sb.append("******Response"+"\n");
		sb.append(response);
		
		return  FieldMap.current.get().get("DEBUG").equals("true")?sb.toString():"";
	}

	private long getPlayerId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/player/@id", d, XPathConstants.STRING));
	}
	
	private long getTeamId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/team/@id", d, XPathConstants.STRING));
	}
	
	private long getPilotId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/pilot/@id", d, XPathConstants.STRING));
	}
	
	private int getTeamVersion(Document d) throws NumberFormatException, XPathExpressionException {
		return Integer.parseInt((String) xPath.evaluate("/game/team/@version", d, XPathConstants.STRING));
	}
	
	public void assertSuccess(Document d) throws XpathException, XPathExpressionException {
		XMLAssert.assertXpathExists("/game/status[.=\"success\"]", d);
	}
	
	public void assertFailure(Document d) throws XpathException, XPathExpressionException {
		XMLAssert.assertXpathNotExists("/game/status[.=\"success\"]", d);
	}

	
}


