package org.soen387.domain.model.team.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.team.ITeam;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.tdg.TeamMembershipTDG;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class TeamOutputMapper extends GenericOutputMapper<Long, Team>{

	public static void insertStatic(ITeam t) throws SQLException {
		TeamTDG.insert(t.getId(), t.getVersion(), t.getName(), t.getPlayer().getId());
		for(IPilot p: t.getMembers()) {
			TeamMembershipTDG.insert(p.getId(), t.getId());
		}
	}

	public static void updateStatic(ITeam t) throws SQLException, LostUpdateException {
		int count = TeamTDG.update(t.getId(), t.getVersion(), t.getName(), t.getPlayer().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing player with id " + t.getId());
		t.setVersion(t.getVersion()+1);
		TeamMembershipTDG.deleteByTeam(t.getId());
		for(IPilot p: t.getMembers()) {
			TeamMembershipTDG.insert(p.getId(), t.getId());
		}
	}
	
	public static void deleteStatic(ITeam t) throws SQLException, LostUpdateException {
		int count = TeamTDG.delete(t.getId(), t.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting player with id " + t.getId());
		//
		// What's the process for deleting a Team... do we need to delete users and games?
		// More on that when we discuss referential integrity.
		//
		TeamMembershipTDG.deleteByTeam(t.getId());
		
	}
	
	@Override
	public void insert(Team d) throws MapperException {
		try {
			insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Team d) throws MapperException {
		try {
			updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Team d) throws MapperException {
		try {
			deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
