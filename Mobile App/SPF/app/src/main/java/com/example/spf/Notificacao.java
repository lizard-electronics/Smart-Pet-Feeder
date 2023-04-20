package com.example.spf;

public class Notificacao {
    int id, critical;
    String name, descrition;

    public Notificacao(int id, int critical, String name, String descrition) {
        this.id = id;
        this.critical = critical;
        this.name = name;
        this.descrition = descrition;
    }

    public int getId() {
        return id;
    }

    public int getCritical() {
        return critical;
    }

    public String getName() {
        return name;
    }

    public String getDescrition() {
        return descrition;
    }
}
