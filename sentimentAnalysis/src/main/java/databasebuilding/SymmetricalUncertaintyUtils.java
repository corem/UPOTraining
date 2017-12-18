package databasebuilding;

import java.util.Arrays;

/**
 *
 * @author mgarciat[at]upo[dot]es
 */
public class SymmetricalUncertaintyUtils {
    /**
     * The small deviation allowed in double comparisons.
     */
    /**
     * Tests if a is equal to b.
     *
     * @param a a double
     * @param b a double
     */
    public static boolean eq(double a, double b) {
        //System.out.println("(a-b)" + (a - b) + "\t(b-a): " + (b-a)
        //        + "\t: " + ((a - b < NumericConstant.DOUBLE_TYPE_PRECISION) && (b - a < NumericConstant.DOUBLE_TYPE_PRECISION)));
        return ((a - b < SymmetricalUncertaintyUtils.DOUBLE_TYPE_PRECISION) && (b - a < SymmetricalUncertaintyUtils.DOUBLE_TYPE_PRECISION));
    }
    public static double DOUBLE_TYPE_PRECISION = 1e-6;
    /**
     * Help method for computing xlogx.
     */
    public static double xlogx(double x) {
        double value = 0;
        // Constant hard coded for efficiency reasons
        if (x >= SymmetricalUncertaintyUtils.DOUBLE_TYPE_PRECISION) {
            value = x * Math.log(x);
        }
        return value;
    }

    public static double symmetricalUncertainty(int[] x, int nxvalues, int[] y, int nyvalues) {
        int[][] ctable = SymmetricalUncertaintyUtils.contingencyTable(x, nxvalues, y, nyvalues);
        return SymmetricalUncertaintyUtils.symmetricalUncertainty(ctable);
    }

    /**
     * Calculates the symmetrical uncertainty for base 2.
     *
     * @param ctable - the contingency table
     * @return the calculated symmetrical uncertainty
     */
    public static double symmetricalUncertainty(int[][] ctable) {
        double sumForColumn, sumForRow, total = 0, columnEntropy = 0,
                rowEntropy = 0, entropyConditionedOnRows = 0, infoGain = 0;

        // Compute entropy for columns
        for (int i = 0; i < ctable[0].length; i++) {
            sumForColumn = 0;
            for (int j = 0; j < ctable.length; j++) {
                sumForColumn += ctable[j][i];
            }
            columnEntropy += SymmetricalUncertaintyUtils.xlogx(sumForColumn);
            total += sumForColumn;
        }
        columnEntropy -= SymmetricalUncertaintyUtils.xlogx(total);

        // Compute entropy for rows and conditional entropy
        for (int i = 0; i < ctable.length; i++) {
            sumForRow = 0;
            for (int j = 0; j < ctable[0].length; j++) {
                sumForRow += ctable[i][j];
                entropyConditionedOnRows += SymmetricalUncertaintyUtils.xlogx(ctable[i][j]);
            }
            rowEntropy += SymmetricalUncertaintyUtils.xlogx(sumForRow);
        }
        entropyConditionedOnRows -= rowEntropy;
        rowEntropy -= SymmetricalUncertaintyUtils.xlogx(total);
        infoGain = columnEntropy - entropyConditionedOnRows;
        if (SymmetricalUncertaintyUtils.eq(columnEntropy, 0) || SymmetricalUncertaintyUtils.eq(rowEntropy, 0)) {
            return 0;
        }
        return 2.0 * (infoGain / (columnEntropy + rowEntropy));
    }

    /**
     * Contingency table with as many rows as values has the first array and as
     * many columns as number of values has the second array. Each values must
     * correspond with the index. i.e. if we have three values: 0, 1, 2. i.e. x
     * = [0, 1, 2, 2, 1, 0]; y = [0, 1, 0, 0, 0, 1];
     *
     * [1 1] ctable = [1 1] [2 0]
     *
     * @param x
     * @param nxvalues
     * @param y
     * @param nyvalues
     * @return
     */
    public static int[][] contingencyTable(int[] x, int nxvalues, int[] y,
                                           int nyvalues) {
        int[][] ctable = new int[nxvalues][nyvalues];
        //init
        for (int e = 0; e < nxvalues; e++) {
            Arrays.fill(ctable[e], 0);
        }
        for (int e = 0; e < x.length; e++) {
            ctable[x[e]][y[e]]++;
        }
        return ctable;
    }

    public static void main(String[] args) throws Exception {
        //int[] x = new int[] {1,0,0,1,1,0,1,0,1,0,0,0,1,1,1,1};
        //int[] y = new int[] {0,1,2,1,2,2,2,1,1,0,0,0,0,1,2,1};

        int[] x = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] y = new int[] {0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0};

        double su = SymmetricalUncertaintyUtils.symmetricalUncertainty(x, 2, y, 5);
        System.out.println("SU:" + su);
    }
}