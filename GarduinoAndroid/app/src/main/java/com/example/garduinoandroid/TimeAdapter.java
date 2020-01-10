package com.example.garduinoandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimeAdapter extends BaseAdapter {
    Context context;
    List<Rule> listObjects;
    ArrayList<Rule> arrayList;
    EditIrrigationRule editIrrigationRule;
    View popUpDeleteView;

    public TimeAdapter(Context context, List<Rule> listObjects, EditIrrigationRule editIrrigationRule, View popUpDeleteView) {
        this.context = context;
        this.listObjects = listObjects;
        this.arrayList = new ArrayList<Rule>();
        this.arrayList.addAll(listObjects);
        this.editIrrigationRule = editIrrigationRule;
        this.popUpDeleteView = popUpDeleteView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listObjects.clear();
        if (charText.length()==0){
            listObjects.addAll(arrayList);
        }
        else {
            for (Rule rule : arrayList){
                if (rule.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    listObjects.add(rule);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return listObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listObjects.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        LayoutInflater inflate = LayoutInflater.from(context);
        view = inflate.inflate(R.layout.rules_list, null);

        TextView title = (TextView) view.findViewById(R.id.textIrrigationRule);
        Switch sw = (Switch) view.findViewById(R.id.RuleActive);
        Button bt = (Button) view.findViewById(R.id.deleteRules);
        bt.setTag(position);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(bt != null) {
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long itemId = getItemId(position);
                    String urlDelete = "http://10.0.2.2:8080/GarduinoApi/ruletimeconditions/delete_rule_time_condition/"+itemId;
                    DeletePopUp popup = new DeletePopUp();
                    popup.showPopUpTimeCondition(editIrrigationRule, popUpDeleteView, TimeAdapter.this, position, urlDelete);
//                    DoDeleteTask task = new DoDeleteTask();
//                    listObjects.remove(position);
//                    task.execute(new String(urlDelete));
                }
            });
        }

        title.setText(listObjects.get(position).getTitle());
        return view;
    }
    private class DoDeleteTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            update(listObjects);
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            HttpURLConnection conn = null;
            InputStream inputStream = null;
            InputStreamReader inputReader = null;
            BufferedReader reader = null;

            for (String url : urls) {
                try {
                    URL myUrl = new URL(url);
                    conn = (HttpURLConnection) myUrl.openConnection();
                    conn.setRequestMethod("DELETE");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");

                    OutputStream os = conn.getOutputStream();

                    os.flush();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = conn.getInputStream();
                        inputReader = new InputStreamReader(inputStream);
                        BufferedReader buffer = new BufferedReader(inputReader);

                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.out.println(response);
            return response;
        }
    }

    private void update(List<Rule> newlist)
    {
        //listObjects.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public void doPositiveClick(int position, String urlDelete){
        DoDeleteTask task = new DoDeleteTask();
        listObjects.remove(position);
        task.execute(new String(urlDelete));
    }
}
