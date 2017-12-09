package id.fantasticfive.teri.dosen.models;

/**
 * Created by Demas on 11/6/2017.
 */

public class Komentar {

    private String idKomentar;
    private String idUser;
    private String isiKomentar;
    private String namaUser;
    private String waktu;

    public Komentar(String idKomentar, String idUser, String isiKomentar, String namaUser, String waktu) {
        this.idKomentar = idKomentar;
        this.idUser = idUser;
        this.isiKomentar = isiKomentar;
        this.namaUser = namaUser;
        this.waktu = waktu;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdKomentar() {
        return idKomentar;
    }

    public void setIdKomentar(String idKomentar) {
        this.idKomentar = idKomentar;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
