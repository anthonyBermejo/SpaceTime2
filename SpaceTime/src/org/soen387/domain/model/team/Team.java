package org.soen387.domain.model.team;

import java.util.List;

import org.dsrg.soenea.domain.DomainObject;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.IPlayer;

public class Team extends DomainObject<Long> implements ITeam{

	String name;
	IPlayer player;
	
	public List<IPilot> getMembers() {
		return members;
	}

	List<IPilot> members;
	
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

	public Team(long id, long version, String name, IPlayer player,
			List<IPilot> members) {
		super(id, version);
		this.name = name;
		this.player = player;
		this.members = members;
	}

	
}
