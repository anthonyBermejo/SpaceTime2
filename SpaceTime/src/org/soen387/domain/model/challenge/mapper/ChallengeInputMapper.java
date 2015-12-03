package org.soen387.domain.model.challenge.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.ChallengeStatus;
import org.soen387.domain.model.challenge.IChallenge;
import org.soen387.domain.model.challenge.tdg.ChallengeFinder;
import org.soen387.domain.model.player.PlayerProxy;

public class ChallengeInputMapper {
	public static Challenge find(long id) throws SQLException, DomainObjectNotFoundException, ObjectRemovedException {

		if(IdentityMap.has(id, Challenge.class)) return IdentityMap.get(id, Challenge.class);

		ResultSet rs = ChallengeFinder.find(id);
		if(rs.next()) {
			Challenge c = buildChallenge(rs);
			rs.close();
			UoW.getCurrent().registerClean(c);
			return c;
		}
		throw new DomainObjectNotFoundException("Could not create a Pilot with id \""+id+"\"");
	}

	public static List<IChallenge> buildCollection(ResultSet rs)
		    throws SQLException, ObjectRemovedException, DomainObjectNotFoundException {
		    ArrayList<IChallenge> l = new ArrayList<IChallenge>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Challenge c = null;
		    	if(IdentityMap.has(id, Challenge.class)) {
		    		c = IdentityMap.get(id, Challenge.class);
		    	} else {
		    		c = buildChallenge(rs);
		    		UoW.getCurrent().registerClean(c);
		    	}
		    	l.add(c);
		    }
		    return l;
		}

	public static List<IChallenge> findAll() throws MapperException {
        try {
            ResultSet rs = ChallengeFinder.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Challenge buildChallenge(ResultSet rs) throws SQLException  {

		// TODO Auto-generated method stub
		return new Challenge(rs.getLong("id"),
				rs.getInt("version"),
				new PlayerProxy(rs.getLong("challenger")),
				new PlayerProxy(rs.getLong("challengee")),
				ChallengeStatus.valueOf(rs.getString("status"))
				);
	}
}
