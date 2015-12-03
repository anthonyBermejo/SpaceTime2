package org.soen387.test;

import java.sql.SQLException;

import org.dsrg.soenea.service.tdg.UserTDG;
import org.soen387.app.AbstractPageController;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;
import org.soen387.domain.model.notification.tdg.NotificationTDG;
import org.soen387.domain.model.pilot.tdg.PilotTDG;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.team.tdg.TeamMembershipTDG;
import org.soen387.domain.model.team.tdg.TeamTDG;

public class Setup {

	public static void main(String[] args) throws InterruptedException {
		AbstractPageController.setupDb();
		try {
			PlayerTDG.createTable();
			UserTDG.createTable();
			UserTDG.createUserRoleTable();
			TeamTDG.createTable();
			PilotTDG.createTable();
			TeamMembershipTDG.createTable();
			ChallengeTDG.createTable();
			NotificationTDG.createTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
