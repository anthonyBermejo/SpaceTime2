package org.soen387.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.role.IRole;
import org.dsrg.soenea.domain.role.impl.GuestRole;
import org.dsrg.soenea.domain.user.User;
import org.dsrg.soenea.domain.user.mapper.UserOutputMapper;
import org.dsrg.soenea.service.tdg.UserTDG;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerOutputMapper;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.role.RegisteredRole;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/RegisterPlayer")
public class RegisterPlayer extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public RegisterPlayer() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			
			//Do Stuff
			/*
		current.get().put("FIRSTNAME_PARAM", "firstName");
		current.get().put("LASTNAME_PARAM", "lastName");
		current.get().put("EMAIL_PARAM", "email");
		current.get().put("USERNAME_PARAM", "username");
		current.get().put("PASSWORD_PARAM", "password");
			 */
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String first = request.getParameter("firstName");
			String last = request.getParameter("lastName");
			String email = request.getParameter("email");
			
			
			List<IRole> roles = new ArrayList<IRole>();
			roles.add(new GuestRole());
			roles.add(new RegisteredRole());
			User u = new User(UserTDG.getMaxId(), 1, username, roles);
			u.setPassword(password);
			Player p = new Player(PlayerTDG.getMaxId(), 1, first, last, email, u);
			
			new UserOutputMapper().insert(u);
			PlayerOutputMapper.insertStatic(p);
			
			request.getSession(true).invalidate();
			request.getSession(true).setAttribute("playerid", p.getId());
			request.setAttribute("player", p);
			//Commit
			DbRegistry.getDbConnection().commit();
			
			//Forward to a jsp, make sure you fill it in properly
			request.getRequestDispatcher("/WEB-INF/jsp/xml/player.jsp").forward(request, response);
		} catch (Exception e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}
