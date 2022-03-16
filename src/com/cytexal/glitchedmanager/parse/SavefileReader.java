package com.cytexal.glitchedmanager.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SavefileReader {

	private BufferedReader r;

	public SavefileReader(File f) {
		try {
			r = new BufferedReader(new FileReader(f));
			char[] c = new char[1];
			while (c[0] != '"')
				r.read(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SavefileReader(String s) {
		r = new BufferedReader(new StringReader(s));
	}

	public SavefileReader(SavefileString s) {
		r = new BufferedReader(new StringReader(s.toString()));
	}

	public byte readHexByte() throws IOException {
		char[] b = new char[2];
		r.read(b);
		return (byte) (Integer.parseInt(new String(b), 16) & 0xff);
	}

	public int readHexInt() throws IOException {
		byte[] bytes = { readHexByte(), readHexByte(), readHexByte(), readHexByte() };
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public double readHexDouble() throws IOException {
		byte[] bytes = { readHexByte(), readHexByte(), readHexByte(), readHexByte(), readHexByte(), readHexByte(),
				readHexByte(), readHexByte() };
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getDouble();
	}

	public String readHexString(int length) throws IOException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = (char) readHexByte();
			if (c >= 32 || c == 13 || c == 10)
				builder.append(c);
			else
				return null;
		}
		return builder.toString();

	}

	public SavefileObject read() throws IOException {
		int type = readHexInt();
		switch (type) {
		case 0:
			return new SavefileDouble(this);
		case 1:
			return new SavefileString(this);
		case 0x0193:
			return new SavefileMap(this);
		case 0x012F:
		case 0x0067:
			return new SavefileList(this);
		case 0x025B:
			return new SavefileTable(this);
		default:
			throw new IOException("Unknown SavefileObject Type");
		}
	}
}
