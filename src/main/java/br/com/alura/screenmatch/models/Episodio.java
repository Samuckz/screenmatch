package br.com.alura.screenmatch.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeracao;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numero, DadosEpisodios d) {
        this.temporada = numero;
        this.titulo = d.titulo();
        this.numeracao = d.numeroEp();

        try{
            this.avaliacao = Double.valueOf(d.avaliacao());
        }catch(NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try{
            this.dataLancamento = LocalDate.parse(d.dataLancamento());
        }catch (DateTimeParseException exception){
            this.dataLancamento = null;
        }
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeracao=" + numeracao +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
