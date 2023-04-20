package com.example.spf;

public class Animal {

    String nome;
    String tipo;
    int peso;
    int idade;
    int id;

    public Animal(String nome, String tipo, int peso, int idade, int id) {
        this.nome = nome;
        this.tipo = tipo;
        this.peso = peso;
        this.idade = idade;
        this.id = id;
    }
    public Animal(String nome, String tipo, int peso, int idade) {
        this.nome = nome;
        this.tipo = tipo;
        this.peso = peso;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPeso() {
        return peso;
    }

    public int getIdade() {
        return idade;
    }

    public int getId() {
        return id;
    }
}
