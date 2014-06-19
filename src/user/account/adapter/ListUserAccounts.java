package user.account.adapter;

import user.account.info.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListUserAccounts extends BaseAdapter {
	Account[] account;
	Context context;
	AccountManager mAccountManager;
	LayoutInflater inflator;

	public ListUserAccounts(Context context, Account[] account,
			AccountManager mAccountManager) {
		this.account = account;
		this.mAccountManager = mAccountManager;
		this.context = context;
		inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		convertView = inflator.inflate(R.layout.display_list, parent,
				false);
		TextView label = (TextView) convertView.findViewById(R.id.label);
		ImageView imgView = (ImageView) convertView
				.findViewById(R.id.imageView);

		label.setText(account[position].name);
		imgView.setImageDrawable(getIconForAccount(account[position],
				mAccountManager, context));

		return convertView;
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

}
