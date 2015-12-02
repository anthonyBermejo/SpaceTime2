package org.soen387.domain.model.challenge;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.soen387.domain.model.player.IPlayer;

public interface IChallenge extends IDomainObject<Long>{
	public IPlayer getChallenger();

	public void setChallenger(IPlayer challenger);

	public IPlayer getChallengee();
	
	public void setChallengee(IPlayer challengee);

	public ChallengeStatus getStatus();

	public void setStatus(ChallengeStatus status);
}
