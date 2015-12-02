package org.soen387.domain.model.pilot.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.PlayerProxy;
import org.soen387.domain.model.team.ITeam;
import org.soen387.domain.model.team.tdg.TeamMembershipTDG;

public class PilotMapper extends GenericOutputMapper<Long, Pilot> {
	
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

	public static Pilot find(long id) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {

		if(IdentityMap.has(id, Pilot.class)) return IdentityMap.get(id, Pilot.class);

		ResultSet rs = PilotTDG.find(id);
		if(rs.next()) {
			Pilot p = buildPilot(rs);
			rs.close();
			UoW.getCurrent().registerClean(p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Pilot with id \""+id+"\"");
	}
	
	public static List<IPilot> find(IPlayer player) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		ResultSet rs = PilotTDG.findByPlayer(player.getId());
		return buildCollection(rs);
	}
	
	public static List<IPilot> find(ITeam team) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		ResultSet rs = TeamMembershipTDG.findByTeam(team.getId());
		return buildCollection(rs);
	}

	public static List<IPilot> buildCollection(ResultSet rs)
		    throws SQLException, ObjectRemovedException, DomainObjectNotFoundException {
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
            ResultSet rs = PilotTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Pilot buildPilot(ResultSet rs) throws SQLException  {
		// TODO Auto-generated method stub
		return new Pilot(rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("name"),
				new PlayerProxy(rs.getLong("player"))
				);
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
