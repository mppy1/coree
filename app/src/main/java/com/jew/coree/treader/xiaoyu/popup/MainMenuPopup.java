//package com.jew.coree.treader.xiaoyu.popup;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.corelibs.utils.DateUtils;
//import com.jew.coree.R;
//import com.jew.coree.treader.xiaoyu.basepopup.BasePopupWindow;
//
//
///**
// * Created by pengzhu on 2016/7/12.
// */
//
//public class MainMenuPopup extends BasePopupWindow implements View.OnClickListener {
//    private View popupView;
//    private RelativeLayout topLL;
//    private RelativeLayout bottomLL;
//    private ImageView backIV;
//    private ImageView voiceIV;
//    private ImageView downloadIV;
//    private ImageView moreIV;
//    private TextView mTitle;
//
//
//    private TextView wenshiBtn;
//    private TextView processBtn;
//    private TextView settingBtn;
//
//    private FojingVo currentFojing;
//    private int voiceSize = 0;
//    private int pageindex = 1, pagecounts = 1;
//
//    public MainMenuPopup(Activity context, FojingVo fojingVo) {
//        super(context);
//        this.currentFojing = fojingVo;
//        bindEvent();
//    }
//
//    public void setPageIndex(int pageindex0, int pagecounts0) {
//        this.pagecounts = pagecounts0;
//        this.pageindex = pageindex0;
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
//    private void bindEvent() {
//        if (popupView != null) {
//            topLL = (RelativeLayout) popupView.findViewById(R.id.topMenuLL);
//            topLL.setVisibility(View.VISIBLE);
//            bottomLL = (RelativeLayout) popupView.findViewById(R.id.bottomMenuLL);
//            bottomLL.setVisibility(View.VISIBLE);
//
//            backIV = (ImageView) popupView.findViewById(R.id.book_back_btn);
//            voiceIV = (ImageView) popupView.findViewById(R.id.book_voice_btn);
//            downloadIV = (ImageView) popupView.findViewById(R.id.book_download_btn);
//            moreIV = (ImageView) popupView.findViewById(R.id.book_more_btn);
//
//            mTitle = (TextView) popupView.findViewById(R.id.textview_title_textview);
//            mTitle.setText(currentFojing.getInfortitle());
//            wenshiBtn = (TextView) popupView.findViewById(R.id.book_wenshi_btn);
//            processBtn = (TextView) popupView.findViewById(R.id.book_process_btn);
//            settingBtn = (TextView) popupView.findViewById(R.id.book_setting_btn);
//
//            backIV.setOnClickListener(this);
//            voiceIV.setOnClickListener(this);
//            downloadIV.setOnClickListener(this);
//            moreIV.setOnClickListener(this);
//            wenshiBtn.setOnClickListener(this);
//            processBtn.setOnClickListener(this);
//            settingBtn.setOnClickListener(this);
//
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.book_back_btn:
//                mContext.finish();
//                break;
//            case R.id.book_voice_btn:
//                dismiss();
//                final VoiceMenuPopup voiceMenuPopup = new VoiceMenuPopup(mContext, null);
//                voiceMenuPopup.setBackPressEnable(false);
//                voiceMenuPopup.setOnDismissListener(new OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        voiceMenuPopup.clear();
//                    }
//                });
//                voiceMenuPopup.showPopupWindow();
////                VoiceMenuPopup voiceMenuPopup = new VoiceMenuPopup(mContext);
////                voiceMenuPopup.setBackPressEnable(false);
////                voiceMenuPopup.showPopupWindow();
//
//                break;
//            case R.id.book_download_btn:
//
//                String fojingId = "" + currentFojing.getInforid();
//                try {
//                    FojingVo vo = FojingVo.where("inforid = ?", fojingId).findFirst(FojingVo.class);
//                    if (vo == null) {
//                        return;
//                    }
//                    if (vo.getDowonloaded() == ConstsType.TYPE_ONE) {
//                        ToastUtil.showShortToast(mContext, R.string.txfj_downloaded);
//                        return;
//                    }
//                    vo.setDowonloaded(ConstsType.TYPE_ONE);
//                    vo.setDownloadTimeMillis(DateUtils.getCurrentTimeInLong());
//                    vo.update(vo.id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(mContext, "正在下载中...", Toast.LENGTH_SHORT).show();
//                try {
//
//                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.book_more_btn:
//                dismiss();
////                MoreMenuPopup moreMenuPopup = new MoreMenuPopup(mContext, null, 1);
////                moreMenuPopup.showPopupWindow();
//                break;
//            case R.id.book_wenshi_btn:
//                Utils.startActivity(mContext, WenshiHomeLabelsActivity.class);
//                break;
//            case R.id.book_process_btn:
//                dismiss();
//                ProcessMenuPopup processMenuPopup = new ProcessMenuPopup(mContext, pageindex, pagecounts);
//                processMenuPopup.showPopupWindow();
//                break;
//            case R.id.book_setting_btn:
//                dismiss();
//                SettingMenuPopup settingMenuPopup = new SettingMenuPopup(mContext);
//                settingMenuPopup.showPopupWindow();
//                break;
//            default:
//                break;
//        }
//
//    }
//
//
//    @Override
//    public View getPopupView() {
//        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_treader_menu_pop, null);
//        return popupView;
//    }
//
//    @Override
//    public View getAnimaView() {
//        return null;
//    }
//}
