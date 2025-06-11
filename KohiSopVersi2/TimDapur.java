import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class TimDapur {
    private static final PriorityQueue<Menu> antriMakanan = new PriorityQueue<>((m1, m2) -> Double.compare(m2.getHarga(), m1.getHarga()));
    private static final Stack<Menu> antriMinuman = new Stack<>();
    private static final LinkedList<Menu> pesananMasuk = new LinkedList<>();
    private static final int BATAS_BATCH = 3;
    private static int jumlahPesanan = 0;
    private static int batchSekarang = 0;

    public static void tambahPesanan(LinkedList<Menu> keranjang) {
        for (Menu menu : keranjang) {
            pesananMasuk.add(menu);
            if (menu instanceof Makanan) {
                antriMakanan.add(menu);
            } else if (menu instanceof Minuman) {
                antriMinuman.push(menu);
            }
        }
        jumlahPesanan++;
    }

    public static boolean siapProses() {
        // akan hanya diproses jika jumlah pesanan mencapai kelipatan 3 (setiap 3 pesanan masuk baru)
        return jumlahPesanan % BATAS_BATCH == 0;
    }

    public static void tampilkanProsesMenu() {
        // keterangan mengapa belum diproses
        if (!siapProses()) {
            System.out.println("\nTim dapur menunggu pesanan lainnya... (" +
                    jumlahPesanan % BATAS_BATCH + "/" + BATAS_BATCH + " pesanan)");
            return;
        }

        batchSekarang = jumlahPesanan / BATAS_BATCH;
        System.out.println("\nMemuat proses pesanan...");
        S.delay(2000); // supaya fokus  ke kwitansi terakhir dulu
        S.move(0, 35);
        System.out.println("\n======================== TIM DAPUR KOHISOP =======================");
        System.out.println("                       URUTAN PROSES PESANAN                      ");
        System.out.println("------------------------------------------------------------------");

        java.time.LocalDateTime waktuSekarang = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatTanggal = java.time.format.DateTimeFormatter
                .ofPattern("EEEE, dd MMMM yyyy");
        String tanggal = waktuSekarang.format(formatTanggal);

        java.time.format.DateTimeFormatter formatWaktu = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = waktuSekarang.format(formatWaktu);

        System.out.printf("%44s\n", tanggal);
        System.out.printf("BATCH #%d %44s [ %s ] \n( ID PESANAN #%d sampai #%d )",
                batchSekarang, "", time, ((batchSekarang - 1) * BATAS_BATCH) + 1, batchSekarang * BATAS_BATCH);

        System.out.println("\n\nANTRIAN PROSES MAKANAN (Prioritas Harga):");
        System.out.println("------------------------------------------------------------------");
        if (!antriMakanan.isEmpty()) {
            System.out.printf("%-5s | %-30s | %-10s  | %-10s\n", "Kode", "Nama", "Harga", "Qty");
            System.out.println("------------------------------------------------------------------");
            while (!antriMakanan.isEmpty()) {
                Menu menu = antriMakanan.poll();
                System.out.printf("%-5s | %-30s | Rp %-8.0f | %d porsi\n",
                        menu.getKode(), menu.getNama(), menu.getHarga(), menu.getKuantitas());
            }
        } else {
            System.out.println("(Tidak ada pesanan makanan)");
        }

        System.out.println("\nANTRIAN PROSES MINUMAN (Last Ordered First Served):");
        System.out.println("------------------------------------------------------------------");
        if (!antriMinuman.isEmpty()) {
            System.out.printf("%-5s | %-30s | %-10s  | %-10s\n", "Kode", "Nama", "Harga", "Qty");
            System.out.println("------------------------------------------------------------------");
            while (!antriMinuman.isEmpty()) {
                Menu menu = antriMinuman.pop();
                System.out.printf("%-5s | %-30s | Rp %-8.0f | %d porsi\n",
                        menu.getKode(), menu.getNama(), menu.getHarga(), menu.getKuantitas());
            }
        } else {
            System.out.println("(Tidak ada pesanan minuman)");
        }
        System.out.println("------------------------------------------------------------------");

        if (siapProses()) {
            antriMakanan.clear();
            antriMinuman.clear();
        }
    }
}