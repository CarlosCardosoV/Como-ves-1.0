package com.eduardoapps.comoves;

import android.graphics.Bitmap;

/**
 * Created by Eduardo on 17/09/2016.
 */
public class Revista {

    int id_revista, reviews, views;
    String titulo, descripcion, año;
    int  portada, bigPortada;

    public Revista(){}

    public int getPortada() {
        return portada;
    }

    public void setPortada(int portada) {
        this.portada = portada;
    }

    public int getBigPortada() {
        return bigPortada;
    }

    public void setBigPortada(int bigPortada) {
        this.bigPortada = bigPortada;
    }

    public int getId_revista() {
        return id_revista;
    }

    public void setId_revista(int id_revista) {
        this.id_revista = id_revista;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }
}
