package com.polc;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SelectedElement implements Parcelable {
    private int mCol;
    private int mRow;
    private int mSum;
    private String mSequence;
    private String mValueSequence;
    private boolean mIsLimitCrossed;
    private int mTotalCost;

    private String mResult;

    public SelectedElement() {
        mSequence = "";
        mValueSequence = "";
    }

    public int getCol() {
        return mCol;
    }

    public void setCol(int col) {
        mCol = col;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        mRow = row;
    }

    public int getSum() {
        return mSum;
    }

    public void setSum(int sum) {
        mSum = sum;
    }

    public String getSequence() {
        return mSequence;
    }

    public void setSequence(String sequence) {
        mSequence = sequence;
    }

    public String getValueSequence() {
        return mValueSequence;
    }

    public void setValueSequence(String valueSequence) {
        mValueSequence = valueSequence;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String mResult) {
        this.mResult = mResult;
    }

    public boolean isLimitCrossed() {
        return mIsLimitCrossed;
    }

    public void setLimitCrossed(boolean isLimitCrossed) {
        mIsLimitCrossed = isLimitCrossed;
    }

    public int getTotalCost() {
        return mTotalCost;
    }

    public void setTotalCost(int totalCost) {
        mTotalCost = totalCost;
    }

    public String getPrintData() {
        return mIsLimitCrossed ? "No" : "Yes" + "\n" +
                "Total Cost: " + mTotalCost + "\n" +
                "Row Sequence: " + mSequence;
    }

    public void print() {
        Log.d(" ", getPrintData());
    }

    private SelectedElement(Parcel parcel) {
        mRow = parcel.readInt();
        mCol = parcel.readInt();
        mSum = parcel.readInt();
        mSequence = parcel.readString();
        mValueSequence = parcel.readString();
        mIsLimitCrossed = parcel.readInt() == 1 ? true : false;
        mTotalCost = parcel.readInt();
    }

    public static final Creator<SelectedElement> CREATOR = new Creator<SelectedElement>() {
        @Override
        public SelectedElement createFromParcel(Parcel parcel) {
            return new SelectedElement(parcel);
        }

        @Override
        public SelectedElement[] newArray(int i) {
            return new SelectedElement[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mRow);
        parcel.writeInt(mCol);
        parcel.writeInt(mSum);
        parcel.writeString(mSequence);
        parcel.writeString(mValueSequence);
        parcel.writeInt(mIsLimitCrossed ? 1 : 0);
        parcel.writeInt(mTotalCost);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

