package com.polc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class FindMinimumCostTest {

    private static final String TAG = "FindMinimumCostTest";

    private FindMinimumCost mFindMinCost;
    // Data to test straight scenario
    private static final int[][] DATA_STRAIGHT = new int[][]{
            {3, 4, 1, 2, 8, 6},
            {6, 1, 8, 2, 7, 4},
            {5, 9, 3, 9, 9, 5},
            {8, 4, 1, 3, 2, 6},
            {3, 7, 2, 8, 6, 4}};
    private static final int[][] DATA_ADJECENT_ROWS = new int[][]{
            {3, 4, 1, 2, 8, 6},
            {6, 1, 8, 2, 7, 4},
            {5, 9, 3, 9, 9, 5},
            {8, 4, 1, 3, 2, 6},
            {3, 7, 2, 1, 2, 3}};
    private static final int[][] DATA_LIMIT_CROSSED = new int[][]{
            {19, 10, 19, 10, 19},
            {21, 23, 20, 19, 20},
            {20, 12, 20, 11, 10}};

    private static final int[][] DATA_SINGLE_ROW = new int[][]{
            {1, 10, 19, 1, 9}};

    private static final int[][] DATA_INSUFFICIENT_COLS = new int[][]{
            {19, 10, 19, 10}};

    private static final int MAX_LIMIT = 50;

    private static final int MIN_ROW = 1;

    private static final int MIN_COL = 5;

    @Before
    public void init() {
        mFindMinCost = new FindMinimumCost(DATA_STRAIGHT, MAX_LIMIT);
    }

    @Test
    public void testData() {
        assertNotNull(mFindMinCost.getData());

        assertTrue(mFindMinCost.getData().length >= MIN_ROW);

        assertTrue(mFindMinCost.getData()[0].length >= MIN_COL);
    }

    @Test
    public void testLowCost() {
        SelectedElement selectedElement = mFindMinCost.getMinimumCost();
        print(selectedElement);
        assertEquals(selectedElement.getSequence(), "1 2 3 4 4 5");
        assertEquals(selectedElement.isLimitCrossed(), false);
    }

    @Test
    public void testLowCostWithAdjecentRows() {
        mFindMinCost.setData(DATA_ADJECENT_ROWS);
        SelectedElement selectedElement = mFindMinCost.getMinimumCost();
        print(selectedElement);
        assertEquals(selectedElement.getSequence(), "1 2 1 5 4 5");
        assertEquals(selectedElement.isLimitCrossed(), false);
    }

    @Test
    public void testLowCostLimitCrossed() {
        mFindMinCost.setData(DATA_LIMIT_CROSSED);
        SelectedElement selectedElement = mFindMinCost.getMinimumCost();
        print(selectedElement);
        assertEquals(selectedElement.getSequence(), "1 1 1");
        assertEquals(selectedElement.isLimitCrossed(), true);
    }

    @Test
    public void testSingleRow() {
        mFindMinCost.setData(DATA_SINGLE_ROW);
        SelectedElement selectedElement = mFindMinCost.getMinimumCost();
        print(selectedElement);
        assertEquals(selectedElement.getSequence(), "1 1 1 1 1");
        assertEquals(selectedElement.getSum(), 40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientColumns() {
        mFindMinCost.setData(DATA_INSUFFICIENT_COLS);
        SelectedElement selectedElement = mFindMinCost.getMinimumCost();
        assertEquals(selectedElement, null);
    }

    @After
    public void clear() {
        mFindMinCost = null;
    }

    private void print(String message) {
        System.out.println(message);
    }

    private void print(SelectedElement selectedElement) {
        print("-----------------------------------------------------");
        print(selectedElement.isLimitCrossed() ? "No" : "Yes");
        print("Total Cost: " + selectedElement.getSum());
        print("Row Sequence: " + selectedElement.getSequence());
        print("-----------------------------------------------------");
    }
}
