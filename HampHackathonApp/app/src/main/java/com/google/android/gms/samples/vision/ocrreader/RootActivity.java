package com.google.android.gms.samples.vision.ocrreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.samples.vision.ocrreader.ui.camera.KnowledgeEngine;

import java.util.ArrayList;

public class RootActivity extends AppCompatActivity {

    private Button receiptBtn;
    private CustomAdapter customAdapter;
    private ArrayList<String> wordList;
    private KnowledgeEngine knowledgeEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        FloatingActionButton camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);

        final Intent ocrIntent = new Intent(this, OcrCaptureActivity.class);
        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                startActivityForResult(ocrIntent, 0);
            }
        });

        FloatingActionButton additem_fab = (FloatingActionButton) findViewById(R.id.additem_fab);
        additem_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
            }
        });

        wordList = new ArrayList<String>();

        ListView itemList = (ListView) findViewById(R.id.item_list);
        customAdapter = new CustomAdapter(this, wordList);
        itemList.setAdapter(customAdapter);

        knowledgeEngine = new KnowledgeEngine(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) {
            if (data.hasExtra("result")) {
                ArrayList<String> rawItemList = data.getStringArrayListExtra("result");

                for (String item : rawItemList)
                {
                    wordList.add(knowledgeEngine.closest(item));
                }
                customAdapter.notifyDataSetChanged();

                if (wordList.size() > 0)
                    Log.w("intent result", wordList.get(wordList.size() - 1));
                else
                    Log.w("intent result", "No items tapped");
            }
        }

        else
        {
            Log.w("intent status", "failed");
        }
    }
}
