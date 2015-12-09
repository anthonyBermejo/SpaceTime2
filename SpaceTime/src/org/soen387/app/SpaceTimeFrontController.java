package org.soen387.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.application.servlet.dispatcher.HttpServletHelper;
import org.dsrg.soenea.application.servlet.service.DispatcherFactory;
import org.dsrg.soenea.domain.helper.Helper;

/**
 * Servlet implementation class FrontController
 */
@WebServlet("/st/*")
public class SpaceTimeFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SpaceTimeFrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 Helper helper = new HttpServletHelper(request);
		 
		 String command = (String)helper.getAttribute("command");
		 System.out.println("COMMAND: " + command);
		 try {
			Dispatcher d = DispatcherFactory.getInstance(command);
			
			d.init(request, response);
			d.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 Helper helper = new HttpServletHelper(request);
		 
		 String command = (String)helper.getAttribute("command");
		 System.out.println("COMMAND: " + command);
		 try {
			Dispatcher d = DispatcherFactory.getInstance(command);
			
			d.init(request, response);
			d.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
