import java.time.LocalDate;

import java.time.LocalDate;

public class Main {
    
    // 1. Sadece menüyü başlatan ana metodumuz
    public static void main(String[] args) {
        Resepsiyonist resepsiyon = new Resepsiyonist();
        resepsiyon.menuyuBaslat();
    }

    // 2. Menüden 4'e basıldığında çalışacak olan test metodumuz
    public static void testleriCalistir() {
        
        System.out.println("Otel Rezervasyon Sistemi Çalışıyor!");
        Customer musteri1 = new Customer("11111", "Mustafa Doğan Özdin", "123123123");
        System.out.println("Yeni Müşteri Eklendi:" + musteri1.getTamAd());
        Room oda1 = new Room("101", 2, 1500, false);
        System.out.println("Yeni oda eklendi: Oda " + oda1.getOdaNo());
        System.out.println("Oda Dolu mu?: " + oda1.DoluMu());
        System.out.println("Rezervasyon Başarılı Müşteri:" + musteri1.getTamAd() + " Oda No:" + oda1.getOdaNo());
        Hotel OtelDGN = new Hotel();
        OtelDGN.MusteriEkle(musteri1);
        OtelDGN.OdaEkle(oda1);

        Hotel otel = new Hotel();

        // 2. Odamızı oluşturup otele ekliyoruz
        Room oda101 = new Room("101", 1, 500);
        otel.OdaEkle(oda101); // Otel sınıfındaki kendi ekleme metodunun adını yaz

        // 3. Müşterileri oluşturup otele kaydediyoruz
        Customer musteri10 = new Customer("11111111111", "Ahmet Yılmaz", "5551234567");
        Customer musteri11 = new Customer("22222222222", "MehmetÖztürk", "5559876543");
        Customer musteri12 = new Customer("33333333333", "Ayşe Demir", "5551112233");

        otel.MusteriEkle(musteri10);
        otel.MusteriEkle(musteri11);
        otel.MusteriEkle(musteri12);

        System.out.println("Rezervasyon Testleri\n");

        // TEST 1: Ahmet için normal bir rezervasyon (Oda boş, kabul edilmeli)
        // Tarih: 10 Mayıs 2026 - 15 Mayıs 2026
        System.out.println("TEST 1: Ahmet (10-15 Mayıs) rezervasyon denemesi...");
        otel.RezervasyonYap("11111111111", "101", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 15));

        // TEST 2: Mehmet için Ahmet'ten sonraki bir tarih (Çakışma yok, kabul edilmeli)
        // Tarih: 16 Mayıs 2026 - 20 Mayıs 2026
        System.out.println("\nTEST 2: Mehmet (16-20 Mayıs) rezervasyon denemesi...");
        otel.RezervasyonYap("22222222222", "101", LocalDate.of(2026, 5, 16), LocalDate.of(2026, 5, 20));

        // TEST 3: Ayşe için ÇAKIŞAN bir tarih (Reddedilmeli!)
        // Tarih: 14 Mayıs 2026 - 18 Mayıs 2026 (Ahmet çıkmadan giriyor, Mehmet girmeden
        // çıkıyor)
        System.out.println("\nTEST 3: Ayşe (14-18 Mayıs) rezervasyon denemesi...");
        otel.RezervasyonYap("33333333333", "101", LocalDate.of(2026, 5, 14), LocalDate.of(2026, 5, 18));

        // TEST 4: Ayşe için temiz bir tarih (En geç çıkıştan sonra, kabul edilmeli)
        // Tarih: 22 Mayıs 2026 - 25 Mayıs 2026
        System.out.println("\nTEST 4: Ayşe (22-25 Mayıs) yeni rezervasyon denemesi...");
        otel.RezervasyonYap("33333333333", "101", LocalDate.of(2026, 5, 22), LocalDate.of(2026, 5, 25));

        // TEST 5: Ahmet için peş peşe rezervasyonlar ve VIP/Fatura kontrolü (3'ten
        // fazla rezervasyon)
        System.out.println("\nTEST 5: Ahmet için peş peşe rezervasyonlar ve VIP testleri...");
        otel.RezervasyonYap("11111111111", "101", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));
        otel.RezervasyonYap("11111111111", "101", LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 12));

        System.out.println("Ahmet'in 4. rezervasyonu (VIP indirimi tetiklenmeli):");
        otel.RezervasyonYap("11111111111", "101", LocalDate.of(2026, 6, 15), LocalDate.of(2026, 6, 20));

        // Ahmet'in son rezervasyonunun faturasını hesaplama
        Reservation ahmetSonRezervasyon = new Reservation(musteri10, oda101, LocalDate.of(2026, 6, 15),
                LocalDate.of(2026, 6, 20));
        otel.faturaHesapla(ahmetSonRezervasyon);

        // TEST 6: Ayşe'nin (22-25 Mayıs) rezervasyonunu iptal etme (Interval Tree
        // silme)
        System.out.println("\nTEST 6: Ayşe'nin (22-25 Mayıs) rezervasyonu iptal ediliyor...");
        Reservation iptalEdilecekRes = new Reservation(musteri12, oda101, LocalDate.of(2026, 5, 22),
                LocalDate.of(2026, 5, 25));
        oda101.getTakvim().delete(iptalEdilecekRes);

        // TEST 7: Silinen tarihe (22-25 Mayıs) Mehmet'in rezervasyon denemesi (Kabul
        // edilmeli)
        System.out.println("\nTEST 7: Silinen tarihe Mehmet (22-25 Mayıs) yeni rezervasyon denemesi...");
        otel.RezervasyonYap("22222222222", "101", LocalDate.of(2026, 5, 22), LocalDate.of(2026, 5, 25));
        
        // TEST 8: Doluluk Raporu Testi (13 Mayıs - 18 Mayıs)
        // 101 dolu olabilir ama otelde başta eklenen Oda 102 ve Oda 103 de var (Resepsiyonist sınıfında eklenmişti)
        System.out.println("TEST 8: 13-18 Mayıs tarihleri arası boş odalar listesi:");
        otel.bosOdalariListele(LocalDate.of(2026, 5, 13), LocalDate.of(2026, 5, 18));

        // TEST 9: Müşteri Geçmişi Testi (Ahmet'in 4 rezervasyonu vardı)
        System.out.println("\nTEST 9: Ahmet Yılmaz'ın tüm rezervasyonları listeleniyor:");
        otel.musteriGecmisiGetir("11111111111");

        // TEST 10: Oda Bazlı Kronolojik Takvim Testi (Ağaç İçi Sıralı Dolaşım)
        System.out.println("\nTEST 10: 101 numaralı odanın güncel takvimi (Tarih sırasına göre çıkmalı):");
        otel.odaTakvimiListele("101");
    }

}
