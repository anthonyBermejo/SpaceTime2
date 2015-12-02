package org.soen387.domain.model.notification;

import org.dsrg.soenea.domain.DomainObject;
import org.soen387.domain.model.player.IPlayer;

public class Notification extends DomainObject<Long> implements INotification{
	private IPlayer recipient;
	private boolean seen;
	
	public Notification(Long id, Long version, IPlayer recipient, boolean seen) {
		super(id, version);
		this.recipient = recipient;
		this.seen = seen;
	}

	public IPlayer getRecipient() {
		return recipient;
	}

	public void setRecipient(IPlayer recipient) {
		this.recipient = recipient;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	
}
