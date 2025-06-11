public class IDR extends  MataUang {
    public IDR() {
        super("IDR", "Indonesian Rupiah", 1);
    }
    
    @Override
    public double konversiDariIDR(double nilaiIDR) {
        // nilai tetap 1 IDR = 1 IDR
        return nilaiIDR; 
    }
    
    @Override
    public double konversiKeIDR(double nilaiIDR) {
        return nilaiIDR;
    }
}