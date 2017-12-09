package id.fantasticfive.teri.mahasiswa.models;

/**
 * Created by Demas on 10/28/2017.
 */

public class JadwalUjian {
    private String namaMataKuliah;
    private String tanggalUjian;
    private String jamUjian;
    private String ruangUjian;
    private String kelas;
    private int value;
    private int notifID;

    public JadwalUjian(String namaMataKuliah, String tanggalUjian, String jamUjian, String ruangUjian, String kelas) {
        this.namaMataKuliah = namaMataKuliah;
        this.tanggalUjian = tanggalUjian;
        this.jamUjian = jamUjian;
        this.ruangUjian = ruangUjian;
        this.kelas = kelas;
        this.value = 0;
        this.notifID = 0;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNotifID() {
        return notifID;
    }

    public void setNotifID(int notifID) {
        this.notifID = notifID;
    }

    public String getNamaMataKuliah() {
        return namaMataKuliah;
    }

    public void setNamaMataKuliah(String namaMataKuliah) {
        this.namaMataKuliah = namaMataKuliah;
    }

    public String getTanggalUjian() {
        return tanggalUjian;
    }

    public void setTanggalUjian(String tanggalUjian) {
        this.tanggalUjian = tanggalUjian;
    }

    public String getJamUjian() {
        return jamUjian;
    }

    public void setJamUjian(String jamUjian) {
        this.jamUjian = jamUjian;
    }

    public String getRuangUjian() {
        return ruangUjian;
    }

    public void setRuangUjian(String ruangUjian) {
        this.ruangUjian = ruangUjian;
    }
}
