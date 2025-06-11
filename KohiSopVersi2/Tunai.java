public class Tunai implements IPayment {    
    @Override
    public boolean saldoCukup(double totalHarga) {
        return true;
    }
    
    @Override
    public String getNamaMetode() {
        return "Tunai";
    }

    @Override
    public double hitungBiayaAdmin() {
        return 0;
    }
    
    @Override
    public double prosesTotalPembayaran(double totalHarga, double totalPajak, Membership member, MataUang mataUang) {
        double total = totalHarga + totalPajak;

        double potonganPoin = hitungPotonganPoin(member, mataUang, total);
        return total - potonganPoin;
    }
}