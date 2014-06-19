package user.account.info;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowUserInfo extends Activity{
	
	ImageView imageProfile;
	TextView textViewName, textViewEmail, textViewGender, textViewBirthday;
	String textName, textEmail, textGender, textBirthday, userImageUrl;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		imageProfile = (ImageView) findViewById(R.id.imageView1);
		textViewName = (TextView) findViewById(R.id.textViewNameValue);
		textViewEmail = (TextView) findViewById(R.id.textViewEmailValue);
		textViewGender = (TextView) findViewById(R.id.textViewGenderValue);
		textViewBirthday = (TextView) findViewById(R.id.textViewBirthdayValue);

		Intent intent = getIntent();
		try{
			textEmail = intent.getStringExtra("email_id");
			textViewEmail.setText(textEmail);
			textName = intent.getStringExtra("name");
			textViewName.setText(textName);
			textGender = intent.getStringExtra("gender");
			textViewGender.setText(textGender);
			textBirthday = intent.getStringExtra("dob");
			textViewBirthday.setText(textBirthday);
			
			userImageUrl = intent.getStringExtra("imageurl");
			new GetImageFromUrl().execute(userImageUrl);
	
		} catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url);
			}
			return map;
		}

		// Sets the Bitmap returned by doInBackground
		@Override
		protected void onPostExecute(Bitmap result) {
			imageProfile.setImageBitmap(result);
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		// Makes HttpURLConnection and returns InputStream
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
		}
	}

}
