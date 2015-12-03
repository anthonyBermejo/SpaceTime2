package org.soen387.domain.model.notification;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.notification.mapper.NotificationInputMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.mapper.PlayerInputMapper;

public class NotificationProxy extends DomainObjectProxy<Long, Notification> implements INotification {
	
	public NotificationProxy(long id) {
		super(id);
	}

	@Override
	public IPlayer getRecipient() {
		return getInnerObject().getRecipient();
	}

	@Override
	public void setRecipient(IPlayer recipient) {
		getInnerObject().setRecipient(recipient);
	}

	@Override
	public boolean isSeen() {
		return getInnerObject().isSeen();
	}

	@Override
	public void setSeen(boolean seen) {
		getInnerObject().setSeen(seen);
	}

	@Override
	protected Notification getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return NotificationInputMapper.find(id);
		} catch (Exception e) {
			// It better be here! That null won't go over well!
			e.printStackTrace();
			return null;
		}
	}
}
