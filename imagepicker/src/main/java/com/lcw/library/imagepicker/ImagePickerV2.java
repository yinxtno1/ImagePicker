package com.lcw.library.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lcw.library.imagepicker.activity.ImagePickerFragmentActivity;
import com.lcw.library.imagepicker.activity.ImagePreActivityV2;
import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;
import com.lcw.library.imagepicker.utils.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 统一调用入口
 * Create by: chenWei.li
 * Date: 2018/8/26
 * Time: 下午6:31
 * Email: lichenwei.me@foxmail.com
 */
public class ImagePickerV2 {

    public static final String EXTRA_SELECT_IMAGES = "selectItems";

    private static volatile ImagePickerV2 mImagePicker;
    private ConfigManagerV2 mConfigManager;

    private ImagePickerV2() {
    }

    /**
     * 创建对象
     *
     * @return
     */
    public static ImagePickerV2 getInstance() {
        if (mImagePicker == null) {
            synchronized (ImagePickerV2.class) {
                if (mImagePicker == null) {
                    mImagePicker = new ImagePickerV2();
                }
            }
        }
        mImagePicker.initConfigManager();
        return mImagePicker;
    }

    private void initConfigManager() {
        mConfigManager = ConfigManagerV2.getClearInstance();
    }

    /**
     * 是否支持相机
     *
     * @param showCamera
     * @return
     */
    public ImagePickerV2 showCamera(boolean showCamera) {
        mConfigManager.setShowCamera(showCamera);
        return mImagePicker;
    }

    /**
     * 是否展示图片
     *
     * @param showImage
     * @return
     */
    public ImagePickerV2 showImage(boolean showImage) {
        mConfigManager.setShowImage(showImage);
        return mImagePicker;
    }

    /**
     * 是否展示视频
     *
     * @param showVideo
     * @return
     */
    public ImagePickerV2 showVideo(boolean showVideo) {
        mConfigManager.setShowVideo(showVideo);
        return mImagePicker;
    }

    public ImagePickerV2 choiceMimeType(Set<MimeType> mimeTypes) {
        mConfigManager.setMimeTypeSet(mimeTypes);
        return mImagePicker;
    }

    /**
     * 图片最大选择数
     *
     * @param maxCount
     * @return
     */
    public ImagePickerV2 setMaxCount(int maxCount) {
        mConfigManager.setMaxCount(maxCount);
        return mImagePicker;
    }

    /**
     * 设置图片或视频的最大选择数（在媒体类型只能单独选择时有效，singleType = true）
     *
     * @param maxImageCount
     * @param maxVideoCount
     * @return
     */
    public ImagePickerV2 setMaxCount(int maxImageCount, int maxVideoCount) {
        if (maxImageCount < 1 || maxVideoCount < 1)
            throw new IllegalArgumentException(("max selectable must be greater than or equal to one"));
        mConfigManager.setMaxImageCount(maxImageCount);
        mConfigManager.setMaxVideoCount(maxVideoCount);
        return mImagePicker;
    }

    /**
     * 媒体类型是否只能单独选择（只能选图片或者视频）
     *
     * @param isSingleType
     * @return
     */
    public ImagePickerV2 setSingleType(boolean isSingleType) {
        mConfigManager.setSingleType(isSingleType);
        return mImagePicker;
    }

    public ImagePickerV2 setVideoMaxDuration(long maxDuration) {
        mConfigManager.setVideoMaxDuration(maxDuration);
        return mImagePicker;
    }

    /**
     * 设置图片加载器
     *
     * @param imageLoader
     * @return
     */
    public ImagePickerV2 setImageLoader(ImageLoader imageLoader) {
        mConfigManager.setImageLoader(imageLoader);
        return mImagePicker;
    }

    /**
     * 启动
     *
     * @param activity
     */
    public void start(Activity activity, int requestCode) {
        if (mConfigManager.getMimeTypes() == null) {
            Set<MimeType> mimeTypes = new HashSet<>();
            if (mConfigManager.isShowImage()) {
                mimeTypes.addAll(MimeType.ofImage());
            }
            if (mConfigManager.isShowVideo()) {
                mimeTypes.addAll(MimeType.ofVideo());
            }
            mConfigManager.setMimeTypeSet(mimeTypes);
        }
        Intent intent = new Intent(activity, ImagePickerFragmentActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

}
