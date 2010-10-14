package org.yapcasia.aYAPCAsia2010;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class CheckedSessionList extends ListActivity {
	public static final String KEY_SESSION = "key_session";

	private ArrayList<Session> sessions;

	private Application app;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checked_list);

        app = (Application) getApplication();

        setSessionList();
    }

    @Override
	protected void onResume() {
		super.onResume();

		setSessionList();
	}

	private void setSessionList() {
		sessions = app.getCheckedSessions();

		if (sessions != null) {
			SessionAdapter adapter = new SessionAdapter(this, sessions);
			setListAdapter(adapter);
		}
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, SessionView.class);
		i.putExtra(KEY_SESSION, sessions.get(position));
        startActivity(i);
    }
}
