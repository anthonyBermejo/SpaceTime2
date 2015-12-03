package org.soen387.domain.model.challenge.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.IChallenge;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;

public class ChallengeOutputMapper extends GenericOutputMapper<Long, Challenge>{
	public static void insertStatic(Challenge c) throws SQLException {
		ChallengeTDG.insert(c.getId(), c.getVersion(), c.getChallenger().getId(), c.getChallengee().getId(), c.getStatus().name());
	}

	public static void updateStatic(IChallenge c) throws SQLException, LostUpdateException {
		int count = ChallengeTDG.update(c.getId(), c.getVersion(), c.getChallenger().getId(), c.getChallengee().getId(), c.getStatus().name());
		if(count==0) throw new LostUpdateException("Lost Update editing challenge with id " + c.getId());
		c.setVersion(c.getVersion()+1);
	}
	
	public static void deleteStatic(IChallenge c) throws SQLException, LostUpdateException {
		int count = ChallengeTDG.delete(c.getId(), c.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting challenge with id " + c.getId());
	}
	
	@Override
	public void insert(Challenge d) throws MapperException {
		try {
			insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Challenge d) throws MapperException {
		try {
			updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Challenge d) throws MapperException {
		try {
			deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
}
