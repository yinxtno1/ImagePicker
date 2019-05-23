package com.lcw.library.imagepicker.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lcw.library.imagepicker.R;
import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.utils.MediaFileUtil;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 媒体选择集合管理类
 * Create by: chenWei.li
 * Date: 2018/8/23
 * Time: 上午1:19
 * Email: lichenwei.me@foxmail.com
 */
public class SelectionManagerV2 {

    private static volatile SelectionManagerV2 mSelectionManager;

    private ArrayList<MediaFile> mSelectMediaFiles = new ArrayList<>();

    private int mMaxCount = 1;
    private boolean mSingleType;
    private int mMaxImageCount = -1;
    private int mMaxVideoCount = -1;

    private SelectionManagerV2() {
    }

    public static SelectionManagerV2 getInstance() {
        if (mSelectionManager == null) {
            synchronized (SelectionManagerV2.class) {
                if (mSelectionManager == null) {
                    mSelectionManager = new SelectionManagerV2();
                }
            }
        }
        return mSelectionManager;
    }

    /**
     * 设置最大选择数
     *
     * @param maxCount
     */
    public void setMaxCount(int maxCount) {
        this.mMaxCount = maxCount;
    }

    public void setSingleType(boolean singleType) {
        this.mSingleType = singleType;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.mMaxImageCount = maxImageCount;
    }

    public void setMaxVideoCount(int maxVideoCount) {
        this.mMaxVideoCount = maxVideoCount;
    }

    /**
     * 获取当前所选图片集合path
     *
     * @return
     */
    public ArrayList<MediaFile> getSelectFile() {
        return mSelectMediaFiles;
    }

    /**
     * 添加媒体资源到选择集合
     *
     * @param media
     * @return
     */
    public boolean addImageToSelectList(MediaFile media) {
        if (mSelectMediaFiles.contains(media)) {
            return false;
        }
        return mSelectMediaFiles.add(media);
    }

    public boolean remove(MediaFile media) {
        return mSelectMediaFiles.remove(media);
    }


    /**
     * 判断当前媒体文件是否被选择
     *
     * @return
     */
    public boolean isImageSelect(MediaFile media) {
        return mSelectMediaFiles.contains(media);
    }

    /**
     * 当前媒体文件是否可以被选中
     *
     * @return
     */
    public boolean canSelected(Context context, MediaFile mediaFile) {
        if (mSelectMediaFiles.isEmpty()) {
            return true;
        }
        if (mSingleType) {
            //图片、视频互斥
            boolean firstIsVideo = MimeType.isVideo(mSelectMediaFiles.get(0).getMime());
            boolean curIsVideo = MimeType.isVideo(mediaFile.getMime());
            if (firstIsVideo != curIsVideo) {
                //类型不同
                Toast.makeText(context, R.string.error_single_type_choose, Toast.LENGTH_SHORT).show();
                return false;
            } else if (firstIsVideo) {
                //同为视频
                int maxCount = mMaxVideoCount > 0 ? mMaxVideoCount : mMaxCount;
                if (mSelectMediaFiles.size() + 1 > maxCount) {
                    //数量超出限制
                    Toast.makeText(context, context.getString(R.string.error_select_video_max, maxCount),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                //同为图片
                //图片
                int maxCount = mMaxImageCount > 0 ? mMaxImageCount : mMaxCount;
                if (mSelectMediaFiles.size() + 1 > maxCount) {
                    //数量超出限制
                    Toast.makeText(context, context.getString(R.string.error_select_image_max, maxCount),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            //图片、视频二者可同时选择
            if (mSelectMediaFiles.size() + 1 > mMaxCount) {
                Toast.makeText(context, context.getString(R.string.error_select_max, mMaxCount),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 清除已选图片
     */
    public void removeAll() {
        mSelectMediaFiles.clear();
    }

}
