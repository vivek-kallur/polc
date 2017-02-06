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
        validateData();
    }

    public void setData(int[][] data) {
        this.mData = data;
        validateData();
    }

    public int[][] getData() {
        return mData;
    }


    public void setMaxLimit(int maxLimit) {
        this.mMaxLimit = maxLimit;
    }

    private void validateData() {
        if (mData == null)
            throw new IllegalArgumentException("Input array cannot be null");

        mMaxRow = mData.length;
        mMaxCol = mData[0].length;

        if (mMaxRow < 1 || mMaxRow > 10)
            throw new IllegalArgumentException("Rows should be between 1 and 10");

        // Min criteria, 1 row and 5 columns
        if (mMaxCol < 5 || mMaxCol > 100)
            throw new IllegalArgumentException("Rows should be between 5 and 100");

    }

    /**
     * Start with position 0, 0
     * For each row find the path with minimum cost
     *
     * @return
     */
    public SelectedElement getMinimumCost() {

        mMaxRow = mData.length;

        mMaxCol = mData[0].length;

        int minSum = 0;

        SelectedElement finalElement = null;

        for (int i = 0; i < mData.length; i++) {

            // initialize selectedElement with first item in a row i
            SelectedElement selectedElement = new SelectedElement();
            selectedElement.setCol(0);

            selectedElement.setRow(i);
            selectedElement.setSum(mData[i][0]);

            selectedElement.setSequence(String.format("%d", i + 1));
            selectedElement.setValueSequence(String.format("%d", mData[i][0]));

            // Find the min cost for each row,
            // if the row is with the min cost or if it is the first row make that row or SelectedItem as
            // the row with minimum cost.
            int sum = findMinimumCost(selectedElement);

            if (i == 0 || minSum > sum) {
                minSum = sum;
                finalElement = selectedElement;
            }
        }

        String result = null;

        mIsLimitCrossed = false;

        mMinimumSum = minSum;

        finalElement.setLimitCrossed(false);

        finalElement.setTotalCost(minSum);

        if (minSum > mMaxLimit) {
            result = String.format("\nmin sum = %d", minSum) + " Limit cross \n sequance Position = " + finalElement.getSequence() + " \n sequance value = " + finalElement.getValueSequence();

            this.mIsLimitCrossed = true;
            finalElement.setLimitCrossed(true);
            finalElement = adjustLimitExceededElement(finalElement);
        } else {
            result = String.format("\nmin sum = %d", minSum) + " \n sequance Position = " + finalElement.getSequence() + " \n sequance value = " + finalElement.getValueSequence();
        }

        finalElement.setResult(result);
        return finalElement;
    }

    /**
     * Re-correct the path of final selected element if the limit has exceeded the limit
     *
     * @param selectedElement
     * @return
     */
    private SelectedElement adjustLimitExceededElement(SelectedElement selectedElement) {
        String[] sequence = selectedElement.getSequence().split(" ");
        StringBuilder sequenceBuilder = new StringBuilder();
        int total = 0;
        int col = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (total + mData[Integer.parseInt(sequence[i].trim()) - 1][col] < mMaxLimit) {
                total += mData[Integer.parseInt(sequence[i].trim()) - 1][col];
                sequenceBuilder.append(sequence[i]);
                sequenceBuilder.append(" ");
            } else
                break;
            col++;
        }
        selectedElement.setSum(total);
        selectedElement.setSequence(sequenceBuilder.toString());
        return selectedElement;
    }

    /**
     * For the selectedElement / Row find the element with the low cost
     * for given row r, col c, consider the elements
     * 1. r-1 (or lenght-1 if it is 0th row), c+1
     * 2. r, c+1
     * 3. r+1 or 0 (if the current row is the last), c+1
     * <p>
     * to find out the min element and consider that element to find the path with min cost.
     * <p>
     * Since this is using recursion, the row with min cost is calculated starting with last column of the matrix
     * all the way through the 0th column
     *
     * @param selectedElement
     * @return
     */
    private int findMinimumCost(SelectedElement selectedElement) {
        int topRow = (selectedElement.getRow() - 1 < 0) ? mMaxRow - 1 : selectedElement.getRow() - 1;

        int bottomRow = (selectedElement.getRow() + 1 < mMaxRow) ? selectedElement.getRow() + 1 : 0;

        int nextCol = selectedElement.getCol() + 1;

        if (nextCol == mMaxCol) {
            return mData[selectedElement.getRow()][selectedElement.getCol()];
        } else {

            // Find r-1th element cost and make it min
            SelectedElement finalResult = null;
            int minVal = 0;
            SelectedElement topRowElement = new SelectedElement();
            topRowElement.setCol(nextCol);
            topRowElement.setRow(topRow);
            topRowElement.setSum(mData[topRow][nextCol]);
            topRowElement.setSequence(String.format("%d", topRow + 1));
            topRowElement.setValueSequence(String.format("%d", mData[topRow][nextCol]));
            finalResult = topRowElement;
            minVal = findMinimumCost(topRowElement);

            // get bottom element
            // Find the cost of r+1th element and make that min if the cost is less than current min
            SelectedElement bottomElement = new SelectedElement();
            bottomElement.setCol(nextCol);
            bottomElement.setRow(bottomRow);
            bottomElement.setSum(mData[bottomRow][nextCol]);
            bottomElement.setSequence(String.format("%d", bottomRow + 1));
            bottomElement.setValueSequence(String.format("%d", mData[bottomRow][nextCol]));

            int bottomSum = findMinimumCost(bottomElement);

            if (bottomSum < minVal) {
                finalResult = bottomElement;
                minVal = bottomSum;
            }

            // Find the cost at the rth element and make that min if it is less than current min
            SelectedElement currentElement = new SelectedElement();
            currentElement.setCol(nextCol);
            currentElement.setRow(selectedElement.getRow());
            currentElement.setSum(mData[selectedElement.getRow()][nextCol]);
            currentElement.setSequence(String.format("%d", selectedElement.getRow() + 1));
            currentElement.setValueSequence(String.format("%d", mData[selectedElement.getRow()][nextCol]));


            int currentSum = findMinimumCost(currentElement);

            if (currentSum < minVal) {
                finalResult = currentElement;
                minVal = currentSum;
            }

            if (selectedElement.getSum() + finalResult.getSum() > mMaxLimit) {
                selectedElement.setLimitCrossed(true);
            }
            // Update the sum and path
            selectedElement.setSum(selectedElement.getSum() + minVal);
            String seq = selectedElement.getSequence() + " " + finalResult.getSequence();
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

        contents = new int[][]{
                {1, 10, 19, 1, 9}};
        findCost.setData(contents);
        findCost.setMaxLimit(50);
        SelectedElement res4 = findCost.getMinimumCost();
        res3.print();
    }
}
