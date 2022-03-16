package com.cytexal.glitchedmanager.parse;

import java.io.IOException;
import java.util.HashMap;

public class SavefileMap extends HashMap<String, SavefileObject> implements SavefileObject {

	public SavefileMap(SavefileReader reader) throws IOException {
		int size = reader.readHexInt();
		for (int i = 0; i < size; i++)
			put(((SavefileString) reader.read()).toString(), reader.read());
	}

	@Override
	public String getEncoded() {
		StringBuilder builder = new StringBuilder();
		builder.append("93010000");
		builder.append(SavefileHelper.hex(size()));
		for (Entry<String, SavefileObject> e : entrySet()) {
			// Key
			builder.append("01000000");
			builder.append(SavefileHelper.hex(e.getKey().getBytes().length));
			builder.append(SavefileHelper.hex(e.getKey()));
			// Value
			builder.append(e.getValue().getEncoded());
		}
		return builder.toString();
	}

}
