package id.fantasticfive.teri.dosen.models;


/**
 * Created by D-Y-U on 09/10/2017.
 */

public class JadwalAjar {
    private String kodeMataKuliah;
    private String kelas;
    private String namaMataKuliah;
    private String hari;
    private String jam;
    private String ruang;
    private String ruangUjian;
    private String user_nomorInduk;
    private int notifID;

    public JadwalAjar(String kodeMataKuliah, String kelas, String namaMataKuliah, String hari, String jam, String ruang, String user_nomorInduk) {
        this.kodeMataKuliah = kodeMataKuliah;
        this.kelas = kelas;
        this.namaMataKuliah = namaMataKuliah;
        this.hari = hari;
        this.jam = jam;
        this.ruang = ruang;
        this.user_nomorInduk = user_nomorInduk;
        this.notifID = 0;
    }

    public int getNotifID() {
        return notifID;
    }

    public void setNotifID(int notifID) {
        this.notifID = notifID;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getNamaMataKuliah() {
        return namaMataKuliah;
    }

    public void setNamaMataKuliah(String namaMataKuliah) {
        this.namaMataKuliah = namaMataKuliah;
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

    public String getRuangUjian() {
        return ruangUjian;
    }

    public void setRuangUjian(String ruangUjian) {
        this.ruangUjian = ruangUjian;
    }

    public String getUser_nomorInduk() {
        return user_nomorInduk;
    }

    public void setUser_nomorInduk(String user_nomorInduk) {
        this.user_nomorInduk = user_nomorInduk;
    }
}


