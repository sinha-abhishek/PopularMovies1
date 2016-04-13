package com.example.abhisheksinha.listviewexample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import models.MovieDataModel;

public class MainActivity extends ActionBarActivity implements  MainFragment.Callback{
    private boolean mHasTwoFragments;
    private ShareActionProvider mShareActionProvider;
    Bundle savedState;

    public static final String AUTHORITY = "com.example.abhisheksinha.Movies";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "dummy.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedState = savedInstanceState;
        if (findViewById(R.id.fragment_detail) != null) {
            mHasTwoFragments = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, new DetailActivityFragment(),DetailActivity.DETAILFRAGMENT_TAG)
                        .commit();
            }
            DetailActivityFragment df = (DetailActivityFragment) getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
            if (df != null) {
                df.SetShareProvider(mShareActionProvider);
            }
        } else {
            mHasTwoFragments = false;
        }
//        Intent intent = new Intent(this,
//                NetworkService.class);
//        startService(intent);
        mAccount = CreateSyncAccount(this);

        ContentResolver resolver = getContentResolver();

       // resolver.addPeriodicSync(mAccount,);

    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         *
         */
        if (null == accountManager.getPassword(newAccount)) {
            if (accountManager.addAccountExplicitly(newAccount, "", null)) {

            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
                ContentResolver.addPeriodicSync(newAccount,
                        AUTHORITY,
                        Bundle.EMPTY,
                        30);

                ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);

            } else {
                return null;
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            }
        }
        return newAccount;
    }


    @Override
    public void onItemSelected(MovieDataModel m) {
        if (!mHasTwoFragments) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, m);
            startActivity(intent);
            return;
        }
        //DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
        Bundle args = new Bundle();
        args.putParcelable(DetailActivityFragment.MOVIE_DETAIL_KEY,m);
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail, fragment, DetailActivity.DETAILFRAGMENT_TAG)
                .commit();
        fragment.SetShareProvider(mShareActionProvider);
        return;
    }

    @Override
    public void onOtherItemSelected(MenuItem item) {

    }

    @Override
    public void onCreateOptions(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_share);
        if (item != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
            df.SetShareProvider(mShareActionProvider);
        }
    }

    @Override
    public void onMoviesLoaded(int activePosition, GridView gridView) {
        if (mHasTwoFragments && savedState == null) {
            try {
                gridView.performItemClick(gridView.getAdapter().getView(activePosition, null, null),
                        activePosition,
                        gridView.getAdapter().getItemId(activePosition));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
