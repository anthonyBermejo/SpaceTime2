package org.soen387.domain.model.match;

import org.dsrg.soenea.domain.DomainObject;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.team.ITeam;

public class Match extends DomainObject<Long> implements IMatch{
	private GameStatus status;
	private ITeam firstTeam;
	private ITeam secondTeam;
	
	public Match(Long id, Long version, GameStatus status, ITeam firstTeam, ITeam secondTeam) {
		super(id, version);
		this.status = status;
		this.firstTeam = firstTeam;
		this.secondTeam = secondTeam;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public ITeam getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(ITeam firstTeam) {
		this.firstTeam = firstTeam;
	}

	public ITeam getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(ITeam secondTeam) {
		this.secondTeam = secondTeam;
	}
	
	public Player getWinner() {
		return null;
	}
	
	public boolean isOver() {
		return false;
	}
	
	public boolean isTied() {
		return false;
	}
}
