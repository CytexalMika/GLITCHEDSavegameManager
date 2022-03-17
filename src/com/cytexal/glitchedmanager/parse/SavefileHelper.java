package com.cytexal.glitchedmanager.parse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SavefileHelper {
	
	public static String hex(int i) {
		return String.format("%02X", i & 0xFF) + String.format("%02X", (i >> 8) & 0xFF)
				+ String.format("%02X", (i >> 16) & 0xFF) + String.format("%02X", (i >> 24) & 0xFF);
	}

	public static String hex(double d) {
		ByteBuffer buff = ByteBuffer.allocate(8);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putDouble(d);
		buff.flip();
		return String.format("%02X", buff.get()) + String.format("%02X", buff.get()) + String.format("%02X", buff.get())
				+ String.format("%02X", buff.get()) + String.format("%02X", buff.get())
				+ String.format("%02X", buff.get()) + String.format("%02X", buff.get())
				+ String.format("%02X", buff.get());
	}

	public static String hex(String s) {
		StringBuilder builder = new StringBuilder();
		for (byte b : s.getBytes()) {
			builder.append(String.format("%02X", b));
		}
		return builder.toString();
	}
}
