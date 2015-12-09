package org.soen387.app;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

import org.dsrg.soenea.application.filter.PermalinkFilter;
import org.dsrg.soenea.application.servlet.DispatcherServlet;

@WebFilter (value="/st/*")
public class SpaceTimeFilter extends PermalinkFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		config.getServletContext().setAttribute(DispatcherServlet.BASE_URI_ATTR, config.getServletContext().getContextPath() + "/");	
	}
}
