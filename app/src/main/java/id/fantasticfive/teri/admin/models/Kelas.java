package id.fantasticfive.teri.admin.models;

/**
 * Created by alfifau on 14/11/2017.
 */

public class Kelas{
    private String namaKelas;
    private String kodeMatkul;
    private String hariKuliah;
    private String jamKuliah;
    private String ruangKuliah;
    private String tanggalUjian;
    private String jamUjian;
    private String ruangUjian;
    private String idKuliah;
    private String idUjian;


    public Kelas(String namaKelas, String kodeMatkul, String hariKuliah, String jamKuliah, String ruangKuliah, String tanggalUjian, String jamUjian, String ruangUjian, String idKuliah, String idUjian) {
        this.namaKelas = namaKelas;
        this.kodeMatkul = kodeMatkul;
        this.hariKuliah = hariKuliah;
        this.jamKuliah = jamKuliah;
        this.ruangKuliah = ruangKuliah;
        this.tanggalUjian = tanggalUjian;
        this.jamUjian = jamUjian;
        this.ruangUjian = ruangUjian;
        this.idKuliah = idKuliah;
        this.idUjian = idUjian;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getKodeMatkul() {
        return kodeMatkul;
    }

    public void setKodeMatkul(String kodeMatkul) {
        this.kodeMatkul = kodeMatkul;
    }

    public String getHariKuliah() {
        return hariKuliah;
    }

    public void setHariKuliah(String hariKuliah) {
        this.hariKuliah = hariKuliah;
    }

    public String getJamKuliah() {
        return jamKuliah;
    }

    public void setJamKuliah(String jamKuliah) {
        this.jamKuliah = jamKuliah;
    }

    public String getRuangKuliah() {
        return ruangKuliah;
    }

    public void setRuangKuliah(String ruangKuliah) {
        this.ruangKuliah = ruangKuliah;
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

    public String getIdKuliah() {
        return idKuliah;
    }

    public void setIdKuliah(String idKuliah) {
        this.idKuliah = idKuliah;
    }

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }
}
