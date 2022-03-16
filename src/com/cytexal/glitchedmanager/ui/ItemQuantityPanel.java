package com.cytexal.glitchedmanager.ui;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.cytexal.glitchedmanager.parse.SavefileDouble;
import com.cytexal.glitchedmanager.parse.SavefileList;
import com.cytexal.glitchedmanager.parse.SavefileReader;
import com.cytexal.glitchedmanager.parse.SavefileString;

public class ItemQuantityPanel extends CheckBoxComponentList<JFormattedTextField> {

	public static final int DOUBLE_TYPE = 0, STRING_TYPE = 1;

	private SavefileList nameList;
	private SavefileList amountList;
	private SavefileString nameListEncoded;
	private SavefileString amountListEncoded;
	
	
	private Map<String, Integer> typeMap;

	public ItemQuantityPanel(String title, SavefileString nameListEncoded, SavefileString amountListEncoded)
			throws IOException {
		super(title);
		
		this.nameListEncoded = nameListEncoded;
		this.amountListEncoded = amountListEncoded;
		
		this.nameList = (SavefileList) new SavefileReader(nameListEncoded).read();
		this.amountList = (SavefileList) new SavefileReader(amountListEncoded).read();
		load();
	}

	@Override
	public void load() {
		clear();
		try {
			JSONTokener t = new JSONTokener(new FileReader(getJSONFile()));
			JSONArray a = new JSONArray(t);
			typeMap = new HashMap<>();
			for (Object o : a) {
				JSONObject obj = (JSONObject) o;
				JFormattedTextField tf = new JFormattedTextField(1);
				addEntry(new ListEntry(obj.getString("Name"), tf));

				switch (obj.getString("Type").toUpperCase()) {
				case "DOUBLE":
					typeMap.put(obj.getString("Name"), DOUBLE_TYPE);
					break;
				case "STRING":
					typeMap.put(obj.getString("Name"), STRING_TYPE);
					break;

				default:
					throw new IllegalArgumentException("Unexpected value: " + obj.getString("Type"));
				}

			}
			for (int i = 0; i < nameList.size(); i++) {

				String str = nameList.get(i).toString();
				double amount = 0;

				if (amountList.get(i) instanceof SavefileDouble) {
					amount = ((SavefileDouble) amountList.get(i)).doubleValue();
					typeMap.put(str, DOUBLE_TYPE);
				} else if (amountList.get(i) instanceof SavefileString) {
					amount = Double.parseDouble(amountList.get(i).toString());
					typeMap.put(str, STRING_TYPE);
				}

				ListEntry entry = getEntry(str);

				if (entry != null) {
					entry.getCheckbox().setSelected(true);
					entry.getOtherComponent().setValue(amount);
				} else {
					JFormattedTextField tf = new JFormattedTextField(amount);
					addEntry(new ListEntry(str, tf, true));

					JSONObject obj = new JSONObject();

					obj.put("Name", str);
					obj.put("Type", typeMap.get(str) == DOUBLE_TYPE ? "DOUBLE" : "STRING");

					a.put(obj);

					FileWriter w = new FileWriter(getJSONFile());
					w.write(a.toString(2));
					w.close();
					System.out.println("New Entry found for " + getName() + ": " + str + "! Saved it into "
							+ getJSONFile().getPath());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		super.save();
		nameListEncoded.set(nameList.getEncoded());
		amountListEncoded.set(amountList.getEncoded());
	}

	@Override
	public void saveEntry(CheckBoxComponentList<JFormattedTextField>.ListEntry entry) {
		int i = nameList.indexOf(new SavefileString(entry.getName()));
		if (i > 0) {
			if (!entry.getCheckbox().isSelected()) {
				nameList.remove(i);
				amountList.remove(i);
			} else {
				if (typeMap.get(entry.getName()) == DOUBLE_TYPE)
					amountList.set(i,
							new SavefileDouble((Double)entry.getOtherComponent().getValue()));
				else
					amountList.set(i, new SavefileString((String) entry.getOtherComponent().getValue()));

			}
		} else {
			if (entry.getCheckbox().isSelected()) {
				nameList.add(new SavefileString(entry.getName()));
				if (typeMap.get(entry.getName()) == DOUBLE_TYPE)
					amountList.add(new SavefileDouble((Double) entry.getOtherComponent().getValue()));
				else
					amountList.add(new SavefileString(entry.getOtherComponent().getValue().toString()));
			}
		}
	}

}
