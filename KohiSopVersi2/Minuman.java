public class Minuman extends Menu {
    private static final int MAX_KUANTITAS = 3;

    public Minuman(String kode, String nama, double harga) {
        super(kode, nama, harga);
    }

    @Override
    public String getKategori() {
        return "Minuman";
    }

    public static int getMaxKuantitas() {
        return MAX_KUANTITAS;
    }
}
