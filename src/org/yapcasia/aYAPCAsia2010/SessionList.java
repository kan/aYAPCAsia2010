package org.yapcasia.aYAPCAsia2010;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SessionList extends ListActivity {
	public static final String KEY_ROOM_NAME = "room_name";

	public static final String KEY_SESSION = "key_session";

	private ArrayList<Session> sessions;
	private Spinner room_spinner;
	private Spinner date_spinner;

	private AdapterView.OnItemSelectedListener listner = new AdapterView.OnItemSelectedListener() {
		@SuppressWarnings("rawtypes")
		@Override
		public void onItemSelected(AdapterView parent, View view, int position, long id) {

			setSessionList();
	    }

		@SuppressWarnings("rawtypes")
		@Override
		public void onNothingSelected(AdapterView parent) {

		}
	};

	private Application app;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        room_spinner = (Spinner) findViewById(R.id.room_spinner);
        date_spinner = (Spinner) findViewById(R.id.date_spinner);
        room_spinner.setOnItemSelectedListener(listner);
        date_spinner.setOnItemSelectedListener(listner);

        app = (Application) getApplication();

        if (app.getRooms() == null) {
	        GetDataTask task = new GetDataTask(this);
	        task.execute("http://github.com/fujiwara/irubykaigi/raw/master/Project/timetable.json");
        } else {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, app.getRooms());
	        room_spinner.setAdapter(adapter);
        }
    }

    private void setSessionList() {
    	String room = "";
    	if (room_spinner.getSelectedItem() != null) {
    		room = room_spinner.getSelectedItem().toString();
    	}
		String date = date_spinner.getSelectedItem().toString();

		sessions = app.getSessions("2010/"+date+room);

		if (sessions != null) {
			SessionAdapter adapter = new SessionAdapter(this, sessions);
			setListAdapter(adapter);
		}
    }

	private void showErrorToast(Exception e) {
		Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, SessionView.class);
		i.putExtra(KEY_SESSION, sessions.get(position));
        startActivity(i);
    }

	public void onGetSubsTaskCompleted(JSONObject result, Exception error) {
		if (error != null) {
			showErrorToast(error);
			return;
		}

		try {
			JSONArray json_rooms = result.getJSONObject("ja").getJSONArray("rooms");
			ArrayList<String> rooms = new ArrayList<String>();
			for (int i=0; i<json_rooms.length(); i++) {
				rooms.add(json_rooms.getString(i));
			}

			JSONArray json_tt = result.getJSONObject("ja").getJSONArray("timetables");
			HashMap<String, ArrayList<Session>> timetable = new HashMap<String, ArrayList<Session>>();
			for (int i=0; i<json_tt.length(); i++) {
				JSONObject tt = json_tt.getJSONObject(i);
				String day = tt.getString("day");

				JSONArray sessions = tt.getJSONArray("sessions");
				for (int j=0; j<sessions.length(); j++) {
					Session session = new Session(sessions.getJSONObject(j), day);
					ArrayList<Session> list = timetable.get(day+session.room);
					if (list == null) {
						list = new ArrayList<Session>();
					}
					list.add(session);
					timetable.put(day+session.room, list);
				}
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rooms);
	        room_spinner.setAdapter(adapter);

	        app.setLocalData(timetable, rooms);

		} catch (JSONException e) {
			showErrorToast(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    getMenuInflater().inflate(R.menu.session_list_menu, menu);
	    return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()) {
        default:
            ret = super.onOptionsItemSelected(item);
            break;
        case R.id.menu_reload:
	        GetDataTask task = new GetDataTask(this);
	        task.execute(getResources().getString(R.string.timetable_json_url));
            break;
        case R.id.menu_checked:
            Intent i = new Intent(this, CheckedSessionList.class);
            startActivity(i);
        	break;
        case R.id.menu_yapc_site:
        	Uri uri = Uri.parse(getResources().getString(R.string.yapc_asia_url));
        	Intent i2 = new Intent(Intent.ACTION_VIEW, uri);
        	startActivity(i2);
        	break;
        case R.id.menu_about:
        	new AlertDialog.Builder(this).setTitle("about").setMessage(R.string.about).show();
        }
        return ret;
    }

	@Override
	protected void onDestroy() {
    	app.saveLocalDataToFile();
		super.onDestroy();
	}
}