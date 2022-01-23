package com.example.occupationdessalles.beans;
import com.google.gson.annotations.SerializedName;
public class Bloc {
    @SerializedName("_id")
    private int id;
    @SerializedName("code")
    private String nom;
    private Bloc(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Bloc{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
