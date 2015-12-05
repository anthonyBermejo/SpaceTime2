package org.soen387.domain.model.notification;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.notification.tdg.NotificationTDG;
import org.soen387.domain.model.player.IPlayer;

public class NotificationFactory {
	public static Notification createNew(IPlayer recipient, boolean seen) throws SQLException, MissingMappingException, MapperException {
		Notification result = new Notification(NotificationTDG.getMaxId(), 01, recipient, seen);
		UoW.getCurrent().registerNew(result);
		return result;
	}
	
	public static Notification createClean(long id, long version, IPlayer recipient, boolean seen) throws SQLException, MissingMappingException, MapperException {
		Notification result = new Notification(id, version, recipient, seen);
		UoW.getCurrent().registerClean(result);
		return result;
	}
}
