package org.soen387.domain.model.challenge;

import org.dsrg.soenea.domain.DomainObject;
import org.soen387.domain.model.player.IPlayer;

public class Challenge extends DomainObject<Long> implements IChallenge{
	private IPlayer challenger;
	private IPlayer challengee;
	private ChallengeStatus status;
	
	public Challenge(long id, long version, IPlayer challenger, IPlayer challengee, ChallengeStatus status) {
		super(id, version);
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
	}

	public IPlayer getChallenger() {
		return challenger;
	}

	public void setChallenger(IPlayer challenger) {
		this.challenger = challenger;
	}

	public IPlayer getChallengee() {
		return challengee;
	}

	public void setChallengee(IPlayer challengee) {
		this.challengee = challengee;
	}

	public ChallengeStatus getStatus() {
		return status;
	}

	public void setStatus(ChallengeStatus status) {
		this.status = status;
	}
	
	
}
