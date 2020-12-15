package com.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class MainTab04 extends Fragment {
    View newsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsLayout = inflater.inflate(R.layout.main_tab_04, container, false);
        initView();
        return newsLayout;
    }

    public void initView() {
        newsLayout.findViewById(R.id.linear_setting_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        newsLayout.findViewById(R.id.linear_setting_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));

            }
        });
        TextView tvUser = newsLayout.findViewById(R.id.tv_loginuser);
        String login_user = getActivity().getSharedPreferences("login", MODE_PRIVATE).getString("login_user", "");
        tvUser.setText(login_user);
    }

}
