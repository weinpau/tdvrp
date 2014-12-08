package de.hszg.tdvrp.solver.ga;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class CXTest {

    public CXTest() {
    }

    /**
     * Test of cross method, of class CX.
     */
    @Test
    public void testCross() {
        Chromosome p1 = new Chromosome(null, null, null, new int[]{9, 5, 8, 4, 7, 3, 6, 2, 10, 1}, 0);
        Chromosome p2 = new Chromosome(null, null, null, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 0);

        CX instance = new CX();

        ChromosomePair result = instance.cross(p1, p2, 4);

        int[] c1 = result.left().route;
        int[] c2 = result.right().route;

        assertArrayEquals(new int[]{9, 2, 3, 4, 5, 6, 7, 8, 10, 1}, c1);
        assertArrayEquals(new int[]{1, 5, 8, 4, 7, 3, 6, 2, 9, 10}, c2);

    }

}
