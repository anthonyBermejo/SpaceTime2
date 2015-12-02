package org.soen387.domain.model.player;

import java.util.List;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.dsrg.soenea.domain.user.IUser;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.team.ITeam;

public class PlayerProxy extends DomainObjectProxy<Long, Player> implements IPlayer {

	public PlayerProxy(long id) {
		super(id);
	}


	@Override
	protected Player getFromMapper(Long id) throws MapperException,
			DomainObjectCreationException {
		try {
			return PlayerMapper.find(id);
		} catch (Exception e) {
			// It better be here! That null won't go over well!
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getFirstName() {
		return getInnerObject().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getInnerObject().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getInnerObject().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getInnerObject().setLastName(lastName);
	}

	@Override
	public String getEmail() {
		return getInnerObject().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getInnerObject().setEmail(email);
	}

	@Override
	public IUser getUser() {
		return getInnerObject().getUser();
	}

	@Override
	public void setUser(IUser user) {
		getInnerObject().setUser(user);
	}


	@Override
	public List<IPilot> getRoster() {
		return getInnerObject().getRoster();
	}


	@Override
	public void setRoster(List<IPilot> roster) {
		getInnerObject().setRoster(roster);
	}


	@Override
	public List<ITeam> getTeams() {
		return getInnerObject().getTeams();
	}


	@Override
	public void setTeams(List<ITeam> teams) {
		getInnerObject().setTeams(teams);
	}

}
