package org.soen387.domain.model.pilot;

import org.dsrg.soenea.domain.DomainObject;
import org.soen387.domain.model.player.IPlayer;

public class Pilot extends DomainObject<Long> implements IPilot{
	String name;
	IPlayer player;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public IPlayer getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IPlayer player) {
		this.player = player;
	}
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.pilot.IPilot#getId()
	 */

	public Pilot(long id, long version, String name, IPlayer player) {
		super(id, version);
		this.name = name;
		this.player = player;
	}

}
