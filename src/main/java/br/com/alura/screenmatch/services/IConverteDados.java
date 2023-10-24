package br.com.alura.screenmatch.services;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> tClass);
    // Entidade Generica
}
