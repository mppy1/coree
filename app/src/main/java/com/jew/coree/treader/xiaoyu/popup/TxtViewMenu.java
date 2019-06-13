package com.jew.coree.treader.xiaoyu.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jew.coree.R;
import com.jew.coree.utils.Utils;


public class TxtViewMenu extends PopupWindow {
    private Context mContext;
    private int mWindow_With;
    private int mWindow_Heigh;

    private TxtMenuClockListener mListener;

    public TxtViewMenu(Context context) {
        this.mContext = context;
        inite();
    }

    public void setOnTxtMenuClickListener(TxtMenuClockListener listener) {
        mListener = listener;
    }

    @SuppressLint("NewApi")
    private void inite() {
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(metrics);

        mWindow_With = metrics.widthPixels;
        mWindow_Heigh = metrics.heightPixels;

        int rootwith = mWindow_With;
        int rootheigh = mWindow_Heigh / 7;

        LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.txtmenu_layout, null);

        this.setWidth(rootwith);
        this.setHeight(Utils.dip2px(mContext, 49));
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.setContentView(layout);
//		ColorDrawable dw = new ColorDrawable(Color.parseColor("#88000000"));
//		this.setBackgroundDrawable(dw);

        RelativeLayout bottomMenuLL = (RelativeLayout) layout.findViewById(R.id.bottomMenuLL);
        TextView book_process_btn = (TextView) layout.findViewById(R.id.book_process_btn);
        TextView book_setting_btn = (TextView) layout.findViewById(R.id.book_setting_btn);



        book_process_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mListener != null)
                    mListener.onProgressMenuClicked();
            }
        });

        book_setting_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mListener != null)
                    mListener.onSettingMenuClicked();
            }
        });


    }

    public interface TxtMenuClockListener {

        void onTextQuestionClicked();

        void onProgressMenuClicked();

        void onSettingMenuClicked();


    }

}
