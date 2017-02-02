package com.polc;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import polc.com.polc.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int UI_ROW = 5;
    private static final int UI_COL = 6;

    private TableLayout mTableLayout;
    private Button mCalculateButton;
    private TextView mResultTextView;

    private Handler mHandler;

    private static final int SUCCESS = 1;
    private static final int FAILED = 2;
    private static final String MESSAGE = "msg";
    private static final String DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix);

        mHandler = new Handler(mLowestPathCallback);

        // Hardcoding the orientation to be Portrait to keep it simple
        mTableLayout = (TableLayout) findViewById(R.id.keypad);

        mCalculateButton = (Button) findViewById(R.id.calculate);

        mCalculateButton.setOnClickListener(mCalculateClickListener);

        mResultTextView = (TextView) findViewById(R.id.result);

        for (int i = 0; i < UI_ROW; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < UI_COL; j++) {
                EditText cell = new EditText(this);
                if (i == UI_ROW-1 && j == UI_COL - 1)
                    cell.setImeOptions(EditorInfo.IME_ACTION_DONE);
                else
                    cell.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER);
                row.addView(cell);
            }
            mTableLayout.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private View.OnClickListener mCalculateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mHandler.post(mLowestPathTask);
        }
    };

    private Runnable mLowestPathTask = new Runnable() {
        @Override
        public void run() {
            int[][] data = getIntegerArray();
            Message message = new Message();
            Bundle bundle = new Bundle();

            if (data != null) {
                FindMinimumCost findMinimumCost = new FindMinimumCost(data, 50);
                SelectedElement selectedElement = findMinimumCost.getMinimumCost();
                message.what = SUCCESS;
                bundle.putString(MESSAGE, selectedElement.getPrintData());
                bundle.putParcelable(DATA, selectedElement);
                message.setData(bundle);
            } else {
                message.what = FAILED;
                bundle.putString(MESSAGE, "Something went wrong!!");
                message.setData(bundle);
            }
            mHandler.dispatchMessage(message);
        }
    };

    private int[][] getIntegerArray() {
        int[][] uiData = null;
        if (mTableLayout.getChildCount() > 0) {
            int r = mTableLayout.getChildCount();
            int c = ((TableRow) mTableLayout.getChildAt(0)).getChildCount();
            uiData = new int[r][c];
        }
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            View view = mTableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow tableRow = (TableRow) view;
                for (int j=0; j<tableRow.getChildCount();j++) {
                    EditText item = (EditText) tableRow.getChildAt(j);
                    String data = item.getText().toString().trim();
                    int intData = TextUtils.isEmpty(data) ? 0 : Integer.parseInt(data);
                    uiData[i][j] = intData;
                }
            }
        }
        return uiData;
    }

    private Handler.Callback mLowestPathCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle bundle = message.getData();
            mResultTextView.setText(bundle.getString(MESSAGE));
            return true;
        }
    };
}


