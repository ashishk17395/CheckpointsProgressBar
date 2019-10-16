package com.exp.checkpointsprogbar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CSProgressBar csProg;
    Button resetButton;
    Button increaseProg;
    TextView checkPointReached, progressText;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        csProg = (CSProgressBar) findViewById(R.id.progBar);
        resetButton = (Button) findViewById(R.id.btn);
        increaseProg = (Button) findViewById(R.id.incProgBtn);
        progressText = (TextView) findViewById(R.id.progressText);
        checkPointReached = (TextView) findViewById(R.id.checkPointReached);
        csProg.setCsSize(200);
        resetButton.setOnClickListener(this);
        increaseProg.setOnClickListener(this);

        csProg.setCsProgressChangeListener(new CSProgressBar.ProgressBarChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                progressText.setText(String.valueOf(progress));
            }

            @Override
            public void onCheckPointCleared(int indexOfLatestCheckpoint) {
                checkPointReached.setText(String.valueOf(indexOfLatestCheckpoint) + "_" + ++count);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                csProg.setCsProgress(0);
                break;
            case R.id.incProgBtn:
                csProg.setCsProgress(csProg.getCsProgress() + 1);
                break;
            default:
                break;
        }
    }
}
