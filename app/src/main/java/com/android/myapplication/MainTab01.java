package com.android.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.Adapter.NoteListAdapter;
import com.android.myapplication.bean.GlobalValues;
import com.android.myapplication.bean.NoteBean;
import com.android.myapplication.db.NoteDao;
import com.android.myapplication.utils.AlarmTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
        mNoteListAdapter.setOnItemClickListener(new NoteListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NoteBean note) {
                Intent intent = new Intent(getContext(), NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        rv_list_main.setAdapter(mNoteListAdapter);
        inflate.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("flag", 0);
                intent.putExtra("login_user", login_user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mNoteListAdapter.getPosition();
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case Menu.FIRST + 1://查看该笔记
                Intent intent = new Intent(getContext(), NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", noteList.get(position));
                intent.putExtra("data", bundle);
                startActivity(intent);
                break;

            case Menu.FIRST + 2://编辑该笔记
                Intent intent2 = new Intent(getContext(), EditActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("note", noteList.get(position));
                intent2.putExtra("data", bundle2);
                intent2.putExtra("flag", 1);//编辑笔记
                startActivity(intent2);
                break;

            case Menu.FIRST + 3://删除该笔记
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("确定删除笔记？");
                builder.setCancelable(false);
                final int finalPosition = position;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ret = noteDao.DeleteNote(noteList.get(finalPosition).getId());
                        if (ret > 0) {
                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            refreshNoteList();
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;

            case Menu.FIRST + 4://标记为已完成
                NoteBean bean = noteList.get(position);
                if (bean.getMark() == 1) {
                    Toast.makeText(getContext(), "它早就被完成了啊", Toast.LENGTH_SHORT).show();
                } else {
                    bean.setMark(1);
                    noteDao.updateNote(bean);
                    //noteList.get(position).setMark(1);
                    refreshNoteList();
                    mNoteListAdapter.notifyItemRangeChanged(position, position);
                }
                break;


            case Menu.FIRST + 5://标记为未完成
                NoteBean bean2 = noteList.get(position);
                if (bean2.getMark() == 0) {
                    Toast.makeText(getContext(), "它本来就没完成啊", Toast.LENGTH_SHORT).show();
                } else {
                    bean2.setMark(0);
                    noteDao.updateNote(bean2);
                    //noteList.get(position).setMark(0);
                    refreshNoteList();
                    mNoteListAdapter.notifyItemRangeChanged(position, position);
                }
                break;
            case Menu.FIRST + 6://设置提醒时间
                NoteBean bean3 = noteList.get(position);
                selectTime(bean3.getId(), bean3, position);
                break;
            case Menu.FIRST + 7://清除提醒
                NoteBean noteBean = noteList.get(position);
                AlarmTimer.cancelAlarmTimer(getContext(), GlobalValues.TIMER_ACTION, noteBean.getId());
                noteBean.setAlarm(0);
                noteDao.updateNote(noteBean);
                //noteList.get(position).setMark(0);
                refreshNoteList();
                mNoteListAdapter.notifyItemRangeChanged(position, position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void selectTime(final int alarmId, final NoteBean noteBean, final int position) {

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpdialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        // TODO Auto-generated method stub
                        // 更新EditText控件日期 小于10加0
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        final TimePickerDialog tpdialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
                long timeInMillis = calendar.getTimeInMillis();
                noteBean.setAlarm(1);
                noteDao.updateNote(noteBean);
                AlarmTimer.setAlarmTimer(getContext(), timeInMillis, GlobalValues.TIMER_ACTION, AlarmManager.RTC_WAKEUP, alarmId, noteBean);

                refreshNoteList();
                mNoteListAdapter.notifyItemRangeChanged(position, position);
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dpdialog.show();
        dpdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tpdialog.show();
            }
        });
    }

    //刷新数据库数据，其实对notelist单一更新即可，不必重新获取，但是偷懒了
    private void refreshNoteList() {//mark--0=查询未完成，1=查询已完成，>1=查询所有
        initData();
        mNoteListAdapter.setmNotes(noteList);
        mNoteListAdapter.notifyDataSetChanged();
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
