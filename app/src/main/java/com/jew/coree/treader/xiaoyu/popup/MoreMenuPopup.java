//package com.jew.coree.treader.xiaoyu.popup;
//
//import android.app.Activity;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.corelibs.utils.LogUtils;
//import com.jew.coree.R;
//import com.jew.coree.treader.Config;
//import com.jew.coree.treader.db.BookList;
//import com.jew.coree.treader.xiaoyu.basepopup.BasePopupWindow;
//import com.jew.coree.utils.Utils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by pengzhu on 2016/7/12.
// */
//
//public class MoreMenuPopup extends BasePopupWindow implements View.OnClickListener, AsyncTaskListener {
//    private View popupView;
//    LinearLayout ll_we_chat, ll_we_zone, ll_sina_weibo, ll_qq, ll_qq_zone,
//            ll_jubao, ll_night_mode, ll_font_set, ll_share_collect, ll_day_mode, ll_search;
//
//    TextView tv_cancle;
//    ImageView iv_night_mode;
//    ImageView iv_collect;
//    TextView tv_night_mode;
//    onMoreChangeListener mListener;
//
//    int fojingId;
//    BookList bookList;
//    FojingVo fojingVo = null;
//    String bookdata = "";
//
//    public ShareVo shareVo;  //第三方分享内容
//    public int btnShareType = 0;  //1微信好友 2微信朋友圈 3新浪微博 4qq好友 5qq空间
//
//    public MoreMenuPopup(Activity activity, BookList bookList, String bookdata) {
//        super(activity);
//        this.bookList = bookList;
//        this.bookdata = bookdata;
//        this.fojingId = bookList.getInforid();
//        bindEvent();
//    }
//
//    public void setonMoreChangeListener(onMoreChangeListener listener) {
//        mListener = listener;
//    }
//
//
//    public void doHandlerTask(int taskID) {
//        TAsyncTask task = new TAsyncTask(MoreMenuPopup.this);
//        task.setTaskID(taskID);
//        task.execute();
//    }
//
//    /**
//     * 获取分享内容
//     *
//     * @param otherJson     其他特殊字段
//     * @param informationid
//     * @param sharetype     3 新闻文章图集 详情 4寺院详情 6开示详情13问师父18功德列表19功德详情20功德证
//     *                      22个人主页,23 寺院主页分享,24分享问师父 25 佛经28功德支付完成 29 住持方丈开示
//     *                      30:活动 31:投票
//     * @return
//     */
//    public String getShareContent(JSONObject otherJson, String informationid, int sharetype) {
//        String result = "";
//        try {
//            if (null == otherJson) {
//                otherJson = new JSONObject();
//            }
//            int accountId = 0;
//            if (IntentUtils.isUserLogin(mContext)) {
//                accountId = BasicSetting.getInstance(mContext).getAccountid();
//            }
//            otherJson.put("accountid", accountId);
//            otherJson.put("informationid", informationid + "");
//            otherJson.put("sharetype", sharetype);
//            LogUtils.e("otherJson===" + otherJson.toString());
//            result = NetAide.getInstance().postData(otherJson, Global.common_web_share);
//            LogUtils.e("getShareContent===" + result);
//            String dataStr = Utils.getJsonData(result);
//            //设置分享内容
//            if (!StringUtils.isEmpty(dataStr)) {
//                shareVo = Utils.beanfromJson(dataStr, ShareVo.class);
//                if (shareVo != null) {
//                    switch (btnShareType) {
//                        case 1:
//                            ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.WEIXIN, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                                    , shareVo.image, shareVo.url);
//                            break;
//                        case 2:
//                            ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.WEIXIN_CIRCLE, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                                    , shareVo.image, shareVo.url);
//                            break;
//                        case 3:
//                            ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.SINA, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                                    , shareVo.image, shareVo.url);
//                            break;
//                        case 4:
//                            ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.QQ, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                                    , shareVo.image, shareVo.url);
//                            break;
//                        case 5:
//                            ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.QZONE, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                                    , shareVo.image, shareVo.url);
//                            break;
//                    }
//
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_we_chat: //微信
//                dismiss();
//                btnShareType = 1;
//                if (shareVo == null) {
//                    doHandlerTask(TaskId.wechatshare_getWechatShare);
//                } else {
//                   ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.WEIXIN, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                            , shareVo.image, shareVo.url);
//                }
//                break;
//            case R.id.ll_we_zone: //微信朋友圈
//                dismiss();
//                btnShareType = 2;
//                if (shareVo == null) {
//                    doHandlerTask(TaskId.wechatshare_getWechatShare);
//                } else {
//                    ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.WEIXIN_CIRCLE, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                            , shareVo.image, shareVo.url);
//                }
//                break;
//            case R.id.ll_sina_weibo: //新浪
//                dismiss();
//                btnShareType = 3;
//                if (shareVo == null) {
//                    doHandlerTask(TaskId.wechatshare_getWechatShare);
//                } else {
//                    ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.SINA, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                            , shareVo.image, shareVo.url);
//                }
//                break;
//            case R.id.ll_qq: //qq
//                dismiss();
//                btnShareType = 4;
//                if (shareVo == null) {
//                    doHandlerTask(TaskId.wechatshare_getWechatShare);
//                } else {
//                    ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.QQ, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                            , shareVo.image, shareVo.url);
//                }
//                break;
//            case R.id.ll_qq_zone: //qq空间
//                dismiss();
//                btnShareType = 5;
//                if (shareVo == null) {
//                    doHandlerTask(TaskId.wechatshare_getWechatShare);
//                } else {
//                    ShareUtils.getInstance(mContext).performShare(SHARE_MEDIA.QZONE, shareVo.title, Html.fromHtml(StringUtils.checkNull(shareVo.descript)).toString()
//                            , shareVo.image, shareVo.url);
//                }
//                break;
//            case R.id.ll_night_mode:
//                Config.getInstance().setDayOrNight(!Config.getInstance().getDayOrNight());
//                if (mListener != null) {
//                    mListener.onTextStyleChange();
//                }
//                checkColorProfile();
//                break;
//            case R.id.ll_font_set:
//                dismiss();
//                if (mListener != null) {
//                    mListener.showSetting();
//                }
//                break;
//            case R.id.ll_collect:
//                if (!IntentUtils.isUserLogin(mContext)) {
//                    IntentUtils.goLoginPhoneActivity(mContext);
//                    return;
//                }
//                if (fojingVo.iscollection == 1) {
//                    doHandlerTask(TaskId.common_deleteCollect);
//                } else {
//                    doHandlerTask(TaskId.common_insertCollect);
//                }
//                break;
//            case R.id.ll_search:
//                dismiss();
//                String strContent = bookList.getCharset();
//                if (!StringUtils.isEmpty(strContent)) {
//                    LogUtils.e(strContent);
//                }
//                IntentUtils.goTxfjQuanwensousuo(mContext, bookdata);
//                break;
//            case R.id.tv_cancle:
//                dismiss();
//                break;
//        }
//
//    }
//
//    //检查模式
//    private void checkColorProfile() {
//        if (true == Config.getInstance().getDayOrNight()) {
//            tv_night_mode.setText(R.string.action_day_mode);
//            iv_night_mode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_day_mode));
//        } else {
//            tv_night_mode.setText(R.string.action_night_mode);
//            iv_night_mode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_night_mode));
//        }
//    }
//
//    private void bindEvent() {
//
//        try {
//            fojingVo = FojingVo.where("inforid = ?", String.valueOf(fojingId)).findFirst(FojingVo.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (popupView != null) {
//            ll_we_chat = (LinearLayout) popupView.findViewById(R.id.ll_we_chat);
//            ll_we_chat.setOnClickListener(this);
//            ll_we_zone = (LinearLayout) popupView.findViewById(R.id.ll_we_zone);
//            ll_we_zone.setOnClickListener(this);
//            ll_sina_weibo = (LinearLayout) popupView.findViewById(R.id.ll_sina_weibo);
//            ll_sina_weibo.setOnClickListener(this);
//            ll_qq = (LinearLayout) popupView.findViewById(R.id.ll_qq);
//            ll_qq.setOnClickListener(this);
//            ll_qq_zone = (LinearLayout) popupView.findViewById(R.id.ll_qq_zone);
//            ll_qq_zone.setOnClickListener(this);
//
//            //夜间模式
//            ll_night_mode = (LinearLayout) popupView.findViewById(R.id.ll_night_mode);
//            ll_night_mode.setOnClickListener(this);
//            iv_night_mode = (ImageView) popupView.findViewById(R.id.iv_night_mode);
//            tv_night_mode = (TextView) popupView.findViewById(R.id.tv_night_mode);
//            checkColorProfile();
//            //字体
//            ll_font_set = (LinearLayout) popupView.findViewById(R.id.ll_font_set);
//            ll_font_set.setOnClickListener(this);
//            //收藏
//            ll_share_collect = (LinearLayout) popupView.findViewById(R.id.ll_collect);
//            ll_share_collect.setOnClickListener(this);
//            iv_collect = (ImageView) popupView.findViewById(R.id.iv_collect);
//
//            //全文搜索
//            ll_search = (LinearLayout) popupView.findViewById(R.id.ll_search);
//            ll_search.setOnClickListener(this);
//
//            tv_cancle = (TextView) popupView.findViewById(R.id.tv_cancle);
//            tv_cancle.setOnClickListener(this);
//
//            if (bookList != null) {
//                if (fojingVo.iscollection == 1) {
//                    iv_collect.setImageResource(R.drawable.icon_collect_yi);
//
//                } else {
//                    iv_collect.setImageResource(R.drawable.icon_collect);
//                }
//
//            }
//
//        }
//    }
//
//    @Override
//    protected Animation getShowAnimation() {
//        return null;
//    }
//
//    @Override
//    protected View getClickToDismissView() {
//        return popupView.findViewById(R.id.ll_out_pop);
//    }
//
//    @Override
//    public View getPopupView() {
//        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_treader_more, null);
//        return popupView;
//    }
//
//    @Override
//    public View getAnimaView() {
//        return null;
//    }
//
//    @Override
//    public void onBeforeTask(int taskId, int num, boolean isShowDlg) {
//
//    }
//
//    @Override
//    public Object onTask(int taskId, int num, boolean isShowDlg, Object... params) throws Exception {
//        String result = null;
//        JSONObject jsonObject = new JSONObject();
//
//        if (taskId == TaskId.common_insertCollect) {
//            jsonObject.put("accountid", BasicSetting.getInstance(mContext).getAccountid());
//            jsonObject.put("hostid", fojingId);
//            jsonObject.put("hosttype", 40);
//            result = NetAide.getInstance().postData(jsonObject,
//                    Global.common_insertCollect);
//        } else if (taskId == TaskId.common_deleteCollect) {
//            jsonObject.put("accountid", BasicSetting.getInstance(mContext).getAccountid());
//            jsonObject.put("hostid", fojingId);
//            jsonObject.put("hosttype", 40);
//            result = NetAide.getInstance().postData(jsonObject,
//                    Global.common_deleteCollect);
//        } else if (taskId == TaskId.wechatshare_getWechatShare) {
//            result = getShareContent(null, fojingId + "", 25);
//        }
//
//        return result;
//    }
//
//    @Override
//    public void onAfterTask(int taskId, int num, boolean isShowDlg, Object result) {
//        String jsonResult = (String) result;
//        if (!StringUtils.isEmpty(jsonResult)) {
//
//            if (taskId == TaskId.common_insertCollect) {
//                initCollect(jsonResult, 0);
//            } else if (taskId == TaskId.common_deleteCollect) {
//                initCollect(jsonResult, 1);
//            }
//        } else {
//            if (taskId == TaskId.wechatshare_getWechatShare) {
//                ToastUtil.showShortToast(mContext, "分享失败，请稍后重试");
//            }
//        }
//    }
//
//    @Override
//    public void onTaskError(int taskId, int num, boolean isShowDlg, Exception exception) {
//
//    }
//
//    //收藏和取消收藏
//    private void initCollect(String jsonResult, int addCollect) {
//        dismiss();
//        if (ToastUtil.checkJsonForToast(jsonResult, false, mContext)) {
//            try {
//                FojingVo vo = FojingVo.where("inforid = ?", String.valueOf(fojingId)).findFirst(FojingVo.class);
//                if (vo != null) {
//                    vo.setDowonloaded(vo.dowonloaded);
//                    vo.setReadType(vo.readType);
//                    vo.setDownloadTimeMillis(vo.downloadTimeMillis);
//                    vo.setReadTimeMillis(vo.readTimeMillis);
//                }
//                if (vo.iscollection == 1) {
//                    ToastUtil.showShortToast(mContext, "取消收藏");
//                    vo.setIscollection(ConstsType.TYPE_TWO);
//                } else {
//                    ToastUtil.showShortToast(mContext, "收藏成功");
//                    vo.setIscollection(ConstsType.TYPE_ONE);
//                }
//                int updateRows = vo.update(vo.id);
//                LogUtils.e("更新结果" + updateRows);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            ToastUtil.showShortToast(mContext, "操作失败，请检查网络后重试");
//        }
//    }
//
//    public interface onMoreChangeListener {
//
//        void onTextStyleChange();
//
//        void showSetting();
//    }
//}
