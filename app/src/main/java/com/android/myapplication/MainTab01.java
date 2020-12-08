package com.android.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.Adapter.NoteListAdapter;
import com.android.myapplication.bean.NoteBean;
import com.android.myapplication.db.NoteDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainTab01 extends Fragment {
    private RecyclerView rv_list_main;
    private View inflate;
    private NoteDao noteDao;
    private String login_user;
    private List<NoteBean> noteList;
    private NoteListAdapter mNoteListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.main_tab_01, container, false);
        initView();
        return inflate;

    }

    public void initView() {
        noteDao = new NoteDao(getContext());
        login_user = getActivity().getSharedPreferences("login", MODE_PRIVATE).getString("login_user", "");

        rv_list_main = inflate.findViewById(R.id.rv_list_main);
        initData();
        //设置RecyclerView的属性
        rv_list_main.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//竖向列表
        rv_list_main.setLayoutManager(layoutManager);

        mNoteListAdapter = new NoteListAdapter();
        mNoteListAdapter.setmNotes(noteList);
        rv_list_main.setAdapter(mNoteListAdapter);

        //RecyclerViewItem单击事件
        mNoteListAdapter.setOnItemClickListener(new NoteListAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, NoteBean note) {
                //Toast.makeText(MainActivity.this,""+note.getId(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    //初始化数据库数据
    private void initData() {
        Cursor cursor = noteDao.getAllData(login_user);
        noteList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                NoteBean bean = new NoteBean();
                bean.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
                bean.setTitle(cursor.getString(cursor.getColumnIndex("note_tittle")));
                bean.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
                bean.setType(cursor.getString(cursor.getColumnIndex("note_type")));
                bean.setMark(cursor.getInt(cursor.getColumnIndex("note_mark")));
                bean.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                bean.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                bean.setOwner(cursor.getString(cursor.getColumnIndex("note_owner")));
                bean.setYear(cursor.getString(cursor.getColumnIndex("year")));
                bean.setMonth(cursor.getString(cursor.getColumnIndex("month")));
                bean.setDay(cursor.getString(cursor.getColumnIndex("day")));
                bean.setAlarm(cursor.getInt(cursor.getColumnIndex("isneedAlarm")));
                noteList.add(bean);
            }
        }
        cursor.close();

    }
}
