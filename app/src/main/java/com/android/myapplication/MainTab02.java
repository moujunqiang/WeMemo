package com.android.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.myapplication.bean.NoteBean;
import com.android.myapplication.db.NoteDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MainTab02 extends Fragment {
    private EditText et_new_title;
    private EditText et_new_content;
    private TextView tv_time;
    private Spinner spinner;
    private NoteDao noteDao;
    private NoteBean note;
    private int myID;
    private String myTitle;
    private String myContent;
    private String myCreate_time;
    private String myUpdate_time;
    private String mySelect_time;
    private String myType;
    private Calendar calendar;
    private String login_user;
    View inflate;
    private int flag;//区分是新建还是修改

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.main_tab_02, container, false);
        initView();
        return inflate;
    }

    public void initView() {
        et_new_title = (EditText) inflate.findViewById(R.id.et_new_title);
        et_new_content = (EditText) inflate.findViewById(R.id.et_new_content);
        tv_time = (TextView) inflate.findViewById(R.id.tv_remindtime);
        spinner = (Spinner) inflate.findViewById(R.id.type_select);
        login_user = getActivity().getSharedPreferences("login", MODE_PRIVATE).getString("login_user", "");

        myCreate_time = getNowTime();
        myUpdate_time = getNowTime();
        inflate.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存数据到数据库
                saveNoteDate();
            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });
    }

    /**
     * 格式化时间
     *
     * @return
     */
    private String getNowTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 显示时间弹框
     */
    private void selectTime() {


        calendar = Calendar.getInstance();
        DatePickerDialog dpdialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv_time.setText(format.format(calendar.getTime()));
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

    /**
     * 保存数据
     */
    private void saveNoteDate() {
        String noteremindTime = tv_time.getText().toString();
        if (noteremindTime.equals("点击设置完成时间")) {
            Toast.makeText(getContext(), "请设置备忘事件的完成时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String noteTitle = et_new_title.getText().toString();
        if (noteTitle.length() > 14) {
            Toast.makeText(getContext(), "标题长度应在15字以下", Toast.LENGTH_SHORT).show();
            return;
        } else if (noteTitle.isEmpty()) {
            Toast.makeText(getContext(), "标题内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String noteContent = et_new_content.getText().toString();
        if (noteContent.isEmpty()) {
            Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String notecreateTime = myCreate_time;
        String noteupdateTime = getNowTime();
        int noteID = myID;
        if (note == null) {
            note = new NoteBean();
        }
        noteDao = new NoteDao(getContext());
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setUpdateTime(noteupdateTime);
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        note.setYear(calendar.get(Calendar.YEAR) + "");
        note.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
        note.setDay(calendar.get(Calendar.DAY_OF_MONTH) + "");
        note.setRemindTime(noteremindTime);
        note.setType(spinner.getSelectedItem().toString());
        note.setOwner(login_user);
        //插入数据
        noteDao.insertNote(note);


    }


}
