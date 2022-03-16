package com.cytexal.glitchedmanager.parse;

import java.io.IOException;
import java.util.Vector;

public class SavefileTable extends Vector<Vector<SavefileObject>> implements SavefileObject {

	private int columns;
	private int rows;

	SavefileTable(SavefileReader reader) throws IOException {
		columns = reader.readHexInt();
		rows = reader.readHexInt();
		for (int i = 0; i < columns; i++) {
			Vector<SavefileObject> vec = new Vector<SavefileObject>();
			add(vec);
			for (int x = 0; x < rows; x++) {
				vec.add(reader.read());
			}
		}
	}

	@Override
	public String getEncoded() {
		StringBuilder builder = new StringBuilder();
		builder.append("5B020000");
		builder.append(SavefileHelper.hex(columns));
		builder.append(SavefileHelper.hex(rows));
		for (int i = 0; i < columns; i++) {
			for (int x = 0; x < rows; x++) {
				SavefileObject o = get(i).get(x);
				builder.append(o.getEncoded());
			}
		}
		return builder.toString();
	}

}
