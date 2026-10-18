package playlistrekursif;

import java.io.OutputStream;
import java.io.PrintStream;

/*
 * Bagian 3: Eksperimen Pengukuran Waktu Eksekusi.
 * Memanggil fungsi rekursif dari PlaylistRekursif untuk n = 3, 5, 10 lagu
 * (ditambah n lebih besar supaya pola pertumbuhan linear O(n) terlihat).
 *
 * Catatan metode:
 * - Waktu diukur dengan System.nanoTime(), lalu dikonversi ke milidetik.
 * - Sebelum diukur, ada tahap pemanasan (warm-up) agar JIT tidak ikut terhitung.
 * - Tiap fungsi dijalankan berulang kali, lalu diambil rata-ratanya.
 * - Output println milik tampilkanMundur() dialihkan ke "tempat sampah"
 *   saat pengulangan, supaya yang terukur adalah kerja fungsinya,
 *   bukan kecepatan layar/konsol.
 */
public class PengukuranWaktu {

    static final int WARMUP = 20_000;
    static final int ULANG = 100_000;

    static Lagu[] buatPlaylist(int n) {
        Lagu[] data = new Lagu[n];
        for (int i = 0; i < n; i++) {
            // durasi dibuat bervariasi antara 2.50 s.d. 5.49 menit
            data[i] = new Lagu("Lagu " + (i + 1), "Artis " + (i % 3 + 1),
                    2.5 + (i * 37 % 300) / 100.0);
        }
        return data;
    }

    // hasil dummy dicatat supaya JIT tidak membuang pemanggilan yang tidak terpakai
    static double sink = 0;

    static double ukurTotal(Lagu[] p, int ulang) {
        long t0 = System.nanoTime();
        for (int i = 0; i < ulang; i++) {
            sink += PlaylistRekursif.totalDurasi(p, p.length);
        }
        return (System.nanoTime() - t0) / 1_000_000.0 / ulang;
    }

    static double ukurMundur(Lagu[] p, int ulang) {
        long t0 = System.nanoTime();
        for (int i = 0; i < ulang; i++) {
            PlaylistRekursif.tampilkanMundur(p, p.length - 1);
        }
        return (System.nanoTime() - t0) / 1_000_000.0 / ulang;
    }

    static double ukurTerpanjang(Lagu[] p, int ulang) {
        long t0 = System.nanoTime();
        for (int i = 0; i < ulang; i++) {
            sink += PlaylistRekursif.cariDurasiTerpanjang(p, p.length - 1);
        }
        return (System.nanoTime() - t0) / 1_000_000.0 / ulang;
    }

    public static void main(String[] args) {
        PrintStream konsol = System.out;
        PrintStream kosong = new PrintStream(new OutputStream() {
            @Override public void write(int b) { }
            @Override public void write(byte[] b, int off, int len) { }
        });

        int[] ukuran = {3, 5, 10, 100, 1000, 5000};

        // pemanasan di berbagai ukuran
        System.setOut(kosong);
        for (int n : ukuran) {
            Lagu[] p = buatPlaylist(n);
            ukurTotal(p, WARMUP);
            ukurMundur(p, WARMUP / 10);
            ukurTerpanjang(p, WARMUP);
        }

        double[][] hasil = new double[ukuran.length][3];
        for (int k = 0; k < ukuran.length; k++) {
            int n = ukuran[k];
            Lagu[] p = buatPlaylist(n);
            int ulang = (n >= 1000) ? ULANG / 100 : ULANG;
            hasil[k][0] = ukurTotal(p, ulang);
            hasil[k][1] = ukurMundur(p, ulang);
            hasil[k][2] = ukurTerpanjang(p, ulang);
        }
        System.setOut(konsol);

        System.out.println("=== PENGUKURAN WAKTU EKSEKUSI (rata-rata per pemanggilan) ===");
        System.out.printf("%-12s | %-18s | %-20s | %-24s%n",
                "Jumlah Lagu", "totalDurasi()", "tampilkanMundur()", "cariDurasiTerpanjang()");
        System.out.println("-".repeat(85));
        for (int k = 0; k < ukuran.length; k++) {
            System.out.printf("%-12d | %-18s | %-20s | %-24s%n", ukuran[k],
                    String.format("%.6f ms", hasil[k][0]),
                    String.format("%.6f ms", hasil[k][1]),
                    String.format("%.6f ms", hasil[k][2]));
        }

        // rasio terhadap n = 3 sebagai bukti pertumbuhan linear
        System.out.println("\n=== PERBANDINGAN n (rasio) vs WAKTU (rasio, acuan n = 10) ===");
        int acuan = 2; // indeks n = 10
        for (int k = acuan + 1; k < ukuran.length; k++) {
            System.out.printf("n x%-5.0f -> totalDurasi x%-7.1f tampilkanMundur x%-7.1f cariTerpanjang x%.1f%n",
                    ukuran[k] / (double) ukuran[acuan],
                    hasil[k][0] / hasil[acuan][0],
                    hasil[k][1] / hasil[acuan][1],
                    hasil[k][2] / hasil[acuan][2]);
        }
    }
}
