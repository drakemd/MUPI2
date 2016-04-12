package coorp.ah.mupi2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import coorp.ah.mupi2.helper.DatabaseHelper;
import coorp.ah.mupi2.model.Soal;
import coorp.ah.mupi2.model.Tempat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bMain, bAt;
    DatabaseHelper db;
    Soal soal[];
    Tempat tempat[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bMain = (Button)findViewById(R.id.mulai);
        bAt = (Button)findViewById(R.id.aturan);

        bMain.setOnClickListener(this);
        bAt.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("dataasep",MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        SharedPreferences.Editor ed = sp.edit();
        if(count==0){
            ed.putInt("count", 1);
            //masukan query soal
            db = new DatabaseHelper(getApplicationContext());
            //insert soal
            soal = new Soal[16];
            int i = 0;
            long temp;

            soal[0] = new Soal(0,"Apa nama ruangan ini?","Auditorium", "FPMIPAA", 0,"gambar", "pic1");
            soal[1] = new Soal(1,"Sebutkan nama Access-Point yang ada di gedung utara?","FPMIPA-UTARA", "FPMIPAA", 0,"teks");
            soal[2] = new Soal(2,"Negara apakah yang membantu membangun gedung FPMIPA-A?","Jepang", "FPMIPAA", 0,"teks");
            soal[3] = new Soal(3,"Jurusan apakah mahasiswa di gambar ini?","Kimia", "FPMIPAA", 0,"gambar", "pic2");
            soal[4] = new Soal(4,"Apa warna cat tiang depan PERPUSTAKAAN?","Merah", "PERPUSTAKAAN", 0,"teks");
            soal[5] = new Soal(5,"Apa nama tempat di Perpustakaan ini?","Pojok Perancis", "PERPUSTAKAAN", 0,"gambar", "pic5");
            soal[6] = new Soal(6,"Apa yang harus dibawa mahasiswa sebelum masuk Perpustakaan?","KTM", "PERPUSTAKAAN", 0,"teks");
            soal[7] = new Soal(7,"Berapa lama maksimum peminjaman buku (gunakan huruf)?","Dua Minggu", "PERPUSTAKAAN", 0,"teks");
            soal[8] = new Soal(8,"Sebutkan ruangan kelas terbesar di FPMIPAC","IK-201", "FPMIPAC", 0,"teks");
            soal[9] = new Soal(9,"Siapakah Dosen Wali kelas C2 2013","Eddy", "FPMIPAC", 0,"teks");
            soal[10] = new Soal(10,"Gedung apakah ini","GIK", "FPMIPAC", 0,"gambar","pic7gik");
            soal[11] = new Soal(11,"Siapakah Mahasiswa Ilkom yang ada di gambar?","Asep", "FPMIPAC", 0,"gambar","pic8gik");
            soal[12] = new Soal(12,"Di lantai berapa ruang dosen Teknik Arsitektur (gunakan huruf)?","Dua", "FPTK", 0,"teks");
            soal[13] = new Soal(13,"Jurusan apakah mahasiswa di gambar?","Teknik Sipil", "FPTK", 0,"gambar", "pic3");
            soal[14] = new Soal(14,"Di lantai berapa ruangan di gambar (gunakan huruf)?", "Empat", "FPTK", 0,"gambar", "pic4");
            soal[15] = new Soal(15,"Siapa ketua HME 2015/2016?", "Diaz Kautsar", "FPTK", 0,"teks");
            for(i=0;i<16;i++){
                temp = db.insertSoal(soal[i]);
            }

            //insert Tempat
            tempat = new Tempat[4];
            tempat[0]= new Tempat(0,"FPMIPAA","-6.861988", "107.589890" ,1);
            tempat[1]= new Tempat(0,"PERPUSTAKAAN","-6.860997", "107.591401",0);
            tempat[2]= new Tempat(0,"FPMIPAC","-6.860290","107.589882",0);
            tempat[3] = new Tempat(0, "FPTK", "-6.864083", "107.593538", 0);
            for(i=0;i<4;i++){
                db.insertTempat(tempat[i]);
            }

        }
        ed.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.mulai :
                finish();
                i = new Intent(MainActivity.this, ListTempat.class);
                startActivity(i);
                break;
            case R.id.aturan :
                finish();
                i = new Intent(MainActivity.this, AturanActivity.class);
                startActivity(i);
                break;
        }
    }
}
