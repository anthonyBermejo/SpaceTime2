package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.DispatcherServlet;
import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.application.servlet.dispatcher.HttpServletHelper;
import org.dsrg.soenea.application.servlet.service.DispatcherFactory;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.domain.user.GuestUser;
import org.dsrg.soenea.domain.user.IUser;
import org.dsrg.soenea.domain.user.User;
import org.dsrg.soenea.domain.user.mapper.UserInputMapper;
import org.dsrg.soenea.domain.user.mapper.UserOutputMapper;
import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.authorization.ApplicationAuthorizaton;
import org.dsrg.soenea.service.registry.Registry;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.mapper.ChallengeOutputMapper;
import org.soen387.domain.model.match.Match;
import org.soen387.domain.model.match.mapper.MatchOutputMapper;
import org.soen387.domain.model.notification.Notification;
import org.soen387.domain.model.notification.mapper.NotificationOutputMapper;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.mapper.PilotOutputMapper;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerOutputMapper;
import org.soen387.domain.model.team.Team;
import org.soen387.domain.model.team.mapper.TeamOutputMapper;

/**
 * Servlet implementation class FrontController
 */
@WebServlet("/st/*")
public class FrontController extends DispatcherServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FrontController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		prepareDbRegistry();
		setupUoW();
	}

	public static void setupUoW() {
		MapperFactory m = new MapperFactory();
		m.addMapping(User.class, UserOutputMapper.class);
		m.addMapping(Player.class, PlayerOutputMapper.class);
		m.addMapping(Pilot.class, PilotOutputMapper.class);
		m.addMapping(Team.class, TeamOutputMapper.class);
		m.addMapping(Challenge.class, ChallengeOutputMapper.class);
		m.addMapping(Notification.class, NotificationOutputMapper.class);
		m.addMapping(Match.class, MatchOutputMapper.class);
		UoW.initMapperFactory(m);
	}

	public static void prepareDbRegistry() {
		prepareDbRegistry("");
	}

	public static void prepareDbRegistry(String key) {
		MySQLConnectionFactory f = new MySQLConnectionFactory(null, null, null, null);
		try {
			f.defaultInitialization();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		DbRegistry.setConFactory(f);
		String tablePrefix;
		try {
			tablePrefix = Registry.getProperty("mySqlTablePrefix");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			tablePrefix = "";
		}
		if (tablePrefix == null) {
			tablePrefix = "";
		}
		DbRegistry.setTablePrefix(tablePrefix);
	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Helper helper = null;
		Dispatcher command = null;
		String commandName = null;
		IUser user = null;

		helper = new HttpServletHelper(request);
		commandName = (String) helper.getAttribute("command");

		if (commandName == null)
			commandName = "";

//		user = (IUser) helper.getSessionAttribute("CurrentUser");
//
//		if (user == null) {
//			user = new GuestUser();
//			request.getSession(true).setAttribute("CurrentUser", user);
//		}

		try {

//			if (!(user instanceof GuestUser)) {
//				user = UserInputMapper.find(user.getId());
//				request.getSession(true).setAttribute("CurrentUser", user);
//			}
//
//			if (!ApplicationAuthorizaton.hasAuthority(commandName, user.getRoles(),
//					ApplicationAuthorizaton.RequestMethod.valueOf(request.getMethod()))) {
//				throw new Exception("Access Denied to " + commandName + " for user " + user.getUsername());
//			}

			System.out.println("COMMAND: " + commandName);
			
			command = DispatcherFactory.getInstance(commandName);
			command.init(request, response);
			command.execute();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void preProcessRequest(HttpServletRequest request, HttpServletResponse response) {
		super.preProcessRequest(request, response);
		UoW.newCurrent();
		try {
			DbRegistry.getDbConnection().setAutoCommit(false);
			DbRegistry.getDbConnection().createStatement().execute("START TRANSACTION;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void postProcessRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			DbRegistry.getDbConnection().createStatement().execute("ROLLBACK;");
			DbRegistry.getDbConnection().close();
			DbRegistry.closeDbConnectionIfNeeded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThreadLocalTracker.purgeThreadLocal();
	}
}
