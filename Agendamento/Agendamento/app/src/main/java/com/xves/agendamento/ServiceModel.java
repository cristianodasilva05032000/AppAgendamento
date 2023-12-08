package com.xves.agendamento;

public class ServiceModel {
    public String id;
    public String id_profissional;
    public String endereco;
    public String data;
    public String hora;
    public String valor;
    public String descricao;
    public String id_cliente;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getId_profissional() {
        return id_profissional;
    }

    public void setId_profissional(String id_profissional) {
        this.id_profissional = id_profissional;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getIdCliente() {
        return id_cliente;
    }
}
