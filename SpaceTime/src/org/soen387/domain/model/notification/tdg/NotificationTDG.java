package org.soen387.domain.model.notification.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class NotificationTDG {
	public static final String BASE = "Notification";
	public static final String TABLE_NAME = DbRegistry.getTablePrefix()+BASE;
	public static final String COLUMNS = "id, version, recipient, seen ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "id BIGINT, "
			+ "version BIGINT, "
			+ "recipient BIGINT, "
			+ "seen TINYINT, "
			+ "PRIMARY KEY(id), "
			+ "INDEX(recipient) "
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
			+ "VALUES (?,?,?,?);";
	public static int insert(long id,  long version, long recipient, boolean seen) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,id);
		ps.setLong(2,version);
		ps.setLong(3,recipient);
		ps.setBoolean(4,seen);
		return ps.executeUpdate();
	}
	
	public static final String UPDATE = "UPDATE " + TABLE_NAME + " set version = version+1, recipient=?, seen=? "
			+ " WHERE id=? AND version=?;";
	public static int update(long id, long version, long recipient, boolean seen) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1,recipient);
		ps.setBoolean(2,seen);
		ps.setLong(3,id);
		ps.setLong(4,version);
		return ps.executeUpdate();
	}
	
	public static final String DELETE = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE id=? AND version=?;";
	public static int delete(long id, long version) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1,id);
		ps.setLong(2,version);
		return ps.executeUpdate();
	}
	
	public static final String DELETE_BY_PLAYER = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE player=?";
	public static int deleteByPlayer(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1,player);
		return ps.executeUpdate();
	}
	
	public static long getMaxId() throws SQLException {
		return UniqueIdFactory.getMaxId(BASE, "id");
	}
}
