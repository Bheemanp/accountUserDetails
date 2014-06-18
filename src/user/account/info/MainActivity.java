package user.account.info;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class MainActivity extends Activity implements OnItemClickListener {
	private AccountManager mAccountManager;
	private ListView lv;
	private Account[] accounts;
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_item);
		lv = (ListView) findViewById(R.id.lst_view);
		mAccountManager = AccountManager.get(getApplicationContext());
		accounts = mAccountManager.getAccounts();
		ListUserAccounts lsUsrAcc = new ListUserAccounts(this, accounts,
				mAccountManager);
		lv.setAdapter(lsUsrAcc);
		lv.setOnItemClickListener(this);
	}

	class ListUserAccounts extends BaseAdapter {
		Account[] account;
		Context context;
		AccountManager mAccountManager;

		public ListUserAccounts(Context context, Account[] account,
				AccountManager mAccountManager) {
			this.account = account;
			this.mAccountManager = mAccountManager;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return account.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater inflator = getLayoutInflater();

			convertView = inflator.inflate(R.layout.fragment_main, parent,
					false);
			TextView label = (TextView) convertView.findViewById(R.id.label);
			ImageView imgView = (ImageView) convertView
					.findViewById(R.id.imageView);

			label.setText(account[position].name);
			imgView.setImageDrawable(getIconForAccount(account[position],
					mAccountManager, context));

			return convertView;
		}

	}

	private Drawable getIconForAccount(Account account, AccountManager manager,
			Context context) {
		AuthenticatorDescription[] descriptions = manager
				.getAuthenticatorTypes();
		for (AuthenticatorDescription description : descriptions) {
			if (description.type.equals(account.type)) {
				PackageManager pm = context.getPackageManager();
				return pm.getDrawable(description.packageName,
						description.iconId, null);
			}
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		
		Toast.makeText(MainActivity.this, accounts[position].name+ "", 10000).show();
		mAccountManager.getAuthToken(accounts[position], "android", null, MainActivity.this, new AccountManagerCallback<Bundle>(){
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				// TODO Auto-generated method stub
				try{
					String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
					Toast.makeText(MainActivity.this, token, 10000).show();
					Log.v("token", token);
					
					processToken(token);
					
				}catch(Exception e){
					Toast.makeText(MainActivity.this, "exception", 10000).show();
					e.printStackTrace();
				}
			}
			
		},null);
		/*mAccountManager.getAuthToken(accounts[position], "android", null, MainActivity.this,new AccountManagerCallback<Bundle>(){

			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				// TODO Auto-generated method stub
				
			}
			
		});*/
	}
	
	private void processToken(String token){
		
	}

}
