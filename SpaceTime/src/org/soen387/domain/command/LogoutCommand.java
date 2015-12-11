package org.soen387.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.domain.user.GuestUser;
import org.dsrg.soenea.uow.MissingMappingException;

public class LogoutCommand extends ValidatorCommand {

	public LogoutCommand(Helper helper) {
		super(helper);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 * Firstly, the fields are public to make the reflection stuff easier! I'm sure there's a better way, but I haven't gone back to fix it in years.
	 * 
	 * We want to pull from the capture group in PermaLink.xml. The attribute name is the key for the source, but since it's the name of the command field, 
	 * ValidatorCommand assumes. SetInRequestAttribute is self-explanatory.
	 * 
	 */
	//@Source(sources={PermalinkSource.class})
	//@IdentityBasedProducer(mapper = Santas.class)
	//@SetInRequestAttribute
	
	@Override
	public void process() throws CommandException {
		try {
			helper.setSessionAttribute("CurrentUser", new GuestUser());
				
		} catch (MissingMappingException e1) {
			throw new CommandException();
		}
	}
}
