import java.util.Scanner;

public class EMoney implements IPayment {
    private final double saldo;
    private static final double BIAYA_ADMIN = 20000;
    private double totalDiskon;

    public EMoney(Scanner input) {
        this.saldo = inputSaldo(input);
    }

    private double inputSaldo(Scanner input) {
        while (true) {
            System.out.print("Masukkan saldo EMoney Anda (IDR): ");
            try {
                double saldoInput = Double.parseDouble(input.nextLine());
                if (saldoInput >= 0) {
                    return saldoInput;
                } else {
                    S.delay(100);
                    System.out.println("Saldo tidak valid. Silakan masukkan nilai positif.");
                }
            } catch (NumberFormatException e) {
                S.delay(100);
                System.out.println("Input tidak valid. Silakan masukkan angka.");
            }
        }
    }

    @Override
    public double hitungBiayaAdmin() {
        return BIAYA_ADMIN;
    }

    @Override
    public boolean saldoCukup(double totalHarga) {
        double total = prosesTotalPembayaran(totalHarga, 0, null, new IDR());
        return saldo >= total;
    }

    @Override
    public String getNamaMetode() {
        return "EMoney";
    }
    
    @Override
    public double prosesTotalPembayaran(double totalHarga, double totalPajak, Membership member, MataUang mataUang) {
        double total = totalHarga + totalPajak;
        totalDiskon = (total * 0.07);
        total = (total * 0.93) + BIAYA_ADMIN;
        
        double potonganPoin = hitungPotonganPoin(member, mataUang, total);
        return total - potonganPoin;
    }

    public double getTotalDiskon() {
        return totalDiskon;
    }

    public double getBiayaAdmin() {
        return BIAYA_ADMIN;
    }
}