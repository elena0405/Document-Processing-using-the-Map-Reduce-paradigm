import java.io.*;
import java.util.*;

/**
 * Aceasta este clasa principala.
 */
public class Tema2 {
    // Aici retin lista de tupluri aferenta fiecarui task, ce rezulta in urma
    // etapei de Map.
    public static ArrayList<MapTriple> list = new ArrayList<>();
    // Aici retin vectorul cuprimele 17 elemente ale sirului Fibonacci.
    public static ArrayList<Float> fibonacci = new ArrayList<>();

    /**
     * Aceasta este functia principala, in care am facut pasii esentiali.
     * @param args reprezinta variabila in care stochez datele de la
     *             citirea de la tastatura.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }

        int nrOfWorkers = Integer.parseInt(args[0]); // Retin numarul de workeri.
        String inputFile = args[1];  // Retin calea catre fisierul de intrare.
        String outputFile = args[2]; // Retin calea catre fisierul de iesire.

        File myFile = new File(inputFile);         // Deschid fisierul de citire.
        ArrayList<Task> tasks = new ArrayList<>(); // Creez un vector de taskuri.
        // Creez un vector de thread-uri pentru etapa de Map.
        MapWorker[] mapWorkers = new MapWorker[nrOfWorkers];
        ArrayList<MapTriple> groups;

        try {

            int numberOfBytes, numberOfFiles;

            Scanner scanner = new Scanner(myFile);
            // retin ndimensiunea maxima a unui fragment
            numberOfBytes = Integer.parseInt(scanner.nextLine());
            // retin numarul de fisiere din fisierul curent
            numberOfFiles = Integer.parseInt(scanner.nextLine());
            File[] files = new File[numberOfFiles]; // creez vectorul de fisiere

            int index = 0;
            while (scanner.hasNext()) {
                // retin in vectorul de fisiere fisierele citite
                files[index] = new File(scanner.nextLine());
                index++;
            }

            scanner.close(); // am terminat citirea

            int totalNumberOfTasks = 0, numberOfTasks, offset, size;

            for (int i = 0; i < numberOfFiles; i++) {
                // Aici retin offset-ul fiecarui task creat.
                offset = 0;
                // Pentru fiecare fisier, determin cate fragmente are,
                // in functie de lungimea sa si de lungimea maxima a
                // unui fragment.
                numberOfTasks = (int) (files[i].length() / numberOfBytes);
                if (files[i].length() % numberOfBytes != 0) {
                    numberOfTasks += 1;
                }
                // Pentru fiecare fragment, creez un task nou si il
                // adaug in lista de taskuri.
                for (int j = 0; j < numberOfTasks; j++) {
                    // Determin lungimea fragmnetului curent; ea va avea valoarea de mai jos
                    // deoarece, in cazul in care ma aflu la finalul fisierului, de obicei,
                    // ultimul fragment va avea o valoare mai mica decat dimensiunea maxima
                    // stabilita.
                    size = Math.min((int) files[i].length() - offset, numberOfBytes);
                    Task task = new Task(offset, size, files[i]);
                    tasks.add(task);
                    // Determin valoarea offset-ului pentru fragmentul urmator.
                    offset += numberOfBytes;
                }
                // Aici retin numarul total de taskuri pe care le creez,
                // in functie de numarul total de fragmente din toate
                // fisierele.
                totalNumberOfTasks += numberOfTasks;
            }

            // Creez thread-urile.
            for (int i = 0; i < nrOfWorkers; i++) {
                mapWorkers[i] = new MapWorker(i, tasks, totalNumberOfTasks, nrOfWorkers);
                mapWorkers[i].start();
            }

            // Dau join pe thread-uri.
            for (int i = 0; i < nrOfWorkers; i++) {
                try {
                    mapWorkers[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Creez sirul Fibonacci (adaug primele 17 elemente).
            fibonacci.add(0.0f);
            fibonacci.add(1.0f);
            fibonacci.add(1.0f);
            fibonacci.add(2.0f);
            fibonacci.add(3.0f);
            fibonacci.add(5.0f);
            fibonacci.add(8.0f);
            fibonacci.add(13.0f);
            fibonacci.add(21.0f);
            fibonacci.add(34.0f);
            fibonacci.add(55.0f);
            fibonacci.add(89.0f);
            fibonacci.add(144.0f);
            fibonacci.add(233.0f);
            fibonacci.add(377.0f);
            fibonacci.add(610.0f);
            fibonacci.add(987.0f);
            fibonacci.add(1597.0f);
            fibonacci.add(2584.0f);

            // Creez un vector de thread-uri pentru etapa de reduce.
            ReduceWorker[] reduceWorkers = new ReduceWorker[numberOfFiles];
            for (int i = 0; i < numberOfFiles; i++) {
                // In aceasta variabila, retin toate tuplurile aferente unui fisier.
                groups = new ArrayList<>();
                for (MapTriple element : list) {
                    if (element.getFile() == files[i]) {
                        groups.add(element);
                    }
                }

                // Creez un worker nou pentru etapa de Reduce, ce contine numele fisierului si
                // lista de tupluri aferenta acestuia.
                reduceWorkers[i] = new ReduceWorker(files[i], groups);
                reduceWorkers[i].start();
            }

            // Dau join pe noile thread-uri.
            for (int i = 0; i < numberOfFiles; i++) {
                try {
                    reduceWorkers[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Sortez lista de workeri aferenta taskului Reduce in functie de rang.
            Arrays.sort(reduceWorkers, Comparator.comparing(ReduceWorker::getRank));

            try {
                // Deschid fisierul de scriere.
                FileWriter f = new FileWriter(outputFile);
                BufferedWriter bufferedWriter = new BufferedWriter(f);

                // Scriu in fisierul deschis output-ul cerut in cerinta.
                for (int i = numberOfFiles - 1; i >= 0; i--) {
                    bufferedWriter.write(String.format(reduceWorkers[i].getFile().toString().substring(12) + "," + "%.2f", reduceWorkers[i].getRank()) + "," + reduceWorkers[i].getMaxLength() + "," + reduceWorkers[i].getNrOfWords());
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
                f.close(); // Inchid fisierul de scriere.

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
