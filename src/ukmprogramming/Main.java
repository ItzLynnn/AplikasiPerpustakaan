package ukmprogramming;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import ukmprogramming.anggota.AnggotaManager;
import ukmprogramming.buku.BukuManager;
import ukmprogramming.database.DatabaseConnection;
import ukmprogramming.database.InitDatabase;
import ukmprogramming.kategori.KategoriManager;
import ukmprogramming.peminjaman.PeminjamanManager;
import ukmprogramming.statistik.StatistikManager;


public class Main {
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		
		/* Menghubungkan Database */
		try (Connection con = DatabaseConnection.getConnection()) {
		    if (con != null) System.out.println("Database sukses dihubungkan!");
		} catch (SQLException e) {
			System.err.println("[!] Terjadi Kesalahan saat Menghubungkan dengan Database");
			System.err.println("Error: " + e.getMessage());
		}
		
		/* Init Database */
		try {
			InitDatabase.init();
		} catch (SQLException e) {
			System.err.println("[!] Terjadi Kesalahan saat Melakukan Init Database");
			System.err.println("Error: " + e.getMessage());
		}
		
		int pilihan;

		/* Menampilkan Menu Tampilan Perpustakaan */
		do { 
			clear();
			
			System.out.println("╔════════════════════════════════════════╗");
		    System.out.println("║      PERPUSTAKAAN UKM PROGRAMMING      ║");
		    System.out.println("╠════════════════════════════════════════╣");
		    System.out.println("║ 1. Daftar Buku                         ║");
		    System.out.println("║ 2. Tambah Buku                         ║");
		    System.out.println("║ 3. Update Buku                         ║");
		    System.out.println("║ 4. Hapus Buku                          ║");
	        System.out.println("║ 5. Daftar Anggota                      ║");
		    System.out.println("║ 6. Tambah Anggota                      ║");
		    System.out.println("║ 7. Update Anggota                      ║");
		    System.out.println("║ 8. Hapus Anggota                       ║");
		    System.out.println("║ 9. Peminjaman Buku                     ║");
		    System.out.println("║ 10. Pengembalian Buku                  ║");
		    System.out.println("║ 11. Cek Peminjaman Buku                ║");
		    System.out.println("║ 12. Daftar Peminjaman Buku             ║");
		    System.out.println("║ 13. Cek Stok Buku                      ║");
		    System.out.println("║ 14. Daftar Kategori                    ║");
		    System.out.println("║ 15. Tambah Kategori                    ║");
		    System.out.println("║ 16. Update Kategori                    ║");
		    System.out.println("║ 17. Hapus Kategori                     ║");
		    System.out.println("║ 18. Statistik                          ║");
		    System.out.println("║ 0. Keluar                              ║");
		    System.out.println("╚════════════════════════════════════════╝");

		    pilihan = readInt("Pilih Menu: ");
		    
		    switch (pilihan) {
		    	case 1:
		    		tampilBuku();
		            break;
		        case 2:
		            tambahBuku();
		            break;
		        case 3:
		            updateBuku();
		            break;
		        case 4:
		            hapusBuku();
		            break;
		        case 5:
		        	tampilAnggota();
		            break;
		        case 6:
		        	tambahAnggota();
		        	break;
		        case 7:
		        	updateAnggota();
		        	break;
		        case 8:
		        	hapusAnggota();
		        	break;
		        case 9:
		        	peminjamanBuku();
		        	break;
		        case 10:
		        	pengembalianBuku();
		        	break;
		        case 11:
		        	cekPeminjaman();
		        	break;
		        case 12:
		        	tampilPeminjaman();
		        	break;
		        case 13:
		        	cekStokBuku();
		        	break;
		        case 14:
		        	tampilKategori();
		        	break;
		        case 15:
		        	tambahKategori();
		        	break;
		        case 16:
		        	updateKategori();
		        	break;
		        case 17:
		        	hapusKategori();
		        	break;
		        case 18:
		        	tampilStatistik();
		        	break;
		        case 0:
		            System.out.println("Terimakasih telah menggunakan Sistem Perpustakaan UKM Programming!");
		            break;
		         default:
		            System.out.println("Pilihan tidak valid!");
		    }

		} while (pilihan != 0);
	}
	
	/* Function untuk membaca input berupa integer dengan anti zero dan error handling */
	private static int readInt(String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        String input = scanner.nextLine().trim();

	        try {
	            int value = Integer.parseInt(input);

	            if (value < 0) {
	                System.err.println("[!] Angka tidak boleh kurang dari 0");
	                continue;
	            }

	            return value;

	        } catch (NumberFormatException e) {
	            System.err.println("[!] Input harus berupa angka");
	        }
	    }
	}
	
	/* Function untuk membaca inpupt berupa integer dengan opsi jika input nya kosong untuk Update Data */
	private static Integer readOptionalInt(String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        String input = scanner.nextLine().trim();

	        if (input.isEmpty()) {
	            return null;
	        }

	        try {
	            return Integer.parseInt(input);
	        } catch (NumberFormatException e) {
	            System.err.println("[!] Input harus angka atau ENTER untuk skip");
	        }
	    }
	}
	
	/* Function untuk sekedar clear console */
	private static void clear() {
	    for (int i = 0; i < 5; i++) System.out.println();
	}

	/* Function untuk menampilkan header berbasis tabel */
	private static void header(String title) {
	    System.out.println("╔══════════════════════════════════════════════════════════════╗");
	    System.out.printf ("║ %-60s ║%n", title);
	    System.out.println("╚══════════════════════════════════════════════════════════════╝");
	}
	
	/* Functoin untuk menampilkan List Buku*/
	private static void tampilBuku() {
		clear();
	    BukuManager.tampilListBuku();
	}
	
	/* Function untuk menambahkan Buku*/
	private static void tambahBuku() {
		int konfirmasi;
		clear();
		header("Tambah Buku");
		
	    System.out.print("Judul Buku: ");
	    String judul = scanner.nextLine();

	    System.out.print("Pengarang Buku: ");
	    String pengarang = scanner.nextLine();

	    System.out.print("Penerbit Buku: ");
	    String penerbit = scanner.nextLine();

	    int tahunTerbit = readInt("Tahun Terbit: ");

	    int stok = readInt("Stok Buku: ");

	    int idKategori = readInt("ID Kategori: ");

	    header("Konfirmasi Tambah Buku");
	    System.out.println("╔══════════════════════════════════════════════════════════════╗");
	    System.out.printf ("║ %-60s ║%n", "Nama Buku: " + judul);
	    System.out.printf ("║ %-60s ║%n", "Pengarang Buku: " + pengarang);
	    System.out.printf ("║ %-60s ║%n", "Penerbit Buku: " + penerbit);
	    System.out.printf ("║ %-60s ║%n", "Tahun Terbit: " +tahunTerbit);
	    System.out.printf ("║ %-60s ║%n", "Stok: " + stok);
	    System.out.printf ("║ %-60s ║%n", "ID Kategori: " + idKategori);
	    System.out.println("╚══════════════════════════════════════════════════════════════╝");

	    while (true) {
		    konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
	        if (konfirmasi == 1 || konfirmasi == 2) break;
	        System.out.println("[!] Pilihan hanya 1 atau 2");
	    }

	    if (konfirmasi == 2) {
	        System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
	        return;
	    } else if(konfirmasi == 1) {
		    BukuManager.tambahBuku(judul, pengarang, penerbit, tahunTerbit, stok, idKategori);	
	    }
	}

	/* Function untuk mengupdate Buku*/
	private static void updateBuku() {
		int idBuku = 0;
		int konfirmasi;
		clear();
		header("Update Buku");
		
		System.out.print("ID Buku yang ingin diupdate: ");
		
		try {
			idBuku = Integer.parseInt(scanner.nextLine());

            if (idBuku < 0) {
                System.err.println("[!] Angka tidak boleh kurang dari 0");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("[!] Input harus berupa angka");
            return;
        }

		try (ResultSet rs = BukuManager.getBukuByIdRaw(idBuku)){
			if(!rs.next()) {
				System.out.println("[!] Buku tidak ditemukan");
		        return;
		     }

		     String judulLama = rs.getString("judul");
		     String pengarangLama = rs.getString("pengarang");
		     String penerbitLama = rs.getString("penerbit");
		     int tahunLama = rs.getInt("tahunTerbit");
		     int stokLama = rs.getInt("stok");
		     int kategoriLama = rs.getInt("idKategori");

		     System.out.println("Kosongkan field jika tidak ingin diubah");
		     System.out.println("Note: Data yang berada di [] merupakan data lama yang tersimpan berdasarkan id buku yang di input");

		     System.out.print("Judul Buku [" + judulLama + "]: ");
		     String judul = scanner.nextLine();
		     if(judul.isBlank()) judul = judulLama;

		     System.out.print("Pengarang Buku [" + pengarangLama + "]: ");
		     String pengarang = scanner.nextLine();
		     if (pengarang.isBlank()) pengarang = pengarangLama;

		     System.out.print("Penerbit Buku [" + penerbitLama + "]: ");
		     String penerbit = scanner.nextLine();
		     if (penerbit.isBlank()) penerbit = penerbitLama;

		     Integer tahunIn = readOptionalInt("Tahun [" + tahunLama + "]: ");
		     int tahun = (tahunIn == null) ? tahunLama : tahunIn;

		     Integer stokln = readOptionalInt("Stok [" + stokLama + "]: ");
		     int stok = (stokln == null) ? stokLama : stokln;

		     Integer katIn = readOptionalInt("Kategori Buku [" + kategoriLama + "]: ");
		     int kategori = (katIn == null) ? kategoriLama : katIn;
		     
		     header("Konfirmasi Update Buku");
			 System.out.println("╔══════════════════════════════════════════════════════════════╗");
			 System.out.printf ("║ %-60s ║%n", "Nama Buku: " + judul);
			 System.out.printf ("║ %-60s ║%n", "Pengarang Buku: " + pengarang);
			 System.out.printf ("║ %-60s ║%n", "Penerbit Buku: " + penerbit);
			 System.out.printf ("║ %-60s ║%n", "Tahun Terbit: " + tahunIn);
			 System.out.printf ("║ %-60s ║%n", "Stok: " + stok);
			 System.out.printf ("║ %-60s ║%n", "ID Kategori: " + kategori);
			 System.out.println("╚══════════════════════════════════════════════════════════════╝");

			 while (true) {
				 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
			     if (konfirmasi == 1 || konfirmasi == 2) break;
			     System.out.println("[!] Pilihan hanya 1 atau 2");
			 }

			 if (konfirmasi == 2) {
				 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
			     return;
			 } else if(konfirmasi == 1) {
				 BukuManager.updateBuku(idBuku, judul, pengarang, penerbit, tahun, stok, kategori);
			 }
		} catch (Exception e) {
			System.err.println("Terjadi Kesalahan saat Mengupdate Buku");
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/* Function untuk nenghapus Buku */
	private static void hapusBuku() throws SQLException {
		int konfirmasi;
		clear();
		header("Hapus Buku");
		
	    int idBuku = readInt("ID Buku yang mau dihapus: ");
	    
	    try (ResultSet rs = BukuManager.getBukuByIdRaw(idBuku)){
			if(!rs.next()) {
				System.out.println("[!] Buku tidak ditemukan");
		        return;
		     }
			 String judul = rs.getString("judul");
		     String pengarang = rs.getString("pengarang");
		     String penerbit = rs.getString("penerbit");
		     int tahunTerbit = rs.getInt("tahunTerbit");
		     int stok = rs.getInt("stok");
		     int kategori = rs.getInt("idKategori");
		     
		     header("Konfirmasi Hapus Buku");
			 System.out.println("╔══════════════════════════════════════════════════════════════╗");
			 System.out.printf ("║ %-60s ║%n", "Nama Buku: " + judul);
			 System.out.printf ("║ %-60s ║%n", "Pengarang Buku: " + pengarang);
			 System.out.printf ("║ %-60s ║%n", "Penerbit Buku: " + penerbit);
			 System.out.printf ("║ %-60s ║%n", "Tahun Terbit: " + tahunTerbit);
			 System.out.printf ("║ %-60s ║%n", "Stok: " + stok);
			 System.out.printf ("║ %-60s ║%n", "ID Kategori: " + kategori);
			 System.out.println("╚══════════════════════════════════════════════════════════════╝");

			 while (true) {
				 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
			     if (konfirmasi == 1 || konfirmasi == 2) break;
			     System.out.println("[!] Pilihan hanya 1 atau 2");
			 }

			 if (konfirmasi == 2) {
				 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
			     return;
			 } else if(konfirmasi == 1) {
				 BukuManager.hapusBuku(idBuku);
			 }
	    }
			

	    BukuManager.hapusBuku(idBuku);
	}

	/* Function untuk menampilkan List Anggota */
	private static void tampilAnggota() {
	    AnggotaManager.tampilListAnggota();
	}

	/* Function untuk menambahkan Anggota */
	private static void tambahAnggota() {
		int konfirmasi;
		clear();
		header("Tambah Anggota");
		
	    System.out.print("Nama Anggota: ");
	    String nama = scanner.nextLine();

	    int umur = readInt("Umur: ");

	    System.out.print("Alamat: ");
	    String alamat = scanner.nextLine();
	    
	    header("Konfirmasi Tambah Anggota");
	    System.out.println("╔══════════════════════════════════════════════════════════════╗");
	    System.out.printf ("║ %-60s ║%n", "Nama Anggota: " + nama);
	    System.out.printf ("║ %-60s ║%n", "Umur: " + umur);
	    System.out.printf ("║ %-60s ║%n", "Alamat: " + alamat);
	    System.out.println("╚══════════════════════════════════════════════════════════════╝");

	    while (true) {
			 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
		     if (konfirmasi == 1 || konfirmasi == 2) break;
		     System.out.println("[!] Pilihan hanya 1 atau 2");
		 }

		 if (konfirmasi == 2) {
			 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
		     return;
		 } else if(konfirmasi == 1) {
			 AnggotaManager.tambahAnggota(nama, umur, alamat);	
		 }
	}

	/* Function untuk mengupdate Anggota */
	private static void updateAnggota() {
		int konfirmasi;
		clear();
		header("Update Anggota");
		
		int idAnggota = readInt("ID Anggota yang mau diupdate: ");
	    
	    try (ResultSet rs = AnggotaManager.getAnggotaByIdRaw(idAnggota)){
			if(!rs.next()) {
				System.out.println("[!] Anggota tidak ditemukan");
		        return;
		     }

		     String namaLama = rs.getString("nama");
		     int umurLama = rs.getInt("umur");
		     String alamatLama = rs.getString("alamat");

		     System.out.println();
		     System.out.println("Kosongkan field jika tidak ingin diubah");
		     System.out.println("Note: Data yang berada di [] merupakan data lama yang tersimpan berdasarkan id anggota yang di input");
		     
		     System.out.println();

		     System.out.print("Nama Anggota [" + namaLama + "]: ");
		     String nama = scanner.nextLine();
		     if(nama.isBlank()) nama = namaLama;

		     Integer umurIn = readOptionalInt("Umur [" + umurLama + "]: ");
		     int umur = (umurIn == null) ? umurLama : umurIn;

		     System.out.print("Alamat [" + alamatLama + "]: ");
		     String alamat = scanner.nextLine();
		     if (alamat.isBlank()) alamat = alamatLama;
		     
		     header("Konfirmasi Update Anggota");
			 System.out.println("╔══════════════════════════════════════════════════════════════╗");
			 System.out.printf ("║ %-60s ║%n", "Nama Anggota: " + nama);
			 System.out.printf ("║ %-60s ║%n", "Umur: " + umur);
			 System.out.printf ("║ %-60s ║%n", "Alamat: " + alamat);
			 System.out.println("╚══════════════════════════════════════════════════════════════╝");

			 while (true) {
				 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
			     if (konfirmasi == 1 || konfirmasi == 2) break;
			     System.out.println("[!] Pilihan hanya 1 atau 2");
			 }

			 if (konfirmasi == 2) {
				 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
			     return;
			 } else if(konfirmasi == 1) {
			    AnggotaManager.updateAnggota(idAnggota, nama, umur, alamat);
			 }
		} catch (Exception e) {
			System.err.println("Terjadi Kesalahan saat Mengupdate Anggota");
			System.err.println("Error: " + e.getMessage());
		}
	}

	/* Function untuk menghapus Anggota */
	private static void hapusAnggota() {
		int konfirmasi;
		clear();
		header("Hapus Anggota");
		
		int idAnggota = readInt("ID Anggota yang mau dihapus: ");
	    
	    try (ResultSet rs = AnggotaManager.getAnggotaByIdRaw(idAnggota)){
			if(!rs.next()) {
				System.err.println("[!] Anggota tidak ditemukan");
		        return;
		     }

		     String nama = rs.getString("nama");
		     int umur = rs.getInt("umur");
		     String alamat = rs.getString("alamat");
		     
		     header("Konfirmasi Hapus Anggota");
			 System.out.println("╔══════════════════════════════════════════════════════════════╗");
			 System.out.printf ("║ %-60s ║%n", "Nama Anggota: " + nama);
			 System.out.printf ("║ %-60s ║%n", "Umur: " + umur);
			 System.out.printf ("║ %-60s ║%n", "Alamat: " + alamat);
			 System.out.println("╚══════════════════════════════════════════════════════════════╝");

			 while (true) {
				 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
			     if (konfirmasi == 1 || konfirmasi == 2) break;
			     System.out.println("[!] Pilihan hanya 1 atau 2");
			 }

			 if (konfirmasi == 2) {
				 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
			     return;
			 } else if(konfirmasi == 1) {
			     AnggotaManager.hapusAnggota(idAnggota);
			 }
		} catch (Exception e) {
			System.err.println("[!] Terjadi Kesalahan saat Menghapus Anggota");
			System.err.println("Error: " + e.getMessage());
		}
	}

	/* Functioin untuk peminjaman Buku */
	private static void peminjamanBuku() throws SQLException {
		int konfirmasi;
		clear();
		header("Peminjaman Buku");
		
	    int idAnggota = readInt("ID Anggota: ");
	    
	    int idBuku = readInt("ID Buku: ");
	    
	    int lama = readInt("Lama Peminjaman (Hari): ");
	    
        if (!BukuManager.isBukuAda(idBuku)) {
            System.err.println("[!] Buku tidak ditemukan");
            return;
        }

        if (!BukuManager.stokCukup(idBuku)) {
            System.err.println("[!] Stok buku habis");
            return;
        }
        
        if (!AnggotaManager.isAnggotaAda(idAnggota)) {
            System.err.println("[!] Anggota tidak ditemukan");
            return;
        }
        
	    try (ResultSet rs = AnggotaManager.getAnggotaByIdRaw(idAnggota);
	    	ResultSet rs1 = BukuManager.getBukuByIdRaw(idBuku)){
	    	
	    	rs.next();
	    	rs1.next();
	    	
	    	String namaBuku = rs1.getString("judul");
	    	String namaAnggota = rs.getString("nama");
	    	
	    	header("Konfirmasi Peminjaman Buku");
		    System.out.println("╔══════════════════════════════════════════════════════════════╗");
		    System.out.printf ("║ %-60s ║%n", "ID Buku: " + idBuku);
		    System.out.printf ("║ %-60s ║%n", "Nama Buku: " + namaBuku);
		    System.out.printf ("║ %-60s ║%n", "ID Anggota: " + idAnggota);
		    System.out.printf ("║ %-60s ║%n", "Nama Anggota: " + namaAnggota);
		    System.out.printf ("║ %-60s ║%n", "Lama Pinjam: " + lama + " Hari");
		    System.out.println("╚══════════════════════════════════════════════════════════════╝");
	    
	    } catch (Exception e) {
	    	System.err.println("[!] Terjadi Kesalahan saat Peminjaman Buku");
	        System.err.println("Error: " + e.getMessage());
	    }
	    
	    while (true) {
			 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
		     if (konfirmasi == 1 || konfirmasi == 2) break;
		     System.out.println("[!] Pilihan hanya 1 atau 2");
		 }

		 if (konfirmasi == 2) {
			 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
		     return;
		 } else if(konfirmasi == 1) {
			 PeminjamanManager.pinjamBuku(idAnggota, idBuku, lama);
		 }
	}

	/* Function untuk pengembalian Buku */
	private static void pengembalianBuku() {
		int konfirmasi;
		clear();
		header("Pengembalian Buku");
		
	    int idPeminjaman = readInt("ID Peminjaman: ");
	    
	    try {
	    	PeminjamanManager.PeminjamanInfo info = PeminjamanManager.getPeminjamanInfo(idPeminjaman);
	    	if (info == null) {
	            System.err.println("[!] Data peminjaman tidak ditemukan / sudah dikembalikan");
	            return;
	        }
	    	
	    	header("Konfirmasi Pengembalian Buku");
		    System.out.println("╔══════════════════════════════════════════════════════════════╗");
		    System.out.printf ("║ %-60s ║%n", "ID Peminjaman: " + idPeminjaman);
		    System.out.printf ("║ %-60s ║%n", "ID Buku: " + info.idBuku);
		    System.out.printf ("║ %-60s ║%n", "Nama Buku: " + info.judulBuku);
		    System.out.printf ("║ %-60s ║%n", "ID Anggota: " + info.idAnggota);
		    System.out.printf ("║ %-60s ║%n", "Nama Anggota: " + info.namaAnggota);
		    System.out.println("╚══════════════════════════════════════════════════════════════╝");
		    
		    while (true) {
				 konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
			     if (konfirmasi == 1 || konfirmasi == 2) break;
			     System.out.println("[!] Pilihan hanya 1 atau 2");
			 }

			 if (konfirmasi == 2) {
				 System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
			     return;
			 } else if(konfirmasi == 1) {
				 PeminjamanManager.kembalikanBuku(idPeminjaman);
			 }
	    } catch (Exception e) {
	    	System.err.println("[!] Terjadi Kesalahan saat Pengembalian Buku");
	        System.err.println("Error: " + e.getMessage());
	    }
	}
	
	/* Function untuk menampilkan List Peminjaman Aktif */
	private static void tampilPeminjaman() {
		PeminjamanManager.tampilListPeminjaman();
	}
	
	/* Function untuk mengecek Peminjaman */
	private static void cekPeminjaman() {
		clear();
		header("Cek Peminjaman Buku");
		
	    int idPeminjaman = readInt("ID Peminjaman yang mau dicek: ");
	    
	    try {
	    	PeminjamanManager.cekPeminjaman(idPeminjaman);
	    } catch (Exception e) {
	    	System.err.println("[!] Terjadi Kesalahan saat Cek Peminjaman");
	        System.err.println("Error: " + e.getMessage());
	    }
	}
	
	/* Function untuk mengecek Stok Buku */
	private static void cekStokBuku() {
		clear();
		header("Cek Stok Buku");
		
	    int idBuku = readInt("Masukkan ID Buku: ");

		try (ResultSet rs = BukuManager.getBukuByIdRaw(idBuku)){
			if(!rs.next()) {
				System.err.println("[!] Buku tidak ditemukan");
		        return;
		    }

		    String judulBuku = rs.getString("judul");
		    int stokBuku = rs.getInt("stok");
		     
			 System.out.println("╔══════════════════════════════════════════════════════════════╗");
			 System.out.printf ("║ %-60s ║%n", "Nama Buku: " + judulBuku);
			 System.out.printf ("║ %-60s ║%n", "Stok Tersedia: " + stokBuku);
			 System.out.println("╚══════════════════════════════════════════════════════════════╝");

		} catch (SQLException e) {
	        System.err.println("[!] Terjadi Kesalahan saat Cek Stok");
	        System.err.println("Error: " + e.getMessage());
	    }
	}
	
	/* Function untuk menampilkan List Kategori */
	private static void tampilKategori() {
		KategoriManager.tampilListKategori();
	}
	
	/* Function untuk menambahkan Kategori */
	private static void tambahKategori() {
		int konfirmasi;
		clear();
		header("Tambah Kategori");
		
	    System.out.print("Nama kategori: ");
	    String namaKategori = scanner.nextLine();
	    
	    header("Konfirmasi Tambah Kategori");
		System.out.println("╔══════════════════════════════════════════════════════════════╗");
		System.out.printf ("║ %-60s ║%n", "Nama Kategori: " + namaKategori);
		System.out.println("╚══════════════════════════════════════════════════════════════╝");

		while (true) {
			konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
		    if (konfirmasi == 1 || konfirmasi == 2) break;
		    System.out.println("[!] Pilihan hanya 1 atau 2");
		}

		if (konfirmasi == 2) {
			System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
		    return;
		} else if(konfirmasi == 1) {
		    KategoriManager.tambahKategori(namaKategori);
		}
	}
	
	/* Function untuk mengupdate Kategori */
	private static void updateKategori() {
		int konfirmasi;
		clear();
		header("Update Kategori");
		
	    int idKategori = readInt("ID Kategori yang mau diupdate: ");

	    System.out.print("Nama Kategori baru: ");
	    String namaKategori = scanner.nextLine();
	    
	    header("Konfirmasi Update Kategori");
		System.out.println("╔══════════════════════════════════════════════════════════════╗");
		System.out.printf ("║ %-60s ║%n", "ID Kategori: " + idKategori);
		System.out.printf ("║ %-60s ║%n", "Nama Kategori: " + namaKategori);
		System.out.println("╚══════════════════════════════════════════════════════════════╝");

		while (true) {
			konfirmasi = readInt("Apakah sudah sesuai?\n1. Konfirmasi\n2. Batal dan Kembali Menu Utama\nPilihan: ");
		    if (konfirmasi == 1 || konfirmasi == 2) break;
		    System.out.println("[!] Pilihan hanya 1 atau 2");
		}

		if (konfirmasi == 2) {
			System.err.println("[!] Proses dibatalkan, kembali ke menu utama");
		    return;
		} else if(konfirmasi == 1) {
		    KategoriManager.updateKategori(idKategori, namaKategori);
		}
	}
	
	/* Function untuk menghapus Kategori */
	private static void hapusKategori() throws SQLException {
		clear();
		header("Hapus Kategori");
		
	    int idKategori = readInt("ID Kategori yang mau dihapus: ");

	    KategoriManager.hapusKategori(idKategori);
	}
	
	/* Function untuk menampilkan Statistik */
	private static void tampilStatistik() {
		StatistikManager.tampilkanStatistik();
	}

}

