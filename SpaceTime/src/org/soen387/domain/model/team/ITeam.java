package org.soen387.domain.model.team;

import java.util.List;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.IPlayer;

public interface ITeam extends IDomainObject<Long>{

	public abstract String getName();

	public abstract void setName(String name);

	public abstract IPlayer getPlayer();

	public abstract void setPlayer(IPlayer player);

	public abstract List<IPilot> getMembers();

}