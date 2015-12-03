package org.soen387.domain.model.team.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.pilot.tdg.PilotTDG;

public class TeamMembershipFinder {
	public static final String FIND_BY_PILOT = "SELECT " + TeamMembershipTDG.COLUMNS + " FROM " + TeamMembershipTDG.TABLE_NAME + " WHERE pilot=?;";
	public static ResultSet findByPilot(long pilot) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_PILOT);
		ps.setLong(1,pilot);
		return ps.executeQuery();
	}
	
	public static final String FIND_BY_TEAM = "SELECT " + PilotTDG.COLUMNS + " FROM " + TeamMembershipTDG.TABLE_NAME + " INNER JOIN " + PilotTDG.TABLE_NAME + " ON id=pilot WHERE team=?;";
	public static ResultSet findByTeam(long team) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_TEAM);
		ps.setLong(1,team);
		return ps.executeQuery();
	}
}
