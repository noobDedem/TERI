package id.fantasticfive.teri.admin.models;

/**
 * Created by alfifau on 16/11/2017.
 */

public class JadwalKuliah {
    private String kelas;
    private String hari;
    private String jam;
    private String ruang;
    private String kodeMatkul;

    public JadwalKuliah(String kelas, String hari, String jam, String ruang, String kodeMatkul) {
        this.kelas = kelas;
        this.hari = hari;
        this.jam = jam;
        this.ruang = ruang;
        this.kodeMatkul = kodeMatkul;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getRuang() {
        return ruang;
    }

    public void setRuang(String ruang) {
        this.ruang = ruang;
    }

    public String getKodeMatkul() {
        return kodeMatkul;
    }

    public void setKodeMatkul(String kodeMatkul) {
        this.kodeMatkul = kodeMatkul;
    }
}
