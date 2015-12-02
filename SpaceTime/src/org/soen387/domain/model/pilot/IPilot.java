package org.soen387.domain.model.pilot;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.soen387.domain.model.player.IPlayer;

public interface IPilot extends IDomainObject<Long>{

	public abstract String getName();

	public abstract void setName(String name);

	public abstract IPlayer getPlayer();

	public abstract void setPlayer(IPlayer player);

}