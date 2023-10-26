package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.models.DadosEpisodios;
import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.ConverteDados;
import org.springframework.cglib.core.Local;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "6585022c";

    private final Scanner in = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados converteDados = new ConverteDados();
    public void exibirMenu(){

        System.out.println("Digite o nome da série para busca: ");
        var serie = in.nextLine();
        var json = consumoApi.obterDados( ADDRESS + serie.replace(" ", "+") + "&apikey=" + APIKEY);
        var dados = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        // Dados tempoarada

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++){
            var jsonSeasons = consumoApi.obterDados(ADDRESS + serie.replace(" ", "+") + "&season=" + i + "&apikey=" + APIKEY);
            DadosTemporada dadosTemporada = converteDados.obterDados(jsonSeasons, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }

//        temporadas.forEach(System.out::println);

        for(int i=0; i< dados.totalTemporadas(); i++){
            List<DadosEpisodios> epsSeasons = temporadas.get(i).episodios();
            epsSeasons.forEach(episodio -> {
                System.out.println(episodio.titulo());
            });
        }

//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n->n.startsWith("N"))
//                .map(String::toUpperCase)
//                .forEach(System.out::println);


        List<DadosEpisodios> dadosEpisodios  =
                temporadas.stream()
                        .flatMap(t -> t.episodios().stream())
                        .toList(); // Obs: O toList gera uma lista imutavel

        System.out.println("\nTop 5 episódios");

//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                        .toList();

//        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = in.nextInt();
        in.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        episodios.stream()
                .filter(e-> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e-> System.out.println(
                        "------------" +
                        "\nTemporada: " + e.getTemporada() +
                        "\nEpisodio: " + e.getTitulo() +
                        "\nData Lancamento " + e.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ));

    }
}
