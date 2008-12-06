/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
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
package org.freeplane.service.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.freeplane.controller.FreeMindAction;
import org.freeplane.controller.Freeplane;
import org.freeplane.map.tree.MapModel;
import org.freeplane.map.tree.NodeModel;
import org.freeplane.service.filter.condition.ICondition;
import org.freeplane.service.filter.condition.NoFilteringCondition;
import org.freeplane.service.filter.condition.SelectedViewCondition;
import org.freeplane.ui.FreeMindToolBar;

class FilterToolbar extends FreeMindToolBar {
	private class EditFilterAction extends FreeMindAction {
		EditFilterAction() {
			super("filter_edit_description", "images/Btn_edit.gif");
		}

		public void actionPerformed(final ActionEvent arg0) {
			final Object selectedItem = getFilterConditionModel()
			    .getSelectedItem();
			if (selectedItem != null) {
				getFilterDialog().setSelectedItem(selectedItem);
			}
			getFilterDialog().show();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		private FilterComposerDialog getFilterDialog() {
			if (filterDialog == null) {
				filterDialog = new FilterComposerDialog(FilterToolbar.this);
				getFilterDialog().setLocationRelativeTo(FilterToolbar.this);
			}
			return filterDialog;
		}
	}

	private class FilterChangeListener extends AbstractAction implements
	        ItemListener, PropertyChangeListener {
		/*
		 * (non-Javadoc)
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public FilterChangeListener() {
		}

		public void actionPerformed(final ActionEvent arg0) {
			resetFilter();
			setMapFilter();
			refreshMap();
			DefaultFilter.selectVisibleNode(Freeplane.getController()
			    .getMapView());
		}

		private void filterChanged() {
			resetFilter();
			setMapFilter();
			final MapModel map = fc.getMap();
			if (map != null) {
				activeFilter.applyFilter();
				refreshMap();
				DefaultFilter.selectVisibleNode(Freeplane.getController()
				    .getMapView());
			}
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent
		 * )
		 */
		public void itemStateChanged(final ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				filterChanged();
			}
		}

		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("model")) {
				addStandardConditions();
				filterChanged();
			}
		}
	}

	private class UnfoldAncestorsAction extends AbstractAction {
		/**
		 *
		 */
		UnfoldAncestorsAction() {
			super(null, new ImageIcon(Freeplane.getController()
			    .getResourceController().getResource("images/unfold.png")));
		}

		public void actionPerformed(final ActionEvent e) {
			if (getSelectedCondition() != null) {
				unfoldAncestors(Freeplane.getController().getModel()
				    .getRootNode());
			}
		}

		private void setFolded(final NodeModel node, final boolean state) {
			if (node.getModeController().getMapController().hasChildren(node)
			        && (node.getModeController().getMapController().isFolded(
			            node) != state)) {
				Freeplane.getController().getModeController()
				    .getMapController().setFolded(node, state);
			}
		}

		private void unfoldAncestors(final NodeModel parent) {
			for (final Iterator i = parent.getModeController()
			    .getMapController().childrenUnfolded(parent); i.hasNext();) {
				final NodeModel node = (NodeModel) i.next();
				if (showDescendants.isSelected()
				        || node.getFilterInfo().isAncestor()) {
					setFolded(node, false);
					unfoldAncestors(node);
				}
			}
		}
	}

	private IFilter activeFilter;
	final private JComboBox activeFilterConditionComboBox;
	final private JButton btnEdit;
	final private JButton btnUnfoldAncestors;
	final private FilterController fc;
	final private FilterChangeListener filterChangeListener;
	private FilterComposerDialog filterDialog = null;
	final private String pathToFilterFile;
	final private JCheckBox showAncestors;
	final private JCheckBox showDescendants;

	FilterToolbar() {
		super();
		fc = Freeplane.getController().getFilterController();
		setVisible(false);
		setFocusable(false);
		filterChangeListener = new FilterChangeListener();
		add(new JLabel(Freeplane.getController().getResourceController()
		    .getResourceString("filter_toolbar")
		        + " "));
		activeFilter = null;
		activeFilterConditionComboBox = new JComboBox() {
			@Override
			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		activeFilterConditionComboBox.setFocusable(false);
		pathToFilterFile = Freeplane.getController().getResourceController()
		    .getFreemindUserDirectory()
		        + File.separator
		        + "auto."
		        + FilterController.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT;
		btnEdit = add(new EditFilterAction());
		add(btnEdit);
		btnUnfoldAncestors = add(new UnfoldAncestorsAction());
		btnUnfoldAncestors.setToolTipText(Freeplane.getController()
		    .getResourceController().getResourceString(
		        "filter_unfold_ancestors"));
		add(btnUnfoldAncestors);
		showAncestors = new JCheckBox(
		    Freeplane.getController().getResourceController()
		        .getResourceString("filter_show_ancestors"), true);
		add(showAncestors);
		showAncestors.getModel().addActionListener(filterChangeListener);
		showDescendants = new JCheckBox(Freeplane.getController()
		    .getResourceController().getResourceString(
		        "filter_show_descendants"), false);
		add(showDescendants);
		showDescendants.getModel().addActionListener(filterChangeListener);
	}

	void addStandardConditions() {
		final DefaultComboBoxModel filterConditionModel = fc
		    .getFilterConditionModel();
		final ICondition noFiltering = NoFilteringCondition.createCondition();
		filterConditionModel.insertElementAt(noFiltering, 0);
		filterConditionModel.insertElementAt(SelectedViewCondition
		    .CreateCondition(), 1);
		if (filterConditionModel.getSelectedItem() == null) {
			filterConditionModel.setSelectedItem(noFiltering);
		}
	}

	ComboBoxModel getFilterConditionModel() {
		return activeFilterConditionComboBox.getModel();
	}

	/**
	 */
	FilterComposerDialog getFilterDialog() {
		return filterDialog;
	}

	private ICondition getSelectedCondition() {
		return (ICondition) activeFilterConditionComboBox.getSelectedItem();
	}

	void initConditions() {
		try {
			fc.loadConditions(fc.getFilterConditionModel(), pathToFilterFile);
		}
		catch (final Exception e) {
		}
		addStandardConditions();
		activeFilterConditionComboBox.setSelectedIndex(0);
		activeFilterConditionComboBox.setRenderer(fc.getConditionRenderer());
		add(activeFilterConditionComboBox);
		add(Box.createHorizontalGlue());
		activeFilterConditionComboBox.addItemListener(filterChangeListener);
		activeFilterConditionComboBox
		    .addPropertyChangeListener(filterChangeListener);
	}

	/**
	 */
	void mapChanged(final MapModel newMap) {
		if (!isVisible()) {
			return;
		}
		IFilter filter;
		if (newMap != null) {
			filter = newMap.getFilter();
			if (filter != activeFilter) {
				activeFilter = filter;
				activeFilterConditionComboBox.setSelectedItem(filter
				    .getCondition());
				showAncestors.setSelected(filter.areAncestorsShown());
				showDescendants.setSelected(filter.areDescendantsShown());
			}
		}
		else {
			filter = null;
			activeFilterConditionComboBox.setSelectedIndex(0);
		}
	}

	private void refreshMap() {
		fc.refreshMap();
	}

	/**
	 *
	 */
	public void resetFilter() {
		activeFilter = null;
	}

	void saveConditions() {
		try {
			fc.saveConditions(fc.getFilterConditionModel(), pathToFilterFile);
		}
		catch (final Exception e) {
		}
	}

	void setFilterConditionModel(final ComboBoxModel filterConditionModel) {
		activeFilterConditionComboBox.setModel(filterConditionModel);
	}

	void setMapFilter() {
		if (activeFilter == null) {
			activeFilter = new DefaultFilter(getSelectedCondition(),
			    showAncestors.getModel().isSelected(), showDescendants
			        .getModel().isSelected());
		}
		final MapModel map = fc.getMap();
		if (map != null) {
			map.setFilter(activeFilter);
		}
	}
}
