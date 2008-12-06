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
package org.freeplane.map.cloud.mindmapmode;

import java.awt.Color;

import org.freeplane.controller.Freeplane;
import org.freeplane.map.cloud.CloudController;
import org.freeplane.map.cloud.CloudModel;
import org.freeplane.map.tree.NodeModel;
import org.freeplane.modes.mindmapmode.MModeController;
import org.freeplane.undo.IUndoableActor;

/**
 * @author Dimitry Polivaev
 */
public class MCloudController extends CloudController {
	static private boolean actionsCreated = false;

	public MCloudController(final MModeController modeController) {
		super(modeController);
		if (!actionsCreated) {
			actionsCreated = true;
			Freeplane.getController().addAction("cloud",
			    new CloudAction(modeController));
			Freeplane.getController().addAction("cloudColor",
			    new CloudColorAction(modeController));
		}
	}

	public void setCloud(final NodeModel node, final boolean enable) {
		final CloudModel cloud = node.getCloud();
		if ((cloud != null) == enable) {
			return;
		}
		final Color color = cloud != null ? cloud.getColor() : null;
		final MModeController modeController = (MModeController) node
		    .getModeController();
		final IUndoableActor actor = new IUndoableActor() {
			public void act() {
				if (enable) {
					enable();
				}
				else {
					disable();
				}
			}

			private void disable() {
				node.setCloud(null);
				modeController.getMapController().nodeChanged(node);
			}

			private void enable() {
				final CloudModel cloud = new CloudModel();
				cloud.setColor(color);
				node.setCloud(cloud);
				modeController.getMapController().nodeChanged(node);
			}

			public String getDescription() {
				return "setCloud";
			}

			public void undo() {
				if (enable) {
					disable();
				}
				else {
					enable();
				}
			}
		};
		modeController.execute(actor);
	}

	public void setColor(final NodeModel node, final Color color) {
		setCloud(node, true);
		final MModeController modeController = (MModeController) node
		    .getModeController();
		final Color oldColor = node.getCloud().getColor();
		if (color.equals(oldColor)) {
			return;
		}
		final IUndoableActor actor = new IUndoableActor() {
			public void act() {
				node.getCloud().setColor(color);
				modeController.getMapController().nodeChanged(node);
			}

			public String getDescription() {
				return "setColor";
			}

			public void undo() {
				node.getCloud().setColor(oldColor);
				modeController.getMapController().nodeChanged(node);
			}
		};
		modeController.execute(actor);
	}
}
