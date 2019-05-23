package com.lcw.library.imagepicker.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.manager.ConfigManager;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;

import java.util.Iterator;
import java.util.Set;

/**
 * 媒体库扫描类(图片)
 * Create by: chenWei.li
 * Date: 2018/8/21
 * Time: 上午1:01
 * Email: lichenwei.me@foxmail.com
 */
public class ImageScannerV2 extends AbsMediaScanner<MediaFile> {

    public ImageScannerV2(Context context) {
        super(context);
    }


    @Override
    protected Uri getScanUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getProjection() {
        return new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };
    }

    @Override
    protected String getSelection() {
        Set<MimeType> mimeTypes = ConfigManagerV2.getInstance().getMimeTypes();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (MimeType mimeType : mimeTypes) {
            if (MimeType.isImage(mimeType.mMimeTypeName)) {
                sb.append("'")
                        .append(mimeType.mMimeTypeName)
                        .append("'")
                        .append(",");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        return MediaStore.Images.Media.MIME_TYPE + " IN " + sb.toString();
    }

    @Override
    protected String[] getSelectionArgs() {
        return null;
    }

    @Override
    protected String getOrder() {
        return MediaStore.Images.Media.DATE_TAKEN + " desc";
    }

    /**
     * 构建媒体对象
     *
     * @param cursor
     * @return
     */
    @Override
    protected MediaFile parse(Cursor cursor) {

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String mime = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        Integer folderId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        long dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));

        MediaFile mediaFile = new MediaFile();
        mediaFile.setPath(path);
        mediaFile.setMime(mime);
        mediaFile.setFolderId(folderId);
        mediaFile.setFolderName(folderName);
        mediaFile.setDateToken(dateToken);

        return mediaFile;
    }


}
