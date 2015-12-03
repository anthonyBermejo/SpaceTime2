package org.soen387.app;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.mapper.TeamInputMapper;
import org.soen387.domain.model.team.mapper.TeamOutputMapper;
import org.soen387.domain.model.team.tdg.TeamTDG;

/**
 * Servlet implementation class AddTeam
 */
@WebServlet("/AddTeam")
public class AddTeam extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public AddTeam() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			
			// Do Stuff
			/*
			 * current.get().put("TEAM_NAME", "teamname");
			 */
			Player p = getCurrentPlayer(request);
			if (p != null) {

				String teamname = request.getParameter("teamname");
				Team t = new Team(TeamTDG.getMaxId(), 1, teamname,
						getCurrentPlayer(request), new LinkedList<IPilot>());

				TeamOutputMapper.insertStatic(t);

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
