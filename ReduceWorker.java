import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creez o clasa ce reprezinta un worker aferent etapei de Reduce.
 */
public class ReduceWorker extends Thread {
    private int nr;
    private Float rank;
    private final File file;
    private Integer maxLength;
    private Integer nrOfWords;
    private final ArrayList<MapTriple> mapTriple;

    /**
     * Constructorul aferent clasei ReduceWorker
     * @param file indica numele fisierului ce va fi prelucrat
     * @param mapTriple indica lista de tupluri ce rezulta din etapa de Map,
     *                  care va fi prelucrata in etapa Reduce
     */
    public ReduceWorker(File file, ArrayList<MapTriple> mapTriple) {
        nr = 0;                         // aici retin numarul de cuvinte ce au lungimea maxima
        rank = 0.0f;                    // aici retin rangul fisierului
        maxLength = 0;                  // aici retin lungimea maxima a unui cuvant din fisier
        this.file = file;
        this.mapTriple = mapTriple;
    }

    /**
     * Metoda folosita pentru a face operatiile etapei de Reduce.
     */
    @Override
    public void run() {
        // Aici retin map-ul rezultat din concatenarea tuturor map-urilor din lista mapTriple.
        Map<Object, Integer> mapResult;

        // Daca lista nu este vida
        if (mapTriple.size() > 0) {
            // Initializez map-ul rezultat cu valoarea primului map din lista de tupluri.
            mapResult = mapTriple.get(0).getHashMap();

            // pentru fiecare tuplu din lista de tupluri
            for (int i = 1; i < mapTriple.size(); i++) {
                // Fac concatenarea map-urilor din lista de tupluri.
                mapResult = Stream.concat(mapResult.entrySet().stream(),
                        mapTriple.get(i).getHashMap().entrySet().stream()).collect(
                        Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                Integer::sum));
            }

            // Folosesc aceasta variabila pentru a determina suma.
            float sum = 0.0f;

            // Pentru perechile (key, value) din map-ul nou creat (result), calculez
            // numarul total de cuvinte si determin suma acestora, conform specificatiilor
            // din enunt.
            for (Map.Entry<Object, Integer> entry : mapResult.entrySet()) {
                nr += entry.getValue();
                if (maxLength < (Integer) entry.getKey()) {
                    maxLength = (Integer) entry.getKey();
                }
                sum += Tema2.fibonacci.get((Integer) entry.getKey() + 1) * entry.getValue();
            }

            // Calculez rangul fisierului asociat worker-ului.
            rank = sum / nr;

            // Determin numarul de cuvinte cu lungimea maxima.
            nrOfWords = mapResult.get(maxLength);

        }
    }

    /**
     * Metoda care intoarce valoarea rangului fisierului curent
     * (folosita pentru accesul la aceasta variabila privata).
     * @return rank - intoarce valoarea rangului fisierului
     */
    public Float getRank() {
        return rank;
    }

    /**
     * Metoda care intoarce numarul de cuvinte din fisier, care au lungimea maxima
     * (folosita pentru accesul la aceasta variabila privata).
     * @return nrOfWords - intoarce valoarea numarului de cuvinte din fisierul curent, ce au lungimea maxima
     */
    public Integer getNrOfWords() {
        return nrOfWords;
    }

    /**
     * Metoda care determina lungimea cuvantului de dimensiune maxima din fisier
     * (folosita pentru accesul la aceasta variabila privata).
     * @return maxLength - intoarce lungimea cuvantului de dimensiune maxima din fisierul curent.
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Metoda care determina fisierul prelucrat (folosita pentru accesul la aceasta variabila privata).
     * @return file - intoarce valoarea fisierului prelucrat.
     */
    public File getFile() {
        return file;
    }

}
