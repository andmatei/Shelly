package com.shelly.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.shelly.R;

public class FloatingToolBar extends ConstraintLayout {

    private Context context;
    private Activity activity;
    private LayoutInflater layoutInflater;
    public ImageButton HomeIB;
    public ImageButton MenuIB;
    public ImageButton ProfileIB;
    public ImageButton EntryIB;
    public ImageButton SettingsIB;
    public LinearLayout ExtendedMenuLL;

    boolean MenuIsExtended;

    public FloatingToolBar(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        activity = (Activity) context;
        Inflate();
        BindViews();
        ToolBarSetup();
    }

    private void Inflate() {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.tool_bar, this, true);
    }

    private void BindViews() {
        HomeIB = findViewById(R.id.HomeImageButton);
        MenuIB = findViewById(R.id.MenuImageButton);
        ProfileIB = findViewById(R.id.ProfileImageButton);
        EntryIB = findViewById(R.id.EntryImageButton);
        SettingsIB = findViewById(R.id.ProfileSettingsImageButton);
        ExtendedMenuLL = findViewById(R.id.ExtendedMenu);

        MenuIsExtended = false;
    }

    private void ToolBarSetup () {
        MenuIB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MenuIsExtended) {
                    MenuIB.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu));
                    ExtendedMenuLL.setVisibility(View.GONE);
                } else {
                    MenuIB.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close_menu));
                    ExtendedMenuLL.setVisibility(View.VISIBLE);
                }
                MenuIsExtended = !MenuIsExtended;
            }
        });
    }
}

