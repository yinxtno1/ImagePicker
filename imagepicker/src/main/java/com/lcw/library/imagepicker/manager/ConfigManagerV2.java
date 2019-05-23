package com.lcw.library.imagepicker.manager;

import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.utils.ImageLoader;

import java.util.ArrayList;
import java.util.Set;

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
     * 需要被展示的 mimeType
     */
    private Set<MimeType> mimeTypeSet;
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
     * 视频最大时长
     */
    private long videoMaxDuration = Long.MAX_VALUE;

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

    public static ConfigManagerV2 getClearInstance(){
        ConfigManagerV2 instance = getInstance();
        instance.reset();
        return instance;
    }

    private void reset(){
        showCamera = false;
        showImage = true;
        showVideo = true;
        mimeTypeSet = null;
        selectionMode = SELECT_MODE_SINGLE;
        maxCount = 1;
        singleType = false;
        maxImageCount = -1;
        maxVideoCount = -1;
        imageLoader = null;
        videoMaxDuration = Long.MAX_VALUE;
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

    public void setMimeTypeSet(Set<MimeType> mimeTypes) {
        this.mimeTypeSet = mimeTypes;
    }

    public Set<MimeType> getMimeTypes(){
        return mimeTypeSet;
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

    public void setVideoMaxDuration(long maxDuration) {
        this.videoMaxDuration = maxDuration;
    }

    public long getVideoMaxDuration() {
        return videoMaxDuration;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
