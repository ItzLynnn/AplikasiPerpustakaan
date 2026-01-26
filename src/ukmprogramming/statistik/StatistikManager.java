package ukmprogramming.statistik;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ukmprogramming.database.DatabaseConnection;

public class StatistikManager {

    /* Function untuk menghitung jumlah data output dari sql */
    private static int count(String sql) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }

    /* Function untuk menampilkan Statistik */
    public static void tampilkanStatistik() {
        try {
            int jumlahAnggota = count("SELECT COUNT(*) FROM anggota");
            int jumlahBuku = count("SELECT COUNT(*) FROM buku");
            int jumlahKategori = count("SELECT COUNT(*) FROM kategori");
            int jumlahPeminjaman = count("SELECT COUNT(*) FROM peminjaman");
            int peminjamanAktif = count("SELECT COUNT(*) FROM peminjaman WHERE tanggalKembali IS NULL");

            System.out.println("╔══════════════════════════════════════════╗");
            System.out.printf ("║ %-40s ║%n", "STATISTIK PERPUSTAKAAN UKM PROGRAMMING");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf ("║ %-25s : %-12d ║%n", "Jumlah Anggota", jumlahAnggota);
            System.out.printf ("║ %-25s : %-12d ║%n", "Jumlah Buku", jumlahBuku);
            System.out.printf ("║ %-25s : %-12d ║%n", "Jumlah Kategori", jumlahKategori);
            System.out.printf ("║ %-25s : %-12d ║%n", "Total Peminjaman", jumlahPeminjaman);
            System.out.printf ("║ %-25s : %-12d ║%n", "Peminjaman Aktif", peminjamanAktif);
            System.out.println("╚══════════════════════════════════════════╝");

        } catch (SQLException e) {
            System.err.println("[!] Terjadi kesalahan saat Menampilkan Statistik");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
