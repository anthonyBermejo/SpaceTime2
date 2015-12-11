package org.soen387.app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.domain.command.CommandException;
import org.soen387.domain.command.LogoutCommand;

public class LogoutDispatcher extends Dispatcher {

	@Override
	public void execute() throws ServletException, IOException {
		try {
			new LogoutCommand(myHelper).execute();
			myRequest.getSession().invalidate();
			forward("/WEB-INF/jsp/xml/success.jsp");
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			forward("/WEB-INF/jsp/xml/failure.jsp");
		}

	}

}
