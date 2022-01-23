package com.example.occupationdessalles.beans;
import com.google.gson.annotations.SerializedName;
public class Occupation {
    @SerializedName("_id")
    private String id;
    @SerializedName("idSalle")
    private String salle;
    @SerializedName("idCreneau")
    private String creneau;
    @SerializedName("date")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Occupation() {
    }

    public Occupation(String salle, String creneau) {
        this.salle = salle;
        this.creneau = creneau;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getCreneau() {
        return creneau;
    }

    public void setCreneau(String creneau) {
        this.creneau = creneau;
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "id='" + id + '\'' +
                ", salle='" + salle + '\'' +
                ", creneau='" + creneau + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
