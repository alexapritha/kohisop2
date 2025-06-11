public class Makanan extends Menu {
    private static final int MAX_KUANTITAS = 2;

    public Makanan(String kode, String nama, double harga) {
        super(kode, nama, harga);
    }

    @Override
    public String getKategori() {
        return "Makanan";
    }

    public static int getMaxKuantitas() {
        return MAX_KUANTITAS;
    }
}
