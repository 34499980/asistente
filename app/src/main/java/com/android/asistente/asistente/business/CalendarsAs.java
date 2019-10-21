package com.android.asistente.asistente.business;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteservice;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;





public class CalendarsAs {
    static Map<String, String> messages = new HashMap<String, String>();

    String CLINTID = "756261266614-s9gtom27puq9ag5n73igirq74472f3e1.apps.googleusercontent.com";
    String KEY = "mgUAx8GpQL5UTVPlxCgWT0zf";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String APPLICATION_NAME = "Asistente";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "756261266614-s9gtom27puq9ag5n73igirq74472f3e1.apps.googleusercontent.com";
    public static final String[] FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    static ContentResolver contentResolver;
    static Set<String> calendars = new HashSet<String>();







    public static void getCalendar() {
        try {
            if(messages.isEmpty()) {
                final String DEBUG_TAG = "MyActivity";
                final String[] INSTANCE_PROJECTION = new String[]{
                        CalendarContract.Instances.EVENT_ID,      // 0
                        CalendarContract.Instances.BEGIN,         // 1
                        CalendarContract.Instances.TITLE          // 2
                };

                ContentResolver cr = asistenteservice.getContext().getContentResolver();

                String selection = CalendarContract.Instances.EVENT_ID + " = ?";

                Cursor cur = null;
                final String[] EVENT_PROJECTION =
                        new String[]{
                                /*CalendarContract.Calendars.NAME,
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                                CalendarContract.Calendars.CALENDAR_COLOR,
                                CalendarContract.Calendars.VISIBLE           // 3*/
                                "_id", "calendar_displayName"
                        };
                Uri uri = CalendarContract.Calendars.CONTENT_URI;
                selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
                String[] selectionArgs = new String[]{"aribrenman@gmail.com", "com.google"};
// Submit the query and get a Cursor object back.
                cur = cr.query(uri, EVENT_PROJECTION, null, null, null);


// Submit the query
                final int PROJECTION_ID_INDEX = 0;
                final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
                final int PROJECTION_DISPLAY_NAME_INDEX = 1;
                final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
                HashSet<Long> calendarIds = new HashSet<Long>();
                long calID = 0;
                while (cur.moveToNext()) {
                    String displayName = null;
                    String accountName = null;
                    String ownerName = null;

                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    //accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    //ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                    calendarIds.add(calID);
                }
                cur.close();
                //Fecha uno
                Date date1 = new Date();

                long time1 = date1.getTime();
                //Fecha dos
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.MONTH, 3);


                long time2 = cal.getTimeInMillis();

               // String selectionEvent = "((dtstart >= " + time1 + "))";
                String selectionEvent = "((dtstart >= " + time1 + ") AND (dtend <= " + time2 + "))";
                for (Long id : calendarIds) {
                    Uri.Builder builder = Uri.parse("content://com.android.calendar/events").buildUpon();
                    //Uri.Builder builder = Uri.parse("content://com.android.calendar/calendars").buildUpon();
                    Cursor eventCursor = cr.query(builder.build(),
                            new String[]{"title", "description", "eventLocation", "dtstart", "dtend"}, selectionEvent,
                            null, "dtstart");
                    while (eventCursor.moveToNext()) {
                        String title = eventCursor.getString(0);
                        String description = eventCursor.getString(1);
                        String eventLocation = eventCursor.getString(2);
                        String start = eventCursor.getString(3);
                        Date mDate = General.addDays(new Date(eventCursor.getLong(3)),1);
                        Date nDate = new Date(eventCursor.getLong(4));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
                        String sDate = simpleDateFormat.format(mDate);
                        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MMMM");
                        String smonth = simpleDateFormatMonth.format(mDate);

                        SimpleDateFormat simpleDateformatDay = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
                        String day = simpleDateformatDay.format(mDate);
                        switch (simpleDateformatDay.format(mDate).toLowerCase()){
                            case"monday":
                                day = "lunes";
                                break;
                            case"tuesday":
                                day = "martes";
                                    break;
                            case"wednesday":
                                day = "miercoles";
                                break;
                            case"thursday":
                                day = "jueves";
                                break;
                            case"friday":
                                day = "viernes";
                                break;
                            case"saturday":
                                day = "sabado";
                                break;
                            case"sunday":
                                day = "domingo";
                                break;

                        }
                        messages.put(title, day+" "+sDate+" de "+smonth);
                        if(!title.toLowerCase().contains("puente")){
                            break;
                        }


                    }

                    eventCursor.close();
                }
            }
           boolean flag;
            String afterTitle="";
            String afterDate="";
            for(Map.Entry<String, String> entry : messages.entrySet()) {
                if(entry.getKey().contains("Puente")) {
                     afterDate = entry.getValue();
                     afterTitle = entry.getKey();

                }else{
                    if(afterTitle =="")
                    {
                        TTSService.speak("El proximo feriado es el: " + entry.getValue() + " por " + entry.getKey());
                    }else{
                        TTSService.speak("El proximo feriado es el: " + afterDate + " por " +afterTitle + " del " + entry.getKey());

                    }
                    break;
                }


            }




        } catch (Exception ex) {
            Log.appendLog("Calendar: "+ex.getMessage());
        }
    }




}