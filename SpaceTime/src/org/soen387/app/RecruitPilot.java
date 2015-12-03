package org.soen387.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.mapper.PilotOutputMapper;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.Player;
import org.soen387.ser.name.NameFactory;

/**
 * Servlet implementation class RecruitPilot
 */
@WebServlet("/RecruitPilot")
public class RecruitPilot extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public RecruitPilot() {
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
				Pilot pilot = new Pilot(PilotTDG.getMaxId(), 1, NameFactory.getName(), p);
				PilotOutputMapper.insertStatic(pilot);
				request.setAttribute("pilot", pilot);
			} else {
				throw new Exception("Must be logged in to list pilots!");
			}
			//Commit
			DbRegistry.getDbConnection().commit();
			
			//Forward to a jsp, make sure you fill it in properly
			request.getRequestDispatcher("/WEB-INF/jsp/xml/pilot.jsp").forward(request, response);
		} catch (Exception e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}
