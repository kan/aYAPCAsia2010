package org.yapcasia.aYAPCAsia2010;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.Toast;

public class Application extends android.app.Application {
	private ArrayList<String> rooms;
	private HashMap<String, ArrayList<Session>> timetable;

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

	public void setLocalData(HashMap<String, ArrayList<Session>> tt, ArrayList<String> r) {
		timetable = tt;
		rooms = r;
		saveLocalDataToFile();
	}

	@SuppressWarnings("unchecked")
    private void loadLocalData() {
		try {
			ObjToFile of = new ObjToFile(this, "");
			timetable = (HashMap<String, ArrayList<Session>>) of.get("time_table");
			rooms = (ArrayList<String>) of.get("rooms");
		}
		catch (Exception e) {
		}
    }

	public void saveLocalDataToFile() {
		try {
			ObjToFile of = new ObjToFile(this, "");
			of.put("time_table", timetable);
			of.put("rooms", rooms);
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"saveLocalDataToFile ERROR: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}
}
