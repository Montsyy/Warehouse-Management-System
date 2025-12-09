
import java.util.Scanner;

// Kelas Item merepresentasikan barang di gudang
class Item {
    String itemID;    // ID unik barang
    String name;      // Nama barang
    String category;  // Kategori barang
    int quantity;     // Jumlah stok barang
    double price;     // Harga per unit barang

    // Konstruktor untuk membuat objek Item baru dengan data lengkap
    public Item(String itemID, String name, String category, int quantity, double price) {
        this.itemID = itemID;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    // Metode toString untuk menampilkan informasi barang dalam format yang rapi
    @Override
    public String toString() {
        return "ID      : " + itemID +
               "\nName    : " + name +
               "\nCategory: " + category +
               "\nQty     : " + quantity +
               "\nPrice   : " + price;
    }
}

// Kelas utama untuk sistem manajemen gudang
public class WarehouseManagement {
    static final int MAX = 2000;           // Kapasitas maksimal barang yang bisa disimpan
    static Item[] items = new Item[MAX];   // Array untuk menyimpan data barang
    static int itemCount = 0;              // Variabel untuk menghitung jumlah barang saat ini

    // Scanner untuk menerima input dari user dengan Locale US untuk format desimal
    static Scanner in = new Scanner(System.in);

    // Metode main - entry point program
    public static void main(String[] args) {
        int pilihan;
        do {
            menu();  // Tampilkan menu
            pilihan = inputInt("Pilih menu: ");
            // Switch case untuk menjalankan fungsi sesuai pilihan user
            switch (pilihan) {
                case 1:
                    addItem();  // Menambah barang baru
                    break;
                case 2:
                    displayAll();  // Menampilkan semua barang
                    break;
                case 3:
                    sortByID();  // Mengurutkan berdasarkan ID
                    break;
                case 4:
                    sortByQuantity();  // Mengurutkan berdasarkan jumlah stok
                    break;
                case 5:
                    searchByIDBinary();  // Cari barang dengan binary search
                    break;
                case 6:
                    searchByNameLinear();  // Cari barang dengan linear search
                    break;
                case 7:
                    updateStock();  // Update stok barang (masuk/keluar)
                    break;
                case 8:
                    initDataContoh();  // Inisialisasi ulang data contoh
                    break;
                case 0:
                    System.out.println("Keluar. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
            System.out.println();
        } while (pilihan != 0);  // Lanjutkan sampai user memilih keluar (0)
    }

    // ========== METODE INISIALISASI DATA CONTOH ==========
    // Fungsi untuk menambahkan 10 data contoh ke dalam array
    static void initDataContoh() {
        items[itemCount++] = new Item("AA001", "Laptop Dell", "Elektronik", 15, 8500000.00);
        items[itemCount++] = new Item("AA002", "Mouse Logitech", "Aksesoris", 50, 350000.00);
        items[itemCount++] = new Item("AA003", "Keyboard Mechanical", "Aksesoris", 30, 750000.00);
        items[itemCount++] = new Item("AA004", "Monitor LG 24\"", "Elektronik", 12, 1800000.00);
        items[itemCount++] = new Item("AA005", "Headset Gaming", "Aksesoris", 25, 450000.00);
        items[itemCount++] = new Item("AA006", "SSD 1TB", "Storage", 40, 950000.00);
        items[itemCount++] = new Item("AA007", "RAM DDR4 16GB", "Komponen", 35, 850000.00);
        items[itemCount++] = new Item("AA008", "Power Supply 650W", "Komponen", 20, 1200000.00);
        items[itemCount++] = new Item("AA009", "Webcam Logitech", "Aksesoris", 18, 550000.00);
        items[itemCount++] = new Item("AA010", "USB Hub 7-Port", "Aksesoris", 45, 280000.00);
        System.out.println("✓ Data contoh berhasil dimuat! (10 item)");
    }

    // Metode untuk menampilkan menu utama
    static void menu() {
        System.out.println("===== WAREHOUSE MANAGEMENT SYSTEM =====");
        System.out.println("1. Tambah Item");
        System.out.println("2. Tampilkan Semua Item");
        System.out.println("3. Sorting by ItemID (ascending) [Merge Sort]");
        System.out.println("4. Sorting by Quantity (descending) [Merge Sort]");
        System.out.println("5. Cari Item by ID (Binary Search - rekursif)");
        System.out.println("6. Cari Item by Name (Linear Search)");
        System.out.println("7. Update Stock (In/Out)");
        System.out.println("8. Contoh Data");
        System.out.println("0. Keluar");
    }

    // ========== METODE INPUT HELPER ==========
    // Metode untuk input integer dengan validasi
    static int inputInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = in.nextLine();
                return Integer.parseInt(s.trim());  // Konversi string ke integer
            } catch (Exception e) {
                System.out.println("Input harus angka. Coba lagi.");
            }
        }
    }

    // Metode untuk input double dengan validasi
    static double inputDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = in.nextLine();
                return Double.parseDouble(s.trim());  // Konversi string ke double
            } catch (Exception e) {
                System.out.println("Input harus angka desimal. Coba lagi.");
            }
        }
    }

    // Metode untuk input string
    static String inputString(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();  // Hapus spasi di awal dan akhir
    }

    // ========== FUNGSI-FUNGSI MENU ==========
    // Fungsi untuk menambah barang baru ke gudang
    static void addItem() {
        // Cek apakah gudang sudah penuh
        if (itemCount >= MAX) {
            System.out.println("Kapasitas gudang penuh!");
            return;
        }
        
        String id = inputString("Masukkan Item ID: ");
        
        // Cek apakah ID sudah ada di gudang
        if (findIndexByID(id) != -1) {
            System.out.println("Item ID sudah ada. Gunakan menu update untuk merubah stok.");
            return;
        }
        
        // Minta data barang dari user
        String name = inputString("Masukkan Nama Item: ");
        String category = inputString("Masukkan Kategori: ");
        int qty = inputInt("Masukkan Quantity awal: ");
        double price = inputDouble("Masukkan Harga per unit: ");

        // Tambahkan barang baru ke array dan increment counter
        items[itemCount++] = new Item(id, name, category, qty, price);
        System.out.println("Item berhasil ditambahkan.");
    }

    // Fungsi untuk menampilkan semua barang di gudang
    static void displayAll() {
        // Cek apakah ada barang
        if (itemCount == 0) {
            System.out.println("Belum ada item di gudang.");
            return;
        }
        
        System.out.println("===== LIST ITEM =====");
        // Loop untuk menampilkan setiap barang
        for (int i = 0; i < itemCount; i++) {
            System.out.println("Item ke-" + (i + 1));
            System.out.println(items[i].toString());
            System.out.println("---------------------");
        }
    }

    // ========== FUNGSI SORTING: MERGE SORT ==========
    // Fungsi untuk mengurutkan barang berdasarkan ID (ascending)
    static void sortByID() {
        if (itemCount <= 1) {
            System.out.println("Data kurang dari 2, tidak perlu diurutkan.");
            return;
        }
        
        // Buat array temporary untuk proses merge sort
        Item[] temp = new Item[itemCount];
        // Panggil merge sort dengan range 0 sampai itemCount-1
        mergeSortByID(items, temp, 0, itemCount - 1);
        System.out.println("Data berhasil diurutkan berdasarkan ItemID (ascending).");
    }

    // Fungsi merge sort rekursif berdasarkan ID
    // Parameter: arr (array utama), temp (array temporary), left (indeks kiri), right (indeks kanan)
    static void mergeSortByID(Item[] arr, Item[] temp, int left, int right) {
        if (left >= right) return; // Basis rekursi: jika left >= right, sudah selesai
        
        // Cari titik tengah
        int mid = (left + right) / 2;
        
        // Recursively sort bagian kiri
        mergeSortByID(arr, temp, left, mid);
        
        // Recursively sort bagian kanan
        mergeSortByID(arr, temp, mid + 1, right);
        
        // Merge kedua bagian yang sudah terurut
        mergeByID(arr, temp, left, mid, right);
    }

    // Fungsi merge untuk menggabungkan dua bagian array yang sudah terurut berdasarkan ID
    static void mergeByID(Item[] arr, Item[] temp, int left, int mid, int right) {
        // Copy data ke array temporary
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        
        // Pointer untuk bagian kiri, kanan, dan hasil merge
        int i = left, j = mid + 1, k = left;
        
        // Bandingkan dan merge: ambil yang lebih kecil lebih dulu (ascending)
        while (i <= mid && j <= right) {
            if (temp[i].itemID.compareTo(temp[j].itemID) <= 0) {
                arr[k++] = temp[i++];  // Ambil dari kiri
            } else {
                arr[k++] = temp[j++];  // Ambil dari kanan
            }
        }

        // Salin sisa elemen dari bagian
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
    }

    // Fungsi untuk mengurutkan barang berdasarkan jumlah stok (descending)
    static void sortByQuantity() {
        if (itemCount <= 1) {
            System.out.println("Data kurang dari 2, tidak perlu diurutkan.");
            return;
        }
        
        Item[] temp = new Item[itemCount];
        // Panggil merge sort dengan range 0 sampai itemCount-1
        mergeSortByQty(items, temp, 0, itemCount - 1);
        System.out.println("Data berhasil diurutkan berdasarkan Quantity (descending).");
    }

    // Fungsi merge sort rekursif berdasarkan jumlah stok
    static void mergeSortByQty(Item[] arr, Item[] temp, int left, int right) {
        if (left >= right) return; // Basis rekursi
        
        int mid = (left + right) / 2;
        mergeSortByQty(arr, temp, left, mid);      // Sort bagian kiri
        mergeSortByQty(arr, temp, mid + 1, right); // Sort bagian kanan
        mergeByQty(arr, temp, left, mid, right);   // Merge kedua bagian
    }

    // Fungsi merge untuk menggabungkan dua bagian array berdasarkan jumlah stok (descending)
    static void mergeByQty(Item[] arr, Item[] temp, int left, int mid, int right) {
        // Copy data ke array temporary
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        
        int i = left, j = mid + 1, k = left;
        
        // Bandingkan dan merge: urutan descending berarti yang lebih besar duluan
        while (i <= mid && j <= right) {
            if (temp[i].quantity >= temp[j].quantity) {
                arr[k++] = temp[i++];  // Ambil dari kiri (quantity lebih besar/sama)
            } else {
                arr[k++] = temp[j++];  // Ambil dari kanan
            }
        }
        // Salin sisa elemen dari bagian yang belum terurut
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
    }

    // ========== FUNGSI SEARCHING ==========
    // Fungsi linear search: mencari barang berdasarkan nama (substring, case-insensitive)
    static void searchByNameLinear() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        
        String key = inputString("Masukkan keyword nama: ").toLowerCase();
        boolean found = false;
        
        // Loop melalui semua barang untuk mencari yang cocok
        for (int i = 0; i < itemCount; i++) {
            // Cek apakah nama barang mengandung keyword (case-insensitive)
            if (items[i].name.toLowerCase().contains(key)) {
                if (!found) {
                    System.out.println("Item yang cocok:");
                    found = true;
                }
                System.out.println("---------------------");
                System.out.println(items[i].toString());
            }
        }
        
        if (!found) System.out.println("Tidak ada item yang cocok.");
    }

    // Fungsi binary search: mencari barang berdasarkan ID (rekursif)
    // PENTING: Array harus sudah diurutkan berdasarkan ID terlebih dahulu!
    static void searchByIDBinary() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        
        System.out.println("Pastikan data sudah terurut berdasarkan ItemID.");
        String key = inputString("Masukkan ItemID yang dicari: ");
        
        // Panggil fungsi binary search dan dapatkan index
        int idx = binarySearchByID(items, 0, itemCount - 1, key);
        
        if (idx == -1) {
            System.out.println("Item dengan ID " + key + " tidak ditemukan.");
        } else {
            System.out.println("Item ditemukan pada indeks " + idx + ":");
            System.out.println(items[idx].toString());
        }
    }

    // Fungsi binary search rekursif berdasarkan ID
    // Parameter: arr (array), left (batas kiri), right (batas kanan), key (ID yang dicari)
    // Return: index item jika ditemukan, -1 jika tidak ditemukan
    static int binarySearchByID(Item[] arr, int left, int right, String key) {
        if (left > right) return -1; // Basis rekursi: item tidak ditemukan
        
        int mid = (left + right) / 2;
        int cmp = arr[mid].itemID.compareTo(key); // Bandingkan ID di tengah dengan key
        
        if (cmp == 0) {
            return mid; // ID ditemukan!
        } else if (cmp > 0) {
            // ID di tengah lebih besar, cari di bagian kiri
            return binarySearchByID(arr, left, mid - 1, key);
        } else {
            // ID di tengah lebih kecil, cari di bagian kanan
            return binarySearchByID(arr, mid + 1, right, key);
        }
    }

    // Fungsi helper untuk mencari index barang berdasarkan ID (iteratif)
    // Digunakan untuk cek keunikan ID dan mencari barang untuk update
    static int findIndexByID(String id) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].itemID.equals(id)) return i;
        }
        return -1; // Tidak ditemukan
    }

    // ========== FUNGSI UPDATE STOK ==========
    // Fungsi untuk update stok barang (masuk atau keluar)
    static void updateStock() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        
        String id = inputString("Masukkan ItemID yang akan diupdate: ");
        
        // Cari barang dengan ID tersebut
        int idx = findIndexByID(id);
        if (idx == -1) {
            System.out.println("Item tidak ditemukan.");
            return;
        }
        
        System.out.println("Item ditemukan:");
        System.out.println(items[idx].toString());
        System.out.println("1. Tambah stok (IN)");
        System.out.println("2. Kurangi stok (OUT)");
        int pilih = inputInt("Pilih aksi: ");
        
        // Jika user memilih tambah stok
        if (pilih == 1) {
            int add = inputInt("Masukkan jumlah masuk: ");
            if (add < 0) { 
                System.out.println("Jumlah tidak valid."); 
                return; 
            }
            items[idx].quantity += add;  // Tambah ke stok
            System.out.println("Stok berhasil ditambah. Qty sekarang: " + items[idx].quantity);
        } 
        // Jika user memilih kurangi stok
        else if (pilih == 2) {
            int sub = inputInt("Masukkan jumlah keluar: ");
            // Validasi: jumlah tidak boleh negatif dan tidak boleh melebihi stok
            if (sub < 0 || sub > items[idx].quantity) {
                System.out.println("Jumlah tidak valid atau melebihi stok.");
                return;
            }
            items[idx].quantity -= sub;  // Kurangi dari stok
            System.out.println("Stok berhasil dikurangi. Qty sekarang: " + items[idx].quantity);
        } 
        else {
            System.out.println("Pilihan tidak valid.");
        }
    }
}
