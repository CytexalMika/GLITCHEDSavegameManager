package com.cytexal.glitchedmanager.ui;

import javax.swing.*;


import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
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

public abstract class CheckBoxList extends JScrollPane implements SavefileEditorTab {
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private Map<String, JCheckBox> checkBoxMap;
	private JPanel container;

	public CheckBoxList(String name) {
		setName(name);
		container = new JPanel();
		container.setLayout(new GridLayout(0, 1));
		setViewportView(container);
		checkBoxMap = new HashMap<String, JCheckBox>();
	}

	protected void clear() {
		container.removeAll();
		checkBoxMap.clear();
	}
	
	void addCheckBox(String name) {
		addCheckBox(name, false);
	}

	void addCheckBox(String name, boolean selected) {
		JCheckBox checkbox = new JCheckBox(name, selected);
		checkbox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		container.add(checkbox);
		checkBoxMap.put(name, checkbox);
	}

	public File getJSONFile() {
		return new File("json/" + StringUtils.deleteWhitespace(getName()) + ".json");
	}

	public boolean isSelected(String name) {
		return checkBoxMap.get(name).isSelected();
	}
	
	public boolean hasCheckBox(String name) {
		return checkBoxMap.containsKey(name);
	}
	
	public void setSelected(String name, boolean b) {
		checkBoxMap.get(name).setSelected(b);
	}
	
	@Override
	public void save() {
		for(String key : checkBoxMap.keySet())
			saveCheckbox(key);
	}

	public abstract void saveCheckbox(String key);
}