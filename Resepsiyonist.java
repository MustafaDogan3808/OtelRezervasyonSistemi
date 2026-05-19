import java.time.LocalDate;
import java.util.Scanner;

public class Resepsiyonist {

    private Hotel otel;
    private Scanner scanner;

    public Resepsiyonist() {
        otel = new Hotel();
        scanner = new Scanner(System.in);

        // Örnek odalar
        otel.OdaEkle(new Room("101", 2, 1500));
        otel.OdaEkle(new Room("102", 3, 2000));
        otel.OdaEkle(new Room("103", 1, 1000));
    }

    public void menuyuBaslat() {

        while (true) {

            System.out.println("\n===== OTEL REZERVASYON SİSTEMİ =====");
            System.out.println("1- Müşteri Kaydı");
            System.out.println("2- Rezervasyon Yap");
            System.out.println("3- Odaları Listele");
            System.out.println("4- Testleri Çalıştır"); // YENİ EKLENEN SEÇENEK
            System.out.println("5- Çıkış");           // 5'E KAYDIRILDI
            System.out.print("Seçiminiz: ");

            try {

                int secim = scanner.nextInt();
                scanner.nextLine();

                switch (secim) {

                    case 1:
                        musteriKaydi();
                        break;

                    case 2:
                        rezervasyonYap();
                        break;

                    case 3:
                        odalariListele();
                        break;

                    case 4:
                        System.out.println("\n>>> TESTLER BAŞLATILIYOR <<<");
                        Main.testleriCalistir(); // Main.java'daki test metodunu çağırıyoruz
                        System.out.println("\n>>> TESTLER TAMAMLANDI <<<");
                        break;

                    case 5:
                        System.out.println("Program kapatılıyor...");
                        return;

                    default:
                        System.out.println("Geçersiz seçim!");
                }

            } catch (Exception e) {
                System.out.println("Hatalı giriş yaptınız!");
                scanner.nextLine();
            }
        }
    }

    private void musteriKaydi() {

        String tc;

        while (true) {
            System.out.print("TC: ");
            tc = scanner.nextLine();

            if (!tc.matches("[0-9]+")) {
                System.out.println("TC kimlik numarası sadece rakamlardan oluşmalıdır!");
            } else if (tc.length() < 11) {
                System.out.println("Eksik girdiniz! TC kimlik numarası 11 haneli olmalıdır.");
            } else if (tc.length() > 11) {
                System.out.println("Fazla girdiniz! TC kimlik numarası 11 haneli olmalıdır.");
            } else if (tc.charAt(0) == '0') {
                System.out.println("TC kimlik numarasının ilk rakamı 0 olamaz!");
            } else {
                break;
            }
        }

        System.out.print("Ad Soyad: ");
        String ad = scanner.nextLine();

        System.out.print("Telefon: ");
        String tel = scanner.nextLine();

        Customer yeni = new Customer(tc, ad, tel);

        otel.MusteriEkle(yeni);

        System.out.println("Müşteri başarıyla eklendi.");
    }

    private void rezervasyonYap() {

        try {

            String tc;

            while (true) {
                System.out.print("TC: ");
                tc = scanner.nextLine();

                if (!tc.matches("[0-9]+")) {
                    System.out.println("TC kimlik numarası sadece rakamlardan oluşmalıdır!");
                } else if (tc.length() < 11) {
                    System.out.println("Eksik girdiniz! TC kimlik numarası 11 haneli olmalıdır.");
                } else if (tc.length() > 11) {
                    System.out.println("Fazla girdiniz! TC kimlik numarası 11 haneli olmalıdır.");
                } else if (tc.charAt(0) == '0') {
                    System.out.println("TC kimlik numarasının ilk rakamı 0 olamaz!");
                } else {
                    break;
                }
            }

            String odaNo;

            while (true) {

                System.out.print("Oda No (101 / 102 / 103): ");
                odaNo = scanner.nextLine();

                if (odaNo.equals("101") ||
                        odaNo.equals("102") ||
                        odaNo.equals("103")) {

                    break;

                } else {
                    System.out.println("Geçersiz oda numarası girdiniz!");
                }
            }
            System.out.print("Giriş Tarihi (YYYY-AA-GG): ");
            LocalDate giris = LocalDate.parse(scanner.nextLine());

            System.out.print("Çıkış Tarihi (YYYY-AA-GG): ");
            LocalDate cikis = LocalDate.parse(scanner.nextLine());

            if (giris.isBefore(LocalDate.now())) {
                System.out.println("Giriş tarihi bugünden önce olamaz!");
                return;
            }

            if (cikis.isBefore(giris)) {
                System.out.println("Çıkış tarihi giriş tarihinden önce olamaz!");
                return;
            }

            otel.RezervasyonYap(tc, odaNo, giris, cikis);

        } catch (Exception e) {
            System.out.println("Tarih formatı hatalı!");
        }
    }

    private void odalariListele() {

        System.out.println("\nOdalar:");
        System.out.println("101");
        System.out.println("102");
        System.out.println("103");
    }
}