package id.fantasticfive.teri.mahasiswa.models;

/**
 * Created by Demas on 10/28/2017.
 */

public class Tugas {
    private String kodeTugas;
    private String nama;
    private String format;
    private String deadline;
    private String keterangan;
    private String namaMatkul;
    private String kodeMataKuliah;
    private String enrollmentKey;

    public Tugas(String kodeTugas, String nama, String format, String deadline, String keterangan, String namaMatkul, String kodeMataKuliah, String enrollmentKey) {
        this.kodeTugas = kodeTugas;
        this.nama = nama;
        this.format = format;
        this.deadline = deadline;
        this.keterangan = keterangan;
        this.namaMatkul = namaMatkul;
        this.kodeMataKuliah = kodeMataKuliah;
        this.enrollmentKey = enrollmentKey;
    }

    public String getEnrollmentKey() {
        return enrollmentKey;
    }

    public void setEnrollmentKey(String enrollmentKey) {
        this.enrollmentKey = enrollmentKey;
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
