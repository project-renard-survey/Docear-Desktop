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
package org.freeplane.io.url;

import java.awt.event.ActionEvent;

import org.freeplane.controller.Freeplane;
import org.freeplane.modes.ModeController;
import org.freeplane.modes.ModeControllerAction;

class SaveAction extends ModeControllerAction {
	/**
	 *
	 */
	final private ModeController modeController;

	public SaveAction(final ModeController modeController) {
		super(modeController, "save", "images/filesave.png");
		this.modeController = modeController;
	}

	public void actionPerformed(final ActionEvent e) {
		final boolean success = getMModeController().save();
		if (success) {
			Freeplane.getController().getViewController().out(
			    modeController.getText("saved"));
		}
		else {
			Freeplane.getController().errorMessage("Saving failed.");
		}
		Freeplane.getController().getViewController().setTitle();
	}
}
