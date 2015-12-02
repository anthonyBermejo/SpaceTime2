package org.soen387.domain.model.team;

import java.util.List;

import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.team.mapper.TeamMapper;

public class TeamProxy extends DomainObjectProxy<Long, Team> implements ITeam {
	
	public TeamProxy(long id) {
		super(id);
	}

	@Override
	public Team getFromMapper(Long id) {
		try {
			return TeamMapper.find(id);
		} catch (Exception e) {
			// It better be here! That null won't go over well!
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getName() {
		return getInnerObject().getName();
	}

	@Override
	public void setName(String name) {
		getInnerObject().setName(name);
	}

	@Override
	public IPlayer getPlayer() {
		return getInnerObject().getPlayer();
	}

	@Override
	public void setPlayer(IPlayer player) {
		getInnerObject().setPlayer(player);
	}

	@Override
	public List<IPilot> getMembers() {
		return getInnerObject().getMembers();
	}

}
