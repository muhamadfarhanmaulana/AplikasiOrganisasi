import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AplikasiOrganisasi extends JFrame {
    private final JTextField txtNim = new JTextField(15);
    private final JTextField txtNama = new JTextField(20);
    private final JComboBox<String> cmbDivisi = new JComboBox<>(new String[]{
        "Inti / BPH", "Sekretaris", "Bendahara", "Kaderisasi", "Humas & Media", "Logistik & Rumah Tangga"
    });
    private final JTextField txtJabatan = new JTextField(15);

    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnReset = new JButton("Reset Form");
    private final JLabel lblStatus = new JLabel("Siap");

    private final DefaultTableModel tableModel;
    private final JTable tableAnggota;
    private final AnggotaDAO dao = new AnggotaDAO();
    private int selectedId = -1;

    public AplikasiOrganisasi() {
        setTitle("Sistem Manajemen Anggota Organisasi");
        setSize(700, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Form Input
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Data Pengurus / Anggota Organisasi"));
        panelForm.add(new JLabel("NIM:"));
        panelForm.add(txtNim);
        panelForm.add(new JLabel("Nama Lengkap:"));
        panelForm.add(txtNama);
        panelForm.add(new JLabel("Divisi:"));
        panelForm.add(cmbDivisi);
        panelForm.add(new JLabel("Jabatan:"));
        panelForm.add(txtJabatan);

        // Panel Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelTombol.add(btnSimpan);
        panelTombol.add(btnUpdate);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);

        // Tabel Data Anggota
        String[] kolom = {"ID", "NIM", "Nama", "Divisi", "Jabatan"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableAnggota = new JTable(tableModel);
        tableAnggota.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tableAnggota), BorderLayout.CENTER);

        // Panel Status Bar
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatus.setBorder(BorderFactory.createEtchedBorder());
        panelStatus.add(new JLabel("Status: "));
        panelStatus.add(lblStatus);
        add(panelStatus, BorderLayout.SOUTH);

        // Event Klik Baris Tabel untuk Edit/Hapus
        tableAnggota.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableAnggota.getSelectedRow() != -1) {
                int row = tableAnggota.getSelectedRow();
                selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtNim.setText(tableModel.getValueAt(row, 1).toString());
                txtNama.setText(tableModel.getValueAt(row, 2).toString());
                cmbDivisi.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                txtJabatan.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // Listener Tombol
        btnSimpan.addActionListener(e -> simpanAsync());
        btnUpdate.addActionListener(e -> updateAsync());
        btnHapus.addActionListener(e -> hapusAsync());
        btnReset.addActionListener(e -> resetForm());

        // Muat data awal saat aplikasi dibuka
        muatDataAsync();
    }

    private void setTombolEnabled(boolean aktif) {
        btnSimpan.setEnabled(aktif);
        btnUpdate.setEnabled(aktif);
        btnHapus.setEnabled(aktif);
        btnReset.setEnabled(aktif);
    }

    private void resetForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        cmbDivisi.setSelectedIndex(0);
        txtJabatan.setText("");
        tableAnggota.clearSelection();
        lblStatus.setText("Form direset.");
    }

    private void muatDataAsync() {
        setTombolEnabled(false);
        lblStatus.setText("Memuat data anggota...");

        new Thread(() -> {
            try {
                List<Anggota> list = dao.getAllAnggota();
                SwingUtilities.invokeLater(() -> {
                    isiTabel(list);
                    lblStatus.setText("Data termuat (" + list.size() + " anggota).");
                    setTombolEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Gagal memuat data: " + ex.getMessage());
                    setTombolEnabled(true);
                });
            }
        }).start();
    }

    private void simpanAsync() {
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String divisi = cmbDivisi.getSelectedItem().toString();
        String jabatan = txtJabatan.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan Jabatan wajib diisi!");
            return;
        }

        // Penanganan klik -> nonaktifkan tombol -> tampilkan label "Menyimpan..."
        setTombolEnabled(false);
        lblStatus.setText("Menyimpan...");

        new Thread(() -> {
            try {
                boolean berhasil = dao.tambahAnggota(new Anggota(nim, nama, divisi, jabatan));
                List<Anggota> listTerbaru = dao.getAllAnggota();

                SwingUtilities.invokeLater(() -> {
                    if (berhasil) {
                        resetForm();
                        lblStatus.setText("Data anggota berhasil disimpan.");
                    } else {
                        lblStatus.setText("Gagal menyimpan data.");
                    }
                    isiTabel(listTerbaru);
                    setTombolEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Error saat menyimpan: " + ex.getMessage());
                    setTombolEnabled(true);
                });
            }
        }).start();
    }

    private void updateAsync() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris anggota di tabel terlebih dahulu!");
            return;
        }

        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String divisi = cmbDivisi.getSelectedItem().toString();
        String jabatan = txtJabatan.getText().trim();

        setTombolEnabled(false);
        lblStatus.setText("Memperbarui data...");

        new Thread(() -> {
            try {
                boolean berhasil = dao.updateAnggota(new Anggota(selectedId, nim, nama, divisi, jabatan));
                List<Anggota> listTerbaru = dao.getAllAnggota();

                SwingUtilities.invokeLater(() -> {
                    if (berhasil) {
                        resetForm();
                        lblStatus.setText("Data anggota berhasil diperbarui.");
                    } else {
                        lblStatus.setText("Gagal memperbarui data.");
                    }
                    isiTabel(listTerbaru);
                    setTombolEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Error saat update: " + ex.getMessage());
                    setTombolEnabled(true);
                });
            }
        }).start();
    }

    private void hapusAsync() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris anggota yang akan dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this, "Yakin ingin menghapus anggota ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION
        );
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        setTombolEnabled(false);
        lblStatus.setText("Menghapus data...");

        new Thread(() -> {
            try {
                boolean berhasil = dao.hapusAnggota(selectedId);
                List<Anggota> listTerbaru = dao.getAllAnggota();

                SwingUtilities.invokeLater(() -> {
                    if (berhasil) {
                        resetForm();
                        lblStatus.setText("Data anggota berhasil dihapus.");
                    } else {
                        lblStatus.setText("Gagal menghapus data.");
                    }
                    isiTabel(listTerbaru);
                    setTombolEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Error saat menghapus: " + ex.getMessage());
                    setTombolEnabled(true);
                });
            }
        }).start();
    }

    private void isiTabel(List<Anggota> list) {
        tableModel.setRowCount(0);
        for (Anggota a : list) {
            tableModel.addRow(new Object[]{
                a.getId(), a.getNim(), a.getNama(), a.getDivisi(), a.getJabatan()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiOrganisasi().setVisible(true));
    }
}