package org.soen387.domain.model.match.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.match.IMatch;
import org.soen387.domain.model.match.Match;
import org.soen387.domain.model.match.tdg.MatchTDG;


public class MatchOutputMapper extends GenericOutputMapper<Long, Match> {
	public static void insertStatic(Match c) throws SQLException {
		MatchTDG.insert(c.getId(), c.getVersion(), c.getFirstTeam().getId(), c.getSecondTeam().getId(), c.getStatus().name());
	}

	public static void updateStatic(IMatch c) throws SQLException, LostUpdateException {
		int count = MatchTDG.update(c.getId(), c.getVersion(), c.getFirstTeam().getId(), c.getSecondTeam().getId(), c.getStatus().name());
		if(count==0) throw new LostUpdateException("Lost Update editing match with id " + c.getId());
		c.setVersion(c.getVersion()+1);
	}
	
	public static void deleteStatic(IMatch c) throws SQLException, LostUpdateException {
		int count = MatchTDG.delete(c.getId(), c.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting match with id " + c.getId());
	}
	
	@Override
	public void insert(Match d) throws MapperException {
		try {
			insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Match d) throws MapperException {
		try {
			updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Match d) throws MapperException {
		try {
			deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
}
