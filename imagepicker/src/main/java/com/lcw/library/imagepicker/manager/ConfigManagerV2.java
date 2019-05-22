package com.lcw.library.imagepicker.manager;

import com.lcw.library.imagepicker.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 统一配置管理类
 * Create by: chenWei.li
 * Date: 2019/1/23
 * Time: 10:32 AM
 * Email: lichenwei.me@foxmail.com
 */
public class ConfigManagerV2 {

    public static final int SELECT_MODE_SINGLE = 0;
    public static final int SELECT_MODE_MULTI = 1;

    /**
     * 是否显示拍照Item，默认不显示
     */
    private boolean showCamera;
    /**
     * 是否显示图片，默认显示
     */
    private boolean showImage = true;
    /**
     * 是否显示视频，默认显示
     */
    private boolean showVideo = true;
    /**
     * 选择模式，默认单选
     */
    private int selectionMode = SELECT_MODE_SINGLE;
    /**
     * 最大选择数量，默认为1
     */
    private int maxCount = 1;
    /**
     * 是否只支持选单类型（图片或者视频）
     */
    private boolean singleType;
    /**
     * 当 singleType = true ,即只支持选择单类型时，允许的最大图片选择数量
     */
    private int maxImageCount = -1;
    /**
     * 当 singleType = true ,即只支持选择单类型时，允许的最大视频选择数量
     */
    private int maxVideoCount = -1;
    /**
     * 上一次选择的图片地址集合
     */
    private ArrayList<String> imagePaths;

    private ImageLoader imageLoader;

    private static volatile ConfigManagerV2 mConfigManager;

    private ConfigManagerV2() {
    }

    public static ConfigManagerV2 getInstance() {
        if (mConfigManager == null) {
            synchronized (SelectionManager.class) {
                if (mConfigManager == null) {
                    mConfigManager = new ConfigManagerV2();
                }
            }
        }
        return mConfigManager;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }

    public boolean isShowVideo() {
        return showVideo;
    }

    public void setShowVideo(boolean showVideo) {
        this.showVideo = showVideo;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        if (maxCount > 1) {
            setSelectionMode(SELECT_MODE_MULTI);
        }
        this.maxCount = maxCount;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(int mSelectionMode) {
        this.selectionMode = mSelectionMode;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public ImageLoader getImageLoader() throws Exception {
        if (imageLoader == null) {
            throw new Exception("imageLoader is null");
        }
        return imageLoader;
    }

    public boolean isSingleType() {
        return singleType;
    }

    public void setSingleType(boolean singleType) {
        this.singleType = singleType;
    }

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    public int getMaxVideoCount() {
        return maxVideoCount;
    }

    public void setMaxVideoCount(int maxVideoCount) {
        this.maxVideoCount = maxVideoCount;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
