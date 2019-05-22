package com.lcw.library.imagepicker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lcw.library.imagepicker.R;

/**
 * @author :尹晓童
 * company: 山东六臂网络科技有限公司
 * email：yinxtno1@yeah.net
 * time：2019/5/22
 * desc：承载 ImagePickerFramgnet 的 Activity
 */
public class ImagePickerFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepicker_fragment);
    }
}
