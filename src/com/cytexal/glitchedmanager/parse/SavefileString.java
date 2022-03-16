package com.cytexal.glitchedmanager.parse;

import java.io.IOException;

public class SavefileString implements CharSequence, SavefileObject {

	String value;

	SavefileString(SavefileReader reader) throws IOException {
		value = reader.readHexString(reader.readHexInt());
	}

	public SavefileString(String val) {
		value = val;
	}

	@Override
	public String getEncoded() {
		return "01000000" + SavefileHelper.hex(value.getBytes().length) + SavefileHelper.hex(value);
	}

	@Override
	public int length() {
		return value.length();
	}

	@Override
	public char charAt(int index) {
		return value.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return value.subSequence(start, end);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SavefileString)
			return value.equals(((SavefileString)obj).value);
		return false;
	}

	public void set(String text) {
		value = text;
	}

}
