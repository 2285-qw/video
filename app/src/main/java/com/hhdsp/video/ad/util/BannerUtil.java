package com.hhdsp.video.ad.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.PersonalizationPrompt;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.hhdsp.video.ad.config.TTAdManagerHolder;
import com.hhdsp.video.utils.LogUtils;
import com.hhdsp.video.utils.shareUtils;
import com.umeng.commonsdk.debug.UMLogUtils;

import java.util.List;

import static com.hhdsp.video.ad.util.TimeUtil.BannerRunTime;


public class BannerUtil {
    private TTAdNative mTTAdNative;
    private FrameLayout mExpressContainer;
    private Context mContext;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;

    private void initTTSDKConfig() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);
    }

    /*
     *加载广告
     * 广告样式600*90，600*150，600*260，600*300，600*400，600*100，690*388，600*500
     *加载广告的FrameLayout
     * */
    public void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight, FrameLayout mExpressContainer, Context mContext) {
        this.mContext = mContext;
        this.mExpressContainer = mExpressContainer;
        initTTSDKConfig();
        mExpressContainer.removeAllViews();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                TToast.show(mContext, "load error : " + code + ", " + message);
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
                TToast.show(mContext, "load success!");
            }
        });
    }


    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                TToast.show(mContext, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                TToast.show(mContext, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                TToast.show(mContext, "渲染成功");
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                TToast.show(mContext, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    TToast.show(mContext, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                TToast.show(mContext, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                TToast.show(mContext, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                TToast.show(mContext, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                TToast.show(mContext, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    public void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            final DislikeInfo dislikeInfo = ad.getDislikeInfo();
            if (dislikeInfo == null || dislikeInfo.getFilterWords() == null || dislikeInfo.getFilterWords().isEmpty()) {
                return;
            }
            final DislikeDialog dislikeDialog = new DislikeDialog(mContext, dislikeInfo);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    TToast.show(mContext, "点击 " + filterWord.getName());
                    shareUtils.putLong(mContext,BannerRunTime,System.currentTimeMillis());
                    LogUtils.d("TIME"+System.currentTimeMillis());
                    //用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();

                }
            });
            dislikeDialog.setOnPersonalizationPromptClick(new DislikeDialog.OnPersonalizationPromptClick() {
                @Override
                public void onClick(PersonalizationPrompt personalizationPrompt) {
                    TToast.show(mContext, "点击了为什么看到此广告");
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onShow() {
            }

            @Override

            public void onSelected(int position, String value, boolean enforce) {
                TToast.show(mContext, "点击 " + value);
                shareUtils.putLong(mContext,BannerRunTime,System.currentTimeMillis());
                mExpressContainer.removeAllViews();
                //用户选择不喜欢原因后，移除广告展示
                if (enforce) {
                    TToast.show(mContext, "模版Banner 穿山甲sdk强制将view关闭了");
                }
            }

            @Override
            public void onCancel() {
                TToast.show(mContext, "点击取消 ");
            }

        });
    }

    public void deDestroy() {
        if (mTTAd != null) {
            mTTAd.destroy();
        }

    }
}



