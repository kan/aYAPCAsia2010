package org.yapcasia.aYAPCAsia2010;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SessionAdapter extends BaseAdapter {

	private ArrayList<Session> items;
	private LayoutInflater inflater;

	public SessionAdapter(Context context, ArrayList<Session> sessions) {
		items = sessions;

		inflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int i) {
		return items.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup) {
		if (view == null) {
			view = inflater.inflate(R.layout.session_row, null);
		}
		Session session = items.get(i);
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(session.title);
		TextView speaker = (TextView)view.findViewById(R.id.speaker);
		speaker.setText(session.speaker);
		TextView room = (TextView)view.findViewById(R.id.room);
		room.setText(session.room);
		TextView time = (TextView)view.findViewById(R.id.time);
		time.setText(session.start_at+" - "+session.end_at);
		return view;
	}

}
