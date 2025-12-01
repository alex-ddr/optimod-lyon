package fr.insa.optimod.modele;

import java.util.HashMap;

public class TSP {


    protected HashMap<Long, HashMap<Long, PointLivraison>> memD = new HashMap<>();


    private PointLivraison getMemo(long nodeId, long s) {
        HashMap<Long, PointLivraison> inner = memD.get(nodeId);
        if (inner == null) return null;
        return inner.get(s);
    }


    private void putMemo(long nodeId, long s, PointLivraison pl) {
        if (!memD.containsKey(nodeId)) {
            memD.put(nodeId, new HashMap<>());
        }
        memD.get(nodeId).put(s, pl);
    }



    static boolean isEmpty(long s) {
        return s == 0L;
    }


    static boolean isIn(int j, long s) {
        long mask = 1L << (j - 1);
        return (s & mask) != 0L;
    }


    static long removeElement(int j, long s) {
        long mask = 1L << (j - 1);
        return s & ~mask;
    }


    static long createFullSet(int n) {
        return (1L << (n - 1)) - 1L;
    }



    public PointLivraison computeD(Integer i, long s, int n, double[][] cost, Carte carte, HashMap<Integer, Long> mapIndexAId) {


        PointLivraison memo = getMemo(i, s);
        if (memo != null) return memo;


        if (isEmpty(s)) {
            double g = cost[(int)i][0];
            Noeud depot = carte.obtenirNoeud(mapIndexAId.get(0));
            PointLivraison plDepot = new PointLivraison(depot, 0.0, 0.0, null);

            PointLivraison plI = new PointLivraison(
                    carte.obtenirNoeud(mapIndexAId.get(i)),
                    g,
                    0.0,
                    plDepot
            );

            putMemo(i, s, plI);
            return plI;

        }

        double min = Double.POSITIVE_INFINITY;
        PointLivraison bestChild = null;


        for (int j = 1; j < n; j++) {
            if (isIn(j, s)) {


                if ((j!=0) && (j%2==0)) {
                    int p = j-1;

                    if (isIn(p, s)) {
                        continue;
                    }
                }

                long sSansJ = removeElement(j, s);

                PointLivraison child = computeD(j, sSansJ, n, cost, carte, mapIndexAId);
                double candidate = cost[(int)i][j] + child.getG();

                if (candidate < min) {
                    min = candidate;
                    bestChild = child;
                }
            }
        }


        PointLivraison result = new PointLivraison(carte.obtenirNoeud(mapIndexAId.get(i)), min, 0.0, bestChild);

        putMemo(i, s, result);
        return result;
    }






    public PointLivraison computeDTest(Integer i, long s, int n, double[][] cost, Carte carte, HashMap<Integer, Long> mapIndexAId) {


        PointLivraison memo = getMemo(i, s);
        if (memo != null) return memo;


        if (isEmpty(s)) {
            double g = cost[(int)i][0];
            Noeud depot = new Noeud(0l, 0.0, 0.0);
            PointLivraison plDepot = new PointLivraison(depot, 0.0, 0.0, null);

            Noeud n1 = new Noeud(i.longValue(), 0.0, 0.0);
            PointLivraison plI = new PointLivraison(
                    n1,
                    g,
                    0.0,
                    plDepot
            );

            putMemo(i, s, plI);
            return plI;

        }

        double min = Double.POSITIVE_INFINITY;
        PointLivraison bestChild = null;


        for (int j = 1; j < n; j++) {
            if (isIn(j, s)) {


                if ((j!=0) && (j%2==0)) {
                    int p = j-1;

                    if (isIn(p, s)) {
                        continue;
                    }
                }

                long sSansJ = removeElement(j, s);

                PointLivraison child = computeDTest(j, sSansJ, n, cost, carte, mapIndexAId);
                double candidate = cost[(int)i][j] + child.getG();

                if (candidate < min) {
                    min = candidate;
                    bestChild = child;
                }
            }
        }

        Noeud n2 = new Noeud(i.longValue(), 0.0, 0.0);
        PointLivraison result = new PointLivraison(n2, min, 0.0, bestChild);

        putMemo(i, s, result);
        return result;
        }

    }