package org.soen387.domain.model.player;

import java.util.List;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.dsrg.soenea.domain.user.IUser;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.team.ITeam;

public interface IPlayer extends IDomainObject<Long>{

	public abstract String getFirstName();

	public abstract void setFirstName(String firstName);

	public abstract String getLastName();

	public abstract void setLastName(String lastName);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract IUser getUser();

	public abstract void setUser(IUser user);
	
	public List<IPilot> getRoster();

	public void setRoster(List<IPilot> roster);

	public List<ITeam> getTeams();

	public void setTeams(List<ITeam> teams);

}