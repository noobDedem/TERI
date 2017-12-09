package id.fantasticfive.teri.admin.models;

/**
 * Created by alfifau on 09/10/2017.
 */

public class MataKuliah {
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String enrollment;
    private String dosenPengampu1;
    private String dosenPengampu2;

    public MataKuliah(String kodeMataKuliah, String namaMataKuliah, String enrollment, String dosenPengampu1, String dosenPengampu2) {
        this.kodeMataKuliah = kodeMataKuliah;
        this.namaMataKuliah = namaMataKuliah;
        this.enrollment = enrollment;
        this.dosenPengampu1 = dosenPengampu1;
        this.dosenPengampu2 = dosenPengampu2;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public String getNamaMataKuliah() {
        return namaMataKuliah;
    }

    public void setNamaMataKuliah(String namaMataKuliah) {
        this.namaMataKuliah = namaMataKuliah;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getDosenPengampu1() {
        return dosenPengampu1;
    }

    public void setDosenPengampu1(String dosenPengampu1) {
        this.dosenPengampu1 = dosenPengampu1;
    }

    public String getDosenPengampu2() {
        return dosenPengampu2;
    }

    public void setDosenPengampu2(String dosenPengampu2) {
        this.dosenPengampu2 = dosenPengampu2;
    }
}
