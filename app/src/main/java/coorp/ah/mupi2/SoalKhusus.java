package coorp.ah.mupi2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import coorp.ah.mupi2.helper.DatabaseHelper;
import coorp.ah.mupi2.model.Soal;
import coorp.ah.mupi2.model.Tempat;

public class SoalKhusus extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    DatabaseHelper db;
    TextView gambarsoal;
    ImageView img;
    EditText jawaban;
    String kunciJawaban;
    int id;
    Soal soal;
    Button submit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambar_soal);

        //soal
        id = getIntent().getIntExtra("id",0);
        db = new DatabaseHelper(getApplicationContext());
        soal = db.getSoal(id);
        kunciJawaban = soal.getKunciJawaban();

        //button
        submit = (Button)findViewById(R.id.tombolOk);
        submit.setOnClickListener(this);
        back = (Button)findViewById(R.id.tombolClear);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        gambarsoal = (TextView)findViewById(R.id.soalGambar);
        jawaban = (EditText)findViewById(R.id.soaJawaban);
        img = (ImageView) findViewById(R.id.gambarGambar);
        String uri = "drawable/" + soal.getGambar();
        gambarsoal.setText("" + soal.getSoal());

        // int imageResource = R.drawable.icon;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable image = getResources().getDrawable(imageResource);
        img.setImageDrawable(image);
        scaleImage(img, 350);

        if(soal.getStatus()==1){
            jawaban.setText(kunciJawaban);
            jawaban.setEnabled(false);
           // submit.setClickable(false);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            //device memiliki accelerometer lanjutkan
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }else{

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tombolOk : {
                Log.d("jwb", "" + jawaban.getText());
                Log.d("kunci", soal.getKunciJawaban());

                if(jawaban.getText().toString().equalsIgnoreCase(soal.getKunciJawaban())){
                    Toast.makeText(getApplicationContext(), "Selamat jawaban anda benar", Toast.LENGTH_SHORT).show();
                    db.StatusSoal(soal);
                    finish();
                    if(db.getSumStat(soal) == 4){
                        Tempat t = db.getTempat(soal.getTempat());
                        Log.d("tag", "" + (t.getId()+1));
                        if(t.getId()+1 == 5){
                            Toast.makeText(this,"You are the Champion", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SoalKhusus.this, Credit.class);
                            startActivity(i);
                        }else{
                            t = db.getTempatbyID(t.getId()+1);
                            db.StatusTempat(t);
                            Intent i = new Intent(SoalKhusus.this, ListTempat.class);
                            startActivity(i);
                        }
                    }else{
                        Intent i = new Intent(SoalKhusus.this, SoalActivity.class);
                        i.putExtra("tempat", soal.getTempat());
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Jawaban anda masih salah",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.tombolClear : {
                finish();
                Intent in = new Intent(SoalKhusus.this, SoalActivity.class);
                in.putExtra("tempat", soal.getTempat());
                startActivity(in);
                break;
            }
        }
    }

    private void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SoalKhusus.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double az=0;
        //menangkap perubahan nilai sensor
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            az = event.values[2];
            if(az<=-8){
                //jika hp dibalikan
                gambarsoal.setText(soal.getSoal());
                img.setVisibility(View.VISIBLE);
                jawaban.setEnabled(true);
                submit.setEnabled(true);
            }else{
                //jika hp dibalikan
                gambarsoal.setText("Hadapkan Ke bawah HP anda");
                img.setVisibility(View.GONE);
                jawaban.setEnabled(false);
                submit.setEnabled(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
