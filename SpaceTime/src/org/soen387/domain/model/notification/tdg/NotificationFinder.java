package org.soen387.domain.model.notification.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class NotificationFinder {
	public static final String FIND = "SELECT " + NotificationTDG.COLUMNS + " FROM " + NotificationTDG.TABLE_NAME + " WHERE id=?;";
	public static ResultSet find(long id) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND);
		ps.setLong(1,id);
		return ps.executeQuery();
	}
	
	public static final String FIND_BY_RECIPIENT = "SELECT " + NotificationTDG.COLUMNS + " FROM " + NotificationTDG.TABLE_NAME + " WHERE recipient=?;";
	public static ResultSet findByPlayer(long recipient) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_RECIPIENT);
		ps.setLong(1, recipient);
		return ps.executeQuery();
	}
	
	public static final String FIND_ALL = "SELECT " + NotificationTDG.COLUMNS + " FROM " + NotificationTDG.TABLE_NAME + ";";
	public static ResultSet findAll() throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		return ps.executeQuery();
	}
}
