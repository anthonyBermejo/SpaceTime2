package org.soen387.domain.model.team.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.pilot.tdg.PilotTDG;

public class TeamMembershipTDG {
	public static final String TABLE_NAME = "TeamMembership";
	public static final String COLUMNS = "pilot, team ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "pilot BIGINT, "
			+ "team BIGINT, "
			+ "INDEX(pilot), "
			+ "INDEX(team) "
			+ ");";

	public static void createTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		Statement update = con.createStatement();
		update.execute(CREATE_TABLE);
	}

	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		Statement update = con.createStatement();
		update.execute(TRUNCATE_TABLE);
		update = con.createStatement();
		update.execute(DROP_TABLE);
	}
	
	
	public static final String INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMNS + ") "
			+ "VALUES (?,?);";
	public static int insert(long pilot, long team) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,pilot);
		ps.setLong(2,team);
		return ps.executeUpdate();
	}
	
	public static final String DELETE = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE pilot=? AND team=?;";
	public static int delete(long pilot, long team) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1,pilot);
		ps.setLong(2,team);
		return ps.executeUpdate();
	}
	
	public static final String DELETE_BY_PILOT = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE pilot=?;";
	public static int deleteByPilot(long pilot) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE_BY_PILOT);
		ps.setLong(1,pilot);
		return ps.executeUpdate();
	}
	
	public static final String DELETE_BY_TEAM = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE team=?;";
	public static int deleteByTeam(long team) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE_BY_TEAM);
		ps.setLong(1,team);
		return ps.executeUpdate();
	}	
}
