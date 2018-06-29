package com.shelly.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shelly.Models.TextTransformationUtils;
import com.shelly.R;
import com.shelly.Utils.TextTransformListAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditModeActivity extends AppCompatActivity {

    //Views
    private ImageButton mExitEditMode;
    private ImageButton mTextFormatBtn;
    private ImageButton mSettingsEditMode;
    private ImageButton mAddResource;
    private ScrollView mScrollView;
    private EditText mEntryContent;
    private RecyclerView mTextTypeRV;
    private RecyclerView mTextStyleRV;
    private TextTransformListAdapter mTextTypeAdapter;
    private TextTransformListAdapter mTextStyleAdapter;
    private ConstraintLayout mTextFormatMenu;
    private ImageButton mCloseTextFormatMenu;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;

    //Variables
    private List<TextTransformationUtils> mTextTypeList;
    private List<TextTransformationUtils> mTextStyleList;
    private List<String> mTags;
    boolean mTextFormatMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        //Binding views
        mExitEditMode = findViewById(R.id.ExitEditModeImageButton);
        mTextFormatBtn = findViewById(R.id.TextFormatImageButton);
        mSettingsEditMode = findViewById(R.id.EditModeSettingsImageButton);
        mAddResource = findViewById(R.id.EditModeAddImageButton);
        mScrollView = findViewById(R.id.EditModeScrollView);
        mEntryContent = findViewById(R.id.EditModeContentEditText);
        mTextTypeRV = findViewById(R.id.TextTypeRecyclerView);
        mTextStyleRV = findViewById(R.id.TextStyleRecyclerView);
        mTextFormatMenu = findViewById(R.id.TextFormatMenu);
        mCloseTextFormatMenu = findViewById(R.id.CloseTextFormatImageButton);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();

        //Variable Binding
        mTextTypeList = new ArrayList<>();
        mTextStyleList = new ArrayList<>();
        mTags = new ArrayList<>();

        //Implementing Functionalities
        initializeTextTransformation();
        mTextTypeAdapter = new TextTransformListAdapter(mTextTypeList, this, 0, mTags);
        mTextStyleAdapter = new TextTransformListAdapter(mTextStyleList, this, 1, mTags);
        mTextTypeRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTextStyleRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTextTypeRV.setAdapter(mTextTypeAdapter);
        mTextStyleRV.setAdapter(mTextStyleAdapter);

        mSettingsEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEntryContent.getText().toString();
                Log.e("Text", text);
            }
        });

        mExitEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditModeActivity.this, EntryActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCloseTextFormatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextFormatMenuOpen = false;
                Animation slideOutBottom = AnimationUtils.loadAnimation(EditModeActivity.this, R.anim.slide_out_bottom);
                mTextFormatMenu.startAnimation(slideOutBottom);

                slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTextFormatMenu.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        mTextFormatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextFormatMenuOpen = !mTextFormatMenuOpen;
                if(mTextFormatMenuOpen) {
                    Animation slideInBottom = AnimationUtils.loadAnimation(EditModeActivity.this, R.anim.slide_in_bottom);
                    mTextFormatMenu.startAnimation(slideInBottom);

                    slideInBottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            mTextFormatMenu.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    Animation slideOutBottom = AnimationUtils.loadAnimation(EditModeActivity.this, R.anim.slide_out_bottom);
                    mTextFormatMenu.startAnimation(slideOutBottom);

                    slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mTextFormatMenu.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

    }

    private void initializeTextTransformation() {

        Typeface SegoeUI = ResourcesCompat.getFont(this, R.font.segoe_ui);
        Typeface SegoeUISemibold = ResourcesCompat.getFont(this, R.font.segoe_semibold);

        TextTransformationUtils textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("Title");
        textTransformationUtils.setAssignedTag("h2");
        textTransformationUtils.setTextFont(SegoeUISemibold);
        textTransformationUtils.setSelected(false);
        mTextTypeList.add(textTransformationUtils);

        textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("<b>Heading<b>");
        textTransformationUtils.setAssignedTag("h4");
        textTransformationUtils.setTextFont(SegoeUI);
        textTransformationUtils.setSelected(false);
        mTextTypeList.add(textTransformationUtils);

        textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("Body");
        textTransformationUtils.setAssignedTag("p");
        textTransformationUtils.setTextFont(SegoeUI);
        textTransformationUtils.setSelected(true);
        mTextTypeList.add(textTransformationUtils);
        mTags.add(textTransformationUtils.getAssignedTag());

        textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("<b>B<b>");
        textTransformationUtils.setAssignedTag("b");
        textTransformationUtils.setTextFont(SegoeUI);
        textTransformationUtils.setSelected(false);
        mTextStyleList.add(textTransformationUtils);

        textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("<i>I<i>");
        textTransformationUtils.setAssignedTag("i");
        textTransformationUtils.setTextFont(SegoeUI);
        textTransformationUtils.setSelected(false);
        mTextStyleList.add(textTransformationUtils);

        textTransformationUtils = new TextTransformationUtils();
        textTransformationUtils.setFieldValue("<u>U<u>");
        textTransformationUtils.setAssignedTag("u");
        textTransformationUtils.setTextFont(SegoeUI);
        textTransformationUtils.setSelected(false);
        mTextStyleList.add(textTransformationUtils);

        for(TextTransformationUtils t : mTextTypeList) {
            Log.e("testStyle", "" + t.getFieldValue());
        }

    }
}
