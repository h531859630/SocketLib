package com.dydl.electronicdemo.view.fragment.electronic;

import android.widget.Button;

import com.dydl.electronicdemo.R;
import com.dydl.electronicdemo.view.activity.electronic.EleSignActivity;
import com.dydl.electronicdemo.view.fragment.base.BaseFragment;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;

/**
 * Created by lwx on 2018/3/9.
 */

public class ContactFragment extends BaseFragment {


    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    public void operation() {

        RxView.clicks(btnLast).subscribe(o -> {
            ((EleSignActivity) getActivity()).replaceBusiness();
        });

        RxView.clicks(btnNext).subscribe(o -> {
            ((EleSignActivity) getActivity()).replaceUpload();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contact;
    }
}
