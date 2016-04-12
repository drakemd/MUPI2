package coorp.ah.mupi2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import coorp.ah.mupi2.helper.DatabaseHelper;
import coorp.ah.mupi2.model.Soal;
import coorp.ah.mupi2.model.Tempat;

public class SoalRinciActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper db;
    TextView textSoal, jawaban;
    String kunciJawaban;
    int id;
    Soal soal;
    Button submit, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_rinci);

        id = getIntent().getIntExtra("id",0);
        db = new DatabaseHelper(getApplicationContext());
        soal = db.getSoal((long) id);
        submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        textSoal = (TextView)findViewById(R.id.textSoal);
        textSoal.setText(soal.getSoal());
        jawaban = (TextView) findViewById(R.id.editText);
        kunciJawaban = soal.getKunciJawaban();
        if(soal.getStatus()==1){
            jawaban.setText(kunciJawaban);
            jawaban.setEnabled(false);
           // submit.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button : {
                Log.d("jwb", "" + jawaban.getText());
                Log.d("kunci", soal.getKunciJawaban());

                if(jawaban.getText().toString().equalsIgnoreCase(soal.getKunciJawaban())){
                    Toast.makeText(getApplicationContext(),"Selamat jawaban anda benar",Toast.LENGTH_SHORT).show();
                    db.StatusSoal(soal);
                    finish();
                    if(db.getSumStat(soal) == 4){
                        Tempat t = db.getTempat(soal.getTempat());
                        Log.d("tag", "" + (t.getId()+1));
                        if(t.getId()+1 == 5){
                            Toast.makeText(this,"You are the Champion", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SoalRinciActivity.this, Credit.class);
                            startActivity(i);
                        }else{
                            t = db.getTempatbyID(t.getId()+1);
                            db.StatusTempat(t);
                            Intent i = new Intent(SoalRinciActivity.this, ListTempat.class);
                            startActivity(i);
                        }
                    }else{
                        Intent i = new Intent(SoalRinciActivity.this, SoalActivity.class);
                        i.putExtra("tempat", soal.getTempat());
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Jawaban anda masih salah",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.back : {
                finish();
                Intent in = new Intent(SoalRinciActivity.this, SoalActivity.class);
                in.putExtra("tempat", soal.getTempat());
                startActivity(in);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SoalRinciActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

}
