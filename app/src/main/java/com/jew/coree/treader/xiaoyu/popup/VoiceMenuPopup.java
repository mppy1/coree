//package com.jew.coree.treader.xiaoyu.popup;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.corelibs.utils.LogUtils;
//import com.jew.coree.R;
//import com.jew.coree.treader.util.PageFactory;
//import com.jew.coree.treader.util.TRPage;
//import com.jew.coree.treader.xiaoyu.basepopup.BasePopupWindow;
//import com.jew.coree.utils.Utils;
//import com.jew.coree.view.ReadActivity;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * Created by pengzhu on 2016/7/14.
// */
//
//public class VoiceMenuPopup extends BasePopupWindow implements View.OnClickListener {
//
//
//    private View popupView;
//
//    LinearLayout ll_main;
//    SeekBar seekBar;
//    TextView navigation_voice_male, navigation_voice_female;
//    TextView tv_cancle;
//
//    String voiceStr;
//    /* wym 这个应该是播放的文本, 每个item对应一页 */
//    List<String> voiceArr;
//    SpeechHandler speechHandler;
//    PageFactory pageFactory;
//    TRPage page;
//    private boolean isChange = false;
//
//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 0:
//                    onVoice();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };
//
//    public VoiceMenuPopup(Activity activity, PageFactory pageFactory) {
//        super(activity);
//        this.pageFactory = pageFactory;
//        this.page = pageFactory.getCurrentPage();
//        speechHandler = new SpeechHandler(mContext);
//        bindEvent();
//    }
//
//    /**
//     * 合成回调监听。
//     */
//    private SynthesizerListener mTtsListener = new SynthesizerListener() {
//
//        @Override
//        public void onSpeakBegin() {
//        }
//
//        @Override
//        public void onSpeakPaused() {
//        }
//
//        @Override
//        public void onSpeakResumed() {
//        }
//
//        @Override
//        public void onBufferProgress(int percent, int beginPos, int endPos,
//                                     String info) {
//            // 合成进度
//
//        }
//
//        @Override
//        public void onSpeakProgress(int percent, int beginPos, int endPos) {
//            // 播放进度
//
////            LogUtils.e("播放进度onSpeakProgress:  PERCENT:" + percent + "  BEGIN:" + beginPos + "  END:" + endPos);
//
//            rangeList.get(0);
//
//            ((ReadActivity) mContext).bookpage.postInvalidate();
//
//
//        }
//
//        @Override
//        public void onCompleted(com.iflytek.cloud.SpeechError error) {
//            if (error == null) {
//                if (Utils.isListNotEmpty(voiceArr)) {
//                    voiceStr = voiceArr.get(0);
//                    voiceArr.remove(0);
//                    if (speechHandler == null) {
//                        speechHandler = new SpeechHandler(mContext);
//                    }
//                    speechHandler.start(voiceStr, mTtsListener);
//                } else {
//                    voiceStr = "";
//                    page = pageFactory.getNextPage();
//                    if (page == null) {
//                        dismiss();
//                        return;
//                    }
//                    pageFactory.nextPage();
//                    List<String> ds = page.getLines();
//                    Iterator iterator = ds.iterator();
//                    while (iterator.hasNext()) {
//                        String lineChar = (String) iterator.next();
//                        if (lineChar != null) {
//                            voiceStr += lineChar;
//                        }
//                    }
//
//                    rangeList = new ArrayList<>();
//
//                    String[] voiceArrtemp = voiceStr.split("。");
//                    for (String voice : voiceArrtemp) {
//                        if (!StringUtils.isEmpty(voice)) {
//                            voiceArr.add(voice);
//                            getIndexRange(voice, voiceArrtemp.length);
//                        }
//                    }
//
//                    onVoice();
//                }
//
//            } else if (error != null) {
//                error.getPlainDescription(true);
//            }
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//            //		Log.d(TAG, "session id =" + sid);
//            //	}
//        }
//    };
//
//
//    /**
//     * 更新选中文字
//     *
//     * @return
//     */
//    private String updateHighlight() {
//
//        return "";
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.navigation_voice_male:
//                isChange = true;
//                BasicSetting.getInstance(mContext).storeTxfjVoiceMale("xiaofeng");
//                checkVoiceSex();
//                break;
//            case R.id.navigation_voice_female:
//                isChange = true;
//                BasicSetting.getInstance(mContext).storeTxfjVoiceMale("xiaoyan");
//                checkVoiceSex();
//                break;
//            case R.id.tv_cancle:
//                clear();
//                break;
//        }
//
//    }
//
//    public void clear() {
//        voiceStr = "";
//        voiceStr = null;
//        if (Utils.isListNotEmpty(voiceArr)) {
//            voiceArr.clear();
//        }
//        speechHandler.onDestroy();
//        Toast.makeText(mContext, "播放结束", Toast.LENGTH_SHORT).show();
//        dismiss();
//    }
//
//    private void init() {
//
//        checkVoiceSex();
//
//        voiceStr = "";
//        voiceArr = new ArrayList<>();
//        if (Utils.isListNotEmpty(voiceArr)) {
//            voiceArr.clear();
//        }
//        if (page == null) {
//            dismiss();
//            return;
//        }
//        List<String> ds = page.getLines();
//        Iterator iterator = ds.iterator();
//        while (iterator.hasNext()) {
//            String lineChar = (String) iterator.next();
//            if (lineChar != null) {
//                voiceStr += lineChar;
//            }
//        }
//
//        rangeList = new ArrayList<>();
//        String[] voiceArrtemp = voiceStr.split("。");
//        for (String voice : voiceArrtemp) {
//            if (!StringUtils.isEmpty(voice)) {
//                voiceArr.add(voice);
//                getIndexRange(voice, voiceArrtemp.length);
//            }
//        }
//        //触发读经事件
//        onVoice();
//    }
//
//    /** 语音范围 */
//    public List<int[]> rangeList = new ArrayList<>();
//
//    /**
//     * wym 应该跟随语音显示的电子书index
//     * @param voice
//     */
//    private void getIndexRange(String voice, int sumLength) {
//        int index = voiceArr.lastIndexOf(voice);    // wym
//        int begin = voiceStr.indexOf(voiceArr.get(index));
////        int end = voiceStr.length();
//        int end = begin + voiceArr.get(index).length();
//        if (index == sumLength)
//            end = voiceStr.indexOf(index + 1);
//
//        LogUtils.e("wym === 绘制朗读背景: " + begin + "<>" + end);
////        pageFactory.indexRangeOfVoice = ;
//
//        rangeList.add(new int[]{begin, end});
//
//    }
//
//    private void bindEvent() {
//        if (popupView != null) {
//            ll_main = (LinearLayout) popupView.findViewById(R.id.activity_main_layout);
//            ll_main.setVisibility(View.GONE);
//
//            int speed = BasicSetting.getInstance(mContext).getTxfjVoiceSpeed();
//            //设置读速
//            seekBar = (SeekBar) popupView.findViewById(R.id.navigation_slider);
//            seekBar.setMax(100);
//            seekBar.setProgress(speed);
//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    isChange = true;
//                    BasicSetting.getInstance(mContext).storeTxfjVoiceSpeed(progress);
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//
//            navigation_voice_male = (TextView) popupView.findViewById(R.id.navigation_voice_male);
//            navigation_voice_female = (TextView) popupView.findViewById(R.id.navigation_voice_female);
//
//
//            tv_cancle = (TextView) popupView.findViewById(R.id.tv_cancle);
//
//            navigation_voice_male.setOnClickListener(this);
//            navigation_voice_female.setOnClickListener(this);
//            tv_cancle.setOnClickListener(this);
//            popupView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (ll_main.getVisibility() == View.VISIBLE) {
//                        ll_main.setVisibility(View.GONE);
//                        if (speechHandler == null) {
//                            speechHandler = new SpeechHandler(mContext);
//                        }
//                        if (isChange) {
//                            if (Utils.isListNotEmpty(voiceArr) && !StringUtils.isEmpty(voiceStr)) {
//                                voiceArr.add(0, voiceStr);
//                            }
//                            onVoice();
//                            isChange = false;
//                        } else {
//                            speechHandler.resume();
//                        }
//                    } else {
//                        ll_main.setVisibility(View.VISIBLE);
//                        if (speechHandler == null) {
//                            speechHandler = new SpeechHandler(mContext);
//                        }
//                        speechHandler.pause();
//                    }
//                }
//            });
//
//            init();
//        }
//
//    }
//
//    private void checkVoiceSex() {
//        // 设置是否女声
//        String sex = BasicSetting.getInstance(mContext).getTxfjVoiceMale();
//        if (sex.equals("xiaofeng")) {
//            navigation_voice_male.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_greenlight_rectangle));
//            navigation_voice_male.setTextColor(mContext.getResources().getColor(R.color.font_lake_blue));
//            navigation_voice_female.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_white_rectangle));
//            navigation_voice_female.setTextColor(mContext.getResources().getColor(R.color.white));
//        } else {
//            navigation_voice_female.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_greenlight_rectangle));
//            navigation_voice_female.setTextColor(mContext.getResources().getColor(R.color.font_lake_blue));
//            navigation_voice_male.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_white_rectangle));
//            navigation_voice_male.setTextColor(mContext.getResources().getColor(R.color.white));
//        }
//
//    }
//
//    @Override
//    protected Animation getShowAnimation() {
//        return null;
//    }
//
//    @Override
//    protected View getClickToDismissView() {
//        return null;
//    }
//
//    @Override
//    public View getPopupView() {
//        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_treader_voice, null);
//        return popupView;
//    }
//
//    @Override
//    public View getAnimaView() {
//        return null;
//    }
//
//
//    public void onVoice() {
//        if (Utils.isListNotEmpty(voiceArr)) {
//            voiceStr = voiceArr.get(0);
//            voiceArr.remove(0);
//            if (speechHandler == null) {
//                speechHandler = new SpeechHandler(mContext);
//            }
//            speechHandler.start(voiceStr, mTtsListener);
//        }
//
////        pageFactory.
//    }
//
//
//}
//
//
