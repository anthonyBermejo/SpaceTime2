package org.soen387.domain.model.player.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class PlayerFinder {
	public static final String FIND = "SELECT " + PlayerTDG.COLUMNS + " FROM " + PlayerTDG.TABLE_NAME + " WHERE id=?;";
	public static ResultSet find(long id) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND);
		ps.setLong(1,id);
		return ps.executeQuery();
	}
	
	public static final String FIND_BY_USER = "SELECT " + PlayerTDG.COLUMNS + " FROM " + PlayerTDG.TABLE_NAME + " WHERE user=?;";
	public static ResultSet findByUser(long user) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_USER);
		ps.setLong(1,user);
		return ps.executeQuery();
	}
	
	public static final String FIND_ALL = "SELECT " + PlayerTDG.COLUMNS + " FROM " + PlayerTDG.TABLE_NAME + ";";
	public static ResultSet findAll() throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		return ps.executeQuery();
	}
}
