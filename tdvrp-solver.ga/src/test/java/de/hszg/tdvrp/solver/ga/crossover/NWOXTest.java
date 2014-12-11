package de.hszg.tdvrp.solver.ga.crossover;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.ChromosomePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class NWOXTest {

    public NWOXTest() {
    }

    /**
     * Test of cross method, of class NWOX.
     */
    @Test
    public void testCross() {
        Chromosome p1 = new Chromosome(null, null, null, new int[]{1, 5, 2, 3, 7, 13, 4, 8, 15, 10, 11, 12, 6, 14, 9}, 0);
        Chromosome p2 = new Chromosome(null, null, null, new int[]{6, 4, 1, 14, 11, 8, 12, 13, 9, 7, 10, 5, 2, 3, 15}, 0);

        NWOX instance = new NWOX();

        ChromosomePair result = instance.cross(p1, p2, 6, 9);

        int[] c1 = result.left().route();
        int[] c2 = result.right().route();

        assertArrayEquals(new int[]{1, 5, 2, 3, 4, 8, 12, 13, 9, 7, 15, 10, 11, 6, 14}, c1);
        assertArrayEquals(new int[]{6, 1, 14, 11, 12, 13, 4, 8, 15, 10, 9, 7, 5, 2, 3}, c2);

    }
    
    

}
