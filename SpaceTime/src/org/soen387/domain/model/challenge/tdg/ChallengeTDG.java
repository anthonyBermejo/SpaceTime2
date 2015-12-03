package org.soen387.domain.model.challenge.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class ChallengeTDG {
	public static final String BASE = "Challenge";
	public static final String TABLE_NAME = DbRegistry.getTablePrefix()+BASE;
	public static final String COLUMNS = "id, version, challenger, challengee, status ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "id BIGINT, "
			+ "version BIGINT, "
			+ "challenger BIGINT, "
			+ "challengee BIGINT, "
			+ "status VARCHAR(32), "
			+ "PRIMARY KEY(id), "
			+ "INDEX(challenger), "
			+ "INDEX(challengee), "
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
	public static int insert(long id, long version, long challenger, long challengee, String status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,id);
		ps.setLong(2,version);
		ps.setLong(3,challenger);
		ps.setLong(4,challengee);
		ps.setString(5, status);
		return ps.executeUpdate();
	}
	
	public static final String UPDATE = "UPDATE " + TABLE_NAME + " set version = version+1, challenger=?, challengee=?, status=? "
			+ " WHERE id=? AND version=?;";
	public static int update(long id, long version, long challenger, long challengee, String status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1,challenger);
		ps.setLong(2,challengee);
		ps.setString(3,status);
		ps.setLong(4,id);
		ps.setLong(5,version);
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
		
	public static long getMaxId() throws SQLException {
		return UniqueIdFactory.getMaxId(BASE, "id");
	}
}
