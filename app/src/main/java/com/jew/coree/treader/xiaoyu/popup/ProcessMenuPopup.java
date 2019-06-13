//package com.jew.coree.treader.xiaoyu.popup;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.jew.coree.R;
//import com.jew.coree.treader.xiaoyu.basepopup.BasePopupWindow;
//
//
///**
// * Created by pengzhu on 2016/7/12.
// */
//
//public class ProcessMenuPopup extends BasePopupWindow implements View.OnClickListener {
//    private View popupView;
//    private int pageindex = 1, pagecounts = 1;
//    private onProgressChangeListener mListener;
//
//    public ProcessMenuPopup(Activity activity, int pageindex0, int pagecounts0) {
//        super(activity);
//        this.pagecounts = pagecounts0;
//        this.pageindex = pageindex0;
//
//        bindEvent();
//    }
//
//    public void setonProgressChangeListener(onProgressChangeListener listener) {
//        mListener = listener;
//    }
//
//    @Override
//    protected Animation getShowAnimation() {
//        return null;
//    }
//
//    @Override
//    protected View getClickToDismissView() {
//        return popupView.findViewById(R.id.click_to_dismiss);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    private void bindEvent() {
//        if (popupView != null) {
//            final SeekBar slider = (SeekBar) popupView.findViewById(R.id.navigation_slider);
//            final TextView text = (TextView) popupView.findViewById(R.id.navigation_text);
//            slider.setMax(pagecounts);
//            slider.setProgress(pageindex);
//            text.setText((pageindex + 1) + "/" + (pagecounts + 1));
//
//            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                int pro;
//
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    if (mListener != null) {
//                        mListener.onProgressChange(pro);
//                    }
//                }
//
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    if (fromUser) {
//                        pro = progress;
//                        int page = progress + 1;
//                        int pagesNumber = seekBar.getMax() + 1;
//                        text.setText(makeProgressText(page, pagesNumber));
//
//                    }
//                }
//            });
//
//
//            ImageView pre = (ImageView) popupView.findViewById(R.id.navigation_pre);
//            ImageView next = (ImageView) popupView.findViewById(R.id.navigation_next);
//
//            pre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int page = slider.getProgress();
//                    if (page > 0) {
//                        page -= 1;
//                        if (mListener != null) {
//                            mListener.onProgressChange(page);
//                        }
//                    } else {
//                        Toast.makeText(mContext, "已经是第一页了", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    final int pagesNumber = slider.getMax() + 1;
//                    slider.setProgress(page);
//                    text.setText(makeProgressText(page + 1, pagesNumber));
//
//                }
//            });
//
//
//            next.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int page = slider.getProgress();
//                    if (page < slider.getMax()) {
//                        page += 1;
//                        if (mListener != null) {
//                            mListener.onProgressChange(page);
//                        }
//                    } else {
//                        Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    final int pagesNumber = slider.getMax() + 1;
//                    slider.setProgress(page);
//                    text.setText(makeProgressText(page + 1, pagesNumber));
//                }
//            });
//        }
//    }
//
//    private String makeProgressText(int page, int pagesNumber) {
//        final StringBuilder builder = new StringBuilder();
//        builder.append(page);
//        builder.append("/");
//        builder.append(pagesNumber);
//
//        return builder.toString();
//    }
//
//    @Override
//    public View getPopupView() {
//        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_treader_process, null);
//        return popupView;
//    }
//
//    @Override
//    public View getAnimaView() {
//        return null;
//    }
//
//    public interface onProgressChangeListener {
//        void onProgressChange(int progress);
//
//    }
//}
