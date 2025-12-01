package fr.insa.optimod;

import fr.insa.optimod.modele.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSPTest {


    // ===============================================
    //  TEST PRINCIPAL : DP vs brute force
    // ===============================================
    @Test
    public void testTspPickupDeliveryOptimal() {

        int n = 7;

        double[][] cout = {

                {  0,   4,   9,   3,  12,   7,  15 },
                {  4,   0,   1,   8,   6,   5,  11 },
                {  9,   1,   0,   7,   2,  14,   4 },
                {  3,   8,   7,   0,   1,   2,   9 },
                { 12,   6,   2,   1,   0,  10,   3 },
                {  7,   5,  14,   2,  10,   0,   6 },
                { 15,  11,   4,   9,   3,   6,   0 }
        };


        HashMap<Integer, Long> mapIndexAId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            mapIndexAId.put(i, (long) i);
        }
        ArrayList<Noeud> listeNoeuds = new ArrayList<>();
        HashMap<Long, Noeud> mapNoeuds = new HashMap<>();
        ArrayList<Troncon> listeTroncons = new ArrayList<>();
        Double minLat = null;
        Double minLong = null;
        Double maxLat = null;
        Double maxLong = null;
        Carte  carte = new Carte(listeNoeuds, listeTroncons, mapNoeuds, minLat, minLong, maxLat, maxLong);;


        long s = (1L << (n - 1)) - 1L;

        // ===== 1. Résultat de ton DP =====
        TSP tsp = new TSP(); // ta classe (doit avoir computeD)
        PointLivraison resDP = tsp.computeDTest(0, s, n, cout, carte, mapIndexAId);
        double dpCost = resDP.getG();

        // ===== 2. Résultat brut force =====
        BruteForceResult bf = bruteForceTSP(n, cout);
        double bfCost = bf.cost;

        System.out.println("Coût DP      = " + dpCost);
        System.out.println("Coût brute   = " + bfCost);

        System.out.print("Chemin DP (indices) : ");
        PointLivraison courant = resDP;
        System.out.print(courant.getNoeud().getId());
        courant = courant.getParent();
        while (courant != null) {

            System.out.print(" -> " + courant.getNoeud().getId());
            courant = courant.getParent();
        }
        System.out.println();

        System.out.print("Chemin brute (indices) : 0");
        for (int idx : bf.chemin) {
            System.out.print(" -> " + idx);
        }
        System.out.println(" -> 0");


        assertEquals(bfCost, dpCost, 1e-6,
                "Le coût DP n'est pas égal au coût optimal brut-force");
    }



    static class BruteForceResult {
        double cost;
        int[] chemin;
    }


    static BruteForceResult bruteForceTSP(int n, double[][] cout) {
        int[] perm = new int[n - 1];
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


    private static boolean respecteContraintesPickupDelivery(int[] perm) {
        int max = Arrays.stream(perm).max().orElse(0) + 1;
        boolean[] pickupVu = new boolean[max];

        for (int j : perm) {
            if (j != 0 && j % 2 == 0) {
                int p = j - 1;
                if (p < 0 || p >= pickupVu.length) return false;
                if (!pickupVu[p]) return false;
            } else {

                if (j >= 0 && j < pickupVu.length) {
                    pickupVu[j] = true;
                }
            }
        }
        return true;
    }
}
