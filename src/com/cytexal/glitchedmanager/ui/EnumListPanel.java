package com.cytexal.glitchedmanager.ui;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.cytexal.glitchedmanager.parse.SavefileDouble;
import com.cytexal.glitchedmanager.parse.SavefileMap;
import com.cytexal.glitchedmanager.parse.SavefileObject;
import com.cytexal.glitchedmanager.parse.SavefileReader;
import com.cytexal.glitchedmanager.parse.SavefileString;

public class EnumListPanel extends CheckBoxComponentList<JComboBox<SavefileObject>> {

	private SavefileMap map;
	private SavefileString mapEncoded;

	public EnumListPanel(String title, SavefileString mapEncoded) throws IOException {
		super(title);
		this.map = (SavefileMap) new SavefileReader(mapEncoded).read();
		this.mapEncoded = mapEncoded;
		load();
	}

	@Override
	public void load() {
		try {
			JSONTokener t = new JSONTokener(new FileReader(getJSONFile()));
			JSONObject m = new JSONObject(t);
			for (String n : m.keySet()) {
				JSONObject o = m.getJSONObject(n);
				JSONArray a = o.getJSONArray("Values");
				JComboBox<SavefileObject> comboB = new JComboBox<SavefileObject>();
				for(Object valueItem : a)
				{
					if (valueItem instanceof String)
						comboB.addItem(new SavefileString((String) valueItem));
					else if(valueItem instanceof Double)
						comboB.addItem(new SavefileDouble((Double) valueItem));
					else if(valueItem instanceof Integer)
						comboB.addItem(new SavefileDouble((Integer) valueItem));
				}

				addEntry(new ListEntry(n, comboB));
			}
			for (Entry<String, SavefileObject> entry : map.entrySet()) {

				String key = entry.getKey();
				SavefileObject value = entry.getValue();

				ListEntry listEntry = getEntry(key);

				if (listEntry != null) {
					listEntry.getCheckbox().setSelected(true);
					if (((DefaultComboBoxModel<SavefileObject>) listEntry.getOtherComponent().getModel())
							.getIndexOf(value) == -1) {
						
						if(value instanceof SavefileString)
							m.getJSONObject(key).getJSONArray("Values").put(value);
						else
							m.getJSONObject(key).getJSONArray("Values").put(((SavefileDouble)value).doubleValue());
						

						FileWriter w = new FileWriter(getJSONFile());
						w.write(m.toString(2));
						w.close();
						System.out.println("New Value found for '" + key + "' in '" + getName() + "': '" + value
								+ "'! Saved it into " + getJSONFile().getPath());
					}
					listEntry.getOtherComponent().setSelectedItem(value);
					;
				} else {
					JComboBox<SavefileObject> comboB = new JComboBox<SavefileObject>(new SavefileObject[] { value });
					addEntry(new ListEntry(key, comboB, true));

					JSONObject obj = new JSONObject();
					
					if(value instanceof SavefileString)
						obj.put("Values", new String[] { value.toString() });
					else
						obj.put("Values", new Double[] {((SavefileDouble)value).doubleValue() });
						
					m.put(key, obj);

					FileWriter w = new FileWriter(getJSONFile());
					w.write(m.toString(2));
					w.close();
					System.out.println("New Entry found for '" + getName() + "': '" + key + "'! Saved it into "
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
		mapEncoded.set(map.getEncoded());
	}

	@Override
	public void saveEntry(CheckBoxComponentList<JComboBox<SavefileObject>>.ListEntry entry) {
		if (entry.getCheckbox().isSelected())
			map.put(entry.getName(), (SavefileObject) entry.getOtherComponent().getSelectedItem());
		else
			map.remove(entry.getName());
	}
}
