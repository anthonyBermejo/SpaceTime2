package org.soen387.domain.model.player;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.DomainObject;
import org.dsrg.soenea.domain.producer.IdentityBasedProducer;
import org.dsrg.soenea.domain.user.IUser;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.team.ITeam;

public class Player extends DomainObject<Long> implements IPlayer, IdentityBasedProducer {

	String firstName;
	String lastName;
	String email;
	IUser user;
	List<IPilot> roster;
	List<ITeam> teams;

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
	}
	
	public List<IPilot> getRoster() {
		return roster;
	}

	public void setRoster(List<IPilot> roster) {
		this.roster = roster;
	}

	public List<ITeam> getTeams() {
		return teams;
	}

	public void setTeams(List<ITeam> teams) {
		this.teams = teams;
	}

	public Player(long id, long version, String firstName, String lastName,
			String email, IUser user) {
		super(id, version);

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.user = user;
		this.teams = new ArrayList<ITeam>();
		this.roster = new ArrayList<IPilot>();
	}

}
