package coorp.ah.mupi2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import coorp.ah.mupi2.helper.DatabaseHelper;
import coorp.ah.mupi2.model.Tempat;

public class ListTempat extends AppCompatActivity implements View.OnClickListener{

    ImageView icon[];
    DatabaseHelper db;
    Button tempat[], back;
    Tempat t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tempat);

        tempat = new Button[4];
        tempat[0] = (Button)findViewById(R.id.fpmipaa);
        tempat[1] = (Button)findViewById(R.id.perpus);
        tempat[2] = (Button)findViewById(R.id.fpmipac);
        tempat[3] = (Button)findViewById(R.id.fptk);

        icon = new ImageView[4];
        icon[0]=(ImageView)findViewById(R.id.star1);
        icon[1]=(ImageView)findViewById(R.id.star2);
        icon[2]=(ImageView)findViewById(R.id.star3);
        icon[3]=(ImageView)findViewById(R.id.star4);


        Tempat[] ta = new Tempat[4];
        db = new DatabaseHelper(getApplicationContext());
        db.getReadableDatabase();
        ta[0] = db.getTempat("FPMIPAA");
        ta[1] = db.getTempat("PERPUSTAKAAN");
        ta[2] = db.getTempat("FPMIPAC");
        ta[3] = db.getTempat("FPTK");

        for(int i=0;i<4;i++){
            if(ta[i].getStatus() == 0){
                icon[i].setImageResource(R.drawable.gembok);
                tempat[i].setVisibility(View.GONE);
                tempat[i].setEnabled(false);
                /*
                tempat[i].setBackgroundColor(Color.RED);
                tempat[i].setText("Terkunci");
                */
            }else{
                icon[i].setImageResource(R.drawable.star);
                //tempat[i].setBackgroundColor(Color.BLUE);
            }
            tempat[i].setOnClickListener(this);
        }
        back = (Button) findViewById(R.id.button);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fpmipaa : {
                pindahActivity("FPMIPAA");
                break;
            }
            case R.id.perpus: {
                pindahActivity("PERPUSTAKAAN");
                break;
            }
            case R.id.fpmipac : {
                pindahActivity("FPMIPAC");
                break;
            }
            case R.id.fptk : {
                pindahActivity("FPTK");
                break;
            }
            case R.id.button : {
                finish();
                Intent in = new Intent(ListTempat.this, MainActivity.class);
                startActivity(in);
                break;
            }
        }
    }

    public void pindahActivity(String tempat){
        t = db.getTempat(tempat);
        if(t.getStatus() == 1){
            finish();
            Intent i = new Intent(ListTempat.this,SoalActivity.class);
            i.putExtra("tempat",tempat);
            startActivity(i);
        }else{
            Toast.makeText(this, "Tempat belum terbuka, selesaikan dulu tempat sebelumnya",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListTempat.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
