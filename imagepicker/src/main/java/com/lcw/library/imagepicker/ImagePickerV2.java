package com.lcw.library.imagepicker;

import android.app.Activity;
import android.content.Intent;

import com.lcw.library.imagepicker.activity.ImagePickerFragmentActivity;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;
import com.lcw.library.imagepicker.utils.ImageLoader;

import java.util.ArrayList;

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
        return mImagePicker;
    }

    /**
     * 是否支持相机
     *
     * @param showCamera
     * @return
     */
    public ImagePickerV2 showCamera(boolean showCamera) {
        ConfigManagerV2.getInstance().setShowCamera(showCamera);
        return mImagePicker;
    }

    /**
     * 是否展示图片
     *
     * @param showImage
     * @return
     */
    public ImagePickerV2 showImage(boolean showImage) {
        ConfigManagerV2.getInstance().setShowImage(showImage);
        return mImagePicker;
    }

    /**
     * 是否展示视频
     *
     * @param showVideo
     * @return
     */
    public ImagePickerV2 showVideo(boolean showVideo) {
        ConfigManagerV2.getInstance().setShowVideo(showVideo);
        return mImagePicker;
    }


    /**
     * 图片最大选择数
     *
     * @param maxCount
     * @return
     */
    public ImagePickerV2 setMaxCount(int maxCount) {
        ConfigManagerV2.getInstance().setMaxCount(maxCount);
        return mImagePicker;
    }

    /**
     *
     * @param maxImageCount
     * @param maxVideoCount
     * @return
     */
    public ImagePickerV2 setMaxCount(int maxImageCount, int maxVideoCount) {
        ConfigManagerV2.getInstance().setMaxImageCount(maxImageCount);
        ConfigManagerV2.getInstance().setMaxVideoCount(maxVideoCount);
        return mImagePicker;
    }

    /**
     * 设置单类型选择（只能选图片或者视频）
     *
     * @param isSingleType
     * @return
     */
    public ImagePickerV2 setSingleType(boolean isSingleType) {
        ConfigManagerV2.getInstance().setSingleType(isSingleType);
        return mImagePicker;
    }

    public ImagePickerV2 setMaxImageCount(int maxImageCount) {
        ConfigManagerV2.getInstance().setMaxImageCount(maxImageCount);
        return mImagePicker;
    }

    public ImagePickerV2 setMaxVideoCount(int maxVideoCount) {
        ConfigManagerV2.getInstance().setMaxVideoCount(maxVideoCount);
        return mImagePicker;
    }


    /**
     * 设置图片加载器
     *
     * @param imageLoader
     * @return
     */
    public ImagePickerV2 setImageLoader(ImageLoader imageLoader) {
        ConfigManagerV2.getInstance().setImageLoader(imageLoader);
        return mImagePicker;
    }

    /**
     * 设置图片选择历史记录
     *
     * @param imagePaths
     * @return
     */
    public ImagePickerV2 setImagePaths(ArrayList<String> imagePaths) {
        ConfigManagerV2.getInstance().setImagePaths(imagePaths);
        return mImagePicker;
    }

    /**
     * 启动
     *
     * @param activity
     */
    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImagePickerFragmentActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

}
