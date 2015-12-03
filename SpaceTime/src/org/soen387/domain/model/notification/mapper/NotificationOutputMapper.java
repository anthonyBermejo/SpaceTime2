package org.soen387.domain.model.notification.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.notification.INotification;
import org.soen387.domain.model.notification.Notification;
import org.soen387.domain.model.notification.tdg.NotificationTDG;

public class NotificationOutputMapper extends GenericOutputMapper<Long, Notification> {
	public static void insertStatic(INotification p) throws SQLException {
		NotificationTDG.insert(p.getId(), p.getVersion(), p.getRecipient().getId(), p.isSeen());
	}

	public static void updateStatic(INotification p) throws SQLException, LostUpdateException {
		int count = NotificationTDG.update(p.getId(), p.getVersion(), p.getRecipient().getId(), p.isSeen());
		if(count==0) throw new LostUpdateException("Lost Update editing notification with id " + p.getId());
		p.setVersion(p.getVersion()+1);
	}
	
	public static void deleteStatic(INotification p) throws SQLException, LostUpdateException {
		int count = NotificationTDG.delete(p.getId(), p.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting notification with id " + p.getId());
		//
		// What's the process for deleting a Pilot... do we need to delete users and games?
		// More on that when we discuss referential integrity.
		//
	}
	
	@Override
	public void insert(Notification d) throws MapperException {
		try {
			insertStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Notification d) throws MapperException {
		try {
			updateStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Notification d) throws MapperException {
		try {
			deleteStatic(d);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
}
