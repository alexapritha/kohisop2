import java.util.ArrayList;

public abstract class Menu {
    private final String kode;
    private final String nama;
    private final double harga;
    private int kuantitas;

    public Menu(String kode, String nama, double harga) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.kuantitas = 0;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public double hitungTotalHarga() {
        return harga * kuantitas;
    }

    public double hitungTotalPajak(Membership member) {
        return Pajak.hitungPajak(this, member) * this.kuantitas;
    }

    public abstract String getKategori();

    public static ArrayList<Menu> inisialisasiMenu() {
        ArrayList<Menu> daftarMenu = new ArrayList<>();
        daftarMenu.add(new Minuman("A1", "Caffe Latte", 46000));
        daftarMenu.add(new Minuman("A2", "Cappuccino", 46000));
        daftarMenu.add(new Minuman("E1", "Caffe Americano", 37000));
        daftarMenu.add(new Minuman("E2", "Caffe Mocha", 55000));
        daftarMenu.add(new Minuman("E3", "Caramel Macchiato", 59000));
        daftarMenu.add(new Minuman("E4", "Asian Dolce Latte", 55000));
        daftarMenu.add(new Minuman("E5", "Double Shots Iced Shaken Espresso", 50000));
        daftarMenu.add(new Minuman("B1", "Freshly Brewed Coffee", 23000));
        daftarMenu.add(new Minuman("B2", "Vanilla Sweet Cream Cold Brew", 50000));
        daftarMenu.add(new Minuman("B3", "Cold Brew", 44000));

        daftarMenu.add(new Makanan("M1", "Petemania Pizza", 112000));
        daftarMenu.add(new Makanan("M2", "Mie Rebus Super Mario", 35000));
        daftarMenu.add(new Makanan("M3", "Ayam Bakar Goreng Rebus Spesial", 72000));
        daftarMenu.add(new Makanan("M4", "Soto Kambing Iga Guling", 124000));
        daftarMenu.add(new Makanan("S1", "Singkong Bakar A La Carte", 37000));
        daftarMenu.add(new Makanan("S2", "Ubi Cilembu Bakar Arang", 58000));
        daftarMenu.add(new Makanan("S3", "Tempe Mendoan", 18000));
        daftarMenu.add(new Makanan("S4", "Tahu Bakso Extra Telur", 28000));
        return daftarMenu;
    }

    public static void tampilkanDaftarMenu(ArrayList<Menu> daftarMenu) {
        System.out.println("\n\n========================= SELAMAT DATANG DI KOHISOP ======================");
        System.out.println("                             DAFTAR MENU MINUMAN                          ");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Kode | Nama                                       | Harga (Rp)          ");
        System.out.println("-------------------------------------------------------------------------");
        insertionSortByHarga(daftarMenu);
        for (Menu menu : daftarMenu) {
            if (menu instanceof Minuman) {
                System.out.printf("%-4s | %-42s | %-20.0f\n", menu.getKode(), menu.getNama(), menu.getHarga());
            }
        }
        System.out.println("\n                             DAFTAR MENU MAKANAN                           ");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Kode | Nama                                       | Harga (Rp)          ");
        System.out.println("-------------------------------------------------------------------------");
        for (Menu menu : daftarMenu) {
            if (menu instanceof Makanan) {
                System.out.printf("%-4s | %-42s | %-20.0f\n", menu.getKode(), menu.getNama(), menu.getHarga());
            }
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("*) Ketik 'CC' untuk membatalkan pesanan.\n");
    }

    // sorting supaya ditampilkan dari harga tertinggi ke terendah
    public static void insertionSortByHarga(java.util.List<Menu> list) {
        for (int i = 1; i < list.size(); i++) {
            Menu key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getHarga() < key.getHarga()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}
