import java.io.File;

/**
 * Creez o clasa ce contine informatiile aferente unui task.
 */
public class Task {
    private final int offset; // retin offset-ul
    private final int size;   // retin dimenziunea fragmentului
    private final File file;  // retin fisierul din care face parte fragmentul respectiv

    /**
     * Constructorul aferent clasei Task
     * @param offset indica offset-ul fragmentului asociat taskului respectiv
     * @param size indica dimensiunea fragmentului asociat taskului respectiv
     * @param file indica fisierul din care este preluat fragmentul asociat taskului respectiv
     */
    public Task(int offset, int size, File file) {
        this.offset = offset;
        this.size = size;
        this.file = file;
    }

    /**
     * Metoda ce intoarce valoarea variabilei private file (folosita pentru accesul la aceasta variabila privata)
     * @return file - intoarce valoarea variabilei file
     */
    public File getFile() {
        return file;
    }

    /**
     * Metoda folosita pentru a intoarce valoarea variabilei offset (folisita pentru accesul la aceasta variabila privata)
     * @return offset - intoarce valoarea variabilei offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Metoda folosita pentru a intoarce valoarea variabilei size (folosita pentru accesul la aceasta variabila privata)
     * @return size - intoarce valoarea variabilei size
     */
    public int getSize() {
        return size;
    }
}
