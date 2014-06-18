package user.account.info;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListUserAccount extends ListActivity{

	private AccountManager mAccountManager;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("List user accounts ", "into ListUserAccount class");
		mAccountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = mAccountManager.getAccountsByType("com.google");
        this.setListAdapter(new ArrayAdapter(this, R.layout.list_item, accounts));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Account account = (Account)getListView().getItemAtPosition(position);
		Intent intent = new Intent(ListUserAccount.this,ApplicationInfo.class);
		intent.putExtra("account", account);
		startActivity(intent);
	}
}
