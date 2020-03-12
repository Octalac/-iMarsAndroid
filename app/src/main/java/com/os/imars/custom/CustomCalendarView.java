package com.os.imars.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.os.imars.R;
import com.os.imars.operator.dao.DatesInRangeDao;
import com.os.imars.surveyor.adapter.CalendarAdapter;
import com.os.imars.surveyor.dao.CalendarDataItem;
import com.os.imars.surveyor.dao.CalenderDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomCalendarView extends LinearLayout {

    private static final String TAG = CustomCalendarView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    public CalendarAdapter mAdapter;
    public List<CalenderDao> dayValueInCells;
    HashMap<String, String> hashMap;
    private int isActive = 0;
    public List<CalendarDataItem> calendarDataItemList;
    private View.OnClickListener onClickListener;
    List<DatesInRangeDao> datesInRange = new ArrayList<>();

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {
        dayValueInCells = new ArrayList<>();
        calendarDataItemList = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_custom_calendar, this);
        previousButton = (ImageView) view.findViewById(R.id.previous_month);
        nextButton = (ImageView) view.findViewById(R.id.next_month);
        currentDate = (TextView) view.findViewById(R.id.current_date);
        calendarGridView = (GridView) view.findViewById(R.id.calendar_grid);
        mAdapter = new CalendarAdapter(context, dayValueInCells, cal, calendarDataItemList, onClickListener);
        calendarGridView.setAdapter(mAdapter);

    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter(onClickListener);
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter(onClickListener);
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.changeOnOffStatus(isActive);
            }
        });
    }

    private void setUpCalendarAdapter(OnClickListener onClickListener) {
        datesInRange.clear();
        dayValueInCells.clear();

        Log.d("1234", "datesInRange : " + datesInRange.size());
        Log.d("1234", "dayValueInCells : " + dayValueInCells.size());

        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        for (CalendarDataItem calendarDataItem : calendarDataItemList) {
            String startDate = calendarDataItem.getStart();
            String endDate = calendarDataItem.getEnd();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date startD = null, endD = null;
            try {
                startD = sdf.parse(startDate);
                endD = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startD);

/*
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endD);


            while (startCalendar.before(endCalendar)) {
                Date result = startCalendar.getTime();

                DatesInRangeDao datesInRangeDao = new DatesInRangeDao();
                datesInRangeDao.setDate(result);
                datesInRangeDao.setCalendarDataItem(calendarDataItem);

                datesInRange.add(datesInRangeDao);
                startCalendar.add(Calendar.DATE, 1);
            }*/

            Date result = startCalendar.getTime();
            DatesInRangeDao datesInRangeDao = new DatesInRangeDao();
            datesInRangeDao.setDate(result);
            datesInRangeDao.setCalendarDataItem(calendarDataItem);
            datesInRange.add(datesInRangeDao);
            Log.d("1234", "start date : " + datesInRange.size());
        }


        for (int i = 0; i < MAX_CALENDAR_COLUMN; i++) {
            CalenderDao calenderDao = new CalenderDao();
            calenderDao.setDate(mCal.getTime());
            calenderDao.setPosition(i);
            String dateInCalendar = new SimpleDateFormat("dd-MM-yyyy").format(calenderDao.getDate());

            for (int j = 0; j < datesInRange.size(); j++) {
                String dateInRange = new SimpleDateFormat("dd-MM-yyyy").format(datesInRange.get(j).getDate());
                if (dateInCalendar.equals(dateInRange)) {
                    Log.d(TAG, "setUpCalendarAdapter: " + datesInRange.get(j).getCalendarDataItem().getStatus() + datesInRange.get(j).getDate());
                    calenderDao.setSurveyType(datesInRange.get(j).getCalendarDataItem().getStatus());

                }
            }

            Map<String, Integer> hm = new HashMap<String, Integer>();
            int count = 0;
            for (CalendarDataItem calenderDao1 : calendarDataItemList) {
                if (hm.containsKey(calenderDao1.getStart())) {
                    hm.put(calenderDao1.getStart(), hm.get(calenderDao1.getStart()) + 1);
                } else {
                    hm.put(calenderDao1.getStart(), 1);
                }
            }
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                Log.d("12345","date count in map : "+entry.getKey() + " = " + entry.getValue());
                if (entry.getValue() == 2){
                    count = entry.getValue();
                    calenderDao.setCount(count);
                    break;
                }

            }

            dayValueInCells.add(calenderDao);
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new CalendarAdapter(context, dayValueInCells, cal, calendarDataItemList, onClickListener);
        calendarGridView.setAdapter(mAdapter);
    }

    public void changeStatusColor(int isActive) {
        Log.d(TAG, "changeStatusColor: " + isActive);
        if (mAdapter != null) {
            Log.d("1234", "changeStatusColor: inside");
            mAdapter.changeBackgroundColor(isActive);
        }
    }


    public void loadAdapterDataSet(List<CalendarDataItem> calendarDataItemList, OnClickListener onClickListener) {

        this.calendarDataItemList.clear();

        this.calendarDataItemList.addAll(calendarDataItemList);
        Log.d("1234", "loadAdapterDataSet: " + calendarDataItemList.size());
        this.onClickListener = onClickListener;
        setUpCalendarAdapter(onClickListener);
    }
}