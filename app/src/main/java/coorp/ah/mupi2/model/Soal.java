package coorp.ah.mupi2.model;

/**
 * Created by hidayatasep43 on 17-Dec-15.
 */
public class Soal {
    long id;
    String soal;
    String kunciJawaban;
    String Tempat;
    int status;
    String tipeSoal;
    String gambar;

    //constructor
    public Soal(){

    }

    public  Soal(int id, String soal, String kuncijawaban,String Tempat, int status, String tipeSoal){
        this.id = id;
        this.soal=soal;
        this.kunciJawaban=kuncijawaban;
        this.Tempat=Tempat;
        this.status = status;
        this.tipeSoal = tipeSoal;
    }

    public  Soal(int id, String soal, String kuncijawaban,String Tempat, int status, String tipeSoal, String gambar){
        this.id = id;
        this.soal=soal;
        this.kunciJawaban=kuncijawaban;
        this.Tempat=Tempat;
        this.status = status;
        this.tipeSoal = tipeSoal;
        this.gambar = gambar;
    }

    //set
    public void setId(int id){
        this.id = id;
    }

    public void setSoal(String soal){
        this.soal = soal;
    }


    public void setKunciJawaban(String kunciJawaban){
        this.kunciJawaban = kunciJawaban;
    }

    public void setTempat(String tempat){
        this.Tempat = tempat;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setTipeSoal(String tipeSoal) {
        this.tipeSoal = tipeSoal;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public long getId(){
        return  this.id;
    }

    public String getSoal(){
        return  this.soal;
    }


    public String getKunciJawaban(){
        return  this.kunciJawaban;
    }

    public String getTempat(){
        return  this.Tempat;
    }

    public  int getStatus(){
        return this.status;
    }

    public String getTipeSoal() {
        return this.tipeSoal;
    }

    public String getGambar() {
        return this.gambar;
    }
}
