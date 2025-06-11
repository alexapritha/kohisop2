import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class MainApp {
    private static final int MAX_JENIS_PESANAN = 5;
    private static final Scanner input = new Scanner(System.in);
    // inisialisasi daftar menu dari kelas menu
    private static final ArrayList<Menu> daftarMenu = Menu.inisialisasiMenu();
    private static LinkedList<Menu> keranjang;
    private static int jumlahItemKeranjang = 0;
    public static int totalStrukDicetak = 0;
    private static MataUang mataUangTerpilih;

    public static void main(String[] args) {
        while (true) {
            keranjang = new LinkedList<>();
            jumlahItemKeranjang = 0;
            MataUang.inisialisasiMataUang();
            Membership.incrementTransactionCounter();

            S.clear();
            // panggil untuk menampilkan daftar menu
            Menu.tampilkanDaftarMenu(daftarMenu);

            if (prosesInputPesanan()) {
                Membership memberAktif = Membership.prosesMembership(input);
                IPayment metodePembayaran;
                mataUangTerpilih = MataUang.pilihMataUang(input);
                do {
                    metodePembayaran = IPayment.pilihMetodePembayaran(input);
                } while (!validasiPembayaran(metodePembayaran, memberAktif, mataUangTerpilih));

                // ketika sudah semua sampai pembayaran, cetak kwitansi
                totalStrukDicetak++;
                Kwitansi kwitansi = new Kwitansi(keranjang, jumlahItemKeranjang, metodePembayaran, mataUangTerpilih,
                        memberAktif, totalStrukDicetak);
                kwitansi.tampilkanKwitansi();

                // setelah kwitansi ditampilkan, proses pesanan ke tim dapur
                TimDapur.tambahPesanan(keranjang);
                TimDapur.tampilkanProsesMenu();

                System.out.println("\nTekan ENTER untuk memulai pesanan baru atau ketik 'X' untuk keluar...");
                String Inputan = input.nextLine();
                if (Inputan.equalsIgnoreCase("X")) {
                    break;
                }
                S.clear();
            } else {
                break;
            }
        }
        input.close();
    }

    public static MataUang getMataUangTerpilih() {
        return mataUangTerpilih;
    }

    private static int hitungTotalItemPesanan() {
        int total = 0;
        for (Menu menu : keranjang) {
            total += menu.getKuantitas();
        }
        return total;
    }

    // input ditangani di sini
    private static boolean prosesInputPesanan() {
        while (jumlahItemKeranjang < MAX_JENIS_PESANAN) {
            System.out.println("==== SILAHKAN PESAN ====");
            System.out.print("Masukkan kode menu: ");
            String kodeMenu = input.nextLine().toUpperCase();

            if (kodeMenu.equals("CC")) {
                S.delay(100);
                System.out.println("Pesanan dibatalkan.");
                return false;
            }

            Menu menu = cariMenu(kodeMenu);
            if (menu == null) {
                S.delay(100);
                System.out.println("Menu dengan kode " + kodeMenu + " tidak ditemukan. Silakan coba lagi.");
                S.delay(1000);
                continue;
            } else if (cekMenuSudahAda(kodeMenu)) {
                continue;
            }

            int kuantitas = inputKuantitas(menu);
            if (kuantitas <= 0) {
                continue;
            }

            menu.setKuantitas(kuantitas);
            keranjang.add(menu);
            jumlahItemKeranjang++;

            S.clear();
            tampilkanPesananSaatIni();

            if (jumlahItemKeranjang >= MAX_JENIS_PESANAN) {
                S.delay(100);
                System.out.println("Anda telah mencapai batas maksimum jenis pesanan (" + MAX_JENIS_PESANAN + ").\n");
                break;
            }

            if (!tanyaLanjutPesan()) {
                break;
            }
        }
        return true;
    }

    private static Menu cariMenu(String kode) {
        // linear search utk cari menu berdasarkan kode
        for (Menu menu : daftarMenu) {
            if (menu != null && menu.getKode().equals(kode)) {
                return menu;
            }
        }
        return null;
    }

    private static boolean cekMenuSudahAda(String kodeMenu) {
        // linear search utk cek apakah menu sudah ada di keranjang
        for (Menu menu : keranjang) {
            if (menu.getKode().equals(kodeMenu)) {
                System.out.println("Menu " + menu.getNama() + " sudah ada di keranjang anda.\n");
                return true;
            }
        }
        return false;
    }

    private static int inputKuantitas(Menu menu) {
        int maxKuantitas = (menu instanceof Minuman) ? 3 : 2;

        System.out.print("Masukkan kuantitas (1-" + maxKuantitas + ", 0 atau S untuk membatalkan): ");
        String inputKuantitas = input.nextLine();

        if (inputKuantitas.equalsIgnoreCase("S") || inputKuantitas.equals("0")) {
            System.out.println("Pesanan untuk " + menu.getNama() + " dibatalkan.\n");
            return 0;
        }

        try {
            int kuantitas = Integer.parseInt(inputKuantitas);
            if (kuantitas < 1 || kuantitas > maxKuantitas) {
                System.out
                        .println("Kuantitas tidak valid. Silakan masukkan angka antara 1 dan " + maxKuantitas + ".\n");
                return 0;
            }
            return kuantitas;
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Silakan masukkan angka.\n");
            return 0;
        }
    }

    private static void tampilkanPesananSaatIni() {
        System.out.println("\n\n                             PESANAN SAAT INI");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Kode | Nama                                      | Harga     | Kuantitas");
        System.out.println("-------------------------------------------------------------------------");

        java.util.List<Menu> makananList = new java.util.ArrayList<>();
        java.util.List<Menu> minumanList = new java.util.ArrayList<>();

        for (Menu m : keranjang) {
            if (m instanceof Makanan) {
                makananList.add(m);
            } else if (m instanceof Minuman) {
                minumanList.add(m);
            }
        }

        Menu.insertionSortByHarga(makananList);
        Menu.insertionSortByHarga(minumanList);

        double totalSementara = 0;
        if (!makananList.isEmpty()) {
            System.out.println("MAKANAN :");
            for (Menu m : makananList) {
                System.out.printf("%-4s | %-42s| Rp %-6.0f | %-9d\n",
                        m.getKode(), m.getNama(), m.getHarga(), m.getKuantitas());
                totalSementara += m.hitungTotalHarga();
            }
        }
        if (!minumanList.isEmpty()) {
            System.out.println("\nMINUMAN :");
            for (Menu m : minumanList) {
                System.out.printf("%-4s | %-42s| Rp %-6.0f | %-9d\n",
                        m.getKode(), m.getNama(), m.getHarga(), m.getKuantitas());
                totalSementara += m.hitungTotalHarga();
            }
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("Total Sementara:                                   Rp %.0f\n", totalSementara);
        System.out.println("-------------------------------------------------------------------------");
    }

    private static boolean tanyaLanjutPesan() {
        System.out.print("Apakah Anda ingin menambah pesanan lagi? (Y/N): ");
        String lanjut = input.nextLine();

        if (lanjut.equalsIgnoreCase("Y")) {
            S.clear();
            Menu.tampilkanDaftarMenu(daftarMenu);
            return true;
        } else {
            S.clear();
            return false;
        }
    }

    private static boolean validasiPembayaran(IPayment metodePembayaran, Membership memberAktif, MataUang mataUang) {
        if (metodePembayaran == null || mataUang == null) {
            return false;
        }

        double totalHarga = 0;
        double totalPajak = 0;
        int totalItems = hitungTotalItemPesanan();

        for (Menu menu : keranjang) {
            totalHarga += menu.hitungTotalHarga();
            totalPajak += menu.hitungTotalPajak(memberAktif);
        }

        if (memberAktif != null) {
            memberAktif.setPoinSebelumTransaksi(memberAktif.getPoin());
        }

        double totalPembayaran = metodePembayaran.prosesTotalPembayaran(totalHarga, totalPajak, memberAktif, mataUang);

        if (!metodePembayaran.saldoCukup(totalPembayaran)) {
            S.clear();
            S.delay(200);
            System.out.println("\n         ! SALDO ANDA TIDAK MENCUKUPI, SILAHKAN PILIH METODE LAIN !");
            return false;
        }

        if (memberAktif != null) {
            int poinBaru = Membership.hitungPoin(totalItems, memberAktif.getKode());
            memberAktif.setPoinDariTransaksiIni(poinBaru);

            // logika update total poin = poin awal - poin terpakai + poin baru
            memberAktif.setPoin(memberAktif.getPoinSebelumTransaksi() -
                    memberAktif.getPoinTerpakaiTransaksiIni() +
                    poinBaru);
        }

        return true;
    }

}