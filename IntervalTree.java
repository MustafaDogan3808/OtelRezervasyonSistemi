import java.time.LocalDate;

public class IntervalTree {
    private IntervalTreeNode root;

    // 1. REZERVASYON EKLEME
    //
    public void insert(Reservation res) {
        root = insertRecursive(root, res);
        // ağacımızı oluşturuyoruz
    }

    private IntervalTreeNode insertRecursive(IntervalTreeNode node, Reservation res) {
        if (node == null)
            return new IntervalTreeNode(res);// eğer node boşsa oraya yerleşiyor

        // Giriş tarihine göre sola veya sağa ekle
        if (res.getBaslangicTarihi().isBefore(node.reservation.getBaslangicTarihi())) {
            node.left = insertRecursive(node.left, res);
            // tarihleri kıyaslıyoruz eğer baktığımız yerdeki tarih bizim tarihimizden
            // büyükse biz sola gidiyoruz
        } else {
            node.right = insertRecursive(node.right, res);
        }
        // yine tarihkeri kıyasladık bizim tarihimiz büyükse sola koyuyoruz

        // Düğüm eklenirken yukarı doğru maxCikis değerlerini güncelle
        if (node.maxCikis.isBefore(res.getBitisTarihi())) {
            node.maxCikis = res.getBitisTarihi();
        }
        /*
         * Bu maks çıkış mantığında şimdi bi müşterimiz var diyelim mayısın 13 ü ile 17
         * si arasında kalıcak
         * diğer müşteri de 20 si ile 24 ü arasında kalıcak burda bizim maks çıkışımız
         * 24 mayıs
         * şimdi eğer başka birisi gelip 26 mayıs boş mu diye sorduğunda sorgu yapmadan
         * direkt maks çıkışa bakılacak
         * ve boş olduğu anlaşılacak çünkü 24 mayıstan sonra bütün tarihler kesinlikle
         * boş
         */

        return node;
    }

    // 2. ÇAKIŞMA KONTROLÜ (OVERLAP CHECK)
    public boolean isOverlapping(LocalDate start, LocalDate end) {
        return checkOverlap(root, start, end);
        /*
         * bize verilen başlama bitiş tarihlerine roota bakar ve
         * odanın boş olup olmadığını söyler
         */
    }

    private boolean checkOverlap(IntervalTreeNode node, LocalDate start, LocalDate end) {
        if (node == null)
            return false;

        // Klasik çakışma kuralı: (Yeni Giriş < Mevcut Çıkış) VE (Yeni Çıkış > Mevcut
        // Giriş)
        if (start.isBefore(node.reservation.getBitisTarihi()) &&
                end.isAfter(node.reservation.getBaslangicTarihi())) {
            return true;
        }

        // Eğer sol dal boş değilse ve solun max çıkış tarihi bizim girişimizden
        // sonraysa;
        // sol tarafta çakışma ihtimali vardır, sola git.
        if (node.left != null && node.left.maxCikis.isAfter(start)) {
            return checkOverlap(node.left, start, end);
        }

        // Değilse sağa bak
        return checkOverlap(node.right, start, end);
    }

    // 3. REZERVASYON İPTALİ (SİLME)
    public void delete(Reservation res) {
        root = deleteRecursive(root, res);
    }

    private IntervalTreeNode deleteRecursive(IntervalTreeNode node, Reservation res) {
        // Ağaç boşsa veya düğüm bulunamadıysa
        if (node == null) return null;

        // Silinecek düğümü başlangıç tarihine göre ara
        if (res.getBaslangicTarihi().isBefore(node.reservation.getBaslangicTarihi())) {
            node.left = deleteRecursive(node.left, res);
        } else if (res.getBaslangicTarihi().isAfter(node.reservation.getBaslangicTarihi())) {
            node.right = deleteRecursive(node.right, res);
        } else {
            // Başlangıç tarihleri eşleşti. Doğru rezervasyon mu diye bitiş tarihlerini de kontrol edelim.
            if (node.reservation.getBitisTarihi().equals(res.getBitisTarihi())) {
                
                // Durum 1 & 2: Düğümün hiç çocuğu yok veya tek çocuğu var
                if (node.left == null) return node.right;
                else if (node.right == null) return node.left;

                // Durum 3: Düğümün iki çocuğu var. 
                // Sağ alt ağaçtaki en küçük başlangıç tarihli düğümü (inorder successor) bul
                IntervalTreeNode temp = minNode(node.right);
                
                // O düğümün bilgilerini mevcut düğüme kopyala
                node.reservation = temp.reservation;
                
                // Kopyaladığımız o küçük düğümü orijinal yerinden sil
                node.right = deleteRecursive(node.right, temp.reservation);
            } else {
                // Başlangıç tarihi aynı ama farklı bir rezervasyonsa sağdan aramaya devam et
                node.right = deleteRecursive(node.right, res);
            }
        }

        // EN ZOR KISIM: Düğüm silindikten sonra veya alt dallar değiştikçe maxCikis değerini GÜNCELLE
        if (node != null) {
            updateMax(node);
        }

        return node;
    }

    // Sağ alt ağaçtaki en küçük düğümü bulan yardımcı metod
    private IntervalTreeNode minNode(IntervalTreeNode node) {
        IntervalTreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Düğümün maxCikis değerini alt çocuklarına bakarak güncelleyen yardımcı metod
    private void updateMax(IntervalTreeNode node) {
        node.maxCikis = node.reservation.getBitisTarihi(); // Önce kendi bitiş tarihini varsay
        
        if (node.left != null && node.left.maxCikis.isAfter(node.maxCikis)) {
            node.maxCikis = node.left.maxCikis;
        }
        if (node.right != null && node.right.maxCikis.isAfter(node.maxCikis)) {
            node.maxCikis = node.right.maxCikis;
        }
    }
    
    // 4. KRONOLOJİK LİSTELEME (IN-ORDER TRAVERSAL)
    public void kronolojikListele() {
        if (root == null) {
            System.out.println("Bu odanın takvimi tamamen boş. Hiç rezervasyon yok.");
            return;
        }
        inOrderTraversal(root);
    }

    private void inOrderTraversal(IntervalTreeNode node) {
        if (node == null) return;
        
        // 1. Önce sol alt ağaç (Daha eski tarihler)
        inOrderTraversal(node.left);
        
        // 2. Düğümün kendisini yazdır
        Reservation res = node.reservation;
        System.out.println("-> Giriş: " + res.getBaslangicTarihi() + " | Çıkış: " + res.getBitisTarihi() + 
                           " | Müşteri: " + res.getCustomer().getTamAd());
        
        // 3. Sonra sağ alt ağaç (Daha ileri tarihler)
        inOrderTraversal(node.right);
    }
}