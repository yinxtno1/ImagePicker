package com.lcw.library.imagepicker.task;

import android.content.Context;

import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.listener.MediaLoadCallback;
import com.lcw.library.imagepicker.loader.ImageScannerV2;
import com.lcw.library.imagepicker.loader.MediaHandler;
import com.lcw.library.imagepicker.loader.VideoScannerV2;

import java.util.ArrayList;

/**
 * 媒体库扫描任务（图片、视频）
 * Create by: chenWei.li
 * Date: 2018/8/25
 * Time: 下午12:31
 * Email: lichenwei.me@foxmail.com
 */
public class MediaLoadTaskV2 implements Runnable {

    private Context mContext;
    private ImageScannerV2 mImageScannerV2;
    private VideoScannerV2 mVideoScannerV2;
    private MediaLoadCallback mMediaLoadCallback;

    public MediaLoadTaskV2(Context context, MediaLoadCallback mediaLoadCallback) {
        this.mContext = context;
        this.mMediaLoadCallback = mediaLoadCallback;
        mImageScannerV2 = new ImageScannerV2(context);
        mVideoScannerV2 = new VideoScannerV2(context);
    }

    @Override
    public void run() {
        //存放所有照片
        ArrayList<MediaFile> imageFileList = new ArrayList<>();
        //存放所有视频
        ArrayList<MediaFile> videoFileList = new ArrayList<>();

        if (mImageScannerV2 != null) {
            imageFileList = mImageScannerV2.queryMedia();
        }
        if (mVideoScannerV2 != null) {
            videoFileList = mVideoScannerV2.queryMedia();
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.loadMediaSuccess(MediaHandler.getMediaFolder(mContext, imageFileList, videoFileList));
        }

    }

}
