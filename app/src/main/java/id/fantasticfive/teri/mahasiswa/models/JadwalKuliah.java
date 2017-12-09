package id.fantasticfive.teri.mahasiswa.models;

public class JadwalKuliah {
    private String namaMataKuliah;
    private String ruangMataKuliah;
    private String hariMataKuliah;
    private String waktuKuliah;
    private String tanggalUjian;
    private String jamUjian;
    private String ruangUjian;
    private String kelas;
    private int value; /* 0 = checkbox disable, 1 = checkbox enable */
    private int notifID;
    private int notifIDUjian;


    public JadwalKuliah(String namaMataKuliah, String ruangMataKuliah, String hariMataKuliah, String waktuKuliah, String kelas, String tanggalUjian, String jamUjian, String ruangUjian) {
        this.namaMataKuliah = namaMataKuliah;
        this.ruangMataKuliah = ruangMataKuliah;
        this.hariMataKuliah = hariMataKuliah;
        this.waktuKuliah = waktuKuliah;
        this.kelas = kelas;
        this.tanggalUjian = tanggalUjian;
        this.jamUjian = jamUjian;
        this.ruangUjian = ruangUjian;
        this.value = 0;
        this.notifID = 0;
        this.notifIDUjian = 0;
    }

    public JadwalKuliah(String namaMataKuliah, String ruangMataKuliah, String hariMataKuliah, String waktuKuliah, String kelas) {
        this.namaMataKuliah = namaMataKuliah;
        this.ruangMataKuliah = ruangMataKuliah;
        this.hariMataKuliah = hariMataKuliah;
        this.waktuKuliah = waktuKuliah;
        this.kelas = kelas;
        this.value = 0;
        this.notifID = 0;
        this.notifIDUjian = 0;
    }

    public int getNotifIDUjian() {
        return notifIDUjian;
    }

    public void setNotifIDUjian(int notifIDUjian) {
        this.notifIDUjian = notifIDUjian;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
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

    public String getRuangMataKuliah() {
        return ruangMataKuliah;
    }

    public void setRuangMataKuliah(String ruangMataKuliah) {
        this.ruangMataKuliah = ruangMataKuliah;
    }

    public String getHariMataKuliah() {
        return hariMataKuliah;
    }

    public int getValue(){return this.value;}

    public void setHariMataKuliah(String hariMataKuliah) {
        this.hariMataKuliah = hariMataKuliah;
    }

    public String getWaktuKuliah() {
        return waktuKuliah;
    }

    public void setWaktuKuliah(String waktuKuliah) {
        this.waktuKuliah = waktuKuliah;
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

    public void setValue(int value){this.value = value;}
}
