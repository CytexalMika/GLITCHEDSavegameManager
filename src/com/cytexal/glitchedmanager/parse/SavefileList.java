package com.cytexal.glitchedmanager.parse;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SavefileList extends ArrayList<SavefileObject> implements SavefileObject {

	SavefileList(SavefileReader reader) throws IOException {
		int size = reader.readHexInt();
		for (int i = 0; i < size; i++)
			add(reader.read());
	}

	@Override
	public String getEncoded() {
		StringBuilder builder = new StringBuilder();
		builder.append("2F010000");
		builder.append(SavefileHelper.hex(size()));
		for (SavefileObject o : this) {
			builder.append(o.getEncoded());
		}
		return builder.toString();
	}

}
