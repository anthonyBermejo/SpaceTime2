package org.soen387.app.dispatcher;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.soen387.domain.command.LoginCommand;
import org.soen387.domain.command.RegisterCommand;

public class RegisterDispatcher extends Dispatcher {

	@Override
	public void execute() throws ServletException, IOException {
		try {
			new RegisterCommand(myHelper).execute();
			UoW.getCurrent().commit();
			forward("/WEB-INF/jsp/xml/register.jsp");
		} catch (CommandException | InstantiationException | IllegalAccessException | MapperException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			forward("/WEB-INF/jsp/xml/failure.jsp");
		}
	}

}
