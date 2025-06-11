import java.util.Scanner;

public class QRIS implements IPayment {
    private final double saldo;
    private double totalDiskon;

    public QRIS(Scanner input) {
        this.saldo = inputSaldo(input);
    }

    private double inputSaldo(Scanner input) {
        while (true) {
            System.out.print("Masukkan saldo QRIS Anda (IDR): ");
            try {
                double saldo = Double.parseDouble(input.nextLine());

                if (saldo >= 0) {
                    return saldo;
                } else {
                    System.out.println("Saldo tidak valid. Silakan masukkan nilai positif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
            }
        }
    }

    @Override
    public double hitungBiayaAdmin() {
        return 0; // QRIS tidak ada biaya admin
    }

    @Override
    public boolean saldoCukup(double totalHarga) {
        double total = prosesTotalPembayaran(totalHarga, 0, null, new IDR());
        return saldo >= total;
    }

    @Override
    public String getNamaMetode() {
        return "QRIS";
    }

    @Override
    public double prosesTotalPembayaran(double totalHarga, double totalPajak, Membership member, MataUang mataUang) {
        double total = totalHarga + totalPajak;
        totalDiskon = total * 0.05;
        total = total * 0.95;

        double potonganPoin = hitungPotonganPoin(member, mataUang, total);
        return total - potonganPoin;
    }

    public double getTotalDiskon() {
        return totalDiskon;
    }
}
