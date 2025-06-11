import java.util.Scanner;

public interface IPayment {
    boolean saldoCukup(double totalHarga);
    double hitungBiayaAdmin();
    String getNamaMetode();
    double prosesTotalPembayaran(double totalHarga, double totalPajak, Membership member, MataUang mataUang);

    default double hitungPotonganPoin(Membership member, MataUang mataUang, double total) {
        if (member == null || !(mataUang instanceof IDR)) {
            return 0;
        }

        int poinTersedia = member.getPoinSebelumTransaksi();
        if (poinTersedia <= 0) {
            member.setPoinTerpakaiTransaksiIni(0);
            return 0;
        }
        
        int poinDipakai = Math.min(poinTersedia, (int) (total / Membership.NILAI_POIN));
        member.setPoinTerpakaiTransaksiIni(poinDipakai);

        return poinDipakai * Membership.NILAI_POIN;
    }

static IPayment pilihMetodePembayaran(Scanner input) {
    System.out.println("\n================================= KOHISHOP =================================");
    System.out.println("                           PILIH METODE PEMBAYARAN                          ");
    System.out.println("-------------------------------------------------------------------------");
    System.out.println("1. Tunai (Tidak ada diskon)");
    System.out.println("2. QRIS (Diskon 5%)");
    System.out.println("3. EMoney (Diskon 7% + Biaya admin 20.000 IDR)");
    System.out.println("-------------------------------------------------------------------------");
        
        while (true) {
            System.out.print("Masukkan pilihan (1-3): ");
            try {
                int pilihan = Integer.parseInt(input.nextLine());
                if (pilihan >= 1 && pilihan <= 3) {
                    switch (pilihan) {
                        case 1: return new Tunai();
                        case 2: return new QRIS(input);
                        case 3: return new EMoney(input);
                        default: return new Tunai();
                    }
                }
                System.out.println("Pilihan tidak valid. Silakan masukkan angka 1-3.");
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
            }
        }
    }
}