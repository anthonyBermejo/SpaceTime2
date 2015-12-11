package org.soen387.app.dispatcher;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.domain.command.CommandException;
import org.soen387.domain.command.ListPlayersCommand;

public class ListPlayersDispatcher extends Dispatcher {

	@Override
	public void execute() throws ServletException, IOException {
		try {
			new ListPlayersCommand(myHelper).execute();
			forward("/WEB-INF/jsp/xml/players.jsp");
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			forward("/WEB-INF/jsp/xml/failure.jsp");
		}

	}

}
