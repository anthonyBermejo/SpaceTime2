package org.soen387.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.command.validator.source.IdentityBasedProducer;
import org.dsrg.soenea.domain.command.validator.source.Source;
import org.dsrg.soenea.domain.command.validator.source.impl.PermalinkSource;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.user.IUser;
import org.dsrg.soenea.domain.user.User;
import org.dsrg.soenea.domain.user.mapper.UserInputMapper;
import org.dsrg.soenea.uow.MissingMappingException;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerInputMapper;

public class LoginCommand extends ValidatorCommand {

	public LoginCommand(Helper helper) {
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
	public Player p;
	public User u;
	
	@Override
	public void process() throws CommandException {
		try {
			String username = (String) helper.getString("username");
			String password = (String) helper.getString("password");
			User u = UserInputMapper.find(username, password);
			Player p = PlayerInputMapper.find(u);
			
			helper.setRequestAttribute("player", p);
			helper.setRequestAttribute("user", u);
				
		} catch (MissingMappingException | SQLException | MapperException e1) {
			throw new CommandException();
		}
	}
}
