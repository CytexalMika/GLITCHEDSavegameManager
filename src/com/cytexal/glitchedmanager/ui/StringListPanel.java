package com.cytexal.glitchedmanager.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.cytexal.glitchedmanager.parse.SavefileList;
import com.cytexal.glitchedmanager.parse.SavefileObject;
import com.cytexal.glitchedmanager.parse.SavefileReader;
import com.cytexal.glitchedmanager.parse.SavefileString;

public class StringListPanel extends CheckBoxList {

	private SavefileList list;
	private SavefileString listEncoded;

	public StringListPanel(String title, SavefileString listEncoded) throws IOException {
		super(title);
		this.listEncoded = listEncoded;
		this.list = (SavefileList) new SavefileReader(listEncoded).read();
		load();
		
	}
	
	@Override
	public void load() {
		clear();
		try {
			File file = getJSONFile();
			JSONTokener t = new JSONTokener(new FileReader(file));
			JSONArray a = new JSONArray(t);
			Vector<JCheckBox> checkboxes = new Vector<JCheckBox>();
			for (Object o : a) {
				String str = (String) o;
				addCheckBox(str);
			}

			for (SavefileObject o : list) {
				SavefileString str = (SavefileString) o;

				// Tick the checkbox for entries in savefile
				if (hasCheckBox(str.toString()))
					setSelected(o.toString(), true);
				else {
					addCheckBox(str.toString());

					a.put(str.toString());

					FileWriter w = new FileWriter(file);
					w.write(a.toString(2));
					w.close();
					System.out.println("New Entry found for " + getName() + ": " + str.toString() + "! Saved it into "
							+ file.getPath());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		super.save();	
		listEncoded.set(list.getEncoded());
	}

	@Override
	public void saveCheckbox(String key) {
		if(!isSelected(key)) {
			list.remove(new SavefileString(key));
		} else {
			if(!list.contains(new SavefileString(key)))
				list.add(new SavefileString(key));
		}
	}

}
