package de.hszg.tdvrp.solver.ga;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class OXTest {

    public OXTest() {
    }

    /**
     * Test of cross method, of class OXCrossover.
     */
    @Test
    public void testCross() {
        System.out.println("cross");
        Chromosome p1 = new Chromosome(null, null, null, new int[]{1, 3, 2, 6, 4, 5, 9, 7, 8}, 0);
        Chromosome p2 = new Chromosome(null, null, null, new int[]{3, 7, 8, 1, 4, 9, 2, 5, 6}, 0);
        OX instance = new OX();

        ChromosomePair result = instance.cross(p1, p2, 3, 5);

        int[] c1 = result.left().route;
        int[] c2 = result.right().route;

        assertArrayEquals(new int[]{8, 1, 9, 6, 4, 5, 2, 3, 7}, c1);
        assertArrayEquals(new int[]{2, 6, 5, 1, 4, 9, 7, 8, 3}, c2);

    }

}
