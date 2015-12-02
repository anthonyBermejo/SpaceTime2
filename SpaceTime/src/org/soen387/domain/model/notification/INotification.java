package org.soen387.domain.model.notification;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.soen387.domain.model.player.IPlayer;

public interface INotification extends IDomainObject<Long>{
	public IPlayer getRecipient();

	public void setRecipient(IPlayer recipient);

	public boolean isSeen();

	public void setSeen(boolean seen);
}
