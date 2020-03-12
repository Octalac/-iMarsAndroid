package com.os.imars.operator.dao;

import com.os.imars.surveyor.dao.CalendarDataItem;

import java.util.Date;

public class DatesInRangeDao {

    private Date date;
    private CalendarDataItem calendarDataItem;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CalendarDataItem getCalendarDataItem() {
        return calendarDataItem;
    }

    public void setCalendarDataItem(CalendarDataItem calendarDataItem) {
        this.calendarDataItem = calendarDataItem;
    }
}
