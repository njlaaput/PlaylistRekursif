package playlistrekursif;

public class PlaylistRekursif {

    /*
     * Tujuan fungsi : menghitung total durasi seluruh lagu di playlist secara rekursif.
     * Base case     : n == 0 (tidak ada lagu tersisa), mengembalikan 0.
     * Recursive case: durasi lagu ke-n (index n-1) + totalDurasi dari n-1 lagu sisanya.
     * Kompleksitas  : O(n), karena fungsi dipanggil sebanyak n kali dan
     *                 tiap pemanggilan hanya melakukan 1 penjumlahan.
     */
    static double totalDurasi(Lagu[] list, int n) {
        if (n == 0) {
            return 0;
        }
        return list[n - 1].getDurasi() + totalDurasi(list, n - 1);
    }

    /*
    * Tujuan fungsi : menampilkan daftar lagu dari akhir ke awal secara rekursif.
    * Base case     : index < 0, proses berhenti.
    * Recursive case: menampilkan lagu pada index saat ini, kemudian memanggil fungsi untuk index sebelumnya.
    * Kompleksitas  : O(n), karena setiap lagu diproses satu kali.
    */
    static void tampilkanMundur(Lagu[] list, int index) {
        if (index < 0) {
            return;
        }
        System.out.println((index + 1) + ". " + list[index].getJudul());
        tampilkanMundur(list, index - 1);
    }

    /*
     * Tujuan fungsi : mencari durasi lagu terpanjang di playlist secara rekursif.
     * Base case     : index == 0 (tinggal satu lagu), mengembalikan durasi lagu pertama.
     * Recursive case: membandingkan durasi lagu pada index saat ini dengan hasil
     *                 cariDurasiTerpanjang untuk index - 1, lalu mengambil yang terbesar (Math.max).
     * Kompleksitas  : O(n), karena fungsi dipanggil sebanyak n kali dan
     *                 tiap pemanggilan hanya melakukan 1 perbandingan.
     */
    static double cariDurasiTerpanjang(Lagu[] list, int index) {
        if (index == 0) {
            return list[0].getDurasi();
        }
        return Math.max(list[index].getDurasi(), cariDurasiTerpanjang(list, index - 1));
    }

    public static void main(String[] args) {
        // data dummy buat testing bagianku (nanti diganti sama hasil integrasi)
        Lagu[] playlist = {
            new Lagu("Perfect", "Ed Sheeran", 4.23),
            new Lagu("Shivers", "Ed Sheeran", 3.50),
            new Lagu("Yellow", "Coldplay", 4.10),
            new Lagu("Fix You", "Coldplay", 4.52),
            new Lagu("Viva La Vida", "Coldplay", 4.02)
        };

        long mulai = System.nanoTime();
        double total = totalDurasi(playlist, playlist.length);
        long selesai = System.nanoTime();

        System.out.println("=== ANALISIS REKURSIF PLAYLIST ===");
        System.out.println("Jumlah lagu : " + playlist.length);
        System.out.println("Total durasi : " + String.format("%.2f", total) + " menit");
        System.out.println("Execution Time (totalDurasi): "
                + String.format("%.3f", (selesai - mulai) / 1_000_000.0) + " ms");
        System.out.println("\nDaftar lagu (ditampilkan terbalik):"); // Menampilkan judul daftar lagu dengan urutan terbalik.
        tampilkanMundur(playlist, playlist.length - 1); // Memanggil fungsi tampilkanMundur() mulai dari indeks terakhir playlist.

        long mulaiMax = System.nanoTime();
        double terpanjang = cariDurasiTerpanjang(playlist, playlist.length - 1);
        long selesaiMax = System.nanoTime();

        // mencari judul lagu yang durasinya sama dengan durasi terpanjang
        String judulTerpanjang = "";
        for (Lagu l : playlist) {
            if (l.getDurasi() == terpanjang) {
                judulTerpanjang = l.getJudul() + " - " + l.getArtis();
            }
        }
        System.out.println("\nLagu terpanjang : \"" + judulTerpanjang + "\" ("
                + String.format("%.2f", terpanjang) + " menit)");
        System.out.println("Execution Time (cariDurasiTerpanjang): "
                + String.format("%.3f", (selesaiMax - mulaiMax) / 1_000_000.0) + " ms");
    }
}

class Lagu {
    private String judul;
    private String artis;
    private double durasi;

    public Lagu(String judul, String artis, double durasi) {
        this.judul = judul;
        this.artis = artis;
        this.durasi = durasi;
    }

    public String getJudul() { return judul; }
    public String getArtis() { return artis; }
    public double getDurasi() { return durasi; }
}