package com.example.occupationdessalles.beans;

import com.google.gson.annotations.SerializedName;

public class Salle {
    @SerializedName("_id")
    private String id;
    @SerializedName("code")
    private String nom;
    @SerializedName("libelle")
    private String type;
    @SerializedName("bloc")
    private String bloc;
    @SerializedName("nameBloc")
    private String nameBloc;

    public String getNameBloc() {
        return nameBloc;
    }

    public void setNameBloc(String nameBloc) {
        this.nameBloc = nameBloc;
    }

    public Salle(){

    }
    public String getBloc() {
        return bloc;
    }

    public void setBloc(String bloc) {
        this.bloc = bloc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", bloc=" + bloc +
                '}';
    }
}
