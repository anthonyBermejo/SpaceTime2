package org.soen387.domain.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.command.validator.source.IdentityBasedProducer;
import org.dsrg.soenea.domain.command.validator.source.Source;
import org.dsrg.soenea.domain.command.validator.source.impl.PermalinkSource;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.domain.role.IRole;
import org.dsrg.soenea.domain.role.impl.GuestRole;
import org.dsrg.soenea.domain.user.User;
import org.dsrg.soenea.domain.user.mapper.UserOutputMapper;
import org.dsrg.soenea.service.tdg.UserTDG;
import org.dsrg.soenea.uow.MissingMappingException;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.PlayerFactory;
import org.soen387.domain.model.player.mapper.PlayerOutputMapper;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.role.RegisteredRole;

public class RegisterCommand extends ValidatorCommand{

	public RegisterCommand(Helper helper) {
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
	@Source(sources={PermalinkSource.class})
	@IdentityBasedProducer(mapper = Player.class)
	@SetInRequestAttribute
	public String register;
	
	@Override
	public void process() throws CommandException {
		
		String username = (String)helper.getAttribute("username");
		String password = (String)helper.getAttribute("password");
		String first = (String)helper.getAttribute("firstName");
		String last = (String)helper.getAttribute("lastName");
		String email = (String)helper.getAttribute("email");
		
		try {
		List<IRole> roles = new ArrayList<IRole>();
		roles.add(new GuestRole());
		roles.add(new RegisteredRole());
		User u = new User(UserTDG.getMaxId(), 1, username, roles);
		u.setPassword(password);
		PlayerFactory.createNew(first, last, email, u);
		
		new UserOutputMapper().insert(u);
		//PlayerOutputMapper.insertStatic(p);
		} catch (MapperException | MissingMappingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}