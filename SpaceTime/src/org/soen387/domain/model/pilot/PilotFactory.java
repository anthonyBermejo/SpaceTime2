package org.soen387.domain.model.pilot;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;

public class PilotFactory {
	public static Pilot createNew(String name, IPlayer player) throws SQLException, MissingMappingException, MapperException {
		Pilot result = new Pilot(PilotTDG.getMaxId(), 01, name, player);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Pilot createClean(long id, long version, String name, IPlayer player) throws SQLException, MissingMappingException, MapperException {
		Pilot result = new Pilot(id, version, name, player);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
