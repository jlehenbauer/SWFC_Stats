package com.jacoblehenbauer.android.statsforswfc;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class Donate extends AppCompatActivity {

    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Show the up button in the action bar and set its action to return home
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        /*
        Donation buttons using Google Payments API
         */


        Button oneDollarButton = (Button) findViewById(R.id.donate1);
        oneDollarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                clickDonate();
            }

/*            @Override
            public void onClick(View view) {
                //trigger the payment api for $1
                int canBeBilled = 0;
                try {
                    canBeBilled = mService.isBillingSupported(3, getPackageName(), "inapp");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(canBeBilled == 1){
                    Bundle buyIntentBundle = null;
                    try {
                        buyIntentBundle = mService.getBuyIntent(3, getPackageName(), "one_dollar_donation", "inapp", "user_id");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(),
                                1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                Integer.valueOf(0));
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
                else return;
            }*/
        });
        Button twoDollarButton = (Button) findViewById(R.id.donate2);
        twoDollarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trigger the payment api for $2
                clickDonate();
            }
        });
        Button fiveDollarButton = (Button) findViewById(R.id.donate5);
        fiveDollarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trigger the payment api for $5
                clickDonate();
            }
        });
        Button tenDollarButton = (Button) findViewById(R.id.donate10);
        tenDollarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trigger the payment api for $10
                clickDonate();
            }
        });


        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void navigateUpFromSameTask(Donate donate) {
        Intent intent;
        intent = new Intent(this, TopMenu.class);
        this.navigateUpTo(intent);
    }



        @Override
        public void onDestroy () {
            super.onDestroy();
            if (mService != null) {
                unbindService(mServiceConn);
            }
        }


    public void clickDonate(){
        new AlertDialog.Builder(Donate.this)
                .setTitle("Donations coming soon")
                .setMessage("Thanks for your interest in donating to help further development! " +
                        "\nThis option will be available in a later update")
                .setPositiveButton("Okay, I\'ll be back!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Donate Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

     */

