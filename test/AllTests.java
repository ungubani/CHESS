package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BishopTest.class,
        BoardTest.class,
        KingTest.class,
        KnightTest.class,
        PawnTest.class,
        QueenTest.class,
        RookTest.class

})
public class AllTests {
}