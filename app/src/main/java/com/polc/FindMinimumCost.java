package com.polc;

public class FindMinimumCost {
    private static final String TAG = "FindMinimumCost";

    private int[][] mData;
    private int mMaxLimit;
    private int mMaxRow;
    private int mMaxCol;

    private boolean mIsLimitCrossed;
    private int mMinimumSum;

    public FindMinimumCost(int[][] data, int limit) {
        mData = data;
        mMaxLimit = limit;
    }

    public void setData(int[][] data) {
        this.mData = data;
    }

    public int[][] getData() {
        return mData;
    }


    public void setMaxLimit(int maxLimit) {
        this.mMaxLimit = maxLimit;
    }

    public SelectedElement getMinimumCost() {
        if (mData == null) {
            return null;
        }
        mMaxRow = mData.length;
        mMaxCol = mData[0].length;
        // Min criteria, 1 row and 5 columns
        if (mMaxRow <= 0 || mMaxCol < 5)
            return null;
        int minSum = 0;
        SelectedElement finalElement = null;

        for (int i = 0; i < mData.length; i++) {
            SelectedElement selectedElement = new SelectedElement();
            selectedElement.setCol(0);
            selectedElement.setRow(i);
            selectedElement.setSum(mData[i][0]);
            selectedElement.setSequence(String.format("%d", i));
            selectedElement.setValueSequence(String.format("%d", mData[i][0]));

            int sum = findMinimumCost(selectedElement);
            if (i == 0 || minSum > sum) {
                minSum = sum;
                finalElement = selectedElement;
            }
        }

        String result = null;

        this.mIsLimitCrossed = false;
        this.mMinimumSum = minSum;

        finalElement.setLimitCrossed(false);
        finalElement.setTotalCost(minSum);

        if (minSum > mMaxLimit) {
            result = String.format("\nmin sum = %d", minSum) + " Limit cross \n sequance Position = " + finalElement.getSequence() + " \n sequance value = " + finalElement.getValueSequence();

            this.mIsLimitCrossed = true;
            finalElement.setLimitCrossed(true);
        } else {
            result = String.format("\nmin sum = %d", minSum) + " \n sequance Position = " + finalElement.getSequence() + " \n sequance value = " + finalElement.getValueSequence();
        }

        finalElement.setResult(result);
        return finalElement;
    }

    private int findMinimumCost(SelectedElement selectedElement) {
        int topRow = (selectedElement.getRow() - 1 < 0) ? mMaxRow - 1 : selectedElement.getRow() - 1;
        int bottomRow = (selectedElement.getRow() + 1 < mMaxRow) ? selectedElement.getRow() + 1 : 0;
        int nextCol = selectedElement.getCol() + 1;
        if (nextCol == mMaxCol) {
            return mData[selectedElement.getRow()][selectedElement.getCol()];
        } else {

            SelectedElement finalResult = null;
            int minVal = 0;
            SelectedElement topRowElement = new SelectedElement();
            topRowElement.setCol(nextCol);
            topRowElement.setRow(topRow);
            topRowElement.setSum(mData[topRow][nextCol]);
            topRowElement.setSequence(String.format("%d", topRow));
            topRowElement.setValueSequence(String.format("%d", mData[topRow][nextCol]));
            finalResult = topRowElement;
            minVal = findMinimumCost(topRowElement);

            //get bottom element
            SelectedElement bottomElement = new SelectedElement();
            bottomElement.setCol(nextCol);
            bottomElement.setRow(bottomRow);
            bottomElement.setSum(mData[bottomRow][nextCol]);
            bottomElement.setSequence(String.format("%d", bottomRow));
            bottomElement.setValueSequence(String.format("%d", mData[bottomRow][nextCol]));

            int bottomSum = findMinimumCost(bottomElement);
            if (bottomSum < minVal) {
                finalResult = bottomElement;
                minVal = bottomSum;
            }
            SelectedElement currentElement = new SelectedElement();
            currentElement.setCol(nextCol);
            currentElement.setRow(selectedElement.getRow());
            currentElement.setSum(mData[selectedElement.getRow()][nextCol]);
            currentElement.setSequence(String.format("%d", selectedElement.getRow()));
            currentElement.setValueSequence(String.format("%d", mData[selectedElement.getRow()][nextCol]));

            int currentSum = findMinimumCost(currentElement);
            if (currentSum < minVal) {
                finalResult = currentElement;
                minVal = currentSum;
            }
            selectedElement.setSum(selectedElement.getSum() + minVal);
            String seq = selectedElement.getSequence() + "," + finalResult.getSequence();
            selectedElement.setSequence(seq);
            String valSeq = selectedElement.getValueSequence() + "," + finalResult.getValueSequence();

            selectedElement.setValueSequence(valSeq);

            return selectedElement.getSum();
        }
    }

    public static void testData() {
        int[][] contents = new int[][]{
                {3, 4, 1, 2, 8, 6},
                {6, 1, 8, 2, 7, 4},
                {5, 9, 3, 9, 9, 5},
                {8, 4, 1, 3, 2, 6},
                {3, 7, 2, 8, 6, 4}};
        FindMinimumCost findCost = new FindMinimumCost(contents, 50);
        SelectedElement res1 = findCost.getMinimumCost();
        res1.print();

        //second test
        contents = new int[][]{
                {3, 4, 1, 2, 8, 6},
                {6, 1, 8, 2, 7, 4},
                {5, 9, 3, 9, 9, 5},
                {8, 4, 1, 3, 2, 6},
                {3, 7, 2, 1, 2, 3}};

        findCost.setData(contents);
        findCost.setMaxLimit(50);
        SelectedElement res2 = findCost.getMinimumCost();
        res2.print();

        //third test
        contents = new int[][]{
                {19, 10, 19, 10, 19},
                {21, 23, 20, 19, 20},
                {20, 12, 20, 11, 10}};
        findCost.setData(contents);
        findCost.setMaxLimit(50);
        SelectedElement res3 = findCost.getMinimumCost();
        res3.print();
    }
}
