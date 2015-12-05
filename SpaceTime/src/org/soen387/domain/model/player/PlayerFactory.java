package org.soen387.domain.model.player;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.user.IUser;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.player.tdg.PlayerTDG;

public class PlayerFactory {
	public static Player createNew(String firstName, String lastName,
			String email, IUser user) throws SQLException, MissingMappingException, MapperException {
		Player result = new Player(PlayerTDG.getMaxId(), 01, firstName, lastName, email, user);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Player createClean(long id, long version, String firstName, String lastName,
			String email, IUser user) throws SQLException, MissingMappingException, MapperException {
		Player result = new Player(id, version, firstName, lastName, email, user);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
