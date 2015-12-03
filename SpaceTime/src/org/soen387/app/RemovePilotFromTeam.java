package org.soen387.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.mapper.PilotInputMapper;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.mapper.TeamInputMapper;
import org.soen387.domain.model.team.mapper.TeamOutputMapper;

/**
 * Servlet implementation class RemovePilotFromTeam
 */
@WebServlet("/RemovePilotFromTeam")
public class RemovePilotFromTeam extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public RemovePilotFromTeam() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			
			//Do Stuff
			Player p = getCurrentPlayer(request);
			if(p != null) {
			
				Team t = TeamInputMapper.find(Long.parseLong(request.getParameter("team")));
				t.setVersion(Integer.parseInt(request.getParameter("version")));
				Pilot pilot = PilotInputMapper.find(Long.parseLong(request.getParameter("pilot")));
				t.getMembers().remove(pilot);
				TeamOutputMapper.updateStatic(t);
				request.setAttribute("team", t);
			} else {
				throw new Exception("Must be logged in to list pilots!");
			}
			//Commit
			DbRegistry.getDbConnection().commit();
			
			//Forward to a jsp, make sure you fill it in properly
			request.getRequestDispatcher("/WEB-INF/jsp/xml/team.jsp").forward(request, response);
		} catch (Exception e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}
