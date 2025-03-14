package br.com.joinville.mapa;

public class Localizacao {
    private int id;
    private double latitude;
    private double longitude;
    private int idAreaVerde;

    public Localizacao(int id, double latitude, double longitude, int idAreaVerde) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idAreaVerde = idAreaVerde;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIdAreaVerde() {
        return idAreaVerde;
    }

    @Override
    public String toString() {
        return "Localização [ID: " + id + ", Latitude: " + latitude + ", Longitude: " + longitude + "]";
    }
}
