package org.soen387.test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.antlr.stringtemplate.StringTemplate;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestGame {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	public final String BASE_URL = FieldMap.current.get().get("BASE_URL");
	XPath xPath =  XPathFactory.newInstance().newXPath();
		
	@BeforeClass
	public static void beforeClass() {
		try {
			Teardown.main(null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void before() {
		try {
			Setup.main(null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void after() {
		try {
			Teardown.main(null);
		} catch (InterruptedException e) {
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
		register("fred", "fred", "fred", "fredson", "fred@fred.com");
		Document d = login("fred", "fred");
		assertSuccess(d);
		//A full test might then try to challenge a user that one has not challenged to confirmed that one is not allowed
		//to do so when not logged in.
		d = logout();
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
		d = listPlayers(1, 10);
		XMLAssert.assertXpathExists("/game/players/player[@id='" + id + "']", d);
	}
	
	@Test
	public void testListPlayers() throws SAXException, IOException, XpathException, XPathExpressionException {
		register("alice", "alice", "alice", "aliceson", "alice@fred.com");
		register("dora", "dora", "dora", "dorason", "dora@fred.com");
		Document d = listPlayers(1, 10);
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

		d = listPilots(1,10);
		
		XMLAssert.assertXpathExists("/game/pilots/pilot[@id='" + pilotId + "'] ", d);

		assertSuccess(d);
	}
	
	@Test
	public void testFirePilot() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("igor", "igor", "igor", "igorson", "igor@fred.com");
		login("igor", "igor");
		
		d = recruitPilot();
		long pilotId = getPilotId(d);
		
		d = firePilot(pilotId);
		assertSuccess(d);
		
		d = listPilots(1,10);
		
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
		
		d = listPilots(1,10);
		
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
		Document d = register("jesse3", "jesse3", "jesse3", "jesseson3", "jesse3@fred.com");
		login("jesse3", "jesse3");
		

		d = recruitPilot();
		long pilotId = getPilotId(d);

		d = addTeam("TeamB3");
		long teamId = getTeamId(d);
		int teamVersion = getTeamVersion(d);
		
		d = addPilotToTeam(pilotId, teamId, teamVersion);
		d = viewTeam(teamId);
		teamVersion = getTeamVersion(d);
		
		d = removePilotFromTeam(pilotId, teamId, teamVersion);
		
		assertSuccess(d);
		
		d = viewTeam(teamId);
		
		XMLAssert.assertXpathNotExists("/game/team/pilot[@id='" + pilotId + "'] ", d);
	}
	
	@Test
	public void testChallengePlayerSuccess() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);

		assertSuccess(d);
		
		long challenge = getChallengeId(d);
		
		d = viewChallenges(1, 10);
		
		assertSuccess(d);
		
		XMLAssert.assertXpathExists("/game/challenges/challenge[@id='" + challenge + "']/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']", d);
	}
	
	@Test
	public void testReChallengePlayerSuccess() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);

		assertSuccess(d);
		
		long challenge = getChallengeId(d);
		
		logout();
		login("marty", "marty");
		d = viewChallenges(1, 10);
		
		challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "_PARAM"));
		
		assertSuccess(d);
		logout();
		login("biff", "biff");

		d = challengePlayer(marty);

		assertSuccess(d);

	}
	
	@Test
	public void testChallengePlayerFail() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		login("biff", "biff");
				
		d = challengePlayer(marty);

		assertSuccess(d);
		
		//Can't challenge the same person twice
		d = challengePlayer(marty);
		assertFailure(d);
		
		logout();
		
		//Can't challenge when already challenged by the person you are challenging
		login("marty", "marty");
		d = challengePlayer(biff);
		assertFailure(d);
		
		//Can't challenge self
		d = challengePlayer(marty);
		assertFailure(d);
		
	}
	
	@Test
	public void testRespondToChallenge() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);
		assertSuccess(d);
		d = challengePlayer(doc);
		assertSuccess(d);
		
		
		
		logout();
		login("marty", "marty");
		d = viewChallenges(1, 10);

		long challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "_PARAM"));
		
		assertSuccess(d);
		logout();
		login("doc", "doc");
		d = viewChallenges(1, 10);
		
		challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		
		assertSuccess(d);
		
		login("biff", "biff");

		d = viewChallenges(1, 10);
		
		XMLAssert.assertXpathExists("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']", d);
		XMLAssert.assertXpathExists("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']", d);

		assertSuccess(d);
		
	}
	
	@Test
	public void testRespondToChallengeFailure() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);
		assertSuccess(d);
		d = challengePlayer(doc);
		assertSuccess(d);
		
		
		
		logout();
		login("marty", "marty");
		d = viewChallenges(1, 10);

		long challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		logout();
		login("doc", "doc");
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		//We shouldn't be able to respond to someone else's challenge
		assertFailure(d);
		
	
	}
	
	@Test
	public void testViewChallenges() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		d = register("george", "george", "george", "george", "george@fred.com");
		long george = getPlayerId(d);
		d = register("linda", "linda", "linda", "linda", "linda@fred.com");
		long linda = getPlayerId(d);
		d = register("dave", "dave", "dave", "dave", "dave@fred.com");
		long dave = getPlayerId(d);
		
		login("biff", "biff");

		d = challengePlayer(marty);
		d = challengePlayer(doc);
		d = challengePlayer(george);
		d = challengePlayer(linda);
		d = challengePlayer(dave);
		
		d = viewChallenges(1, 10);
		
		assertSuccess(d);
		
		XMLAssert.assertXpathExists("/game/challenges/challenge/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']", d);
		XMLAssert.assertXpathExists("/game/challenges/challenge/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']", d);
		XMLAssert.assertXpathExists("/game/challenges/challenge/challenger[@refid='" + biff + "']/../challengee[@refid='" + george + "']", d);
		XMLAssert.assertXpathExists("/game/challenges/challenge/challenger[@refid='" + biff + "']/../challengee[@refid='" + linda + "']", d);
		XMLAssert.assertXpathExists("/game/challenges/challenge/challenger[@refid='" + biff + "']/../challengee[@refid='" + dave + "']", d);
		
		Assert.assertEquals("5", xPath.evaluate("count(//challenge)", d, XPathConstants.STRING));
		
	}
	
	@Test
	public void testViewChallengesPaging() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		d = register("george", "george", "george", "george", "george@fred.com");
		long george = getPlayerId(d);
		d = register("linda", "linda", "linda", "linda", "linda@fred.com");
		long linda = getPlayerId(d);
		d = register("dave", "dave", "dave", "dave", "dave@fred.com");
		long dave = getPlayerId(d);
		
		login("biff", "biff");

		d = challengePlayer(marty);
		d = challengePlayer(doc);
		d = challengePlayer(george);
		d = challengePlayer(linda);
		d = challengePlayer(dave);
		
		d = viewChallenges(2, 3);
		
		assertSuccess(d);
		
		Assert.assertEquals("2", xPath.evaluate("count(//challenge)", d, XPathConstants.STRING));
		XMLAssert.assertXpathExists("/game/challenges[@page='" + 2 + "' and @max='" + 5 + "']", d);
		
		d = viewChallenges(1, 3);
		Assert.assertEquals("3", xPath.evaluate("count(//challenge)", d, XPathConstants.STRING));
		XMLAssert.assertXpathExists("/game/challenges[@page='" + 1 + "' and @max='" + 5 + "']", d);
		
	}
	
	@Test
	public void testDeletePlayer() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		long biff_version = getPlayerVersion(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		d = register("george", "george", "george", "george", "george@fred.com");
		d = register("linda", "linda", "linda", "linda", "linda@fred.com");
		d = register("dave", "dave", "dave", "dave", "dave@fred.com");
		
		d = listPlayers(1, 10);
		Assert.assertEquals("6", xPath.evaluate("count(//player)", d, XPathConstants.STRING));
		
		login("biff", "biff");

		d = deletePlayer(biff_version);
		assertSuccess(d);
		
		d = listPlayers(1, 10);
		Assert.assertEquals("6", xPath.evaluate("count(//player)", d, XPathConstants.STRING));
		XMLAssert.assertXpathExists("/game/players/player[@firstName='del_" + biff + "']", d);
		XMLAssert.assertXpathNotExists("/game/players/player[@firstName='biff']", d);
	}
	
	@Test
	public void testViewNotifications() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);

		logout();
		
		login("marty", "marty");
		d = viewNotifications(1, 10);
		
		long challenge = Long.parseLong((String)xPath.evaluate("/game/notifications/challengeNotification/challenge/@refid", d, XPathConstants.STRING));
		
		d = viewChallenges(1, 10);
		
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge[@id='" + challenge + "']/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "_PARAM"));
		logout();
		login("biff", "biff");
		d = viewNotifications(1, 10);
		
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		XMLAssert.assertXpathExists("/game/notifications/challengeNotification/challenge[@refid='" + challenge + "' "
				+ "and @status='" + FieldMap.current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "']", d);
		
	}
	
	@Test
	public void testViewSeenNotifications() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);

		logout();
		
		login("marty", "marty");
		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
		long notification = Long.parseLong((String)xPath.evaluate("/game/notifications/challengeNotification/@id", d, XPathConstants.STRING));
		d = seeNotification(notification);
		assertSuccess(d);

		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
	}
	
	@Test
	public void testSeeNotification() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);

		logout();
		
		login("marty", "marty");
		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
		long notification = Long.parseLong((String)xPath.evaluate("/game/notifications/challengeNotification/@id", d, XPathConstants.STRING));
		d = seeNotification(notification);
		assertSuccess(d);

		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
	}
	
	@Test
	public void testDeleteNotification() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);

		logout();
		
		login("marty", "marty");
		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));

		d = viewNotifications(1, 10);
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
		long notification = Long.parseLong((String)xPath.evaluate("/game/notifications/challengeNotification/@id", d, XPathConstants.STRING));
		d = deleteNotification(notification);
		assertSuccess(d);

		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
		d = viewNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
	}

	@Test
	public void testWithdawChallenge() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);
	
		d = withdrawChallenge(getChallengeId(d), getChallengeVersion(d));
		assertSuccess(d);
		logout();
		
		login("marty", "marty");
		d = viewChallenges(1, 10);
		
		Assert.assertEquals("0", xPath.evaluate("count(//challenge)", d, XPathConstants.STRING));

		
	}
	
	@Test
	public void testWithdawChallengeNukesUnseenNotifications() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");

		login("biff", "biff");

		d = challengePlayer(marty);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		logout();
		login("marty", "marty");
		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("1", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
		logout();
		login("biff", "biff");
		
		d = withdrawChallenge(challengeId, challengeVersion);
		assertSuccess(d);
		
		logout();
		login("marty", "marty");
		d = viewNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		d = viewUnseenNotifications(1, 10);
		Assert.assertEquals("0", xPath.evaluate("count(//challengeNotification)", d, XPathConstants.STRING));
		
	}	
	
	@Test
	public void testViewTeam() throws SAXException, IOException, XpathException, XPathExpressionException {
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
	
	@Test
	public void testListMatches() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);
		assertSuccess(d);
		d = challengePlayer(doc);
		assertSuccess(d);
		
		
		
		logout();
		
		d = listMatches(1, 10);
		assertSuccess(d);
		Assert.assertEquals("0", xPath.evaluate("count(//match)", d, XPathConstants.STRING));
		
		login("marty", "marty");
		d = viewChallenges(1, 10);

		long challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		
		assertSuccess(d);
		logout();
		login("doc", "doc");
		d = viewChallenges(1, 10);
		
		challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		
		assertSuccess(d);
		
		logout();
		
		d = listMatches(1, 10);
		assertSuccess(d);
		
		Assert.assertEquals("2", xPath.evaluate("count(//match)", d, XPathConstants.STRING));
		
		XMLAssert.assertXpathExists("/game/matches/match"
				+ "[@status='" + FieldMap.current.get().get("MATCH_STATUS_ONGOING_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']", d);
		XMLAssert.assertXpathExists("/game/matches/match"
				+ "[@status='" + FieldMap.current.get().get("MATCH_STATUS_ONGOING_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']", d);

		assertSuccess(d);
		
	}
	
	@Test
	public void testListPlayerMatches() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("marty", "marty", "marty", "marty", "marty@fred.com");
		long marty = getPlayerId(d);
		d = register("biff", "biff", "biff", "biff", "biff@fred.com");
		long biff = getPlayerId(d);
		d = register("doc", "doc", "doc", "doc", "doc@fred.com");
		long doc = getPlayerId(d);
		login("biff", "biff");

		d = challengePlayer(marty);
		assertSuccess(d);
		d = challengePlayer(doc);
		assertSuccess(d);

		logout();
		
		d = listPlayerMatches(marty, 1, 10);
		assertSuccess(d);
		Assert.assertEquals("0", xPath.evaluate("count(//match)", d, XPathConstants.STRING));
		
		login("marty", "marty");
		d = viewChallenges(1, 10);

		long challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		long version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		
		assertSuccess(d);
		logout();
		login("doc", "doc");
		d = viewChallenges(1, 10);
		
		challenge = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@id", d, XPathConstants.STRING));
		version = Long.parseLong((String)xPath.evaluate("/game/challenges/challenge"
				+ "[@status='" + FieldMap.current.get().get("CHALLENGE_STATUS_ISSUED_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']/.."
				+ "/@version", d, XPathConstants.STRING));
		
		d = respondToChallenge(challenge, version, 
				FieldMap.current.get().get("CHALLENGE_STATUS_" + FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM"));
		
		assertSuccess(d);
		
		logout();
		
		d = listPlayerMatches(marty, 1, 10);
		assertSuccess(d);
		
		Assert.assertEquals("1", xPath.evaluate("count(//match)", d, XPathConstants.STRING));
		
		XMLAssert.assertXpathExists("/game/matches/match"
				+ "[@status='" + FieldMap.current.get().get("MATCH_STATUS_ONGOING_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + marty + "']", d);
		XMLAssert.assertXpathNotExists("/game/matches/match"
				+ "[@status='" + FieldMap.current.get().get("MATCH_STATUS_ONGOING_ID") + "']"
				+ "/challenger[@refid='" + biff + "']/../challengee[@refid='" + doc + "']", d);

		assertSuccess(d);
		
	}
	
	public final String LOGIN = BASE_URL+FieldMap.current.get().get("LOGIN_PATH");
	public Document login(String user, String pass) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LOGIN);

		HttpPost httpPost = new HttpPost(template.toString());
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(LOGOUT);
		
		HttpPost httpPost = new HttpPost(template.toString());
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
	
	public final String ADD_PILOT_TO_TEAM = BASE_URL+FieldMap.current.get().get("ADD_PILOT_TO_TEAM_PATH");
	public Document addPilotToTeam(long pilot, long team, int version) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(ADD_PILOT_TO_TEAM);
		template.setAttribute("id", team+"");

		HttpPost httpPost = new HttpPost(template.toString());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PILOT_ID"), ""+pilot));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_VERSION"), ""+version));
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(REGISTER);

		
		HttpPost httpPost = new HttpPost(template.toString());
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
	public Document listPlayers(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_PLAYERS);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(RECRUIT_PILOT);
		
		HttpPost httpPost = new HttpPost(template.toString());
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(FIRE_PILOT);
		template.setAttribute("id", id+"");

		HttpPost httpPost = new HttpPost(template.toString());
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
	
	public final String LIST_PILOTS = BASE_URL+FieldMap.current.get().get("LIST_PILOTS_PATH");
	public Document listPilots(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_PILOTS);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(ADD_TEAM);

		HttpPost httpPost = new HttpPost(template.toString());	
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
		StringTemplate template = new StringTemplate();
		template.setTemplate(REMOVE_TEAM);
		template.setAttribute("id", id+"");

		HttpPost httpPost = new HttpPost(template.toString());
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
	
	public final String VIEW_TEAM = BASE_URL+FieldMap.current.get().get("VIEW_TEAM_PATH");
	public Document viewTeam(long id) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(VIEW_TEAM);
		template.setAttribute("id", id+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	

	public final String REMOVE_PILOT_FROM_TEAM = BASE_URL+FieldMap.current.get().get("REMOVE_PILOT_FROM_TEAM_PATH");
	public Document removePilotFromTeam(long pilot, long team, int version) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(REMOVE_PILOT_FROM_TEAM);
		template.setAttribute("id", team+"");

		HttpPost httpPost = new HttpPost(template.toString());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PILOT_ID"), ""+pilot));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TEAM_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String CHALLENGE_PLAYER = BASE_URL+FieldMap.current.get().get("CHALLENGE_PLAYER_PATH");
	public Document challengePlayer(long player) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(CHALLENGE_PLAYER);
		template.setAttribute("id", player+"");

		HttpPost httpPost = new HttpPost(template.toString());
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
	
	/*
	 * 
	 * Accepts either an action padsed approach (url says accept or refuse somehow), or the generic param-based approach
	 * If ACCEPTED_CHALLENGE_SUFFIX is set in FieldMap, it'll throw an error if the status is invalid and will assume
	 * REFUSED_CHALLENGE_SUFFIX exists as well.
	 * 
	 */
	public final String RESPOND_TO_CHALLENGE = BASE_URL+FieldMap.current.get().get("RESPOND_TO_CHALLENGE_PATH");
	public Document respondToChallenge(long challenge, long version, String status) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(RESPOND_TO_CHALLENGE);
		template.setAttribute("id", challenge+"");

		String url = template.toString();
		boolean actionBased = (FieldMap.current.get().get("ACCEPTED_CHALLENGE_SUFFIX") != null) && !FieldMap.current.get().get("ACCEPTED_CHALLENGE_SUFFIX").equals("");
		if(actionBased) {
			if(status.equals(FieldMap.current.get().get("CHALLENGE_STATUS_ACCEPT_ID"))) {
				url = url + FieldMap.current.get().get("ACCEPTED_CHALLENGE_SUFFIX");
			} else if(status.equals(FieldMap.current.get().get("CHALLENGE_STATUS_REFUSED_ID"))) {
				url = url + FieldMap.current.get().get("REFUSED_CHALLENGE_SUFFIX");
			} else {
				throw new ParseException("Action-based challenge not recognized with status: " + status);
			}
		}
		
		HttpPost httpPost = new HttpPost(url);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		if(!actionBased) {
			nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_STATUS"), status));
		}
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String VIEW_CHALLENGES = BASE_URL+FieldMap.current.get().get("VIEW_CHALLENGES_PATH");
	public Document viewChallenges(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(VIEW_CHALLENGES);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String WITHDRAW_CHALLENGE = BASE_URL+FieldMap.current.get().get("WITHDRAW_CHALLENGE_PATH");
	public Document withdrawChallenge(long challenge, int version) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(WITHDRAW_CHALLENGE);
		template.setAttribute("id", challenge+"");

		HttpPost httpPost = new HttpPost(template.toString());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_NOTIFICATIONS = BASE_URL+FieldMap.current.get().get("LIST_NOTIFICATIONS_PATH");
	public Document viewNotifications(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_NOTIFICATIONS);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_UNSEEN_NOTIFICATIONS = BASE_URL+FieldMap.current.get().get("LIST_UNSEEN_NOTIFICATIONS_PATH");
	public Document viewUnseenNotifications(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_UNSEEN_NOTIFICATIONS);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String SEE_NOTIFICATION = BASE_URL+FieldMap.current.get().get("SEE_NOTIFICATION_PATH");
	public Document seeNotification(long notification) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(SEE_NOTIFICATION);
		template.setAttribute("id", notification+"");

		HttpPost httpPost = new HttpPost(template.toString());
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
	
	public final String DELETE_NOTIFICATION = BASE_URL+FieldMap.current.get().get("DELETE_NOTIFICATION_PATH");
	public Document deleteNotification(long notification) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(DELETE_NOTIFICATION);
		template.setAttribute("id", notification+"");

		HttpPost httpPost = new HttpPost(template.toString());
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
	
	public final String DELETE_PLAYER = BASE_URL+FieldMap.current.get().get("DELETE_PLAYER_PATH");
	public Document deletePlayer(long version) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(DELETE_PLAYER);

		HttpPost httpPost = new HttpPost(template.toString());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PLAYER_VERSION"), ""+version));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = httpclient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_MATCHES = BASE_URL+FieldMap.current.get().get("LIST_MATCHES_PATH");
	public Document listMatches(int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_MATCHES);
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_PLAYER_MATCHES = BASE_URL+FieldMap.current.get().get("LIST_PLAYER_MATCHES_PATH");
	public Document listPlayerMatches(long player, int page, int rows) throws ParseException, ClientProtocolException, IOException, SAXException {
		StringTemplate template = new StringTemplate();
		template.setTemplate(LIST_PLAYER_MATCHES);
		template.setAttribute("id", player+"");
		template.setAttribute("page", page+"");
		template.setAttribute("rows", rows+"");

		
		String query = template.toString()+"?"+FieldMap.current.get().get("XML_PARAM")+"="+FieldMap.current.get().get("XML_VALUE");
		HttpGet httpGet = new HttpGet(query);
		CloseableHttpResponse requestResponse = httpclient.execute(httpGet);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintGet(query, response);
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
	
	private long getPlayerVersion(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/player/@version", d, XPathConstants.STRING));
	}
	
	private long getTeamId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/team/@id", d, XPathConstants.STRING));
	}
	
	private long getPilotId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/pilot/@id", d, XPathConstants.STRING));
	}
	
	private long getChallengeId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/game/challenge/@id", d, XPathConstants.STRING));
	}

	private int getChallengeVersion(Document d) throws NumberFormatException, XPathExpressionException {
		return Integer.parseInt((String) xPath.evaluate("/game/challenge/@version", d, XPathConstants.STRING));
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

