package com.cytexal.glitchedmanager.parse;

import java.io.IOException;

public class SavefileDouble extends Number implements SavefileObject {

	private double value;

	public SavefileDouble(SavefileReader reader) throws IOException {
		value = reader.readHexDouble();
	}

	public SavefileDouble(double val) {
		value = val;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public String getEncoded() {
		return "00000000" + SavefileHelper.hex(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public int hashCode() {
		return ((Double) value).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SavefileDouble)
			return ((Double)value).equals(((SavefileDouble)obj).value);
		return false;
	}

	public void set(double arg) {
		value = arg;
	}

}
