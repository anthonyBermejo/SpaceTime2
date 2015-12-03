package org.soen387.domain.model.player.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class PlayerOutputMapper extends GenericOutputMapper<Long, Player> {
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
	
	@Override
	public void insert(Player d) throws MapperException {
		try {
			PlayerOutputMapper.insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Player d) throws MapperException {
		try {
			PlayerOutputMapper.updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Player d) throws MapperException {
		try {
			PlayerOutputMapper.deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
}
