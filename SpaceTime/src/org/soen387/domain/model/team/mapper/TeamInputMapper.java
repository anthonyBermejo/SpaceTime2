package org.soen387.domain.model.team.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.pilot.mapper.PilotInputMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.PlayerProxy;
import org.soen387.domain.model.team.ITeam;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.TeamFactory;
import org.soen387.domain.model.team.TeamProxy;
import org.soen387.domain.model.team.tdg.TeamFinder;

public class TeamInputMapper {
	public static Team find(long id) throws SQLException, MissingMappingException, MapperException {
		if(IdentityMap.has(id, Team.class)) return IdentityMap.get(id, Team.class);
		
		ResultSet rs = TeamFinder.find(id);
		if(rs.next()) {
			Team t = buildTeam(rs);
			rs.close();
			UoW.getCurrent().registerClean(t);
			return t;
		}
		throw new DomainObjectNotFoundException("Could not create a Team with id \""+id+"\"");
	}
	
	public static Team find(IPlayer player) throws SQLException, MissingMappingException, MapperException {
		ResultSet rs = TeamFinder.findByPlayer(player.getId());
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
		    throws SQLException, MissingMappingException, MapperException {
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
            ResultSet rs = TeamFinder.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Team buildTeam(ResultSet rs) throws SQLException, MissingMappingException, MapperException  {
		// TODO Auto-generated method stub
		PlayerProxy player = new PlayerProxy(rs.getLong("player"));
		
		long id = rs.getLong("id");
		int version = rs.getInt("version");
		String name = rs.getString("name");
		List<IPilot> pilots = PilotInputMapper.find(new TeamProxy(id));
		return TeamFactory.createClean(id,
				version,
				name,
				player,
				pilots
				);
	}
}
