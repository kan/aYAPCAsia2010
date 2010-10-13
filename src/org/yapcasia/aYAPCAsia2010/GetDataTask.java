package org.yapcasia.aYAPCAsia2010;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetDataTask extends AsyncTask<String, Void, JSONObject> {
	private Exception error = null;
	private SessionList view;
	private ProgressDialog progressDialog;

	public GetDataTask(SessionList v) {
		view = v;
	}

	@Override
	protected void onPreExecute() {
        progressDialog = new ProgressDialog(view);
        progressDialog.setTitle("âÔèÍÉfÅ[É^éÊìæíÜ");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
    	HttpGet req = new HttpGet(urls[0]);
        DefaultHttpClient client = new DefaultHttpClient();

		try {
			HttpResponse response = client.execute(req);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(10*1024);
			response.getEntity().writeTo(baos);

			String jsonstr = baos.toString("UTF-8");

			return new JSONObject(jsonstr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	protected void onPostExecute(JSONObject result) {
		progressDialog.dismiss();
		view.onGetSubsTaskCompleted(result, error);
	}
}
