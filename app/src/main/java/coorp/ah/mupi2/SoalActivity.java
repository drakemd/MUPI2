package coorp.ah.mupi2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import coorp.ah.mupi2.helper.DatabaseHelper;
import coorp.ah.mupi2.model.Soal;
import coorp.ah.mupi2.model.Tempat;

public class SoalActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button bSoal[], back;
    int id[];
    String tipeSoal[];
    DatabaseHelper db;
    Button cariTempat;
    double latitude;
    double longitude;

    double latitude2;
    double longitude2;

    //last location
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    String tempat;
    int status_lokasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);

        tempat = getIntent().getStringExtra("tempat");
        Toast.makeText(getApplicationContext(),tempat,Toast.LENGTH_SHORT).show();

        status_lokasi = 0;
        //button
        bSoal = new Button[4];
        bSoal[0] = (Button)findViewById(R.id.soal1);
        bSoal[1] = (Button)findViewById(R.id.soal2);
        bSoal[2] = (Button)findViewById(R.id.soal3);
        bSoal[3] = (Button)findViewById(R.id.soal4);
        cariTempat = (Button)findViewById(R.id.liatLokasi);

        db = new DatabaseHelper(getApplicationContext());
        cariTempat.setOnClickListener(this);

        id = new int[5];
        tipeSoal = new String[5];


        List<Integer> listId = db.getAllIdTempat(tempat);
        int i = 0;

        for(Integer tempid : listId){
            id[i] = tempid;
            //bSoal[i].setText(""+tempid);
            i+=1;
        }

        List<String> listTipeTempat = db.getAllTipeTempat(tempat);
        i = 0;
        for(String tempid : listTipeTempat){
            tipeSoal[i] = tempid;
            bSoal[i].setText(""+tempid);
            i+=1;
        }

        /*
        List<Soal> listSoal = db.getAllIdTipeTempat(tempat);
        int i = 0;
        for(Soal tempSoal : listSoal){
            id[i] = (int) tempSoal.getId();
            tipeSoal[i] = tempSoal.getTipeSoal();
            bSoal[i].setText(""+tipeSoal[i]);
            i+=1;
        }
        */

        Soal so = new Soal();
        for(i=0;i<4;i++){
            so = db.getSoal(id[i]);
            if(so.getStatus() == 1){
                bSoal[i].setText("Correct");
                bSoal[i].setBackgroundColor(Color.rgb(15,250,15));
            }
            bSoal[i].setOnClickListener(this);
        }
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        Tempat tempat1 = new Tempat();
        tempat1 = db.getTempat(tempat);
        latitude2 = Double.parseDouble(tempat1.getLatitude());
        longitude2 = Double.parseDouble(tempat1.getLongitude());
        Log.d("asep",tempat1.getLatitude() + " "+ tempat1.getLongitude() + " " + tempat1.getNmTempat());

        if(checkGooglePlayServices()){
            builGoogleApiClient();
            createLocationRequest();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.soal1 : {
                pindahSoalRinci(0);
                break;
            }
            case R.id.soal2 : {
                pindahSoalRinci(1);
                break;
            }
            case R.id.soal3 : {
                pindahSoalRinci(2);
                break;
            }
            case R.id.soal4 : {
                pindahSoalRinci(3);
                break;
            }
            case R.id.liatLokasi : {
                Toast.makeText(getApplicationContext(),"Lihat lokasi",Toast.LENGTH_SHORT).show();
                jalan();
                break;
            }
            case R.id.back : {
                finish();
                Intent bac = new Intent(SoalActivity.this, ListTempat.class);
                startActivity(bac);
                break;
            }
        }
    }

    public void pindahSoalRinci(int soalId){
        if(status_lokasi==1){
            finish();
            Intent i;
            if(tipeSoal[soalId].equals("teks")){
                i = new Intent(SoalActivity.this,SoalRinciActivity.class);
                i.putExtra("id", id[soalId]);
                startActivity(i);
            }else{
                if(id[soalId]!=15) {
                    i = new Intent(SoalActivity.this, GambarSoalActivity.class);
                    i.putExtra("id", id[soalId]);
                    startActivity(i);
                }else{
                    i = new Intent(SoalActivity.this, SoalKhusus.class);
                    i.putExtra("id", id[soalId]);
                    startActivity(i);
                }
            }


        }else{
            Toast.makeText(getApplicationContext(),"Anda tidak berada di sekitar " + tempat, Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized  void builGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkGooglePlayServices();
        try{
            if(mGoogleApiClient.isConnected()){
                startLocationUpdates();
            }
        }catch (Exception e){

        }
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        try {
            if(mGoogleApiClient.isConnected()){
                mGoogleApiClient.disconnect();
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            stopLocationUpdates();
        }catch (Exception e){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Mengecek aplikasi belum terkoneksi atau mecoba koneksi
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Service Harus Diinstall",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Location loc = new Location("dummy");
        loc.setLatitude(latitude2);
        loc.setLongitude(longitude2);
        Log.d("LOC", "" + loc.getLatitude() + " " + loc.getLongitude());
        Log.d("LOC2", "" + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
        if(mLastLocation.distanceTo(loc)<50){
            status_lokasi = 1;
        }else{
            status_lokasi=0;
        }
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //mengecek google play service di device
    private boolean checkGooglePlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
              /*
               * jika google play service tidak ada atau perlu diupdate
               * akan mengembalikan kode
               * SUCCESS,
               * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
               * SERVICE_DISABLED, SERVICE_INVALID.
               */
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }

        return true;
    }

    void jalan(){
        Intent i = new Intent(SoalActivity.this,JalurActivity.class);
        i.putExtra("lat", latitude2);
        i.putExtra("long", longitude2);

        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SoalActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
