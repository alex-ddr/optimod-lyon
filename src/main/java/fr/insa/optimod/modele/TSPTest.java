package fr.insa.optimod.modele;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSPTest {

    // ====== Stub minimal pour tester ======
    // Si tu as déjà Carte / Noeud dans ton code, enlève ces classes

    static class Noeud {
        private final long id;
        Noeud(long id) { this.id = id; }
        public long getId() { return id; }
    }

    static class Carte {
        public Noeud obtenirNoeud(long id) {
            return new Noeud(id);
        }
    }

    // ===============================================
    //  TEST PRINCIPAL : DP vs brute force
    // ===============================================
    @Test
    public void testTspPickupDeliveryOptimal() {
        // 0 = dépôt
        // 1 = pickup1, 2 = livraison1
        // 3 = pickup2, 4 = livraison2
        int n = 5;

        double[][] cout = {
                //  0    1    2    3    4
                {  0,   2,   9,   3,   8 }, // 0 ->
                {  2,   0,   1,   4,   7 }, // 1 ->
                {  9,   1,   0,   6,   2 }, // 2 ->
                {  3,   4,   6,   0,   1 }, // 3 ->
                {  8,   7,   2,   1,   0 }  // 4 ->
        };

        // IDs fictifs (ici = mêmes que les indices)
        HashMap<Integer, Long> mapIndexAId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            mapIndexAId.put(i, (long) i);
        }

        Carte carte = new Carte();

        // s = {1,2,3,4}
        long s = (1L << (n - 1)) - 1L;

        // ===== 1. Résultat de ton DP =====
        TSP tsp = new TSP(); // ta classe (doit avoir computeD)
        PointLivraison resDP = tsp.computeD(0, s, n, cout, carte, mapIndexAId);
        double dpCost = resDP.getG();

        // ===== 2. Résultat brut force =====
        BruteForceResult bf = bruteForceTSP(n, cout);
        double bfCost = bf.cost;

        System.out.println("Coût DP      = " + dpCost);
        System.out.println("Coût brute   = " + bfCost);

        System.out.print("Chemin brute (indices) : 0");
        for (int idx : bf.chemin) {
            System.out.print(" -> " + idx);
        }
        System.out.println(" -> 0");

        // On accepte un tout petit delta dû aux doubles :
        assertEquals(bfCost, dpCost, 1e-6,
                "Le coût DP n'est pas égal au coût optimal brut-force");
    }

    // ==========================
    //    BRUTE FORCE ADAPTÉ
    // ==========================

    static class BruteForceResult {
        double cost;
        int[] chemin; // permutation de 1..n-1
    }

    /**
     * Brute force avec ta convention :
     * - 0 = dépôt
     * - indices pairs (2,4,6,...) = livraisons
     * - pickup associé = indice impair juste avant (1,3,5,...)
     */
    static BruteForceResult bruteForceTSP(int n, double[][] cout) {
        int[] perm = new int[n - 1]; // on permute 1..n-1
        for (int i = 0; i < n - 1; i++) {
            perm[i] = i + 1;
        }

        BruteForceResult best = new BruteForceResult();
        best.cost = Double.POSITIVE_INFINITY;
        best.chemin = null;

        permute(perm, 0, n, cout, best);
        return best;
    }

    private static void permute(int[] perm,
                                int l,
                                int n,
                                double[][] cout,
                                BruteForceResult best) {
        if (l == perm.length) {
            if (!respecteContraintesPickupDelivery(perm)) {
                return;
            }

            // coût : 0 -> perm[0] -> ... -> perm[last] -> 0
            double total = 0.0;
            int prev = 0;
            for (int idx : perm) {
                total += cout[prev][idx];
                prev = idx;
            }
            total += cout[prev][0];

            if (total < best.cost) {
                best.cost = total;
                best.chemin = perm.clone();
            }
            return;
        }

        for (int i = l; i < perm.length; i++) {
            swap(perm, l, i);
            permute(perm, l + 1, n, cout, best);
            swap(perm, l, i);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * Contrainte pickup-before-delivery :
     * - j pair (j%2==0) et j!=0 => livraison
     * - pickup associé = j-1
     */
    private static boolean respecteContraintesPickupDelivery(int[] perm) {
        int max = Arrays.stream(perm).max().orElse(0) + 1;
        boolean[] pickupVu = new boolean[max];

        for (int j : perm) {
            if (j != 0 && j % 2 == 0) {
                int p = j - 1; // pickup associé
                if (p < 0 || p >= pickupVu.length) return false;
                if (!pickupVu[p]) return false; // livraison avant pickup -> interdit
            } else {
                // j impair => pickup
                if (j >= 0 && j < pickupVu.length) {
                    pickupVu[j] = true;
                }
            }
        }
        return true;
    }
}
