package id.fantasticfive.teri.mahasiswa.models;

/**
 * Created by Rindh27 on 09/10/2017.
 */

public class Dosen {
    private String namaDosen;
    private String nipDosen;
    private String alamatDosen;
    private String telDosen;
    private String emailDosen;

    public Dosen(String nipDosen, String namaDosen, String alamatDosen, String telDosen, String emailDosen) {
        this.nipDosen = nipDosen;
        this.namaDosen = namaDosen;
        this.alamatDosen = alamatDosen;
        this.telDosen = telDosen;
        this.emailDosen = emailDosen;
    }

    public Dosen(String namaDosen) {
        this.namaDosen = namaDosen;

    }

    public String getNipDosen() {
        return nipDosen;
    }

    public String getAlamatDosen() {
        return alamatDosen;
    }

    public void setAlamatDosen(String alamatDosen) {
        this.alamatDosen = alamatDosen;
    }

    public String getTelDosen() {
        return telDosen;
    }

    public void setTelDosen(String telDosen) {
        this.telDosen = telDosen;
    }

    public String getEmailDosen() {
        return emailDosen;
    }

    public void setEmailDosen(String emailDosen) {
        this.emailDosen = emailDosen;
    }

    public void setNipDosen(String nipDosen) {
        this.nipDosen = nipDosen;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

}
