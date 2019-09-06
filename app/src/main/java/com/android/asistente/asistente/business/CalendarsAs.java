package com.android.asistente.asistente.business;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarsAs {
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

    public ContentResolver CalendarContentResolver(Context ctx) {
        return contentResolver = ctx.getContentResolver();
    }

    public static Set<String> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = asistenteservice.getContext().getContentResolver().query(CALENDAR_URI, FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    calendars.add(displayName);
                }
            }
        } catch (AssertionError ex) {
            Log.appendLog("Calendar: " + ex.getMessage());
        }

        return calendars;
    }

    public static void getEvent() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarsAs.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void prueba(){
       /* private com.google.api.services.calendar.Calendar mService = null;
        private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {     private Exception mLastError = null;
            private boolean FLAG = false;     public MakeRequestTask(GoogleAccountCredential credential) {
                HttpTransport transport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                mService = new com.google.api.services.calendar.Calendar.Builder(
                        transport, jsonFactory, credential)
                        .setApplicationName(“Google Calendar API Android Quickstart”)
                        .build();
            }
             * Background task to call Google Calendar API.
             * @param params no parameters needed for this task.

            @Override
            protected List<String> doInBackground(Void… params) {
                try {
                    getDataFromApi();
                } catch (Exception e) {
                    e.printStackTrace();
                    mLastError = e;
                    cancel(true);
                    return null;
                }
                return null;
            }
             * Fetch a list of the next 10 events from the primary calendar.
             * @return List of Strings describing returned events.
             * @throws IOException

            private void getDataFromApi() throws IOException {
                // List the next 10 events from the primary calendar.
                DateTime now = new DateTime(System.currentTimeMillis());
                List<String> eventStrings = new ArrayList<String>();
                Events events = mService.events().list(“primary”)
                        .setMaxResults(10)
                        .setTimeMin(now)
                        .setOrderBy(“startTime”)
                        .setSingleEvents(true)
                        .execute();
                List<Event> items = events.getItems();
                ScheduledEvents scheduledEvents;
                scheduledEventsList.clear();
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    scheduledEvents = new ScheduledEvents();
                    scheduledEvents.setEventId(event.getId());
                    scheduledEvents.setDescription(event.getDescription());
                    scheduledEvents.setEventSummery(event.getSummary());
                    scheduledEvents.setLocation(event.getLocation());
                    scheduledEvents.setStartDate(start.toString());
                    scheduledEvents.setEndDate(“”);
                    StringBuffer stringBuffer = new StringBuffer();
                    if(event.getAttendees()!=null) {
                        for (EventAttendee eventAttendee : event.getAttendees()) {
                            if(eventAttendee.getEmail()!=null)
                                stringBuffer.append(eventAttendee.getEmail() + ”       “);
                        }
                        scheduledEvents.setAttendees(stringBuffer.toString());
                    }
                    else{
                        scheduledEvents.setAttendees(“”);
                    }
                    scheduledEventsList.add(scheduledEvents);
                    System.out.println(“—–“+event.getDescription()+”, “+event.getId()+”, “+event.getLocation());
                    System.out.println(event.getAttendees());
                    eventStrings.add(
                            String.format(“%s (%s)”, event.getSummary(), start));
                }
            }     @Override
            protected void onPreExecute() {
                mOutputText.setText(“”);
                mProgress.show();
            }     @Override
            protected void onPostExecute(List<String> output) {
                mProgress.hide();
                System.out.println(“——————–“+scheduledEventsList.size());
                if (scheduledEventsList.size()<=0) {
                    mOutputText.setText(“No results returned.”);
                } else {
                    eventListAdapter = new EventListAdapter(CalendarActivity.this, scheduledEventsList);
                    eventListView.setAdapter(eventListAdapter);
                }
            }     @Override
            protected void onCancelled() {
                mProgress.hide();
                if (mLastError != null) {
                    if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                        showGooglePlayServicesAvailabilityErrorDialog(
                                ((GooglePlayServicesAvailabilityIOException) mLastError)
                                        .getConnectionStatusCode());
                    } else if (mLastError instanceof UserRecoverableAuthIOException) {
                        startActivityForResult(
                                ((UserRecoverableAuthIOException) mLastError).getIntent(),
                                CalendarActivity.REQUEST_AUTHORIZATION);
                    } else {
                        mOutputText.setText(“The following error occurred:\n”
                        + mLastError.getMessage());
                    }
                } else {
                    mOutputText.setText(“Request cancelled.”);
                }
            }
        }*/
    }




}