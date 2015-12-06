package org.soen387.domain.model.match.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.match.GameStatus;
import org.soen387.domain.model.match.IMatch;
import org.soen387.domain.model.match.Match;
import org.soen387.domain.model.match.MatchFactory;
import org.soen387.domain.model.match.tdg.MatchFinder;
import org.soen387.domain.model.team.TeamProxy;

public class MatchInputMapper {
	public static Match find(long id) throws SQLException, MissingMappingException, MapperException {

		if(IdentityMap.has(id, Match.class)) return IdentityMap.get(id, Match.class);

		ResultSet rs = MatchFinder.find(id);
		if(rs.next()) {
			Match c = buildMatch(rs);
			rs.close();
			UoW.getCurrent().registerClean(c);
			return c;
		}
		throw new DomainObjectNotFoundException("Could not create a Match with id \""+id+"\"");
	}

	public static List<IMatch> buildCollection(ResultSet rs)
		    throws SQLException, MissingMappingException, MapperException {
		    ArrayList<IMatch> l = new ArrayList<IMatch>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Match c = null;
		    	if(IdentityMap.has(id, Match.class)) {
		    		c = IdentityMap.get(id, Match.class);
		    	} else {
		    		c = buildMatch(rs);
		    		UoW.getCurrent().registerClean(c);
		    	}
		    	l.add(c);
		    }
		    return l;
		}

	public static List<IMatch> findAll() throws MapperException {
        try {
            ResultSet rs = MatchFinder.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Match buildMatch(ResultSet rs) throws SQLException, MissingMappingException, MapperException  {

		// TODO Auto-generated method stub
		return MatchFactory.createClean(rs.getLong("id"),
				rs.getLong("version"),
				GameStatus.valueOf(rs.getString("status")),
				new TeamProxy(rs.getLong("firstTeam")),
				new TeamProxy(rs.getLong("secondTeam"))
				);
	}
}
