package user.account.info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.GoogleAuthUtil;

import user.account.adapter.ListUserAccounts;
import user.account.classes.AccountDetails;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener {
	private AccountManager mAccountManager;
	private ListView lv;
	private Account[] accounts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lst_view);
		mAccountManager = AccountManager.get(getApplicationContext());
		accounts = mAccountManager.getAccounts();
		ListUserAccounts lsUsrAcc = new ListUserAccounts(this, accounts,
				mAccountManager);
		lv.setAdapter(lsUsrAcc);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		if (checkConnectivity()) {
			mAccountManager.getAuthToken(accounts[position], "android", null, MainActivity.this, new AccountManagerCallback<Bundle>(){
				@Override
				public void run(AccountManagerFuture<Bundle> future) {
					// TODO Auto-generated method stub
					try{
						String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
						AccountDetails accDetails = new AccountDetails(token,accounts[position].name);
						new FetchUserInfo().execute(accDetails);
					} catch(Exception e) {
						e.printStackTrace();
						Toast.makeText(MainActivity.this, e.getMessage(), 10000).show();
					}
				}
			},null);
		} else {
			Toast.makeText(MainActivity.this, "Please Check Internet Connectivity", 10000).show();
		}
	}
	
	private boolean checkConnectivity(){
		ConnectivityManager cm = (ConnectivityManager) MainActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	class FetchUserInfo extends AsyncTask<AccountDetails, Void, Void> {
		private String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(AccountDetails... params) {
			// TODO Auto-generated method stub
			try{
				for(int i = 0 ; i < params.length ; i++){
					fetchUserDataFromServer(params[i].getToken(),params[i].getEmailId());
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}catch (JSONException jse) {
				jse.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		private void fetchUserDataFromServer(String token,String email) throws IOException, JSONException {
			// TODO Auto-generated method stub
			try {
				URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+ token);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				int sc = con.getResponseCode();
				Log.v("Fetch", sc+"");
				if (sc == 200) {
					InputStream is = con.getInputStream();
					String GOOGLE_USER_DATA = readResponse(is);
					Log.v("user data", GOOGLE_USER_DATA);
					is.close();
					
					JSONObject profileInfo = new JSONObject(GOOGLE_USER_DATA);
					Intent intent=new Intent(MainActivity.this,ShowUserInfo.class);
					intent.putExtra("name", profileInfo.getString("name"));
					intent.putExtra("gender", profileInfo.getString("gender"));
					intent.putExtra("birthday", profileInfo.getString("birthday"));
					intent.putExtra("imageurl", profileInfo.getString("userImageUrl"));
					startActivity(intent);
					finish();
					return;
				} else if (sc == 401) {
					GoogleAuthUtil.invalidateToken(MainActivity.this,token);
					return;
				}
			} catch(Exception e) {
				
			}
		}
		
		private String readResponse(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[2048];
			int len = 0;
			while ((len = is.read(data, 0, data.length)) >= 0) {
				bos.write(data, 0, len);
			}
			return new String(bos.toByteArray(), "UTF-8");
		}
	}
}
