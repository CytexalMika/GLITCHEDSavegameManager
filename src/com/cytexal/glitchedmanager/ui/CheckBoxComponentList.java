package com.cytexal.glitchedmanager.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

public abstract class CheckBoxComponentList<T extends JComponent> extends JScrollPane implements SavefileEditorTab {
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private Map<String, ListEntry> entryMap;
	private JPanel container;

	public CheckBoxComponentList(String name) {
		setName(name);
		container = new JPanel();
		container.setLayout(new GridLayout(0, 2));
		setViewportView(container);
		entryMap = new HashMap<String, CheckBoxComponentList<T>.ListEntry>();
	}

	public class ListEntry {
		private JCheckBox checkbox;
		private T otherComponent;
		private String name;

		public ListEntry(String name, T otherComp) {
			this(name, otherComp, false);
		}

		public ListEntry(String name, T otherComp, boolean checked) {
			this.name = name;
			this.checkbox = new JCheckBox(name);
			this.checkbox.setSelected(checked);
			this.otherComponent = otherComp;
		}

		public String getName() {
			return name;
		}

		public JCheckBox getCheckbox() {
			return checkbox;
		}

		public T getOtherComponent() {
			return otherComponent;
		}
	}

	protected void clear() {
		container.removeAll();
		entryMap.clear();
	}

	void addEntry(ListEntry entry) {
		entryMap.put(entry.name, entry);
		entry.checkbox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		container.add(entry.checkbox);
		entry.otherComponent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		container.add(entry.otherComponent);
	}

	public File getJSONFile() {
		return new File("json/" + StringUtils.deleteWhitespace(getName()) + ".json");
	}

	public ListEntry getEntry(String name) {
		return entryMap.get(name);
	}

	@Override
	public void save() {
		for (ListEntry e : entryMap.values())
			saveEntry(e);
	}

	public abstract void saveEntry(ListEntry entry);
}