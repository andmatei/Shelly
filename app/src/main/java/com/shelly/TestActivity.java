package com.shelly;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    //Views
    TextView mQuestionTV;
    TextView mQuestionElementTV;
    ImageView mEmojiIV;
    SeekBar mSeekBar;
    Button mNextQuestionBtn;

    //Data
    private List<QuestionItem> mQuestionList;
    private List<Integer> mEmojiList;

    //Variables
    private int mNumQuestion, mNumElement,
                MaxSeekBar;

    //Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Firebase Binding
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();

        //Views Binding
        mQuestionTV = (TextView) findViewById(R.id.TestQuestionTextView);
        mQuestionElementTV = (TextView) findViewById(R.id.QuestionElementsTextView);
        mEmojiIV = (ImageView) findViewById(R.id.TestEmojiImageView);
        mSeekBar = (SeekBar) findViewById(R.id.TestSeekBar);
        mNextQuestionBtn = (Button) findViewById(R.id.NextQuestionButton);

        //Variables Binding
        mQuestionList = new ArrayList<>();
        mEmojiList = new ArrayList<>();
        MaxSeekBar = mSeekBar.getMax();
        mNumElement = 0;
        mNumQuestion = 0;
        initializeData();

        //Implementing functionalities
        mNextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumElement++;
                if(mNumQuestion == mQuestionList.size() - 1) {
                    if(mNumElement == mQuestionList.get(mNumQuestion).Elements.size()) {
                        Intent i = new Intent(TestActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }
                    else if(mNumElement == mQuestionList.get(mNumQuestion).Elements.size() - 1) {
                        mNextQuestionBtn.setText("Finish test");
                    }
                }
                else if(mNumElement >= mQuestionList.get(mNumQuestion).Elements.size()) {
                    mNumElement = 0;
                    mNumQuestion++;
                    QuestionItem questionItem = mQuestionList.get(mNumQuestion);
                    mQuestionTV.setText(questionItem.Question);
                    if(questionItem.Elements.size() > 0) {
                        mQuestionElementTV.setText(questionItem.Elements.get(mNumElement));
                    } else {
                        mQuestionElementTV.setText("");
                    }
                }
                mQuestionElementTV.setText(mQuestionList.get(mNumQuestion).Elements.get(mNumElement));
                mSeekBar.setProgress(0);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mEmojiIV.setImageDrawable(getResources().getDrawable(mEmojiList.get(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initializeData() {

        //Initializing the Emoji Array
        String emojiString = "ic_emoji";
        for(int index = 0; index <= MaxSeekBar; index++) {
            int emojiID = getResources().getIdentifier(emojiString + index, "drawable", "com.shelly");
            mEmojiList.add(emojiID);
        }

        //Initializing the Question Array
        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.child("TestQuestions").getChildren()) {
                    Log.e("Snapshot", ""+ds);
                    Log.e("value", "" + ds.getValue());
                    QuestionItem  questionItem = ds.getValue(QuestionItem.class);
                    mQuestionList.add(questionItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class QuestionItem {
        String Question;
        List<String> Elements;

        public QuestionItem() {

        }

        public QuestionItem(String question, List<String> elements) {
            Question = question;
            Elements = elements;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String question) {
            Question = question;
        }

        public List<String> getElements() {
            return Elements;
        }

        public void setElements(List<String> elements) {
            Elements = elements;
        }

        @Override
        public String toString() {
            return "QuestionItem{" +
                    "Question='" + Question + '\'' +
                    ", Elements=" + Elements +
                    '}';
        }
    }
}
