package com.rotavital.tuguy.service;

import com.rotavital.tuguy.model.requisicao;

public class FilaRequisicoes {

    private No inicio;
    private No fim;

    private static class No {
        private requisicao requisicao;
        private No proximo;

        public No(requisicao requisicao) {
            this.requisicao = requisicao;
        }
    }

    public void enfileirar(requisicao requisicao) {
        No novoNo = new No(requisicao);

        if (inicio == null) {
            inicio = novoNo;
            fim = novoNo;
            return;
        }

        fim.proximo = novoNo;
        fim = novoNo;
    }

    public requisicao desenfileirar() {
        if (inicio == null) {
            return null;
        }

        requisicao requisicaoRetirada = inicio.requisicao;
        inicio = inicio.proximo;

        if (inicio == null) {
            fim = null;
        }

        return requisicaoRetirada;
    }

    public requisicao consultarInicio() {
        if (inicio == null) {
            return null;
        }

        return inicio.requisicao;
    }

    public boolean estaVazia() {
        return inicio == null;
    }
}
