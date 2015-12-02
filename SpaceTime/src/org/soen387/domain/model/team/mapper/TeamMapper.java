package org.soen387.domain.model.team.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.mapper.PilotMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.PlayerProxy;
import org.soen387.domain.model.team.ITeam;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.TeamProxy;
import org.soen387.domain.model.team.tdg.TeamMembershipTDG;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class TeamMapper extends GenericOutputMapper<Long, Team> {

	
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

	public static Team find(long id) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		if(IdentityMap.has(id, Team.class)) return IdentityMap.get(id, Team.class);
		
		ResultSet rs = TeamTDG.find(id);
		if(rs.next()) {
			Team t = buildTeam(rs);
			rs.close();
			UoW.getCurrent().registerClean(t);
			return t;
		}
		throw new DomainObjectNotFoundException("Could not create a Team with id \""+id+"\"");
	}
	
	public static Team find(IPlayer player) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		ResultSet rs = TeamTDG.findByPlayer(player.getId());
		if(rs.next()) {
			long id = rs.getLong("id");
	
			if(IdentityMap.has(id, Team.class)) return IdentityMap.get(id, Team.class);

			Team p = buildTeam(rs);
			rs.close();
			UoW.getCurrent().registerClean(p);
			return p;
			
		}
		throw new DomainObjectNotFoundException("Could not create a Team from Player with id \""+player.getId()+"\"");
	}

	public static List<ITeam> buildCollection(ResultSet rs)
		    throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		    ArrayList<ITeam> l = new ArrayList<ITeam>();
		    while(rs.next()) {	    	
		    	long id = rs.getLong("id");
		    	Team p = null;
		    	if(IdentityMap.has(id, Team.class)) {
		    		p = IdentityMap.get(id, Team.class);
		    	} else {
		    		p = buildTeam(rs);
		    		UoW.getCurrent().registerClean(p);
		    	}
		    	l.add(p);
		    }
		    return l;
		}

	public static List<ITeam> findAll() throws MapperException {
        try {
            ResultSet rs = TeamTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Team buildTeam(ResultSet rs) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException  {
		// TODO Auto-generated method stub
		PlayerProxy player = new PlayerProxy(rs.getLong("player"));
		
		long id = rs.getLong("id");
		int version = rs.getInt("version");
		String name = rs.getString("name");
		List<IPilot> pilots = PilotMapper.find(new TeamProxy(id));
		return new Team(id,
				version,
				name,
				player,
				pilots
				);
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
