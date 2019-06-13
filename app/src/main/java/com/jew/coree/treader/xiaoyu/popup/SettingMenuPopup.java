package com.jew.coree.treader.xiaoyu.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jew.coree.R;
import com.jew.coree.treader.Config;
import com.jew.coree.treader.xiaoyu.basepopup.BasePopupWindow;


/**
 * Created by pengzhu on 2016/7/12.
 */

public class SettingMenuPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    LinearLayout ll_night_mode;
    TextView tv_night_mode;
    ImageView iv_night_mode;
    TextView tv_cancle;
    private onTxtTextChangeListener mListener;
    private int mTextSize;
    private float defaultValue = mContext.getResources().getDimension(R.dimen.reading_default_text_size);

    public SettingMenuPopup(Activity activity) {
        super(activity);
        this.mTextSize = (int) Config.getInstance().getFontSize();
        bindEvent();
    }

    public void setonTxtTextChangeListener(onTxtTextChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_night_mode:
                Config.getInstance().setDayOrNight(!Config.getInstance().getDayOrNight());
                if (mListener != null) {
                    mListener.onTextStyleChange();
                }
                checkColorProfile();
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
        }

    }

    //检查模式
    private void checkColorProfile() {
        if (true == Config.getInstance().getDayOrNight()) {
            tv_night_mode.setText(R.string.action_day_mode);
            iv_night_mode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.txfj_rijianmoshi));
        } else {
            tv_night_mode.setText(R.string.action_night_mode);
            iv_night_mode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.txfj_yejianmoshi));
        }
    }

    //检查字体大小
    private void checkFontSize() {

        if (mTextSize <= (int) defaultValue - 10) {
        } else if (mTextSize <= (int) defaultValue - 5) {
        } else if (mTextSize == (int) defaultValue) {
        } else if (mTextSize >= (int) defaultValue + 10) {
        } else if (mTextSize >= (int) defaultValue + 5) {
        }

    }

    private void bindEvent() {
        if (popupView != null) {
            ll_night_mode = (LinearLayout) popupView.findViewById(R.id.ll_night_mode);
            ll_night_mode.setOnClickListener(this);


            iv_night_mode = (ImageView) popupView.findViewById(R.id.iv_night_mode);
            tv_night_mode = (TextView) popupView.findViewById(R.id.tv_night_mode);

            tv_cancle = (TextView) popupView.findViewById(R.id.tv_cancle);
            tv_cancle.setOnClickListener(this);

            checkColorProfile();
            checkFontSize();
        }

    }


    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_treader_setting, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return null;
    }


    public interface onTxtTextChangeListener {

        void onTextSizeChange(int spTextsize);

        void onTextStyleChange();
    }

}
