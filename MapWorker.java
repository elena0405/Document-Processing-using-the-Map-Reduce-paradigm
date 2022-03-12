import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Aceasta clasa reprezinta un worker din etapa Map.
 */
public class MapWorker extends Thread {
    int N;                  // retin numarul total de taskuri
    int P;                  // retin numarul total de workeri
    int thread_id;          // retin id-ul thread-ului curent
    ArrayList<Task> tasks;  // retin lista de taskuri

    /**
     * Constructorul aferent clasei.
     * @param thread_id reprezinta ID-ul worker-ului curent
     * @param tasks reprezinta taskurile create in thread-ul main
     * @param N reprezinta numarul total de taskuri create in thread-ul main
     * @param P reprezinta numarul total de workeri
     */
    public MapWorker(int thread_id, ArrayList<Task> tasks, int N, int P) {
        this.thread_id = thread_id;
        this.P = P;
        this.tasks = tasks;
        this.N = N;
    }

    @Override
    public void run() {
        // Thread-ului curent ii vor fi asociate cateva taskuri, anume
        // cele cuprinse in intervalul [start, end].
        // Calculez parametrii start si end (formulele le-am luat din laboratorul 1).
        int start = thread_id * N / P;
        int end = (thread_id + 1) * N / P;
        if (end > N) {
            end = N;
        }

        // Creez doua stringuri de cuvinte.
        String[] words, finalWords = new String[10000];
        // Creez un string ce contine delimitatorii.
        String delimiters = ";:/?~.,><`[]{}()!@#$%^&-_+'=*\"| \t\r\n\\";

        // Pentru fiecare task asociat worker-ului respectiv
        for (int i = start; i < end; i++) {
            try {
                // Initializez cu 0 valoarea variabilei, in care voi retine
                // dimensiunea cuvantului de lungime maxima.
                int maxLength = 0;
                // Creez un dictionar, ce va contine, pentru taskul curent:
                // - dimensiunea fiecarui cuvant din fragmentul asociat taskului respectiv
                // - numarul de cuvinte ce au lungimea egala cu dimensiunea respectiva
                Map<Object, Integer> dictionary = new HashMap<>();
                // Deschid fisierul asociat taskului curent, pentru a citi valori din el.
                FileInputStream inputStream = new FileInputStream(tasks.get(i).getFile());
                try {
                    int index; // Aceasta variabila va itera prin fragmentul curent.
                    // Aici retin fiecaru cuvant citit.
                    StringBuilder str = new StringBuilder();

                    // Daca nu ma aflu la inceputul fisierului, voi citi si ultimul
                    // carcater din fragmentul anterior.
                    if (tasks.get(i).getOffset() != 0) {
                        index = 0;
                        // Ma mut cu o pozitie inaintea inceputului fragmentului respectiv.
                        inputStream.skip(tasks.get(i).getOffset() - 1);
                    } else {
                        // Daca ma aflu la inceputul fisierul, nu mai este nevoie sa retin
                        // nimic in plus.
                        index = 1;
                        // Ma mut la inceputul fragmentului respectiv.
                        inputStream.skip(tasks.get(i).getOffset());
                    }

                    int previous; // Aici retin caracterul citit anterior.
                    int c = inputStream.read(); // Citesc primul caracter de la offset-ul stabilit

                    // Daca sirul nu incepe cu un delimitator (exceptand primul fragment)
                    // inseamna ca am un cuvant incomplet;
                    // o sa citesc caracterele pana cand dau de un delimitator, insa nu contorizez
                    // aceste caractere ca sa adaug lungimea in dictionar, pentru ca ele au fost
                    // citite si retinute deja de fragmentul anterior. Trebuie doar sa trec peste ele.
                    if ((tasks.get(i).getOffset() != 0) && (delimiters.indexOf(c) == -1)) {
                        // Cat timp mai am de citit acea bucata de cuvant
                        while ((c != -1) && (delimiters.indexOf(c) == -1)) {
                            // Citesc caracterele ramase din cuvant
                            c = inputStream.read();
                            index++;
                        }
                    }

                    // Retin ultimul caracter citit.
                    previous = c;

                    // In continuare, voi citi fragmentul.
                    // Ca sa ma opresc, trebuie sa termin de citit ultimul cuvant din fragment, insa
                    // sa nu depasesc lungimea fragmentului cu mult si sa ma opresc la finalul
                    // fisierului. De asemenea, daca am depasit dimensiunea fragmentului, voi continua
                    // citirea din fisier doar daca nu am terminat de citit cuvantul curent.
                    while ((c != -1) &&
                            ((index <= tasks.get(i).getSize()) ||
                                    (delimiters.indexOf((char) c) == -1 &&
                                            index > tasks.get(i).getSize() &&
                                            delimiters.indexOf((char) previous) == -1))) {
                        str.append((char) c);
                        // Incrementez numarul de caractere citite
                        index++;
                        previous = c;
                        // Citesc urmatorul caracter
                        c = inputStream.read();
                    }

                    // Separ in cuvinte fragmentul citit.
                    words = str.toString().split("\\W+");
                    // Daca am gasit cuvinte in fragmentul curent
                    if (words.length > 0) {
                        // Retin in variabila maxLength lungimea maxima a cuvintelor
                        // din fragment.
                        for (String s : words) {
                            if (s.length() > maxLength) {
                                maxLength = s.length();
                            }
                            // Daca dimensiunea cuvantului curent este deja in dictionar
                            if (dictionary.containsKey(s.length()) && s.length() > 0) {
                                // Incrementez numarul de aparitii ale cuvintelor cu lungimea
                                // egala cu dimensiunea cuvantului curent.
                                dictionary.put(s.length(), dictionary.get(s.length()) + 1);
                            } else if ((s.length() > 0) && !(dictionary.containsKey(s.length()))) {
                                // In caz contrar, daca lungimea cuvantului curent nu este deja
                                // in dictionar, adaug in dictionar o pereche noua (cheie, valoare),
                                // unde cheie = dimensiuea cuvantului, iar valoare = 1, deoarece,
                                // initial, am o singura aparitie a unui cuvant cu aceasta dimensiune
                                dictionary.put(s.length(), 1);
                            }
                        }

                        index = 0;
                        // Retin in variabila finalWords doar cuvintele care au dimensiunea
                        // egala cu lungimea maxima.
                        for (String s : words) {
                            if (s.length() == maxLength) {
                                finalWords[index] = s;
                                index++;
                            }
                        }
                    }

                    // Adaug in lista de tupluri dictionarul, lista de cuvinte ,finalWords, si numele fisierului
                    // asociat taskului respectiv.
                    Tema2.list.add(new MapTriple(tasks.get(i).getFile(), dictionary, finalWords));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
