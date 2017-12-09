package id.fantasticfive.teri.dosen.models;

/**
 * Created by rindh27 on 11/6/2017.
 */

public class BankTugas {

    private String kodeMataKuliah;
    private String kodeTugas;
    private String namaFile;
    private String filePath;

    public BankTugas(String kodeMataKuliah, String kodeTugas, String namaFile, String filePath) {
        this.kodeMataKuliah = kodeMataKuliah;
        this.kodeTugas = kodeTugas;
        this.namaFile = namaFile;
        this.filePath = filePath;
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
