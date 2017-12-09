package id.fantasticfive.teri.dosen.models;

/**
 * Created by anism on 06/11/2017.
 */

public class MataKuliah {
    private String kodeMataKuliah;
    private String namaMataKuliah;

    public MataKuliah(String kodeMataKuliah, String namaMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
        this.namaMataKuliah = namaMataKuliah;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public String getNamaMataKuliah() {
        return namaMataKuliah;
    }

    public void setNamaMataKuliah(String namaMataKuliah) {
        this.namaMataKuliah = namaMataKuliah;
    }
}
