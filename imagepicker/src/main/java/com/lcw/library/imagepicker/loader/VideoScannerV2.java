package com.lcw.library.imagepicker.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;

import java.util.Set;

/**
 * 媒体库扫描类(视频)
 * Create by: chenWei.li
 * Date: 2018/8/21
 * Time: 上午1:01
 * Email: lichenwei.me@foxmail.com
 */
public class VideoScannerV2 extends AbsMediaScanner<MediaFile> {

    public static final int ALL_IMAGES_FOLDER = -1;//全部图片

    private Context mContext;

    public VideoScannerV2(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected Uri getScanUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getProjection() {
        return new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_TAKEN
        };
    }

    @Override
    protected String getSelection() {
        Set<MimeType> mimeTypes = ConfigManagerV2.getInstance().getMimeTypes();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (MimeType mimeType : mimeTypes) {
            if (MimeType.isVideo(mimeType.mMimeTypeName)) {
                sb.append("'")
                        .append(mimeType.mMimeTypeName)
                        .append("'")
                        .append(",");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        return MediaStore.Video.Media.MIME_TYPE + " IN " + sb.toString() + " AND " + MediaStore.Video.Media.DURATION + "<= ?";
    }

    @Override
    protected String[] getSelectionArgs() {
        return new String[]{String.valueOf(ConfigManagerV2.getInstance().getVideoMaxDuration())};
    }

    @Override
    protected String getOrder() {
        return MediaStore.Video.Media.DATE_TAKEN + " desc";
    }

    /**
     * 构建媒体对象
     *
     * @param cursor
     * @return
     */
    @Override
    protected MediaFile parse(Cursor cursor) {

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        String mime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
        Integer folderId = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
        String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        long dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));

        MediaFile mediaFile = new MediaFile();
        mediaFile.setPath(path);
        mediaFile.setMime(mime);
        mediaFile.setFolderId(folderId);
        mediaFile.setFolderName(folderName);
        mediaFile.setDuration(duration);
        mediaFile.setDateToken(dateToken);

        return mediaFile;
    }


}
