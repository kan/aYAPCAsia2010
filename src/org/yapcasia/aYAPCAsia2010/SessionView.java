package org.yapcasia.aYAPCAsia2010;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class SessionView extends Activity {

	private Session session;
	private Application app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        app = (Application) getApplication();

    	session = (Session) (savedInstanceState != null ? savedInstanceState.get(SessionList.KEY_SESSION) : null);

        if (session == null) {
        	Bundle extra = getIntent().getExtras();
        	session = (Session) (extra != null ? extra.get(SessionList.KEY_SESSION) : null);
        }

        if (session != null) {
			TextView title = (TextView)findViewById(R.id.title);
			title.setText(session.title);
			TextView speaker = (TextView)findViewById(R.id.speaker);
			speaker.setText(session.speaker);
			TextView room = (TextView)findViewById(R.id.room);
			room.setText("room: "+session.room);
			TextView summary = (TextView)findViewById(R.id.summary);
			summary.setText(session.summary);
			TextView time = (TextView)findViewById(R.id.time);
			time.setText(session.day + " " + session.start_at+" - "+session.end_at);

			CheckBox check = (CheckBox) findViewById(R.id.check);
			check.setChecked(app.checkSessionIsChecked(session));
			check.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox c = (CheckBox) v;

					if (c.isChecked()) {
						// チェックされてる
						app.setCheckedSession(session);
					} else {
						// チェックされてない
						app.removeCheckedSession(session);
					}
				}
			});
        }

    }

}
