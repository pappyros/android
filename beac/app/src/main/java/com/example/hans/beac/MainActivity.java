package com.example.hans.beac;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public String ha="";
    MySqlOpenHandler my;
    SQLiteDatabase db;
    ArrayList<KalmanFilter>kf = new ArrayList<>();


    private BeaconManager beaconManager;
    private Region region;

    private TextView state_tv, avg_tv;
    private EditText position_et;
    private Button record_btn, stop_btn, search_btn;

    public ImageView iv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView)findViewById(R.id.point);

//
//        kf.add(new KalmanFilter(81.2025,"90:AB"));
//        kf.add(new KalmanFilter(83.4889,"12:FB"));
//        kf.add(new KalmanFilter(71.8155,"3D:E1"));
//        kf.add(new KalmanFilter(73.3568,"BE:46"));


        final init Init = new init();
        //state_tv = (TextView) findViewById(R.id.state_tv);
        //avg_tv = (TextView) findViewById(R.id.avg_tv);
        //position_et = (EditText) findViewById(R.id.position_et);
        record_btn = (Button) findViewById(R.id.record);
        stop_btn = (Button)findViewById(R.id.stop);
        //search_btn = (Button)findViewById(R.id.search_btn);

        my = new MySqlOpenHandler(this);
        try {
            db = my.getWritableDatabase();
        } catch (SQLiteException e) {
            db = my.getReadableDatabase();
        }

        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {

            int q=0;
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    String str = "";

                    Beacon nearestBeacon = list.get(0);

                    for (int vi = 0; list.size() > vi; vi++) {
                        nearestBeacon = list.get(vi);
                        str = str + "Rssi : " + nearestBeacon.getRssi()
                                + " / " + "Mac_Add : " + nearestBeacon.getMacAddress().toString().substring(13, 18) + "\n";


                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();


                    }


                }
            }
        });
        region = new Region("ranged region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null, null);

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),path, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "start record", Toast.LENGTH_SHORT).show();

                beaconManager.setRangingListener(new BeaconManager.RangingListener() {

                    @Override
                    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                        if (!list.isEmpty()) {

                            String str = "";

                            Beacon nearestBeacon = list.get(0);


                            /////////////////////////////////////////////////


                            for (int vi = 0; list.size() > vi; vi++) {
                                nearestBeacon = list.get(vi);
                                str = str + "Where: " + position_et.getText().toString() + " / " + "Rssi: " + nearestBeacon.getRssi()
                                        + " / " + "Mac_Add: " + nearestBeacon.getMacAddress().toString().substring(13, 18) + "\n";

                                db.execSQL("insert into data values('" + position_et.getText().toString()
                                        + "', " + nearestBeacon.getRssi()
                                        + ", '" + nearestBeacon.getMacAddress() + "');");

                                int nnn=kf.size();
                                for(int i=0;i<=nnn;i++){
                                    if(i==kf.size()){
                                        kf.add(new KalmanFilter(-60,nearestBeacon.getMacAddress().toString().substring(13, 18)));

                                    }
                                    else{
                                        if(kf.get(i).Kalmanid.equals(nearestBeacon.getMacAddress().toString().substring(13, 18))){
                                            db.execSQL("insert into data values('" + position_et.getText().toString()+"Kalman"
                                                    + "', " + kf.get(i).update(nearestBeacon.getRssi())
                                                    + ", '" + nearestBeacon.getMacAddress() + "');");
                                            break;
                                        }



                                    }
                                }



                            }





                            state_tv.setText(str + "");
                        }
                    }
                });
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "stop record", Toast.LENGTH_SHORT).show();
                kf.clear();

                beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                    @Override
                    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                        if (!list.isEmpty()) {

                            String avg_sql = "select pos, mac_a , avg(rssi) from data group by pos, mac_a;";
                            db = my.getWritableDatabase();
                            Cursor cursor = db.rawQuery(avg_sql, null);

                            String str = "";
                            while(cursor.moveToNext()) {
                                str += "where: "+cursor.getString(0)
                                        + " / " + "Mac_Add: " + cursor.getString(1).substring(13, 18)
                                        + " / " + "Rssi: " + cursor.getString(2)
                                        +"\n";
                                db.execSQL("insert into avg_tb values('" + cursor.getString(0)
                                        + "', '" + cursor.getString(1).substring(13, 18)
                                        + "', " + cursor.getString(2) + ");");
                            }

                            avg_tv.setText(str);
                            cursor.close();

                            str = "";

                            Beacon nearestBeacon = list.get(0);

                            for (int vi = 0; list.size() > vi; vi++) {
                                nearestBeacon = list.get(vi);
                                str = str + "Rssi : " + nearestBeacon.getRssi()
                                        + " / " + "Mac_Add : " + nearestBeacon.getMacAddress().toString().substring(13, 18) + "\n";
                            }
                            state_tv.setText(str + "");
                        }
                    }
                });
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }














}