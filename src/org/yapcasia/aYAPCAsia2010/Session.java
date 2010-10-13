package org.yapcasia.aYAPCAsia2010;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Session implements Serializable {
	private static final long serialVersionUID = 828136892838338633L;
	public String title;
	public String speaker;
	public String summary;
	public String room;
	public String start_at;
	public String end_at;
	public String code;

	public Session(JSONObject json) {
		title = getStringFromJSON(json, "title");
		speaker = "";
		try {
			JSONArray s = json.getJSONArray("speakers");
			for(int i=0; i<s.length(); i++) {
				speaker += s.getJSONObject(i).getString("name");
			}
		} catch (JSONException e) {
			speaker = "";
		}
		summary = getStringFromJSON(json, "summary");
		room = getStringFromJSON(json, "room");
		start_at = getStringFromJSON(json, "start_at");
		end_at = getStringFromJSON(json, "end_at");
		code = getStringFromJSON(json, "code");
	}

	private String getStringFromJSON(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}
}
