/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
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

/**
 * Utility Class for displaying local object names in GUI components.
 *
 * @author Dimitry Polivaev
 */
public class NamedObject {
	static public NamedObject literal(final String literal) {
		final NamedObject result = new NamedObject();
		result.object = literal;
		result.name = literal;
		return result;
	}

	private String name;
	private Object object;

	private NamedObject() {
	}

	public NamedObject(final Object object, final String name) {
		this.object = object;
		this.name = name;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof NamedObject) {
			final NamedObject ts = (NamedObject) o;
			return object.equals(ts.object);
		}
		return object.equals(o);
	}

	public Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return name;
	}
}
