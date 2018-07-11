package com.shelly.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shelly.R;
import com.shelly.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    //Views
    TextView mQuestionTV;
    TextView mQuestionElementTV;
    ImageView mEmojiIV;
    SeekBar mSeekBar;
    Button mNextQuestionBtn;

    //Variables
    private List<QuestionItem> mQuestionList;
    private List<Integer> mEmojiList;
    private HashMap<String, Integer> mTestResults;
    private int mNumQuestion, mNumElement,
                MaxSeekBar;

    //Firebase
    private FirebaseMethods mFirebaseMethods;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Firebase Binding
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseMethods = new FirebaseMethods(this);

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
        mTestResults = new HashMap<>();
        mNumElement = 0;
        mNumQuestion = 0;
        initializeData();

        //Implementing functionalities
        if(mUser == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }

        mNextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int progress = mSeekBar.getProgress();
                String key =  mQuestionList.get(mNumQuestion).Elements.get(mNumElement).factor;
                mTestResults.put(key, mTestResults.get(key) + progress);

                mNumElement++;
                if(mNumQuestion == mQuestionList.size() - 1) {
                    if(mNumElement == mQuestionList.get(mNumQuestion).Elements.size()) {
                        mFirebaseMethods.initializeActivities(mTestResults);
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
                    if(questionItem.Elements != null && questionItem.Elements.size() > 0) {
                        mQuestionElementTV.setText(questionItem.Elements.get(mNumElement).element);
                    } else {
                        mQuestionElementTV.setText("");
                    }
                }
                mQuestionElementTV.setText(mQuestionList.get(mNumQuestion).Elements.get(mNumElement).element);
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

        //Initializing the TestResults HashMap
        Query query = mRefDatabase.child(getString(R.string.dbfield_resources)).child(getString(R.string.dbfield_test_resources)).child(getString(R.string.dbfield_test_factors));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    mTestResults.put((String) ds.getValue(), 0);
                    Log.e("TestInitialization", (String) ds.getValue() + " initialized");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Initializing the Emoji Array
        String emojiString = "ic_emoji";
        for(int index = 0; index <= MaxSeekBar; index++) {
            int emojiID = getResources().getIdentifier(emojiString + index, "drawable", "com.shelly");
            mEmojiList.add(emojiID);
        }

        //Initializing the Question Array
        query = mRefDatabase.child(getString(R.string.dbfield_resources)).child(getString(R.string.dbfield_test_resources)).child(getString(R.string.dbfield_test_questions));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    Log.e("Snapshot", ""+ds);
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
        List<Element> Elements;

        public QuestionItem() {

        }

        public QuestionItem(String Question, List<Element> Elements) {
            Question = Question;
            Elements = Elements;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String Question) {
            this.Question = Question;
        }

        public List<Element> getElements() {
            return Elements;
        }

        public void setElements(List<Element> Elements) {
            this.Elements = Elements;
        }

        @Override
        public String toString() {
            return "QuestionItem{" +
                    "Question='" + Question + '\'' +
                    ", Elements=" + Elements +
                    '}';
        }

        static class Element {
            String element;
            String factor;

            public Element() {

            }

            public Element(String element, String factor) {
                this.element = element;
                this.factor = factor;
            }

            public String getElement() {
                return element;
            }

            public void setElement(String element) {
                this.element = element;
            }

            public String getFactor() {
                return factor;
            }

            public void setFactor(String factor) {
                this.factor = factor;
            }

            @Override
            public String toString() {
                return "Element{" +
                        "element='" + element + '\'' +
                        ", factor='" + factor + '\'' +
                        '}';
            }
        }
    }
}
