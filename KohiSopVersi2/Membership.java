import java.util.ArrayList;
import java.util.Random;

public class Membership {
    public static final int POIN_PER_ITEMS = 10;
    public static final int NILAI_POIN = 2000; // besar potongan untuk setiap 1 poin
    private static int transactionCounter = 0;
    private static final ArrayList<Membership> database = new ArrayList<>();

    private final String kode;
    private final String nama;
    private int poin;
    private int poinTerpakaiTransaksiIni;
    private int poinDariTransaksiIni;
    private int poinSebelumTransaksi;

    public Membership(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
        this.poin = 0;
        this.poinTerpakaiTransaksiIni = 0;
        this.poinDariTransaksiIni = 0;
        this.poinSebelumTransaksi = 0;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public int getPoin() {
        return poin;
    }

    public int getPoinDariTransaksiIni() {
        return poinDariTransaksiIni;
    }

    public int getPoinTerpakaiTransaksiIni() {
        return poinTerpakaiTransaksiIni;
    }

    public int getPoinSebelumTransaksi() {
        return poinSebelumTransaksi;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public void setPoinSebelumTransaksi(int poin) {
        this.poinSebelumTransaksi = poin;
    }

    public void setPoinTerpakaiTransaksiIni(int poin) {
        this.poinTerpakaiTransaksiIni = poin;
    }

    public void setPoinDariTransaksiIni(int poin) {
        this.poinDariTransaksiIni = poin;
    }

    public static int getTransactionCount() {
        return transactionCounter;
    }

    public static void incrementTransactionCounter() {
        transactionCounter++;
    }

    public void updatePoin(int poinBaru, int poinTerpakai) {
        this.poinSebelumTransaksi = this.poin;
        if (!(MainApp.getMataUangTerpilih() instanceof IDR)) {
            this.poinTerpakaiTransaksiIni = 0;
            this.poin = this.poinSebelumTransaksi + poinBaru;
        } else {
            this.poinTerpakaiTransaksiIni = poinTerpakai;
            this.poin = this.poinSebelumTransaksi - poinTerpakai + poinBaru;
        }
        this.poinDariTransaksiIni = poinBaru;
    }

    // untuk memberikan kode member acak (A-F, 0-9)
    public static String generateKodeMember() {
        String chars = "ABCDEF0123456789";
        Random acak = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(acak.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Membership tambahMember(String nama) {
        String kode = generateKodeMember();
        Membership member = new Membership(kode, nama);
        database.add(member);
        return member;
    }

    public static void prosesPoinSetelahPembayaran(String kode, int totalItem) {
        Membership member = cariMember(kode);
        if (member != null) {
            int poinBaru = hitungPoin(totalItem, kode);
            member.setPoinDariTransaksiIni(poinBaru);
            member.setPoin(member.getPoinSebelumTransaksi() + poinBaru);
        }
    }

    public static int hitungPoin(int totalItem, String kodeMember) {
        int poin = totalItem / POIN_PER_ITEMS;
        if (kodeMember != null && kodeMember.toUpperCase().contains("A")) {
            poin *= 2;
        }
        return poin;
    }

    public static Membership cariMember(String kode) {
        if (kode != null) {
            for (Membership m : database) {
                if (m.getKode().equals(kode))
                    return m;
            }
        }
        return null;
    }

    public static ArrayList<Membership> getDatabase() {
        return database;
    }

    public static Membership prosesMembership(java.util.Scanner input) {
        System.out.println("\n================================= KOHISHOP =================================");
        System.out.println("                                 MEMBERSHIP                          ");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("*) Setiap 1 poin dapat ditukarkan dengan potongan Rp 2.000,-");
        System.out.println("   (KHUSUS PEMBAYARAN DENGAN IDR dan tidak berlaku untuk mata uang lain)");
        System.out.println("----------------------------------------------------------------------------");

        System.out.print("Masukkan nama Anda: ");
        String nama = input.nextLine().trim();

        System.out.print("Apakah Anda sudah pernah melakukan transaksi dengan KohiSop? (Y/T): ");
        String pernah = input.nextLine().trim();
        Membership member = null;
        System.out.println("----------------------------------------------------------------------------");

        if (pernah.equalsIgnoreCase("Y")) {
            for (Membership m : database) {
                if (m.getNama().equalsIgnoreCase(nama)) {
                    member = m;
                    break;
                }
            }
            if (member != null) {
                member.getPoin();
                System.out.println("Selamat datang kembali, " + member.getNama() + "! Membership Anda ditemukan.");
                System.out.println("\nKode: " + member.getKode());
                System.out.println("Poin dari transaksi sebelumnya: " + member.getPoin());
            } else {
                // tidak terdeteksi akan otomatis dibuatkan membership baru
                System.out.println("Nama tidak terdeteksi di database. Membership akan dibuat otomatis untuk Anda.");
                member = tambahMember(nama);
                System.out.println("\nMembership baru dibuat. Kode: " + member.getKode());
                System.out.println("Poin dari transaksi sebelumnya: 0");
            }
        } else {
            // belum pernah transaksi, juga otomatis dibuatkan membership baru
            System.out.println("KohiSop memberikan membership untuk Anda!");
            member = tambahMember(nama);
            System.out.println("\nMembership baru berhasil dibuat! Kode Anda: " + member.getKode());
            System.out.println("Poin dari transaksi sebelumnya: 0");
        }

        System.out.print("\nTekan ENTER untuk melanjutkan ke pembayaran...");
        input.nextLine();
        S.clear();

        return member;
    }
}
