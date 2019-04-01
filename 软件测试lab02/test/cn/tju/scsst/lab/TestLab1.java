package cn.tju.scsst.lab;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestLab1 {

    private int input;
    private boolean expected;
    private Lab1 lab = null;

    public TestLab1(int input, boolean expected){
        this.input = input;
        this.expected = expected;
    }

    @Before
    public void setUp(){
        lab = new Lab1();
    }

    @Parameters
    public static Collection<Object[]> getData(){
        return Arrays.asList(new Object[][]{
                {1, true},
                {2, true},
                {4, false},
                {5, true},
                {10, true},
                {11, true},
                {14, false},
                {15, false},
                {22, true},
                {29, false},
                {38, false},
                {39, false},
                {53, true},
                {63, true},
                {64, false},
                {83, true},
                {84, false}
        });
    }

    @Test
    public void testLab1(){
        assertEquals(this.expected, lab.triangleProblem(input));
    }
}
