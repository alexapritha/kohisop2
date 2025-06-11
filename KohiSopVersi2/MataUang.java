import java.util.ArrayList;
import java.util.Scanner;

public abstract class MataUang {
    private final String kode;
    private final String nama;
    private final double nilaiTukar;
    private static final ArrayList<MataUang> daftarMataUang = new ArrayList<>();

    public MataUang(String kode, String nama, double nilaiTukar) {
        this.kode = kode;
        this.nama = nama;
        this.nilaiTukar = nilaiTukar;
    }

    public static void inisialisasiMataUang() {
        daftarMataUang.clear();
        daftarMataUang.add(new IDR());
        daftarMataUang.add(new USD());
        daftarMataUang.add(new JPY());
        daftarMataUang.add(new MYR());
        daftarMataUang.add(new EUR());
    }

    public static MataUang pilihMataUang(Scanner input) {
        S.clear();
        System.out.println("================================= KOHISHOP =================================\n");
        System.out.println("                           PILIH MATA UANG PEMBAYARAN                       ");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("1. IDR (Indonesian Rupiah) - 1 IDR = 1 IDR");
        System.out.println("2. USD (US Dollar) - 1 USD = 15 IDR");
        System.out.println("3. JPY (Japanese Yen) - 10 JPY = 1 IDR");
        System.out.println("4. MYR (Malaysian Ringgit) - 1 MYR = 4 IDR");
        System.out.println("5. EUR (Euro) - 1 EUR = 14 IDR");
        System.out.println("-------------------------------------------------------------------------");

        S.delay(100);

        while (true) {
            System.out.print("Masukkan pilihan (1-" + daftarMataUang.size() + "): ");
            try {
                int pilihan = Integer.parseInt(input.nextLine());
                if (pilihan >= 1 && pilihan <= daftarMataUang.size()) {
                    return daftarMataUang.get(pilihan - 1);
                }
                System.out.println("Pilihan tidak valid. Silakan masukkan angka 1-" + daftarMataUang.size());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
            }
        }
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public double getNilaiTukar() {
        return nilaiTukar;
    }

    public double konversiDariIDR(double nilaiIDR) {
        return nilaiIDR / nilaiTukar;
    }

    public double konversiKeIDR(double nilaiMataUang) {
        return nilaiMataUang * nilaiTukar;
    }
}