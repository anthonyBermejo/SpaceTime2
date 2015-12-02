package org.soen387.test;

import java.util.HashMap;
import java.util.Map;

import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;

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
		current.get().put("LOGIN_PATH", "/LogIn");
		current.get().put("USERNAME_PARAM", "username");
		current.get().put("PASSWORD_PARAM", "password");

		//UC 2
		current.get().put("LOGOUT_PATH", "/LogOut");
		
		
		/*
		 * 
		 * Note that I use the same params for username and password
		 * as in LogIn. If you're not doing so already, you should do so
		 * 
		 */
		//UC 3
		current.get().put("REGISTER_PATH", "/RegisterPlayer");
		current.get().put("FIRSTNAME_PARAM", "firstName");
		current.get().put("LASTNAME_PARAM", "lastName");
		current.get().put("EMAIL_PARAM", "email");
		
		//UC 4
		current.get().put("LIST_PLAYERS_PATH", "/ListPlayers");

		//UC 5
		current.get().put("RECRUIT_PILOT_PATH", "/RecruitPilot");
		
		//UC 6		
		current.get().put("FIRE_PILOT_PATH", "/FirePilot");	
		current.get().put("PILOT_ID", "pilot");
		
		//UC 7
		current.get().put("LIST_PILOTS_PATH", "/ListPilots");
		
		//UC 8
		current.get().put("ADD_TEAM_PATH", "/AddTeam");
		current.get().put("TEAM_NAME", "teamname");
		
		//UC 9
		current.get().put("REMOVE_TEAM_PATH", "/AddTeam");
		current.get().put("TEAM_ID", "team");
		
		//UC 10	
		current.get().put("VIEW_TEAM_PATH", "/ViewTeam");	
		//re-used team id from above
		
		//UC 11
		current.get().put("ADD_PILOT_TO_TEAM_PATH", "/AddPilotToTeam");
		current.get().put("TEAM_VERSION", "version");
		//re-used team and pilot id from above
		
		//UC 12
		current.get().put("REMOVE_PILOT_FROM_TEAM_PATH", "/RemovePilotFromTeam");
		//re-used from above
		
	}
}
