package com.example.spf;

public class Devices {
    String nome;
    String local;
    int id;
    int ref_agua;
    int ref_comida;

    public Devices(String nome, int id, int ref_agua, String local, int ref_comida) {
        this.nome = nome;
        this.ref_agua = ref_agua;
        this.local = local;
        this.id = id;
        this.ref_comida = ref_comida;
    }

    public String getNome() {
        return nome;
    }

    public String getLocal() {
        return local;
    }

    public int getRef_agua() {
        return ref_agua;
    }

    public int getRef_comida() {
        return ref_comida;
    }

    public int getId() {
        return id;
    }
}
