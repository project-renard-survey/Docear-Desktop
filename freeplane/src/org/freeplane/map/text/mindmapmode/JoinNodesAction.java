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
package org.freeplane.map.text.mindmapmode;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.freeplane.main.HtmlTools;
import org.freeplane.map.tree.NodeModel;
import org.freeplane.map.tree.mindmapmode.MMapController;
import org.freeplane.map.tree.view.MapView;
import org.freeplane.modes.ModeControllerAction;
import org.freeplane.modes.mindmapmode.MModeController;

class JoinNodesAction extends ModeControllerAction {
	final static Pattern BODY_END = Pattern.compile("</body>",
	    Pattern.CASE_INSENSITIVE);
	final static Pattern BODY_START = Pattern.compile("<body>",
	    Pattern.CASE_INSENSITIVE);

	public JoinNodesAction(final MModeController modeController) {
		super(modeController, "join_nodes");
	}

	public void actionPerformed(final ActionEvent e) {
		final NodeModel selectedNode = getModeController().getMapView()
		    .getSelected().getModel();
		final ArrayList selectedNodes = getModeController().getMapView()
		    .getSelectedNodesSortedByY();
		joinNodes(selectedNode, selectedNodes);
	}

	private String addContent(String content, final boolean isHtml,
	                          String nodeContent, final boolean isHtmlNode) {
		if (isHtml) {
			final String start[] = JoinNodesAction.BODY_END.split(content, -2);
			content = start[0];
			if (!isHtmlNode) {
				final String end[] = JoinNodesAction.BODY_START.split(content,
				    2);
				nodeContent = end[0] + "<body><p>" + nodeContent + "</p>";
			}
		}
		if (isHtmlNode & !content.equals("")) {
			final String end[] = JoinNodesAction.BODY_START.split(nodeContent,
			    2);
			nodeContent = end[1];
			if (!isHtml) {
				content = end[0] + "<body><p>" + content + "</p>";
			}
		}
		if (!(isHtml || isHtmlNode || content.equals(""))) {
			content += " ";
		}
		content += nodeContent;
		return content;
	}

	public void joinNodes(final NodeModel selectedNode, final List selectedNodes) {
		String newContent = "";
		final MapView mapView = getModeController().getMapView();
		for (final Iterator it = selectedNodes.iterator(); it.hasNext();) {
			final NodeModel node = (NodeModel) it.next();
			if (node.getModeController().getMapController().hasChildren(node)) {
				JOptionPane.showMessageDialog(mapView, getModeController()
				    .getText("cannot_join_nodes_with_children"), "FreeMind",
				    JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		boolean isHtml = false;
		for (final Iterator it = selectedNodes.iterator(); it.hasNext();) {
			final NodeModel node = (NodeModel) it.next();
			final String nodeContent = node.toString();
			final boolean isHtmlNode = HtmlTools.isHtmlNode(nodeContent);
			newContent = addContent(newContent, isHtml, nodeContent, isHtmlNode);
			if (node != selectedNode) {
				((MMapController) getModeController().getMapController())
				    .deleteNode(node);
			}
			isHtml = isHtml || isHtmlNode;
		}
		mapView.selectAsTheOnlyOneSelected(mapView.getNodeView(selectedNode));
		((MTextController) getMModeController().getTextController())
		    .setNodeText(selectedNode, newContent);
	}
}
