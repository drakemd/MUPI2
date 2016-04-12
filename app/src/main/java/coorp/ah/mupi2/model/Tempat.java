package coorp.ah.mupi2.model;

/**
 * Created by hidayatasep43 on 14-Dec-15.
 */
public class Tempat {
    int id;
    String nmTempat;
    String Latitude;
    String Longitude;
    int status;

    public Tempat(){

    }

    public Tempat(int id,String nmTempat, String Latitude, String longitude, int status){
        this.id = id;
        this.nmTempat = nmTempat;
        this.Latitude = Latitude;
        this.Longitude = longitude;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setNmTempat(String nmTempat) {
        this.nmTempat = nmTempat;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNmTempat() {
        return nmTempat;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public int getStatus() {
        return status;
    }
}

