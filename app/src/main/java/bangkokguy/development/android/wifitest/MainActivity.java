package bangkokguy.development.android.wifitest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final boolean DEBUG = false;

    private final static String EVENT_HISTORY = "event_history";

    ArrayList<String> logEntries;

    ListView listView;
    ListViewAdapter logEntriesAdapter;


    View.OnClickListener onClick = new MyOnClickListener();
    ToggleButton tb;
    SharedPreferences eventHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = findViewById(R.id.toggleButton);
        tb.setOnClickListener(onClick);

        logEntries = new ArrayList<>();

        logEntriesAdapter = new ListViewAdapter(this, logEntries);

        listView = findViewById(R.id.main_list);
        listView.setAdapter(logEntriesAdapter);

        startService(new Intent(this, NetworkWatchdog.class));

        eventHistory = getSharedPreferences(EVENT_HISTORY, MODE_PRIVATE);
        eventHistory.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                logEntriesAdapter.appendItem (sharedPreferences.getString(s, "--"));
                //listView.invalidate();
                listView.smoothScrollToPosition(listView.getCount()-1);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DEBUG)Log.d(TAG, "onResume");
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final String TAG = MainActivity.TAG+"."+MyOnClickListener.class.getSimpleName();

        @Override
        public void onClick(View view) {
            if(DEBUG)Log.d(TAG, "onClick");
            //listView.invalidate();
        }
    }

    class ListViewAdapter extends BaseAdapter {

        ArrayList<String> list;
        Activity activity;
        LayoutInflater inflater;

        int numberOfEntries;

        ListViewAdapter(Activity activity, ArrayList<String> list) {
            super();
            this.activity = activity;
            this.list = list;
            this.numberOfEntries = 0;
            inflater = activity.getLayoutInflater();
        }

        void appendItem (String secondColumn) {
            list.add(numberOfEntries++, secondColumn);
            notifyDataSetChanged();
            //notifyDataSetInvalidated();
        }

        @Override
        public int getCount() {
            if(DEBUG)Log.i(TAG, "in getCount " + Integer.toString(numberOfEntries));
            return numberOfEntries;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // inflate and init the view which is used to prototype the list columns
            if (convertView==null)
                convertView = inflater.inflate(R.layout.log_entries_columns, null);
            TextView firstColumn = convertView.findViewById(R.id.name);
            TextView secondColumn = convertView.findViewById(R.id.gender);

            if (!(list==null)) {
                if(DEBUG)Log.i(TAG, "!(list==null)");
                firstColumn.setText(Integer.toString(position));
                secondColumn.setText(list.get(position));
            }
            return convertView;
        }
    }

    @Override
    protected void onStart() {super.onStart(); if(DEBUG)Log.d(TAG, "onStart"); }
    @Override
    protected void onStop() {super.onStop(); if(DEBUG)Log.d(TAG, "onStop"); }
    @Override
    protected void onPause() {super.onPause(); if(DEBUG)Log.d(TAG, "onPause"); }
    @Override
    protected void onRestart() {super.onRestart(); if(DEBUG)Log.d(TAG, "onRestart"); }
}
