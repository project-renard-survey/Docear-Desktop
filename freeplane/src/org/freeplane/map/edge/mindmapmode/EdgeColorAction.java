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
package org.freeplane.map.edge.mindmapmode;

import java.awt.Color;
import java.awt.event.ActionEvent;

import org.freeplane.controller.views.ColorTracker;
import org.freeplane.map.tree.NodeModel;
import org.freeplane.modes.mindmapmode.MModeController;

import deprecated.freemind.modes.mindmapmode.actions.undo.MultipleNodeAction;

class EdgeColorAction extends MultipleNodeAction {
	private Color actionColor;

	public EdgeColorAction(final MModeController controller) {
		super(controller, "edge_color");
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final MModeController controller = getMModeController();
		final NodeModel model = controller.getSelectedNode();
		final Color edgeColor = model.getModeController().getEdgeController()
		    .getColor(model);
		actionColor = ColorTracker.showCommonJColorChooserDialog(controller
		    .getMapView().getSelected(), controller
		    .getText("choose_edge_color"), edgeColor);
		if (actionColor == null) {
			return;
		}
		super.actionPerformed(e);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * freemind.modes.mindmapmode.actions.MultipleNodeAction#actionPerformed
	 * (freemind.modes.NodeModel)
	 */
	@Override
	protected void actionPerformed(final ActionEvent e, final NodeModel node) {
		final MEdgeController edgeController = (MEdgeController) node
		    .getModeController().getEdgeController();
		edgeController.setColor(node, actionColor);
	}
}
