package com.cytexal.glitchedmanager.ui;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.cytexal.glitchedmanager.parse.SavefileDouble;
import com.cytexal.glitchedmanager.parse.SavefileList;
import com.cytexal.glitchedmanager.parse.SavefileString;

public class GeneralPanel extends JPanel implements SavefileEditorTab{
	
	SavefileString playerName;
	SavefileDouble xCoordinate;
	SavefileDouble yCoordinate;
	SavefileString map;
	
	JPanel panelPlayerName;
	JPanel panelXCoordinate;
	JPanel panelYCoordinate;
	JPanel panelMap;
	
	
	public GeneralPanel(SavefileList savefile) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		playerName = (SavefileString) savefile.get(0);
		xCoordinate = (SavefileDouble) savefile.get(1);
		yCoordinate = (SavefileDouble) savefile.get(2);
		map = (SavefileString) savefile.get(3);
		
		panelPlayerName = EditorHelper.createTextfield("Player Name", playerName.toString(), null);
		add(panelPlayerName, BorderLayout.PAGE_START);

		
		panelXCoordinate = EditorHelper.createNumberfield("x-Coordinate", xCoordinate.doubleValue(), Double.class, null);
		add(panelXCoordinate, BorderLayout.PAGE_START);

		panelYCoordinate = EditorHelper.createNumberfield("y-Coordinate", yCoordinate.doubleValue(), Double.class, null);
		add(panelYCoordinate, BorderLayout.PAGE_START);

		panelMap = EditorHelper.createTextfield("Map", map.toString(), null);
		add(panelMap, BorderLayout.PAGE_START);

	}

	@Override
	public void load() {
		
	}

	@Override
	public void save() {
		playerName.set(((JTextField)panelPlayerName.getComponent(1)).getText());
		xCoordinate.set(Double.parseDouble(((JFormattedTextField)panelXCoordinate.getComponent(1)).getText()));
		yCoordinate.set(Double.parseDouble(((JFormattedTextField)panelYCoordinate.getComponent(1)).getText()));
		map.set(((JTextField)panelMap.getComponent(1)).getText());
	}
}
