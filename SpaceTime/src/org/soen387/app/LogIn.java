package org.soen387.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.user.IUser;
import org.dsrg.soenea.domain.user.mapper.UserInputMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.mapper.PlayerInputMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/LogIn")
public class LogIn extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public LogIn() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			//login
			try {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				IUser u = UserInputMapper.find(username, password);
				IPlayer p = PlayerInputMapper.find(u);
				request.getSession(true).invalidate();
				request.getSession(true).setAttribute("playerid", p.getId());
				
				request.getRequestDispatcher("/WEB-INF/jsp/xml/success.jsp").forward(request, response);
			} catch (DomainObjectNotFoundException e1) {
				forwardError(request, response, "No User for that username and password!");
			} 
			
		} catch (Exception e) {

			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}
