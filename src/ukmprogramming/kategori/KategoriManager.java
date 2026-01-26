package ukmprogramming.kategori;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ukmprogramming.database.DatabaseConnection;

public class KategoriManager {

    /* Function untuk menambahkan Kategori */
    public static void tambahKategori(String namaKategori) {
        String sql = "INSERT INTO kategori (nama) VALUES (?)";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, namaKategori);
            ps.executeUpdate();
            System.out.println("[OK] Kategori berhasil ditambahkan");

        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menambahkan Kategori");
	        System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function  untuk mengecek apakah kategori tersebut sedang dipakai untuk buku */
    private static boolean isKategoriDipakai(int idKategori) throws SQLException {
        String sql = "SELECT COUNT(*) FROM buku WHERE idKategori = ?";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idKategori);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /* Function untuk mengupdate Kategori */
    public static void updateKategori(int idKategori, String nama) {
		 String sql = "UPDATE kategori SET nama = ? WHERE idKategori = ?";

		 try(Connection con = DatabaseConnection.getConnection();
		    PreparedStatement ps = con.prepareStatement(sql)) {

		    ps.setString(1, nama);
		    ps.setInt(2, idKategori);

		    int affected = ps.executeUpdate();

		    if(affected > 0) {
		    	System.out.println("[OK] Kategori berhasil diupdate");
		    } else {
		        System.err.println("[!] ID Kategori tidak ditemukan");
		    }
		} catch (SQLException e) {
			System.err.println("[!] Terjadi Kesalahan saat Mengupdate Kategori");
		    System.err.println("Error: " + e.getMessage());
		}
	}

    /* Function untuk menghapus Kategori */
    public static void hapusKategori(int idKategori) {
        try {
            if (isKategoriDipakai(idKategori)) {
                System.err.println("[!] Kategori tidak bisa dihapus karena masih digunakan oleh buku");
                return;
            }

            String sql = "DELETE FROM kategori WHERE idKategori = ?";

            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idKategori);
                int affected = ps.executeUpdate();

                if (affected > 0) {
                    System.out.println("[OK] Kategori berhasil dihapus");
                } else {
                    System.err.println("[!] ID Kategori tidak ditemukan");
                }
            }
        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menghapus Kategori");
	        System.err.println("Error: " + e.getMessage());
        }
    }
    
    /* Function untuk menampilkan List Kategori */
    public static void tampilListKategori() {
    	String sql = "SELECT k.idKategori, k.nama FROM kategori k";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

        	boolean addData = false;

            while (rs.next()) {
            	if (!addData) {
            		System.out.println("╔═══════════════════════════════════════════════════════════════╗");
                    System.out.printf ("║ %-61s ║%n", "DAFTAR KATEGORI");
                    System.out.println("╚═══════════════════════════════════════════════════════════════╝");
                    System.out.println("╔═══════════════════════════════════════════════════════════════╗");
                    System.out.printf ("║%-5s %-56s ║%n", "ID", "Nama Kategori");
                    addData = true;
            	}
	            System.out.printf("║%-5s %-56s ║%n",
	           		rs.getInt("idKategori"),
	            	rs.getString("nama")
	            );
            }
            
            if(!addData) {
        	    System.err.println("[!] Data kategori masih kosong");
        	} else {
                System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        	}
        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Menampilkan Anggota");
	        System.err.println("Error: " + e.getMessage());
        }
    }
}