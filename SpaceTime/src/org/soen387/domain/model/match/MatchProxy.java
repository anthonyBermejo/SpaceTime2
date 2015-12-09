package org.soen387.domain.model.match;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.match.mapper.MatchInputMapper;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.team.ITeam;

public class MatchProxy extends DomainObjectProxy<Long, Match> implements IMatch {
	public MatchProxy(long id) {
		super(id);
	}

	@Override
	protected Match getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return MatchInputMapper.find(id);
		} catch (Exception e) {
			// It better be here! That null won't go over well!
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public GameStatus getStatus() {
		return getInnerObject().getStatus();
	}



	@Override
	public void setStatus(GameStatus status) {
		getInnerObject().setStatus(status);
	}



	@Override
	public ITeam getFirstTeam() {
		return getInnerObject().getFirstTeam();
	}



	@Override
	public void setFirstTeam(ITeam firstTeam) {
		getInnerObject().setFirstTeam(firstTeam);
	}



	@Override
	public ITeam getSecondTeam() {
		return getInnerObject().getSecondTeam();
	}



	@Override
	public void setSecondTeam(ITeam secondTeam) {
		getInnerObject().setSecondTeam(secondTeam);
	}



	@Override
	public Player getWinner() {
		return getInnerObject().getWinner();
	}



	@Override
	public boolean isOver() {
		return getInnerObject().isOver();
	}



	@Override
	public boolean isTied() {
		return getInnerObject().isTied();
	}
	
	
}

