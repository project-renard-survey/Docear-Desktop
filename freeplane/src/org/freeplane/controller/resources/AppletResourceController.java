/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is created by Dimitry Polivaev in 2008.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.controller.resources;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JApplet;

/**
 * @author Dimitry Polivaev
 */
public class AppletResourceController extends ResourceController {
	final private JApplet applet;
	public Properties defaultProps;
	public URL defaultPropsURL;
	public Properties userProps;

	/**
	 * @param controller
	 */
	public AppletResourceController(final JApplet applet) {
		super();
		this.applet = applet;
		// TODO Auto-generated constructor stub
		defaultPropsURL = getResource("freemind.properties");
		try {
			defaultProps = new Properties();
			final InputStream in = defaultPropsURL.openStream();
			defaultProps.load(in);
			in.close();
			userProps = defaultProps;
		}
		catch (final Exception ex) {
			System.err.println("Could not load properties.");
		}
		final Enumeration allKeys = userProps.propertyNames();
		while (allKeys.hasMoreElements()) {
			final String key = (String) allKeys.nextElement();
			setPropertyByParameter(key);
		}
	}

	@Override
	public String getFreemindUserDirectory() {
		return null;
	}

	@Override
	public int getIntProperty(final String key, final int defaultValue) {
		try {
			return Integer.parseInt(getProperty(key));
		}
		catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	};

	@Override
	public Properties getProperties() {
		return userProps;
	}

	@Override
	public String getProperty(final String key) {
		return userProps.getProperty(key);
	}

	@Override
	public URL getResource(final String name) {
		final URL resourceURL = this.getClass().getResource("/" + name);
		if (resourceURL == null || !resourceURL.getProtocol().equals("jar")
		        && System.getProperty("freemind.debug", null) == null) {
			return null;
		}
		return resourceURL;
	}

	@Override
	public void saveProperties() {
	}

	@Override
	public void setDefaultProperty(final String key, final String value) {
		userProps.setProperty(key, value);
	}

	@Override
	public void setProperty(final String key, final String value) {
	}

	public void setPropertyByParameter(final String key) {
		final String val = applet.getParameter(key);
		if (val != null && val != "") {
			userProps.setProperty(key, val);
		}
	}
}
