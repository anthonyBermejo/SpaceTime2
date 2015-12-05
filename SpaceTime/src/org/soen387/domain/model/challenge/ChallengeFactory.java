package org.soen387.domain.model.challenge;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;
import org.soen387.domain.model.player.IPlayer;

public class ChallengeFactory {
	public static Challenge createNew(IPlayer challenger, IPlayer challengee, ChallengeStatus status) throws SQLException, MissingMappingException, MapperException {
		Challenge result = new Challenge(ChallengeTDG.getMaxId(), 01, challenger, challengee, status);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Challenge createClean(long id, long version, IPlayer challenger, IPlayer challengee, ChallengeStatus status) throws SQLException, MissingMappingException, MapperException {
		Challenge result = new Challenge(id, version, challenger, challengee, status);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
