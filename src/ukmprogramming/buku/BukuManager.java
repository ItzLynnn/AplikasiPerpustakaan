package ukmprogramming.buku;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ukmprogramming.database.DatabaseConnection;
import ukmprogramming.peminjaman.PeminjamanManager;

public class BukuManager {
	
    /* Function untuk menambahkan Buku */
    public static void tambahBuku(String judul, String pengarang, String penerbit, int tahunTerbit, int stok, int idKategori) {
        try {
            if(!isKategoriAda(idKategori)) {
                System.err.println("[!] Gagal menambah buku");
                System.err.println("Error: Kategori dengan ID " + idKategori + " belum ada");
                return;
            }

            String sql = "INSERT INTO buku (judul, pengarang, penerbit, tahunTerbit, stok, idKategori) VALUES (?, ?, ?, ?, ?, ?)";

            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, judul);
                ps.setString(2, pengarang);
                ps.setString(3, penerbit);
                ps.setInt(4, tahunTerbit);
                ps.setInt(5, stok);
                ps.setInt(6, idKategori);

                ps.executeUpdate();
                System.out.println("[OK] Buku berhasil ditambahkan");
            }

        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menambahkan Buku");
	        System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function untuk menampilkan List Buku */
    public static void tampilListBuku() {
    	String sql = "SELECT b.idBuku, b.judul, b.pengarang, b.penerbit, b.tahunTerbit, b.stok, k.nama FROM buku b JOIN kategori k ON b.idKategori = k.idKategori";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

        	boolean addData = false;

            while (rs.next()) {
            	if(!addData) {
            		System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
                    System.out.printf ("║ %-121s ║%n", "DAFTAR BUKU");
                    System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
                    System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
                    System.out.printf("║%-5s %-25s %-20s %-20s %-6s %-6s %-35s║%n", "ID", "Judul", "Pengarang", "Penerbit", "Tahun", "Stok", "Kategori");
                    
                    addData = true;
            	}
            	System.out.printf("║%-5d %-25s %-20s %-20s %-6d %-6d %-35s║%n", rs.getInt("idBuku"), rs.getString("judul"), rs.getString("pengarang"), rs.getString("penerbit"), rs.getInt("tahunTerbit"), rs.getInt("stok"), rs.getString("nama"));
            }
            
            if(!addData) {
        	    System.err.println("[!] Data buku masih kosong");
        	} else {
                System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        	}
        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menampilkan Buku");
	        System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function untuk mengupdate Buku */
    public static void updateBuku(int idBuku, String judul, String pengarang, String penerbit, int tahunTerbit, int stok, int idKategori) {
        try {
            if(!isKategoriAda(idKategori)) {
                System.err.println("[!] Kategori untuk id " + idKategori + " belum ada. Harap tambahkan terlebih dahulu!");
                return;
            }

            String sql = "UPDATE buku SET judul=?, pengarang=?, penerbit=?, tahunTerbit=?, stok=?, idKategori=? WHERE idBuku=?";

            try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, judul);
                ps.setString(2, pengarang);
                ps.setString(3, penerbit);
                ps.setInt(4, tahunTerbit);
                ps.setInt(5, stok);
                ps.setInt(6, idKategori);
                ps.setInt(7, idBuku);

                int affected = ps.executeUpdate();
                if(affected > 0) {
                    System.out.println("[OK] Buku berhasil diupdate");
                }
            }

        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Mengupdate Buku");
	        System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function untuk menghapus Buku */
    public static void hapusBuku(int idBuku) throws SQLException {
    	if (PeminjamanManager.bukuSedangDipinjam(idBuku)) {
    	    System.out.println("[!] Buku tidak bisa dihapus karena sedang dipinjam");
    	    return;
    	}

        String sql = "DELETE FROM buku WHERE idBuku=?";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            int affected = ps.executeUpdate();

            if(affected > 0) {
                System.out.println("[OK] Buku berhasil dihapus");
            }
        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menghapus Buku");
	        System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function  untuk mengecek apakah buku berada di kategori tersebut */
    private static boolean isKategoriAda(int idKategori) throws SQLException {
        String sql = "SELECT idKategori FROM kategori WHERE idKategori=?";

        try(Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idKategori);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /* Function untuk mengecek apakah buku tersebut ada atau tidak */
    public static boolean isBukuAda(int idBuku) throws SQLException {
        String sql = "SELECT idBuku FROM buku WHERE idBuku=?";

        try(Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /* Function untuk mengecek apakah stok bukunya cukup atau lebih dari 0*/
    public static boolean stokCukup(int idBuku) throws SQLException {
        String sql = "SELECT stok FROM buku WHERE idBuku=?";
        try(Connection con = DatabaseConnection.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("stok") > 0;
            }
        }
    }

    /* Function untuk mengurangi stok */
    public static void kurangiStok(Connection con, int idBuku) throws SQLException {
        String sql = "UPDATE buku SET stok = stok - 1 WHERE idBuku=?";
        try(PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            ps.executeUpdate();
        }
    }

    /* Function untuk menambahkan stok */
    public static void tambahStok(int idBuku) throws SQLException {
        String sql = "UPDATE buku SET stok = stok + 1 WHERE idBuku=?";
        try (Connection con = DatabaseConnection.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            ps.executeUpdate();
        }
    }
    
    /* Function untuk mendapatkan detail buku berdasarkan id buku */
    public static ResultSet getBukuByIdRaw(int idBuku) throws SQLException {
        String sql = "SELECT * FROM buku WHERE idBuku=?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idBuku);
        return ps.executeQuery();
    }
    
}
