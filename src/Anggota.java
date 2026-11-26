public class Anggota {
    private int id;
    private String nim;
    private String nama;
    private String divisi;
    private String jabatan;

    public Anggota(int id, String nim, String nama, String divisi, String jabatan) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.divisi = divisi;
        this.jabatan = jabatan;
    }

    public Anggota(String nim, String nama, String divisi, String jabatan) {
        this(0, nim, nama, divisi, jabatan);
    }

    public int getId() { return id; }
    public String getNim() { return nim; }
    public String getNama() { return nama; }
    public String getDivisi() { return divisi; }
    public String getJabatan() { return jabatan; }
}