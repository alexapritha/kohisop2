import java.util.Date;
import java.util.LinkedList;

public class Kwitansi {
    private final IPayment metodePembayaran;
    private final MataUang mataUang;
    private final Membership member;
    private final java.util.List<Menu> makananList;
    private final java.util.List<Menu> minumanList;
    private final int nomorStruk;
    private double totalHarga;
    private double totalPajak;
    private double totalBayar;


    public Kwitansi(LinkedList<Menu> keranjang, int jumlahItem, IPayment metodePembayaran, MataUang mataUang, Membership member, int nomorStruk) {
        this.metodePembayaran = metodePembayaran;
        this.mataUang = mataUang;
        this.member = member;
        this.makananList = new java.util.ArrayList<>();
        this.minumanList = new java.util.ArrayList<>();
        this.nomorStruk = nomorStruk;
        
        for (Menu menu : keranjang) {
            if (menu instanceof Makanan) {
                makananList.add(menu);
            } else if (menu instanceof Minuman) {
                minumanList.add(menu);
            }
        }
        
        Menu.insertionSortByHarga(makananList);
        Menu.insertionSortByHarga(minumanList);
        hitungTotal();
    }

    private void hitungTotal() {
        totalHarga = 0;
        totalPajak = 0;
        
        for (Menu menu : makananList) {
            totalHarga += menu.hitungTotalHarga();
            totalPajak += menu.hitungTotalPajak(member);
        }
        
        for (Menu menu : minumanList) {
            totalHarga += menu.hitungTotalHarga();
            totalPajak += menu.hitungTotalPajak(member);
        }

        totalBayar = metodePembayaran.prosesTotalPembayaran(totalHarga, totalPajak, member, mataUang);
    }

    private void tampilkanDetailMenu(Menu menu) {
        double hargaSatuan = menu.getHarga();
        int qty = menu.getKuantitas();
        double total = menu.hitungTotalHarga();
        double pajak = menu.hitungTotalPajak(member);
        double persenPajak = (hargaSatuan > 0) ? (pajak / total) * 100 : 0;

        System.out.printf("%-5s %-30s %8.0f   x %2d  %10.0f   %8.0f%%   %8.0f\n",
                menu.getKode(), menu.getNama(), hargaSatuan, qty, total, persenPajak, pajak);
    }

    public void tampilkanKwitansi() {
        S.clear();
        System.out.println("\n\n======================================== KOHISOP =======================================");
        System.out.println("                                        KWITANSI                                    ");
        System.out.println("----------------------------------------------------------------------------------------");
        

        System.out.printf("ID PESANAN - # %d%72s\n", nomorStruk, new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss").format(new Date()));
        if (member != null) {
            System.out.print("Pelanggan     : " + member.getNama());
            System.out.printf("%67s\n", "Kasir : Pritha");
            System.out.println("Kode Member   : " + member.getKode());
            System.out.println("Poin Awal     : " + member.getPoinSebelumTransaksi());
            if (mataUang instanceof IDR && member.getPoinTerpakaiTransaksiIni() > 0) {
                System.out.println("Poin Terpakai : " + member.getPoinTerpakaiTransaksiIni());
            }
            System.out.println("Poin Baru     : " + member.getPoinDariTransaksiIni());
            System.out.println("Total Poin    : " + member.getPoin());
        }

        // Daftar pesanan
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Kode  Menu                              Harga     Qty     Total     Pajak(%)      Pajak");
        System.out.println("----------------------------------------------------------------------------------------");

        if (!makananList.isEmpty()) {
            System.out.println("MAKANAN:");
            for (Menu menu : makananList) {
                tampilkanDetailMenu(menu);
            }
        }
        
        if (!minumanList.isEmpty()) {
            System.out.println("\nMINUMAN:");
            for (Menu menu : minumanList) {
                tampilkanDetailMenu(menu);
            }
        }

        // Total
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("Total Harga Sebelum Pajak               =     Rp %,15.0f\n", totalHarga);
        System.out.printf("Total Pajak                             = (+) Rp %,15.0f\n", totalPajak);
        System.out.printf("Total Harga Setelah Pajak               =     Rp %,15.0f\n", (totalHarga + totalPajak));

        // Info pembayaran
        if (metodePembayaran instanceof QRIS qris) {
            System.out.printf("Diskon QRIS (5%%)                        = (-) Rp %,15.0f\n", qris.getTotalDiskon());
        } else if (metodePembayaran instanceof EMoney emoney) {
            System.out.printf("Diskon EMoney (7%%)                      = (-) Rp %,15.0f\n", emoney.getTotalDiskon());
            System.out.printf("Biaya Admin EMoney                      = (+) Rp %,15.0f\n", emoney.getBiayaAdmin());
        }

        // Info poin
        if (member != null && mataUang instanceof IDR && member.getPoinTerpakaiTransaksiIni() > 0) {
            double potonganPoin = member.getPoinTerpakaiTransaksiIni() * Membership.NILAI_POIN;
            System.out.printf("Potongan Poin (%d poin)                  = (-) Rp %,15.0f\n", 
                member.getPoinTerpakaiTransaksiIni(), potonganPoin);
        }

        // Total akhir
        System.out.printf("Total Akhir (IDR)                       =     Rp %,15.0f\n", totalBayar);
        System.out.printf("Total Bayar (%s)                       =     %s %,14.0f\n",
                mataUang.getKode(), mataUang.getKode(), mataUang.konversiDariIDR(totalBayar));

        // Footer
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("                         Terima kasih dan silakan datang kembali!                        ");
        System.out.println("----------------------------------------------------------------------------------------");
    }

    public double getTotalBayar() {
        return totalBayar;
    }
}