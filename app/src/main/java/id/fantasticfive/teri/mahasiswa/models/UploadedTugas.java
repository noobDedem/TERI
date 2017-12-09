package id.fantasticfive.teri.mahasiswa.models;

/**
 * Created by Demas on 11/21/2017.
 */

public class UploadedTugas {

    private String kodeMataKuliah;
    private String kodeTugas;
    private String namaFile;
    private String filePath;
    private int id;

    public UploadedTugas(String kodeMataKuliah, String kodeTugas, String namaFile, String filePath, int id) {
        this.kodeMataKuliah = kodeMataKuliah;
        this.kodeTugas = kodeTugas;
        this.namaFile = namaFile;
        this.filePath = filePath;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public String getKodeTugas() {
        return kodeTugas;
    }

    public void setKodeTugas(String kodeTugas) {
        this.kodeTugas = kodeTugas;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
