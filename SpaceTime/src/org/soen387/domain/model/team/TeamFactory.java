package org.soen387.domain.model.team;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class TeamFactory {
	public static Team createNew(String name, IPlayer player,
			List<IPilot> members) throws SQLException, MissingMappingException, MapperException {
		Team result = new Team(TeamTDG.getMaxId(), 01, name, player, members);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Team createClean(long id, long version, String name, IPlayer player,
			List<IPilot> members) throws SQLException, MissingMappingException, MapperException {
		Team result = new Team(id, version, name, player, members);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
