package com.cytexal.glitchedmanager.parse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SavefileHelper {

	/*
	 * public String toString() { StringBuilder builder = new StringBuilder();
	 * 
	 * try { int type = readHexInt(); switch(type) { case 0:
	 * builder.append(readHexDouble()); break; case 1: int i = readHexInt();
	 * r.mark(i*2); String s = readHexString(i); SavefileConverter conv = new
	 * SavefileConverter(s); String res = conv.toString(); if(res != null) {
	 * builder.append(res); } else { builder.append("\"" + s+"\""); } break; case
	 * 0x0193: builder.append("{\n"); int num2 = readHexInt(); for(int j = 0; j <
	 * num2; j++) { builder.append(toString()); builder.append(": ");
	 * builder.append(toString()); if(j != num2 -1) builder.append(",");
	 * builder.append("\n"); } builder.append("}"); break; case 0x012F: case 0x0067:
	 * builder.append("[\n"); int num = readHexInt(); for(int j = 0; j < num; j++) {
	 * builder.append(toString()); if(j != num -1) builder.append(",");
	 * builder.append("\n"); } builder.append("]"); break; case 0x025B:
	 * builder.append("[\n"); int columns = readHexInt(); int rows = readHexInt();
	 * for(int j = 0; j < columns; j++) { builder.append("[ "); for(int x = 0; x <
	 * rows; x++) { builder.append(toString()); if(x != rows -1)
	 * builder.append(", "); } builder.append(" ]"); if(j != columns -1)
	 * builder.append(","); builder.append("\n"); } builder.append("]"); break;
	 * default: builder.append("{\"UNK\": \"" + Integer.toHexString(type) + "\"}");
	 * } } catch (IOException e) { e.printStackTrace(); } catch
	 * (NumberFormatException e) { if(builder.length() < 3) return null; } return
	 * builder.toString(); }
	 * 
	 * static JSONArray toJSONArray(File f) { return new JSONArray(new
	 * SavefileConverter(f).toString()); } static String saveFromJSON(JSONArray a) {
	 * StringBuilder builder = new StringBuilder();
	 * builder.append("[Lists]\r\n0=\""); builder.append("2F010000");
	 * builder.append(hex(a.length())); for(int i = 0; i < a.length(); i++) { Object
	 * o = a.get(i); if(o instanceof JSONArray) { if(i == 47)
	 * builder.append(fromJSON(listFromJSON((JSONArray) o, "67000000"))); else if(i
	 * == 48 || i== 52 || i == 58 || i == 59 || i == 70) {
	 * builder.append(fromJSON(tableFromJSON((JSONArray) o))); } else
	 * builder.append(fromJSON(listFromJSON((JSONArray) o))); } else if(i == 66) {
	 * builder.append(fromJSON(tableDictFromJSON((JSONObject) o))); } else if(o
	 * instanceof JSONObject) { builder.append(fromJSON(dictFromJSON((JSONObject)
	 * o))); } else{ builder.append(fromJSON(o)); } } builder.append("\"\r\n");
	 * return builder.toString(); }
	 * 
	 * private static String fromJSON(Object o) { if(o instanceof JSONArray) {
	 * return listFromJSON((JSONArray) o); } else if (o instanceof JSONObject) {
	 * return dictFromJSON((JSONObject) o); } else if(o instanceof BigDecimal) {
	 * return "00000000" + hex(((BigDecimal) o).doubleValue()); } else if(o
	 * instanceof Integer) { return "00000000" + hex(((Integer) o).doubleValue());
	 * }else if(o instanceof String) { return "01000000" + hex(((String)
	 * o).getBytes().length) + hex((String) o); }else return "[unk]"; }
	 * 
	 * private static String dictFromJSON(JSONObject o) { StringBuilder builder =
	 * new StringBuilder(); builder.append("93010000");
	 * builder.append(hex(o.length())); for(String s : o.keySet()) {
	 * builder.append(fromJSON(s)); builder.append(fromJSON(o.get(s))); } return
	 * builder.toString(); }
	 * 
	 * private static String tableDictFromJSON(JSONObject o) { StringBuilder builder
	 * = new StringBuilder(); builder.append("93010000");
	 * builder.append(hex(o.length())); for(String s : o.keySet()) {
	 * builder.append(fromJSON(s));
	 * builder.append(fromJSON(tableFromJSON((JSONArray) o.get(s)))); } return
	 * builder.toString(); }
	 * 
	 * private static String listFromJSON(JSONArray a) { return listFromJSON(a,
	 * "2F010000"); } private static String tableFromJSON(JSONArray a) { int columns
	 * = a.length(); int rows = 0; if(columns > 0) { JSONArray b = (JSONArray)
	 * a.get(0); rows = b.length(); }
	 * 
	 * StringBuilder builder = new StringBuilder(); builder.append("5B020000");
	 * builder.append(hex(columns)); builder.append(hex(rows)); for(int i = 0; i <
	 * columns; i++) { for(int x = 0; x < rows; x++) { Object o =
	 * ((JSONArray)a.get(i)).get(x); builder.append(fromJSON(o)); } } return
	 * builder.toString(); }
	 */
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

	/*
	 * @Override public String toString() { StringBuilder builder = new
	 * StringBuilder(); builder.append("{\r"); while(true) try { int i =
	 * readHexInt(); if(i > 1 && i < 1000000) { r.mark(i*2); String s =
	 * readHexString(i); if(s != null) { SavefileConverter conv = new
	 * SavefileConverter(s); String res = conv.toString(); if(res != null) {
	 * builder.append(res); } else { builder.append(s); builder.append('\n'); } }
	 * else { r.reset(); builder.append(Integer.toHexString(i));
	 * builder.append('\n'); } } else { builder.append(Integer.toHexString(i));
	 * builder.append('\n'); } } catch (IOException e) { e.printStackTrace(); }
	 * catch (NumberFormatException e) { if(builder.length() < 3) return null;
	 * break; } builder.append("}\r"); return builder.toString(); }
	 */
}
