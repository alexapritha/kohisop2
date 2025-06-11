public class Pajak {
    public static double hitungPajak(Menu menu, Membership member) {
        double harga = menu.getHarga();

        if (member.getKode().contains("A")) {
            return 0;
        } else if (menu instanceof Makanan) {
            if (harga > 50000) {
                return harga * 0.08;
            } else {
                return harga * 0.11;
            }
        } else if (menu instanceof Minuman) {
            if (harga < 50000) {
                return 0;
            } else if (harga >= 50000 && harga <= 55000) {
                return harga * 0.08;
            } else if (harga > 55000) {
                return harga * 0.11;
            }
        }
        return 0;
    }
}
