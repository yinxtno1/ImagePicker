package com.lcw.library.imagepicker.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.lcw.library.imagepicker.R;
import com.lcw.library.imagepicker.adapter.ImageFoldersAdapter;
import com.lcw.library.imagepicker.adapter.ImagePickerAdapterV2;
import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MediaFolder;
import com.lcw.library.imagepicker.executors.CommonExecutor;
import com.lcw.library.imagepicker.listener.MediaLoadCallback;
import com.lcw.library.imagepicker.manager.ConfigManagerV2;
import com.lcw.library.imagepicker.manager.SelectionManager;
import com.lcw.library.imagepicker.task.ImageLoadTask;
import com.lcw.library.imagepicker.task.MediaLoadTask;
import com.lcw.library.imagepicker.task.VideoLoadTask;
import com.lcw.library.imagepicker.utils.DataUtil;
import com.lcw.library.imagepicker.utils.MediaFileUtil;
import com.lcw.library.imagepicker.utils.Utils;
import com.lcw.library.imagepicker.view.ImageFolderPopupWindowV2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :尹晓童
 * company: 山东六臂网络科技有限公司
 * email：yinxtno1@yeah.net
 * time：2019/5/22
 * desc：
 */
public class ImagePickerFragment extends Fragment implements ImagePickerAdapterV2.OnItemClickListener, ImageFoldersAdapter.OnImageFolderChangeListener {

    /**
     * 启动参数
     */
    private boolean isShowImage;
    private boolean isShowVideo;
    private boolean isSingleType;
    private int mMaxCount;
    private int mMaxImageCount;
    private int mMaxVideoCount;

    private TextView mTvImageTime;
    private RecyclerView mRecyclerView;
    private TextView mTvImageFolders;
    private ImageFolderPopupWindowV2 mImageFolderPopupWindowV2;
    private ProgressDialog mProgressDialog;

    private GridLayoutManager mGridLayoutManager;
    private ImagePickerAdapterV2 mImagePickerAdapterV2;

    /**
     * 图片数据源
     */
    private List<MediaFile> mMediaFileList;
    /**
     * 文件夹数据源
     */
    private List<MediaFolder> mMediaFolderList;

    /**
     * 是否显示时间
     */
    private boolean isShowTime;

    /**
     * 表示屏幕亮暗
     */
    private static final int LIGHT_OFF = 0;
    private static final int LIGHT_ON = 1;

    private Handler mMyHandler = new Handler(Looper.getMainLooper());
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideImageTime();
        }
    };


    /**
     * 大图预览页相关
     * 用于在大图预览页中点击提交按钮标识
     */
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;

    private View mRootView;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_imagepicker, container, false);
        initConfig();
        initView();
        initListener();
        getData();
        return mRootView;
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        isShowImage = ConfigManagerV2.getInstance().isShowImage();
        isShowVideo = ConfigManagerV2.getInstance().isShowVideo();
        isSingleType = ConfigManagerV2.getInstance().isSingleType();
        mMaxCount = ConfigManagerV2.getInstance().getMaxCount();
        mMaxImageCount = ConfigManagerV2.getInstance().getMaxImageCount();
        mMaxVideoCount = ConfigManagerV2.getInstance().getMaxVideoCount();
        SelectionManager.getInstance().setMaxCount(mMaxCount);

        //载入历史选择记录
        List<String> mImagePaths = ConfigManagerV2.getInstance().getImagePaths();
        if (mImagePaths != null && !mImagePaths.isEmpty()) {
            SelectionManager.getInstance().addImagePathsToSelectList(mImagePaths);
        }
    }

    /**
     * 初始化布局控件
     */
    private void initView() {

        mProgressDialog = ProgressDialog.show(mContext, null, getString(R.string.scanner_image));

        //滑动悬浮标题相关
        mTvImageTime = findViewById(R.id.tv_image_time);

        //底部栏相关
        mTvImageFolders = findViewById(R.id.tv_main_imageFolders);

        //列表相关
        mRecyclerView = findViewById(R.id.rv_main_images);
        mGridLayoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //注释说当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小。
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(60);

        mMediaFileList = new ArrayList<>();
        mImagePickerAdapterV2 = new ImagePickerAdapterV2(mContext, mMediaFileList);
        mImagePickerAdapterV2.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mImagePickerAdapterV2);

    }

    private void initListener() {
        findViewById(R.id.iv_actionBar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了返回", Toast.LENGTH_SHORT).show();
            }
        });


        mTvImageFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageFolderPopupWindowV2 != null) {
                    setLightMode(LIGHT_OFF);
                    mImageFolderPopupWindowV2.showAsDropDown(mTvImageFolders);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                updateImageTime();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateImageTime();
            }
        });
    }

    /**
     * 获取数据源
     */
    private void getData() {
        //此处不进行权限的判断
        startScannerTask();
    }


    /**
     * 开启扫描任务
     */
    private void startScannerTask() {
        Runnable mediaLoadTask = null;

        //照片、视频全部加载
        if (isShowImage && isShowVideo) {
            mediaLoadTask = new MediaLoadTask(mContext, new MediaLoader());
        }

        //只加载视频
        if (!isShowImage && isShowVideo) {
            mediaLoadTask = new VideoLoadTask(mContext, new MediaLoader());
        }

        //只加载图片
        if (isShowImage && !isShowVideo) {
            mediaLoadTask = new ImageLoadTask(mContext, new MediaLoader());
        }

        //不符合以上场景，采用照片、视频全部加载
        if (mediaLoadTask == null) {
            mediaLoadTask = new MediaLoadTask(mContext, new MediaLoader());
        }

        CommonExecutor.getInstance().execute(mediaLoadTask);
    }


    /**
     * 处理媒体数据加载成功后的UI渲染
     */
    class MediaLoader implements MediaLoadCallback {

        @Override
        public void loadMediaSuccess(final List<MediaFolder> mediaFolderList) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!mediaFolderList.isEmpty()) {
                        //默认加载全部照片
                        mMediaFileList.addAll(mediaFolderList.get(0).getMediaFileList());
                        mImagePickerAdapterV2.notifyDataSetChanged();

                        //图片文件夹数据
                        mMediaFolderList = new ArrayList<>(mediaFolderList);
                        mImageFolderPopupWindowV2 = new ImageFolderPopupWindowV2(mContext, mMediaFolderList);
//                        mImageFolderPopupWindowV2.setAnimationStyle(R.style.imageFolderAnimatorV2);
                        mImageFolderPopupWindowV2.getAdapter().setOnImageFolderChangeListener(ImagePickerFragment.this);
                        mImageFolderPopupWindowV2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                setLightMode(LIGHT_ON);
                            }
                        });
                        updateCommitButton();
                    }
                    mProgressDialog.cancel();
                }
            });
        }
    }


    /**
     * 隐藏时间
     */
    private void hideImageTime() {
        if (isShowTime) {
            isShowTime = false;
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 1, 0).setDuration(300).start();
        }
    }

    /**
     * 显示时间
     */
    private void showImageTime() {
        if (!isShowTime) {
            isShowTime = true;
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 0, 1).setDuration(300).start();
        }
    }

    /**
     * 更新时间
     */
    private void updateImageTime() {
        int position = mGridLayoutManager.findFirstVisibleItemPosition();
        MediaFile mediaFile = mImagePickerAdapterV2.getMediaFile(position);
        if (mediaFile != null) {
            if (mTvImageTime.getVisibility() != View.VISIBLE) {
                mTvImageTime.setVisibility(View.VISIBLE);
            }
            String time = Utils.getImageTime(mediaFile.getDateToken());
            mTvImageTime.setText(time);
            showImageTime();
            mMyHandler.removeCallbacks(mHideRunnable);
            mMyHandler.postDelayed(mHideRunnable, 1500);
        }
    }

    /**
     * 设置屏幕的亮度模式
     *
     * @param lightMode 亮度
     */
    private void setLightMode(int lightMode) {
        if (mContext instanceof Activity) {
            WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();
            switch (lightMode) {
                case LIGHT_OFF:
                    layoutParams.alpha = 0.7f;
                    break;
                case LIGHT_ON:
                    layoutParams.alpha = 1.0f;
                    break;
                default:
                    break;
            }
            ((Activity) mContext).getWindow().setAttributes(layoutParams);
        }
    }

    /**
     * 点击图片
     *
     * @param view     view
     * @param position position
     */
    @Override
    public void onMediaClick(View view, int position) {
        if (mMediaFileList != null) {
            DataUtil.getInstance().setMediaData(mMediaFileList);
            Intent intent = new Intent(mContext, ImagePreActivity.class);
            intent.putExtra(ImagePreActivity.IMAGE_POSITION, position);
            startActivityForResult(intent, REQUEST_SELECT_IMAGES_CODE);
        }
    }

    /**
     * 选中/取消选中图片
     *
     * @param view     view
     * @param position position
     */
    @Override
    public void onMediaCheck(View view, int position) {
        //执行选中/取消操作
        MediaFile mediaFile = mImagePickerAdapterV2.getMediaFile(position);
        LogUtils.json(mediaFile);
        String curImagePath = mediaFile.getPath();
        //当前选择的是否是视频
        boolean curIsVideo = MediaFileUtil.isVideoFileType(curImagePath);
        //当前选择的是否已经是被选中的状态
        boolean curIsSelected = SelectionManager.getInstance().isImageSelect(curImagePath);
        //已选中的集合
        ArrayList<String> selectPathList = SelectionManager.getInstance().getSelectPaths();
        if (!selectPathList.isEmpty() && !curIsSelected) {
            if (isSingleType) {
                //图片/视频互斥
                boolean firstIsVideo = MediaFileUtil.isVideoFileType(selectPathList.get(0));
                if (firstIsVideo != curIsVideo) {
                    //类型不同
                    Toast.makeText(mContext, getString(R.string.single_type_choose), Toast.LENGTH_SHORT).show();
                    return;
                }
                //类型相同
                if (firstIsVideo) {
                    //视频
                    if ((mMaxVideoCount > 0 && selectPathList.size() + 1 > mMaxVideoCount)
                            || (mMaxVideoCount < 0 && selectPathList.size() + 1 > mMaxCount)) {
                        Toast.makeText(mContext, "视频数量超了", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    //图片
                    if ((mMaxVideoCount > 0 && selectPathList.size() + 1 > mMaxImageCount)
                            || (mMaxVideoCount < 0 && selectPathList.size() + 1 > mMaxCount)) {
                        Toast.makeText(mContext, "图片数量超了", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                //图片、视频二者可同时选择
                if (selectPathList.size() + 1 > mMaxCount) {
                    Toast.makeText(mContext, "图片和视频的数量超了", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        SelectionManager.getInstance().addImageToSelectList(curImagePath);
        mImagePickerAdapterV2.notifyItemChanged(position);
        updateCommitButton();
//
//        if (isSingleType) {
//            //单类型选取，判断添加类型
//            ArrayList<String> selectPathList = SelectionManager.getInstance().getSelectPaths();
//            if (!selectPathList.isEmpty()) {
//                //判断选中集合中第一项是否为视频
//                String path = selectPathList.get(0);
//                boolean isVideo = MediaFileUtil.isVideoFileType(path);
//                if ((!isVideo && mediaFile.getDuration() != 0) || isVideo && mediaFile.getDuration() == 0) {
//                    //类型不同
//                    Toast.makeText(mContext, getString(R.string.single_type_choose), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        }
//        boolean addSuccess = SelectionManager.getInstance().addImageToSelectList(imagePath);
//        if (addSuccess) {
//            mImagePickerAdapterV2.notifyItemChanged(position);
//        } else {
//            Toast.makeText(mContext, String.format(getString(R.string.select_image_max), mMaxCount), Toast.LENGTH_SHORT).show();
//        }
//        updateCommitButton();
    }


    /**
     * 更新确认按钮状态
     */
    private void updateCommitButton() {
        //改变确定按钮UI
        int selectCount = SelectionManager.getInstance().getSelectPaths().size();
        LogUtils.json(SelectionManager.getInstance().getSelectPaths());
//        if (selectCount == 0) {
//            mTvCommit.setEnabled(false);
//            mTvCommit.setText(getString(R.string.confirm));
//        } else if (selectCount < mMaxCount) {
//            mTvCommit.setEnabled(true);
//            mTvCommit.setText(String.format(getString(R.string.confirm_msg), selectCount, mMaxCount));
//        } else if (selectCount == mMaxCount) {
//            mTvCommit.setEnabled(true);
//            mTvCommit.setText(String.format(getString(R.string.confirm_msg), selectCount, mMaxCount));
//        }
    }

    /**
     * 当图片文件夹切换时，刷新图片列表数据源
     *
     * @param view     view
     * @param position position
     */
    @Override
    public void onImageFolderChange(View view, int position) {
        MediaFolder mediaFolder = mMediaFolderList.get(position);
        //更新当前文件夹名
        String folderName = mediaFolder.getFolderName();
        if (!TextUtils.isEmpty(folderName)) {
            mTvImageFolders.setText(folderName);
        }
        //更新图片列表数据源
        mMediaFileList.clear();
        mMediaFileList.addAll(mediaFolder.getMediaFileList());
        mImagePickerAdapterV2.notifyDataSetChanged();

        mImageFolderPopupWindowV2.dismiss();
    }

    /**
     * 回调
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGES_CODE) {
                commitSelection();
            }
        }
    }

    /**
     * 选择图片完毕，返回
     */
    private void commitSelection() {
        ArrayList<String> list = new ArrayList<>(SelectionManager.getInstance().getSelectPaths());
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list);
        Toast.makeText(mContext, "图片选择完毕", Toast.LENGTH_SHORT).show();
//        setResult(RESULT_OK, intent);
//        SelectionManager.getInstance().removeAll();//清空选中记录
//        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        mImagePickerAdapterV2.notifyDataSetChanged();
        updateCommitButton();
    }

//    @Override
//    public void onBackPressed() {
//        setResult(RESULT_CANCELED);
//        super.onBackPressed();
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMyHandler.removeCallbacksAndMessages(null);
        mMyHandler = null;
        mMediaFileList.clear();
        mMediaFileList = null;
        SelectionManager.getInstance().removeAll();
        try {
            ConfigManagerV2.getInstance().getImageLoader().clearMemoryCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }
}
