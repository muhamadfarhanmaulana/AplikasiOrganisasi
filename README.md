# AplikasiOrganisasi

# CRUD Anggota Organisasi - Java Thread & Database

Latihan/studi kasus materi **Thread dan Database pada Java** (SC - Java Coding Practice - Thread dan Database - 2026).

**NIM :** F1D02410015 **Nama :** [Muhamad Farhan Maulana]

## Deskripsi

Aplikasi desktop berbasis Java Swing untuk mengelola data anggota/pengurus organisasi (NIM, nama, divisi, dan jabatan) dengan operasi CRUD (*Create, Read, Update, Delete*) ke database MySQL (`organisasi`) melalui JDBC. Seluruh operasi akses database dijalankan pada *background thread* terpisah dan diperbarui ke antarmuka melalui `SwingUtilities.invokeLater` agar tampilan GUI tetap responsif dan tidak membeku (*freeze*).

## Fitur

- **CRUD Data Anggota Organisasi**: Menambah (*Create*), menampilkan (*Read*), mengubah (*Update*), dan menghapus (*Delete*) data pengurus/anggota organisasi.
- **Pola DAO (*Data Access Object*)**: Memisahkan logika akses database ke dalam kelas `AnggotaDAO` menggunakan `Connection`, `PreparedStatement`, transaksi (`setAutoCommit(false)`, `commit()`, `rollback()`), dan `ResultSet`.
- **Multithreading (`Thread` terpisah)**: Seluruh proses query database dijalankan di luar *Event Dispatch Thread* (EDT).
- **Pembaruan UI Aman**: Hasil proses dari *background thread* dikirim kembali ke komponen Swing menggunakan `SwingUtilities.invokeLater(...)`.
- **Indikator Status & Kontrol Tombol**: Saat tombol aksi diklik, seluruh tombol aksi **dinonaktifkan sementara (`setEnabled(false)`)** dan label status menampilkan progres proses (seperti `"Menyimpan..."`, `"Memperbarui data..."`, `"Menghapus data..."`, dan `"Memuat data anggota..."`).
- **Validasi Input & Konfirmasi**: Memastikan kolom NIM, Nama, dan Jabatan tidak kosong sebelum disimpan serta menampilkan dialog konfirmasi sebelum menghapus data.

## Pola yang Digunakan

```text
Klik tombol -> disable button + label "Menyimpan..." -> DB diproses di thread lain -> invokeLater -> update UI/GUI

Teknologi
Java (JDK) + Java Swing (GUI Desktop)

MySQL (via XAMPP)

MySQL Connector/J (JDBC Driver)

Visual Studio Code (Extension Pack for Java)


AplikasiOrganisasi/
├── lib/
│   └── mysql-connector-j-9.1.0.jar   # Driver JDBC MySQL (sesuaikan versi)
├── src/
│   ├── DBConnection.java             # Konfigurasi & koneksi JDBC ke database organisasi
│   ├── Anggota.java                  # Model class (entitas data anggota organisasi)
│   ├── AnggotaDAO.java               # Implementasi CRUD, PreparedStatement, & Transaction
│   └── AplikasiOrganisasi.java       # Antarmuka Swing GUI + manajemen Thread & invokeLater
├── screenshot/
│   └── hasil-program.png             # Tangkapan layar aplikasi saat dijalankan
├── schema.sql                        # Script SQL pembuatan database & tabel
└── README.md                         # Dokumentasi proyek


File,Fungsi
DBConnection.java,"Mengatur parameter koneksi (URL, USER, PASSWORD) dan menyediakan method getConnection() untuk terhubung ke database MySQL organisasi."
Anggota.java,"Kelas model (POJO) yang merepresentasikan entitas data anggota dengan atribut id, nim, nama, divisi, dan jabatan beserta getter."
AnggotaDAO.java,"Menangani seluruh operasi database: tambahAnggota, updateAnggota, dan hapusAnggota menggunakan PreparedStatement serta manajemen transaksi (setAutoCommit(false), commit(), rollback()); sedangkan getAllAnggota membaca data menggunakan ResultSet."
AplikasiOrganisasi.java,"Membangun antarmuka pengguna (form input, JComboBox divisi, JTable daftar anggota, tombol aksi, dan label status). Setiap aksi tombol menjalankan proses DAO di dalam new Thread(...) lalu memperbarui GUI melalui SwingUtilities.invokeLater(...)."
