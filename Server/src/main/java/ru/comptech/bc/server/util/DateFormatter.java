package ru.comptech.bc.server.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateFormatter {

  final static DateFormat dateFormat;
  static {
    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  public static String dateToString(Date date) {
    return dateFormat.format(date);
  }

  public static Date stringToData(String iso) throws ParseException {
    return dateFormat.parse(iso);
  }
}
