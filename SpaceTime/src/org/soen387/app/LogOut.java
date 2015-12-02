package org.soen387.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/LogOut")
public class LogOut extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public LogOut() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			//Logout
			request.getSession(true).invalidate();
			request.getRequestDispatcher("/WEB-INF/jsp/xml/success.jsp").forward(request, response);
		} finally {
			teardownRequest();	
		}

	}


}
