package com.entrevista.demo.model;

public class Entrevista {
    String entre, Desc, Perio, Fecha;

    public Entrevista(String entre, String desc, String perio, String fecha) {
        this.entre = entre;
        Desc = desc;
        Perio = perio;
        Fecha = fecha;
    }
    public Entrevista(){
            }

    public String getEntre() {
        return entre;
    }

    public String getDesc() {
        return Desc;
    }

    public String getPerio() {
        return Perio;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setEntre(String entre) {
        this.entre = entre;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setPerio(String perio) {
        Perio = perio;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
