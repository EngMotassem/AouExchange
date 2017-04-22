package apps.aouexchange.StudentActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import apps.aouexchange.R;

public class MainActivity extends AppCompatActivity {
    String AccessPoint="8c:34:fd:68:ae:1a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getWifi().equals(AccessPoint)){
            Toast.makeText(getApplicationContext(),"nice",Toast.LENGTH_LONG).show();
            Intent intent =new Intent(MainActivity.this,StudentActivity.class);
            startActivity(intent);


        }
        else if(getWifi().equals(""))
        {

            Toast.makeText(getApplicationContext(),"bad",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getWifi().equals(AccessPoint)){
            Toast.makeText(getApplicationContext(),"nice",Toast.LENGTH_LONG).show();
            Intent intent =new Intent(MainActivity.this,StudentActivity.class);
            startActivity(intent);


        }
        else if(getWifi().equals(""))
        {

            Toast.makeText(getApplicationContext(),"bad",Toast.LENGTH_LONG).show();

        }


    }

    public String getWifi() {

        WifiManager wifi = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            return "";


        }
        WifiInfo winfo = wifi.getConnectionInfo();
        return winfo.getBSSID();
    }
}
