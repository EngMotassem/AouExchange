package apps.aouexchange.StudentActivity.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.services.Config;

import static apps.aouexchange.StudentActivity.services.Config.*;

public class ShowNotify extends Activity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String TAG=ShowNotify.class.getSimpleName();
    private TextView tv,titleTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notify);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(REGISTRATION_COMPLETE)) {

                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);

                    displayFirebaseRegId();

                }

            }
        };
        titleTv=(TextView)findViewById(R.id.notifyTitle);

        tv=(TextView)findViewById(R.id.notitextView);
        String title=getIntent().getExtras().getString("title");

        String message=getIntent().getExtras().getString("message");

        if(message!="")


        tv.setText(message);

        if(title!="")
            titleTv.setText(title);


    }

    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }

}
