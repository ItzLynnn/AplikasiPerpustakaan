package ukmprogramming.peminjaman;

/* Import Kebutuhan Module */
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import ukmprogramming.buku.BukuManager;
import ukmprogramming.database.DatabaseConnection;

public class PeminjamanManager {

    /* Variable untuk nilai denda per hari dan berlaku kelipatan */
    private static final int DENDA_PER_HARI = 15000;

    /* Function untuk pinjam Buku */
    public static void pinjamBuku(int idAnggota, int idBuku, int lamaPeminjaman) {
        String sql = "INSERT INTO peminjaman (idAnggota, idBuku, tanggalPinjam, lamaPeminjaman) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idAnggota);
                ps.setInt(2, idBuku);
                ps.setDate(3, Date.valueOf(LocalDate.now()));
                ps.setInt(4, lamaPeminjaman);
                ps.executeUpdate();
                
                int affected = ps.executeUpdate();

                if(affected > 0) {
                    try(ResultSet keys = ps.getGeneratedKeys()) {
                        if(keys.next()) {
                            int idPeminjamanBaru = keys.getInt(1);
                            System.out.println("[OK] Peminjaman buku berhasil dilakukan");
                            System.out.println("     ID Peminjaman: " + idPeminjamanBaru);
                        }
                    }
                }
            }

            BukuManager.kurangiStok(con, idBuku);

            con.commit();

        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Melakukan Peminjaman");
	        System.err.println("Error: " + e.getMessage());
        }
    }
    
    /* Function untuk menampilkan List Peminjaman */
    public static void tampilListPeminjaman() {
        String sql = "SELECT p.idPeminjaman, a.nama, b.judul, p.tanggalPinjam, p.lamaPeminjaman FROM peminjaman p JOIN anggota a ON p.idAnggota = a.idAnggota JOIN buku b ON p.idBuku = b.idBuku WHERE p.tanggalKembali IS NULL";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            boolean addData = false;

            while(rs.next()) {
                if(!addData) {
                	System.out.println("╔═════════════════════════════════════════════════════════════════════════════╗");
                    System.out.printf ("║ %-75s ║%n", "DAFTAR PEMINJAMAN AKTIF");
                    System.out.println("╚═════════════════════════════════════════════════════════════════════════════╝");
                    System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
                    System.out.printf("║%-5s %-20s %-25s %-12s %-14s ║%n","ID", "Anggota", "Judul Buku", "Tanggal Pinjam", "Lama Pinjam");
                    
                    addData = true;
                }

                System.out.printf("║%-5d %-20s %-25s %-14s %-14s ║%n", rs.getInt("idPeminjaman"), rs.getString("nama"), rs.getString("judul"), rs.getDate("tanggalPinjam"), rs.getInt("lamaPeminjaman") + " Hari");
            }

            if(!addData) {
                System.err.println("[!] Tidak ada peminjaman aktif");
            } else {
                System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
            }
        } catch (SQLException e) {
            System.err.println("[!] Terjadi kesalahan saat menampilkan Peminjaman");
            System.err.println("Error: " + e.getMessage());
        }
    }

    /* Function untuk mengembalikan buku dan stok buku */
    public static void kembalikanBuku(int idPeminjaman) {
        String sqlAmbil = "SELECT idBuku, tanggalPinjam, lamaPeminjaman FROM peminjaman WHERE idPeminjaman=? AND tanggalKembali IS NULL";

        String sqlUpdate = "UPDATE peminjaman SET tanggalKembali=?, denda=? WHERE idPeminjaman=?";

        try(Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            int idBuku;
            LocalDate tanggalPinjam;
            int lama;

            try(PreparedStatement ps = con.prepareStatement(sqlAmbil)) {
                ps.setInt(1, idPeminjaman);
                ResultSet rs = ps.executeQuery();

                if(!rs.next()) {
                    System.err.println("[!] Data peminjaman tidak ditemukan / sudah dikembalikan");
                    return;
                }

                idBuku = rs.getInt("idBuku");
                tanggalPinjam = rs.getDate("tanggalPinjam").toLocalDate();
                lama = rs.getInt("lamaPeminjaman");
            }

            LocalDate batasKembali = tanggalPinjam.plusDays(lama);
            LocalDate hariIni = LocalDate.now();

            long hariTelat = ChronoUnit.DAYS.between(batasKembali, hariIni);
            int denda = (hariTelat > 0) ? (int) hariTelat * DENDA_PER_HARI : 0;

            try(PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setDate(1, Date.valueOf(hariIni));
                ps.setInt(2, denda);
                ps.setInt(3, idPeminjaman);
                ps.executeUpdate();
            }

            BukuManager.tambahStok(idBuku);

            con.commit();

            System.out.println("[OK] Pengembalian Buku berhasil dilakukan");
            if (denda > 0) {
            	System.err.println("[!] Dikarenakan keterlambatan mengembalikan buku, terdapat sejumlah biaya denda yang harus dibayar");
                System.err.println("    Denda: Rp. " + denda);
            }
        } catch (SQLException e) {
        	System.err.println("[!] Terjadi Kesalahan saat Mengembalikan Buku");
	        System.err.println("Error: " + e.getMessage());
        }
    }
    
    /* Function untuk mengecek apakah buku sedang dipinjam atau tidak */
    public static boolean bukuSedangDipinjam(int idBuku) throws SQLException {
        String sql = "SELECT idBuku FROM peminjaman WHERE idBuku=? AND tanggalKembali IS NULL";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuku);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /* Function untuk mendapatkan detail peminjaman berdasarkan id peminjaman */
    public static PeminjamanInfo getPeminjamanInfo(int idPeminjaman) throws SQLException {
        String sql =
            "SELECT p.idPeminjaman, p.idBuku, p.idAnggota, " +
            "b.judul, a.nama " +
            "FROM peminjaman p " +
            "JOIN buku b ON p.idBuku = b.idBuku " +
            "JOIN anggota a ON p.idAnggota = a.idAnggota " +
            "WHERE p.idPeminjaman=? AND p.tanggalKembali IS NULL";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPeminjaman);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PeminjamanInfo(
                        rs.getInt("idBuku"),
                        rs.getInt("idAnggota"),
                        rs.getString("judul"),
                        rs.getString("nama")
                    );
                }
            }
        }
        return null;
    }
    
    /* Function untuk menampung variabel lokal detail peminjaman */
    public static class PeminjamanInfo {
        public int idBuku;
        public int idAnggota;
        public String judulBuku;
        public String namaAnggota;

        public PeminjamanInfo(int idBuku, int idAnggota, String judulBuku, String namaAnggota) {
            this.idBuku = idBuku;
            this.idAnggota = idAnggota;
            this.judulBuku = judulBuku;
            this.namaAnggota = namaAnggota;
        }
    }
    
    /* Function untuk mengecek peminjaman berdasarkan id peminjaman */
    public static void cekPeminjaman(int idPeminjaman) {
        String sql = "SELECT p.idPeminjaman, a.nama AS namaAnggota, b.judul AS judulBuku, p.tanggalPinjam, p.lamaPeminjaman, p.tanggalKembali, p.denda FROM peminjaman p JOIN anggota a ON p.idAnggota = a.idAnggota JOIN buku b ON p.idBuku = b.idBuku WHERE p.idPeminjaman = ?";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPeminjaman);

            try(ResultSet rs = ps.executeQuery()) {
                if(!rs.next()) {
                    System.err.println("[!] ID Peminjaman tidak ditemukan");
                    return;
                }

                System.out.println("╔═════════════════════════════════════════════════════════════════════════════╗");
                System.out.printf ("║ %-75s ║%n", "DETAIL PEMINJAMAN ID " + idPeminjaman);
                System.out.println("╚═════════════════════════════════════════════════════════════════════════════╝");
                System.out.println("ID Peminjaman   : " + rs.getInt("idPeminjaman"));
                System.out.println("Nama Anggota    : " + rs.getString("namaAnggota"));
                System.out.println("Judul Buku      : " + rs.getString("judulBuku"));
                System.out.println("Tanggal Pinjam  : " + rs.getDate("tanggalPinjam"));
                System.out.println("Lama Pinjam     : " + rs.getInt("lamaPeminjaman") + " hari");

                Date kembali = rs.getDate("tanggalKembali");
                if(kembali == null) {
                    System.out.println("Status          : Masih dipinjam");
                } else {
                    System.out.println("Tanggal Kembali : " + kembali);
                    System.out.println("Denda           : Rp. " + rs.getInt("denda"));
                }
                System.out.println("══════════════════════════════════════════════════════════════════════════════");
            }
        } catch (SQLException e) {
            System.err.println("[!] Terjadi kesalahan saat Cek Peminjaman");
            System.err.println("Error: " + e.getMessage());
        }
    }

}
