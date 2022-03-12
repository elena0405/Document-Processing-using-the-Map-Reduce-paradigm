import java.io.File;
import java.util.Map;

/**
 * Creez o clasa ce reprezinta un tuplu format din:
 * 1. fisierul ce se doreste a fi prelucrat
 * 2. lista de cuvinte cu dimnesiunea maxima din fisierul respectiv
 * 3. map-ul ce contine perechi de tipul (key, value), unde
 *    key este lunigimea unui cuvant, iar value reprezinta numarul de
 *    cuvinte care au lungimea respectiva.
 */
public class MapTriple {
    private final String[] words;
    private final File file;
    private final Map<Object, Integer> hashMap;

    /**
     * Constructorul aferent clasei.
     * @param file reprezinta numele fisierului ce contine cuvintele respective
     * @param hashMap reprezinta map-ul ce contine lungimile cuvintelor si numarul de cuvinte
     *                de o anumita lungime
     * @param words reprezinta lista de cuvinte care au dimensiunea egala cu lungimea maxima
     */
    MapTriple(File file, Map<Object, Integer> hashMap, String[] words) {
        this.file = file;
        this.words = words;
        this.hashMap = hashMap;
    }

    /**
     * Metoda care intoarce numele fisierului ce se doreste a fi prelucrat
     * (folosita pentru accesul la aceasta variabila privata).
     * @return file - intoarce valoarea variabilei file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Metoda care determina hashMap-ul (folosita pentru accesul la aceasta variabila privata).
     * @return hashaMap - intoarce valoarea variabilei hashMap.
     */
    public Map<Object, Integer> getHashMap() {
        return hashMap;
    }

    /**
     * Metoda care intoarce lista de cuvinte ce au dimensiunea egala cu lungimea maxima din hashMap
     * (folosita pentru accesul la aceasta variabila privata).
     * @return words - intoarce valoarea variabilei words.
     */
    public String[] getWords() {
        return words;
    }
}
