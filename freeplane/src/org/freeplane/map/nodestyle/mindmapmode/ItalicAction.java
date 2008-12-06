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
package org.freeplane.map.nodestyle.mindmapmode;

import java.awt.event.ActionEvent;

import org.freeplane.map.tree.NodeModel;
import org.freeplane.modes.mindmapmode.MModeController;
import org.freeplane.ui.SelectableAction;

import deprecated.freemind.modes.mindmapmode.actions.undo.MultipleNodeAction;

@SelectableAction
class ItalicAction extends MultipleNodeAction {
	private boolean italic;

	/**
	 */
	public ItalicAction(final MModeController modeController) {
		super(modeController, "italic", "images/Italic16.gif");
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		italic = !isItalic();
		super.actionPerformed(e);
	}

	@Override
	protected void actionPerformed(final ActionEvent e, final NodeModel selected) {
		((MNodeStyleController) super.getMModeController()
		    .getNodeStyleController()).setItalic(selected, italic);
	}

	boolean isItalic() {
		final NodeModel node = super.getMModeController().getSelectedNode();
		return super.getMModeController().getNodeStyleController().isItalic(
		    node);
	}

	public boolean isSelected() {
		return isItalic();
	}
}
