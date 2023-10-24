package br.com.alura.screenmatch.services;

import br.com.alura.screenmatch.models.DadosSerie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ConverteDados implements IConverteDados{
    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public <T> T obterDados(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
