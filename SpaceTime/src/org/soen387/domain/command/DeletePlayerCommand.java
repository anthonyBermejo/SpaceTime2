package org.soen387.domain.command;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.command.validator.source.IdentityBasedProducer;
import org.dsrg.soenea.domain.command.validator.source.Source;
import org.dsrg.soenea.domain.command.validator.source.impl.PermalinkSource;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.domain.user.User;
import org.dsrg.soenea.uow.MissingMappingException;
import org.soen387.domain.model.pilot.Pilot;
import org.soen387.domain.model.pilot.mapper.PilotOutputMapper;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerInputMapper;
import org.soen387.domain.model.player.mapper.PlayerOutputMapper;
import org.soen387.ser.name.NameFactory;

public class DeletePlayerCommand extends ValidatorCommand {

	public DeletePlayerCommand(Helper helper) {
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
	@SetInRequestAttribute
	public Pilot pilot;
	
	@Override
	public void process() throws CommandException {
		try {	
			//Do Stuff
			User u = (User)helper.getSessionAttribute("CurrentUser");
			Player p = PlayerInputMapper.find(u);
			
			if(p != null) {
				PlayerOutputMapper.deleteStatic(p);
			} else {
				throw new Exception("Must be logged in to list pilots!");
			}
				
		} catch (Exception e1) {
			throw new CommandException(e1.getMessage());
		}
	}
}
