package ukmprogramming.database;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class InitDatabase {

	/* Function untuk membuat Tabel untuk pertama kali dijalankan */
	public static void init() throws SQLException {
		Connection con = DatabaseConnection.getConnection();
		System.out.println("Memulai Init Database...");
		Statement stmt = con.createStatement();
		
		String sqlTabelAnggota = "CREATE TABLE IF NOT EXISTS anggota (idAnggota INT AUTO_INCREMENT PRIMARY KEY, nama VARCHAR(100) NOT NULL, umur INT NOT NULL, alamat VARCHAR(100) NOT NULL)";
		String sqlTabelBuku = "CREATE TABLE IF NOT EXISTS buku (idBuku INT AUTO_INCREMENT PRIMARY KEY, judul VARCHAR(100) NOT NULL, pengarang VARCHAR(50) NOT NULL, penerbit VARCHAR(50) NOT NULL, tahunTerbit INT NOT NULL, stok INT NOT NULL, idKategori INT NOT NULL, FOREIGN KEY (idKategori) REFERENCES kategori(idKategori))";
		String sqlTabelKategori = "CREATE TABLE IF NOT EXISTS kategori (idKategori INT AUTO_INCREMENT PRIMARY KEY, nama VARCHAR(50) NOT NULL)";
		String sqlTabelPeminjaman = "CREATE TABLE IF NOT EXISTS peminjaman (idPeminjaman INT AUTO_INCREMENT PRIMARY KEY, idAnggota INT NOT NULL, idBuku INT NOT NULL, tanggalPinjam DATE NOT NULL, lamaPeminjaman INT NOT NULL, tanggalKembali DATE, denda INT DEFAULT 0, FOREIGN KEY (idAnggota) REFERENCES anggota(idAnggota), FOREIGN KEY (idBuku) REFERENCES buku(idBuku))";
		
		System.out.println("[!] Membuat Tabel Anggota...");
		stmt.executeUpdate(sqlTabelAnggota);
		System.out.println("[~] Tabel Anggota berhasil dibuat!");
		
		System.out.println("[!] Membuat Tabel Kategori...");
		stmt.executeUpdate(sqlTabelKategori);
		System.out.println("[~] Tabel Kategori berhasil dibuat!");
		
		System.out.println("[!] Membuat Tabel Buku...");
		stmt.executeUpdate(sqlTabelBuku);
		System.out.println("[~] Tabel Buku berhasil dibuat!");
		
		System.out.println("[!] Membuat Tabel Peminjaman...");
		stmt.executeUpdate(sqlTabelPeminjaman);
		System.out.println("[~] Tabel Peminjaman berhasil dibuat!");
		
		System.out.println("Selesai Init Database!");
		
	}

}
