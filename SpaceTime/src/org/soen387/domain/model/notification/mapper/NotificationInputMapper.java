package org.soen387.domain.model.notification.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.notification.INotification;
import org.soen387.domain.model.notification.Notification;
import org.soen387.domain.model.notification.tdg.NotificationFinder;
import org.soen387.domain.model.player.PlayerProxy;

public class NotificationInputMapper {
	public static Notification find(long id) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {

		if(IdentityMap.has(id, Notification.class)) return IdentityMap.get(id, Notification.class);

		ResultSet rs = NotificationFinder.find(id);
		if(rs.next()) {
			Notification c = buildNotification(rs);
			rs.close();
			UoW.getCurrent().registerClean(c);
			return c;
		}
		throw new DomainObjectNotFoundException("Could not create a notification with id \""+id+"\"");
	}

	public static List<INotification> buildCollection(ResultSet rs)
		    throws SQLException, ObjectRemovedException, DomainObjectNotFoundException {
		    ArrayList<INotification> l = new ArrayList<INotification>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Notification c = null;
		    	if(IdentityMap.has(id, Notification.class)) {
		    		c = IdentityMap.get(id, Notification.class);
		    	} else {
		    		c = buildNotification(rs);
		    		UoW.getCurrent().registerClean(c);
		    	}
		    	l.add(c);
		    }
		    return l;
		}

	public static List<INotification> findAll() throws MapperException {
        try {
            ResultSet rs = NotificationFinder.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Notification buildNotification(ResultSet rs) throws SQLException  {

		// TODO Auto-generated method stub
		return new Notification(rs.getLong("id"),
				rs.getLong("version"),
				new PlayerProxy(rs.getLong("recipient")),
				rs.getBoolean("seen")
				);
	}
}
