package org.yapcasia.aYAPCAsia2010;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import android.widget.Toast;

public class Application extends android.app.Application {
	private ArrayList<String> rooms;
	private HashMap<String, ArrayList<Session>> timetable;
	private HashMap<String, Session> checked_sessions;

	@Override
	public void onLowMemory() {
		saveLocalDataToFile();
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public HashMap<String, ArrayList<Session>> getTimeTable() {
		if (timetable == null) {
			loadLocalData();
		}
		return timetable;
	}

	public ArrayList<String> getRooms() {
		if (rooms == null) {
			loadLocalData();
		}
		return rooms;
	}

	public ArrayList<Session> getSessions(String key) {
		if (getTimeTable() != null) {
			return getTimeTable().get(key);
		} else {
			return null;
		}
	}

	public ArrayList<Session> getCheckedSessions() {
		if (checked_sessions == null) {
			loadLocalData();
		}

		ArrayList<Session> sessions = new ArrayList<Session>();
		Collection<Session> values = checked_sessions.values();
		for (Object obj:values) {
			sessions.add((Session) obj);
		}
		Collections.sort(sessions);

		return sessions;
	}

	public boolean checkSessionIsChecked(Session session) {
		return checked_sessions.containsKey(session.code);
	}

	public void setCheckedSession(Session session) {
		checked_sessions.put(session.code, session);
	}

	public void removeCheckedSession(Session session) {
		checked_sessions.remove(session.code);
	}

	public void setLocalData(HashMap<String, ArrayList<Session>> tt, ArrayList<String> r) {
		timetable = tt;
		rooms = r;
		saveLocalDataToFile();
	}

	@SuppressWarnings("unchecked")
    private void loadLocalData() {
		try {
			ObjToFile of = new ObjToFile(this, "v2");
			timetable = (HashMap<String, ArrayList<Session>>) of.get("time_table");
			rooms = (ArrayList<String>) of.get("rooms");
			checked_sessions = (HashMap<String, Session>) of.get("checked_sessions");
		}
		catch (Exception e) {
		}

		if (checked_sessions == null) {
			checked_sessions = new HashMap<String, Session>();
		}
    }

	public void saveLocalDataToFile() {
		try {
			ObjToFile of = new ObjToFile(this, "v2");
			of.put("time_table", timetable);
			of.put("rooms", rooms);
			of.put("checked_sessions", checked_sessions);
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"saveLocalDataToFile ERROR: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

}
