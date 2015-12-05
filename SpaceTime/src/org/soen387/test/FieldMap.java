package org.soen387.test;

import java.util.HashMap;
import java.util.Map;

import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;
import org.soen387.domain.model.challenge.ChallengeStatus;

public class FieldMap extends ThreadLocal<Map<String, String>> {
	public static FieldMap current = new FieldMap();
	
	@Override
	protected Map<String, String> initialValue() {
		// TODO Auto-generated method stub
		return new HashMap<String, String>();
	}
	
	static {
		ThreadLocalTracker.registerThreadLocal(current);
		/*
		 * 
		 * This is the URL for your application. Note that it should have no trailing slash
		 * 
		 */
		current.get().put("BASE_URL", "http://localhost:8080/SpaceTime");

		
		/**
		 * 
		 * This effects what is printed to sysout. Not nice logging or anything, sadly.
		 * 
		 */
		current.get().put("DEBUG", "true");
		
		/*
		 * 
		 * If you need a parameter to set it to return an xml response, fill it in here. This
		 * defaults to what we've used in the tutorials, I think.
		 * 
		 */
		current.get().put("XML_PARAM", "mode");
		current.get().put("XML_VALUE", "xml");
		
		/*
		 * 
		 * Each of the Use Cases is represented by a PageController, effectively (though this system 
		 * supports FrontControllers of some flavors as well as group PageControllers). Each Use Case
		 * Needs it's path, starting with a slash. The names of parameters follow. UC 7 has an exception
		 * In the event that you pass a different form of status, this supports the common variances I 
		 * have seen. 
		 * 
		 */
		
		//UC 1
		current.get().put("LOGIN_PATH", "/st/LogIn");
		current.get().put("USERNAME_PARAM", "username");
		current.get().put("PASSWORD_PARAM", "password");

		//UC 2
		current.get().put("LOGOUT_PATH", "/st/LogOut");
		
		
		/*
		 * 
		 * Note that I use the same params for username and password
		 * as in LogIn. If you're not doing so already, you should do so
		 * 
		 */
		//UC 3
		current.get().put("REGISTER_PATH", "/st/Player/Register");
		current.get().put("FIRSTNAME_PARAM", "firstName");
		current.get().put("LASTNAME_PARAM", "lastName");
		current.get().put("EMAIL_PARAM", "email");
		
		//UC 4
		current.get().put("LIST_PLAYERS_PATH", "/st/Player/p$page$r$rows$");

		//UC 5
		current.get().put("RECRUIT_PILOT_PATH", "/st/Pilot/Recruit");
		
		//UC 6		
		current.get().put("FIRE_PILOT_PATH", "/st/Pilot/$id$/Fire");	
		
		
		//UC 7
		current.get().put("LIST_PILOTS_PATH", "/st/Pilot/p$page$r$rows$");
		
		//UC 8
		current.get().put("ADD_TEAM_PATH", "/st/Team/Add");
		current.get().put("TEAM_NAME", "teamname");
		
		//UC 9
		current.get().put("REMOVE_TEAM_PATH", "/st/Team/$id$/Remove");
		current.get().put("TEAM_VERSION", "version");
		//current.get().put("TEAM_ID", "team");
		
		//UC 10	
		current.get().put("VIEW_TEAM_PATH", "/st/Team/$id$");	
		//re-used team id from above
		
		//UC 11
		current.get().put("ADD_PILOT_TO_TEAM_PATH", "/st/Team/$id$/AddPilot");
		current.get().put("PILOT_ID", "pilot");
		//re-used team version from above
		
		//UC 12
		current.get().put("REMOVE_PILOT_FROM_TEAM_PATH", "/st/Team/$id$/RemovePilot");
		//re-used from above
		
		//UC 13 
		current.get().put("CHALLENGE_PLAYER_PATH", "/st/Player/$id$/Challenge");

		//UC 14
		current.get().put("RESPOND_TO_CHALLENGE_PATH", "/st/Challenge/$id$");
		current.get().put("CHALLENGE_VERSION", "version");
		
		//Even action based, I need this to know what I'm passing in the tests.
		current.get().put("CHALLENGE_STATUS_ISSUED_ID", 0+"");
		current.get().put("CHALLENGE_STATUS_ACCEPT_ID", 1+"");
		current.get().put("CHALLENGE_STATUS_REFUSE_ID", 2+"");
		
		//This is only needed for the parameter-based approach, not for the action-based
		current.get().put("CHALLENGE_STATUS", "status");
		//Technically these may not be the ordinal values of the enum or whatnot that could be used above; 
		//theoretically, someone could pass a string param or something...
		current.get().put("CHALLENGE_STATUS_" + current.get().get("CHALLENGE_STATUS_ACCEPT_ID") + "_PARAM", ChallengeStatus.Accepted.ordinal()+"");
		current.get().put("CHALLENGE_STATUS_" + current.get().get("CHALLENGE_STATUS_REFUSE_ID") + "_PARAM", ChallengeStatus.Refused.ordinal()+"");
		//Add others if your system uses this mechanism, otherwise ignore.
		
		//Alternate action-based submission approach:
		//e.g. /st/Challenge/$id$/Refuse
		//current.get().put("ACCEPTED_CHALLENGE_SUFFIX", "/Accept");
		//current.get().put("REFUSE_CHALLENGE_SUFFIX", "/Refuse");
		
		//UC 15 
		current.get().put("VIEW_CHALLENGES_PATH", "/st/Challenge/p$page$r$rows$");
		
		//UC 16
		current.get().put("WITHDRAW_CHALLENGE_PATH", "/st/Challenge/$id$/Withdraw");
		
		//UC 17
		current.get().put("LIST_NOTIFICATIONS_PATH", "/st/Notification/p$page$r$rows$");
		
		//UC 18
		current.get().put("LIST_UNSEEN_NOTIFICATIONS_PATH", "/st/Notification/Unseenp$page$r$rows$");
		
		//UC 19
		current.get().put("SEE_NOTIFICATION_PATH", "/st/Notification/$id$/See");
		//You probably don't want Lost Update checking with notifications so we're skipping it
		//
		//current.get().put("NOTIFICATION_VERSION", "version");
		
		//UC 20
		current.get().put("DELETE_NOTIFICATION_PATH", "/st/Notification/$id$/Remove");
		
		//UC 21
		current.get().put("DELETE_PLAYER_PATH", "/st/Player/Retire");
		current.get().put("PLAYER_VERSION", "version");
		
		//UC 22
		current.get().put("LIST_MATCHES_PATH", "/st/Match/p$page$r$rows$");
		current.get().put("MATCH_STATUS_ONGOING_ID", 0+"");
		current.get().put("MATCH_STATUS_OVER_ID", 1+"");
		current.get().put("MATCH_STATUS_TIED_ID", 2+"");
		
		//UC 23
		current.get().put("LIST_PLAYER_MATCHES_PATH", "/st/Player/$id$/Matchesp$page$r$rows$");
	}
}