package ukmprogramming.anggota;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ukmprogramming.database.DatabaseConnection;

public class AnggotaManager {
	
	/* Function untuk menambahkan Anggota */
	public static void tambahAnggota(String nama, int umur, String alamat) {	    
		String sql = "INSERT INTO anggota (nama, umur, alamat) VALUES (?, ?, ?)";

		try (Connection con = DatabaseConnection.getConnection();
           	PreparedStatement ps = con.prepareStatement(sql);){
	        
			ps.setString(1, nama);
	        ps.setInt(2, umur);
	        ps.setString(3, alamat);
	        ps.executeUpdate();

	        System.out.println("[OK] Anggota berhasil ditambahkan");

	    } catch (SQLException e) {
	    	System.err.println("Terjadi Kesalahan saat Menambahkan Anggota");
	        System.err.println("Error: " + e.getMessage());
	    }
		
	}
	
	/* Function untuk menghapus Anggota */
	public static void hapusAnggota(int idAnggota) {
		String sql = "DELETE FROM anggota WHERE idAnggota = ?";
		 
		try (Connection con = DatabaseConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);){

		    ps.setInt(1, idAnggota);
	     	int affected = ps.executeUpdate();

	        if(affected > 0) {
	        	System.out.println("[OK] Anggota berhasil dihapus");
	        } else {
	            System.err.println("[!] ID Anggota tidak ditemukan");
	        }    
		} catch (SQLException e) {
			System.err.println("[!] Terjadi Kesalahan saat Menghapus Anggota");
		    System.err.println("Error: " + e.getMessage());
	    }
	}
	 
	/* Function untuk mengupdate Anggota */
	public static void updateAnggota(int idAnggota, String nama, int umur, String alamat) {
		String sql = "UPDATE anggota SET nama = ?, umur = ?, alamat = ? WHERE idAnggota = ?";

		try(Connection con = DatabaseConnection.getConnection();
		    PreparedStatement ps = con.prepareStatement(sql)) {

		    ps.setString(1, nama);
		    ps.setInt(2, umur);
		    ps.setString(3, alamat);
		    ps.setInt(4, idAnggota);

		    int affected = ps.executeUpdate();

		    if(affected > 0) {
		    	System.out.println("[OK] Anggota berhasil diupdate");
		    } else {
		        System.err.println("[!] ID Anggota tidak ditemukan");
		    }
		} catch (SQLException e) {
			System.err.println("[!] Terjadi Kesalahan saat Mengupdate Anggota");
			System.err.println("Error: " + e.getMessage());
		}
	}
	 
	/* Function untuk menampilkan List Anggota */
	public static void tampilListAnggota() {
		String sql = "SELECT a.idAnggota, a.nama, a.umur, a.alamat FROM anggota a";

	    try (Connection con = DatabaseConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery()) {

	        boolean addData = false;

	        while(rs.next()) {
	            if(!addData) {
	            	System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗");
	                System.out.printf ("║ %-97s ║%n", "DAFTAR ANGGOTA");
	                System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════╝");
	                System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗");
		            System.out.printf("║%-5s %-30s %-5s %-56s║%n", "ID", "Nama", "Umur", "Alamat");
	    	            
	    	        addData = true;
	            } 
		        
				System.out.printf("║%-5d %-30s %-5d %-56s║%n", rs.getInt("idAnggota"), rs.getString("nama"), rs.getInt("umur"), rs.getString("alamat"));
	        }

	        if(!addData) {
            	System.err.println("[!] Data anggota masih kosong");
            } else {
                System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════╝");
        	}

	    } catch (SQLException e) {
	    	System.err.println("[!] Terjadi Kesalahan saat Menampilkan Anggota");
	        System.err.println("Error: " + e.getMessage());
	    }
	}
	 
	/* Function untuk mengecek apakah anggota tersebut ada atau tidak */
	public static boolean isAnggotaAda(int idAnggota) throws SQLException {
		String sql = "SELECT idAnggota FROM anggota WHERE idAnggota=?";

		try(Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idAnggota);
			try(ResultSet rs = ps.executeQuery()) {
				return rs.next();
	        }
		}
	}
	 
	 /* Function untuk mendapatkan detail anggota berdasarkan id anggota */
	public static ResultSet getAnggotaByIdRaw(int idAnggota) throws SQLException {
		String sql = "SELECT * FROM anggota WHERE idAnggota=?";
		Connection con = DatabaseConnection.getConnection();
     	PreparedStatement ps = con.prepareStatement(sql);
	    ps.setInt(1, idAnggota);
	    return ps.executeQuery();
	}
}
