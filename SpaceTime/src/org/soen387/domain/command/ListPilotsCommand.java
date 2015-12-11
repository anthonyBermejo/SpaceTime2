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
import org.dsrg.soenea.uow.MissingMappingException;
import org.soen387.domain.model.pilot.IPilot;
import org.soen387.domain.model.pilot.mapper.PilotInputMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.mapper.PlayerInputMapper;

public class ListPilotsCommand extends ValidatorCommand {

	public ListPilotsCommand(Helper helper) {
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
	public List<IPilot> pilots;
	
	@Override
	public void process() throws CommandException {
		try {
			//int page = 
			//int rows = 
			
			//System.out.println("PAGE: " + page);
			//System.out.println("ROWS: " + rows);
			
			List<IPilot> pilots = PilotInputMapper.findAll();
			helper.setRequestAttribute("pilots", pilots);
				
		} catch (MissingMappingException | MapperException e1) {
			throw new CommandException();
		}
	}
}
