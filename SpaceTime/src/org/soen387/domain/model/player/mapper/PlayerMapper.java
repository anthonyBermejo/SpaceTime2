package org.soen387.domain.model.player.mapper;

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
import org.dsrg.soenea.domain.user.IUser;
import org.dsrg.soenea.domain.user.UserProxy;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class PlayerMapper extends GenericOutputMapper<Long, Player>{
	
	public static void insertStatic(IPlayer p) throws SQLException {
		PlayerTDG.insert(p.getId(), p.getVersion(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getUser().getId());
	}

	public static void updateStatic(IPlayer p) throws SQLException, LostUpdateException {
		int count = PlayerTDG.update(p.getId(), p.getVersion(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getUser().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing player with id " + p.getId());
		p.setVersion(p.getVersion()+1);
	}
	
	public static void deleteStatic(IPlayer p) throws SQLException, LostUpdateException {
		int count = PlayerTDG.delete(p.getId(), p.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting player with id " + p.getId());
		//
		// What's the process for deleting a Player... do we need to delete users and games?
		// More on that when we discuss referential integrity.
		//
		TeamTDG.deleteByPlayer(p.getId());
		PilotTDG.deleteByPlayer(p.getId());
	}

	public static Player find(long id) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {

		if(IdentityMap.has(id, Player.class)) return IdentityMap.get(id, Player.class);

		ResultSet rs = PlayerTDG.find(id);
		if(rs.next()) {
			Player p = buildPlayer(rs);
			rs.close();
			UoW.getCurrent().registerClean(p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Player with id \""+id+"\"");
	}
	
	public static Player find(IUser u) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {
		ResultSet rs = PlayerTDG.findByUser(u.getId());
		if(rs.next()) {
			long id = rs.getLong("id");

			if(IdentityMap.has(id, Player.class)) return IdentityMap.get(id, Player.class);

			Player p = buildPlayer(rs);
			rs.close();
			UoW.getCurrent().registerClean(p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Player from User with id \""+u.getId()+"\"");
	}

	public static List<IPlayer> buildCollection(ResultSet rs)
		    throws SQLException, ObjectRemovedException, DomainObjectNotFoundException {
		    ArrayList<IPlayer> l = new ArrayList<IPlayer>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Player p = null;

				if(IdentityMap.has(id, Player.class)) {
					p = IdentityMap.get(id, Player.class);
				} else {
			    	p = buildPlayer(rs);
			    	UoW.getCurrent().registerClean(p);
				}
		    	l.add(p);

		    }
		    return l;
		}

	public static List<IPlayer> findAll() throws MapperException {
        try {
            ResultSet rs = PlayerTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Player buildPlayer(ResultSet rs) throws SQLException  {
		// TODO Auto-generated method stub
		return new Player(rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("email"),
				new UserProxy(rs.getLong("user"))
				);
	}

	@Override
	public void insert(Player d) throws MapperException {
		try {
			PlayerMapper.insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Player d) throws MapperException {
		try {
			PlayerMapper.updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Player d) throws MapperException {
		try {
			PlayerMapper.deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
}
