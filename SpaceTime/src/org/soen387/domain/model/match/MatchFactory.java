package org.soen387.domain.model.match;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.match.tdg.MatchTDG;
import org.soen387.domain.model.team.ITeam;

public class MatchFactory {
	public static Match createNew(GameStatus status, ITeam firstTeam, ITeam secondTeam) throws SQLException, MissingMappingException, MapperException {
		Match result = new Match(MatchTDG.getMaxId(), 01, status, firstTeam, secondTeam);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Match createClean(Long id, Long version, GameStatus status, ITeam firstTeam, ITeam secondTeam) throws SQLException, MissingMappingException, MapperException {
		Match result = new Match(id, version, status, firstTeam, secondTeam);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
