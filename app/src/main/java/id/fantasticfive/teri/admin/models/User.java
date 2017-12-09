package id.fantasticfive.teri.admin.models;

/**
 * Created by Rindh27 on 09/10/2017.
 */

public class User {
    private String namaUser;
    private String nipUser;
    private String alamatUser;
    private String teleponUser;
    private String emailUser;
    private String levelUser;
    private String passUser;

    public User(String namaUser, String nipUser, String alamatUser, String teleponUser, String emailUser, String levelUser, String passUser) {
        this.namaUser = namaUser;
        this.nipUser = nipUser;
        this.alamatUser = alamatUser;
        this.teleponUser = teleponUser;
        this.emailUser = emailUser;
        this.levelUser = levelUser;
        this.passUser = passUser;
    }

    public User(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNipUser() {
        return nipUser;
    }

    public void setNipUser(String nipUser) {
        this.nipUser = nipUser;
    }

    public String getAlamatUser() {
        return alamatUser;
    }

    public void setAlamatUser(String alamatUser) {
        this.alamatUser = alamatUser;
    }

    public String getTeleponUser() {
        return teleponUser;
    }

    public void setTeleponUser(String teleponUser) {
        this.teleponUser = teleponUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getLevelUser() {
        return levelUser;
    }

    public void setLevelUser(String levelUser) {
        this.levelUser = levelUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }
}
