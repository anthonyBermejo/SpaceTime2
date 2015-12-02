package org.soen387.domain.model.player.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class PlayerTDG {
	public static final String BASE = "Player";
	public static final String TABLE_NAME = DbRegistry.getTablePrefix()+BASE;
	public static final String COLUMNS = "id, version, firstname, lastname, email, user ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "id BIGINT, "
			+ "version BIGINT, "
			+ "firstname VARCHAR(32), "
			+ "lastname VARCHAR(32), "
			+ "email VARCHAR(254), "
			+ "user BIGINT, "
			+ "PRIMARY KEY(id), "
			+ "INDEX(user), "
			+ "UNIQUE(email) "
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
			+ "VALUES (?,?,?,?,?,?);";
	public static int insert(long id, long version, String firstname, String lastname, String email, long user) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,id);
		ps.setLong(2,version);
		ps.setString(3,firstname);
		ps.setString(4,lastname);
		ps.setString(5,email);
		ps.setLong(6,user);
		return ps.executeUpdate();
	}
	
	public static final String UPDATE = "UPDATE " + TABLE_NAME + " set version = version+1, firstname=?, lastname=?, email=?, user=? "
			+ " WHERE id=? AND version=?;";
	public static int update(long id, long version, String firstname, String lastname, String email, long user) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1,firstname);
		ps.setString(2,lastname);
		ps.setString(3,email);
		ps.setLong(4,user);
		ps.setLong(5,id);
		ps.setLong(6,version);
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
	
	public static final String FIND = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE id=?;";
	public static ResultSet find(long id) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND);
		ps.setLong(1,id);
		return ps.executeQuery();
	}
	
	public static final String FIND_BY_USER = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE user=?;";
	public static ResultSet findByUser(long user) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_USER);
		ps.setLong(1,user);
		return ps.executeQuery();
	}
	
	public static final String FIND_ALL = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + ";";
	public static ResultSet findAll() throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		return ps.executeQuery();
	}
	
	public static long getMaxId() throws SQLException {
		return UniqueIdFactory.getMaxId(BASE, "id");
	}
}
