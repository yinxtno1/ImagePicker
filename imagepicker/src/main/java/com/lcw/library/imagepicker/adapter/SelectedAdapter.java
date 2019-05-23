package com.lcw.library.imagepicker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.R;
import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MimeType;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;
import com.lcw.library.imagepicker.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :尹晓童
 * company: 山东六臂网络科技有限公司
 * email：yinxtno1@yeah.net
 * time：2019/5/23
 * desc：
 */
public class SelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_IMAGE = 1;
    public static final int ITEM_VIDEO = 2;

    private List<MediaFile> mSelectedList = new ArrayList<>();

    public SelectedAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == ITEM_IMAGE) {
            viewHolder = new ImageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_selected_image, parent, false));
        } else {
            viewHolder = new VideoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_selected_video, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaFile mediaFile = mSelectedList.get(position);
        if (holder instanceof ImageViewHolder) {
            try {
                ConfigManagerV2.getInstance()
                        .getImageLoader()
                        .loadImage(((ImageViewHolder) holder).ivImage, mediaFile.getPath());
            } catch (Exception ignored) {

            }
        } else {
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            viewHolder.tvItemVideoDuration.setText(Utils.getVideoDuration(mediaFile.getDuration()));
            try {
                ConfigManagerV2.getInstance()
                        .getImageLoader()
                        .loadImage(viewHolder.ivItemImage, mediaFile.getPath());
            } catch (Exception ignored) {

            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        MediaFile mediaFile = mSelectedList.get(position);
        if (MimeType.isVideo(mediaFile.getMime())) {
            return ITEM_VIDEO;
        } else {
            return ITEM_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return mSelectedList.size();
    }

    public void setNewDatas(List<MediaFile> files) {
        if (files == null) {
            return;
        }
        mSelectedList.clear();
        mSelectedList.addAll(files);
        notifyDataSetChanged();
    }

    public void addData(MediaFile item) {
        if (item == null) {
            return;
        }
        mSelectedList.add(item);
        if (mSelectedList.size() > 1) {
            notifyItemInserted(getItemCount() - 1);
        } else {
            notifyDataSetChanged();
        }

    }

    public void remove(MediaFile item) {
        if (item == null) {
            return;
        }
        int position = mSelectedList.indexOf(item);
        mSelectedList.remove(position);
        notifyItemRemoved(position);
    }

    public List<MediaFile> getData() {
        return mSelectedList;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {


        private ImageView ivItemImage;
        private TextView tvItemVideoDuration;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
            tvItemVideoDuration = itemView.findViewById(R.id.tv_item_videoDuration);
        }
    }

}
