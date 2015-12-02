package org.soen387.domain.model.role;

import org.dsrg.soenea.domain.role.IRole;
import org.dsrg.soenea.domain.role.Role;

public class RegisteredRole extends Role implements IRole {

	public RegisteredRole() {
		super(2l, 1, "Registered");
	}

}
