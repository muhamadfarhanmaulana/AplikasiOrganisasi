import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    // CREATE dengan Transaction
    public boolean tambahAnggota(Anggota anggota) throws SQLException {
        String sql = "INSERT INTO anggota_organisasi (nim, nama, divisi, jabatan) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, anggota.getNim());
                ps.setString(2, anggota.getNama());
                ps.setString(3, anggota.getDivisi());
                ps.setString(4, anggota.getJabatan());

                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    rbEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException cEx) {
                    cEx.printStackTrace();
                }
            }
        }
    }

    // READ dengan ResultSet
    public List<Anggota> getAllAnggota() throws SQLException {
        String sql = "SELECT id, nim, nama, divisi, jabatan FROM anggota_organisasi ORDER BY id DESC";
        List<Anggota> daftar = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(new Anggota(
                    rs.getInt("id"),
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("divisi"),
                    rs.getString("jabatan")
                ));
            }
        }
        return daftar;
    }

    // UPDATE dengan Transaction
    public boolean updateAnggota(Anggota anggota) throws SQLException {
        String sql = "UPDATE anggota_organisasi SET nim = ?, nama = ?, divisi = ?, jabatan = ? WHERE id = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, anggota.getNim());
                ps.setString(2, anggota.getNama());
                ps.setString(3, anggota.getDivisi());
                ps.setString(4, anggota.getJabatan());
                ps.setInt(5, anggota.getId());

                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    rbEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException cEx) {
                    cEx.printStackTrace();
                }
            }
        }
    }

    // DELETE dengan Transaction
    public boolean hapusAnggota(int id) throws SQLException {
        String sql = "DELETE FROM anggota_organisasi WHERE id = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    rbEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException cEx) {
                    cEx.printStackTrace();
                }
            }
        }
    }
}