package org.soen387.domain.model.pilot.mapper;

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
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.PilotFactory;
import org.soen387.domain.model.pilot.tdg.PilotFinder;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.PlayerProxy;
import org.soen387.domain.model.team.ITeam;
import org.soen387.domain.model.team.tdg.TeamMembershipFinder;

public class PilotInputMapper {
	public static Pilot find(long id) throws SQLException, MissingMappingException, MapperException {

		if(IdentityMap.has(id, Pilot.class)) return IdentityMap.get(id, Pilot.class);

		ResultSet rs = PilotFinder.find(id);
		if(rs.next()) {
			Pilot p = buildPilot(rs);
			rs.close();
			UoW.getCurrent().registerClean(p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Pilot with id \""+id+"\"");
	}
	
	public static List<IPilot> find(IPlayer player) throws SQLException, MissingMappingException, MapperException {
		ResultSet rs = PilotFinder.findByPlayer(player.getId());
		return buildCollection(rs);
	}
	
	public static List<IPilot> find(ITeam team) throws SQLException, MissingMappingException, MapperException {
		ResultSet rs = TeamMembershipFinder.findByTeam(team.getId());
		return buildCollection(rs);
	}

	public static List<IPilot> buildCollection(ResultSet rs)
		    throws SQLException, MissingMappingException, MapperException {
		    ArrayList<IPilot> l = new ArrayList<IPilot>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Pilot p = null;
		    	if(IdentityMap.has(id, Pilot.class)) {
		    		p = IdentityMap.get(id, Pilot.class);
		    	} else {
		    		p = buildPilot(rs);
		    		UoW.getCurrent().registerClean(p);
		    	}
		    	l.add(p);
		    }
		    return l;
		}

	public static List<IPilot> findAll() throws MapperException {
        try {
            ResultSet rs = PilotFinder.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Pilot buildPilot(ResultSet rs) throws SQLException, MissingMappingException, MapperException  {
		// TODO Auto-generated method stub
		return PilotFactory.createClean(rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("name"),
				new PlayerProxy(rs.getLong("player"))
				);
	}

}
