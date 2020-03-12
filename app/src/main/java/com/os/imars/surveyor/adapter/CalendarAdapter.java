package com.os.imars.surveyor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.os.imars.R;
import com.os.imars.surveyor.dao.CalendarDataItem;
import com.os.imars.surveyor.dao.CalenderDao;
import com.os.imars.utility.Session;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends ArrayAdapter {
    private static final String TAG = CalendarAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<CalenderDao> monthlyDates;
    private List<CalendarDataItem> calendarDataItemList;
    private Calendar currentDate;
    int isActive = 0;
    int isAvailable = 0;
    private static int selectedItem;
    private View.OnClickListener onClickListener;
    private Context context;
    Session session;


    public CalendarAdapter(Context context, List<CalenderDao> monthlyDates, Calendar currentDate, List<CalendarDataItem> calendarDataItemList, View.OnClickListener onClickListener) {
        super(context, R.layout.custom_calendar_single_row);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.calendarDataItemList = calendarDataItemList;
        mInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.custom_calendar_single_row, parent, false);
        }
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        RelativeLayout event_wrapper = view.findViewById(R.id.event_wrapper);

        session = Session.getInstance(context);

        CalenderDao mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate.getDate());
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);


        if (displayMonth == currentMonth && displayYear == currentYear) {
            event_wrapper.setTag(mDate);
            event_wrapper.setOnClickListener(onClickListener);

            if (isActive == 1) {
                event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                if (mDate.getSurveyType() != null) {
                    if (mDate.getSurveyType().equals("1")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                    } else if (mDate.getSurveyType().equals("0")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#BDBDBD"));
                    } else {
                        if (mDate.getCount() == 2){
                            imageView.setVisibility(View.VISIBLE);
                            imageView1.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }
                        else{
                            imageView.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }

                    }
                }
            } else if (isActive == 0) {
                event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                if (mDate.getSurveyType() != null) {
                    if (mDate.getSurveyType().equals("1")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                    } else if (mDate.getSurveyType().equals("0")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#BDBDBD"));
                    } else {
                        if (mDate.getCount() == 2){
                            imageView.setVisibility(View.VISIBLE);
                            imageView1.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }
                        else{
                            imageView.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }
                    }
                }

            }else if (isActive == 4) {
                event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                if (mDate.getSurveyType() != null) {
                    if (mDate.getSurveyType().equals("1")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                    } else if (mDate.getSurveyType().equals("0")) {
                        event_wrapper.setBackgroundColor(Color.parseColor("#BDBDBD"));
                    } else {
                        if (mDate.getCount() == 2){
                            imageView.setVisibility(View.VISIBLE);
                            imageView1.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }
                        else{
                            imageView.setVisibility(View.VISIBLE);
                            event_wrapper.setBackgroundColor(Color.parseColor("#388E3C"));
                        }
                    }
                }
            }

            if (session.getCalendarAvailabilityStatus().equals("3")){
                event_wrapper.setBackgroundColor(Color.parseColor("#BDBDBD"));
                if (mDate.getSurveyType() != null) {
                    if (mDate.getSurveyType().equals("0") || mDate.getSurveyType().equals("1")) {
                        imageView.setVisibility(View.GONE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }


        } else {
            event_wrapper.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }

        TextView cellNumber = view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

    public void changeBackgroundColor(int isActive) {
        Log.d("1234", "changeBackgroundColor: " + isActive);
        this.isActive = isActive;
        notifyDataSetChanged();
    }


    public void changeOnOffStatus(int isActive) {
        this.isActive = isActive;
        notifyDataSetChanged();
    }

}