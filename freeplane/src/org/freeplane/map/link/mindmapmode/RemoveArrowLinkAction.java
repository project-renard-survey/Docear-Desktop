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
package org.freeplane.map.link.mindmapmode;

import java.awt.event.ActionEvent;

import org.freeplane.map.link.ArrowLinkModel;
import org.freeplane.map.link.NodeLinks;
import org.freeplane.map.tree.NodeModel;
import org.freeplane.modes.ModeControllerAction;
import org.freeplane.undo.IUndoableActor;

class RemoveArrowLinkAction extends ModeControllerAction {
	private ArrowLinkModel mArrowLink;

	/**
	 * can be null can be null.
	 */
	public RemoveArrowLinkAction(final MLinkController linkController,
	                             final ArrowLinkModel arrowLink) {
		super(linkController.getModeController(), "remove_arrow_link",
		    "images/edittrash.png");
		setArrowLink(arrowLink);
	}

	public void actionPerformed(final ActionEvent e) {
		removeArrowLink(mArrowLink);
	}

	/**
	 * @return Returns the arrowLink.
	 */
	public ArrowLinkModel getArrowLink() {
		return mArrowLink;
	}

	public void removeArrowLink(final ArrowLinkModel arrowLink) {
		final IUndoableActor actor = new IUndoableActor() {
			public void act() {
				final NodeModel source = arrowLink.getSource();
				final NodeLinks nodeLinks = (NodeLinks) source
				    .getExtension(NodeLinks.class);
				nodeLinks.removeArrowlink(arrowLink);
				source.getModeController().getMapController().nodeChanged(
				    source);
			}

			public String getDescription() {
				return "removeArrowLink";
			}

			public void undo() {
				final NodeModel source = arrowLink.getSource();
				NodeLinks nodeLinks = (NodeLinks) source
				    .getExtension(NodeLinks.class);
				if (nodeLinks == null) {
					nodeLinks = new NodeLinks();
					source.addExtension(nodeLinks);
				}
				nodeLinks.addArrowlink(arrowLink);
				source.getModeController().getMapController().nodeChanged(
				    source);
			}
		};
		getMModeController().execute(actor);
	}

	/**
	 * The arrowLink to set.
	 */
	public void setArrowLink(final ArrowLinkModel arrowLink) {
		mArrowLink = arrowLink;
	}
}
