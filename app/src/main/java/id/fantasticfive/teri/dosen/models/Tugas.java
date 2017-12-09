package id.fantasticfive.teri.dosen.models;

/**
 * Created by anism on 07/11/2017.
 */

public class Tugas {
    private String kodeTugas;
    private String nama;
    private String format;
    private String deadline;
    private String keterangan;
    private String namaMatkul;
    private String kodeMataKuliah;

    public Tugas(String kodeTugas, String nama, String format, String deadline, String keterangan, String namaMatkul, String kodeMataKuliah) {
        this.kodeTugas = kodeTugas;
        this.nama = nama;
        this.format = format;
        this.deadline = deadline;
        this.keterangan = keterangan;
        this.namaMatkul = namaMatkul;
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public String getKodeTugas() {
        return kodeTugas;
    }

    public void setKodeTugas(String kodeTugas) {
        this.kodeTugas = kodeTugas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaMatkul() {
        return namaMatkul;
    }

    public void setNamaMatkul(String namaMatkul) {
        this.namaMatkul = namaMatkul;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

}

