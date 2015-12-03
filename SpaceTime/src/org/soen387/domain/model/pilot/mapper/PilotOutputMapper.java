package org.soen387.domain.model.pilot.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.team.tdg.TeamMembershipTDG;

public class PilotOutputMapper extends GenericOutputMapper<Long, Pilot>{
	public static void insertStatic(IPilot p) throws SQLException {
		PilotTDG.insert(p.getId(), p.getVersion(), p.getName(), p.getPlayer().getId());
	}

	public static void updateStatic(IPilot p) throws SQLException, LostUpdateException {
		int count = PilotTDG.update(p.getId(), p.getVersion(), p.getName(), p.getPlayer().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing player with id " + p.getId());
		p.setVersion(p.getVersion()+1);
	}
	
	public static void deleteStatic(IPilot p) throws SQLException, LostUpdateException {
		int count = PilotTDG.delete(p.getId(), p.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting player with id " + p.getId());
		//
		// What's the process for deleting a Pilot... do we need to delete users and games?
		// More on that when we discuss referential integrity.
		//
		TeamMembershipTDG.deleteByPilot(p.getId());
	}
	
	@Override
	public void insert(Pilot d) throws MapperException {
		try {
			insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Pilot d) throws MapperException {
		try {
			updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Pilot d) throws MapperException {
		try {
			deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
}
