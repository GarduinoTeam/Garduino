package com.example.garduinoandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditTextConditionAdapter extends BaseAdapter
{
    Context context;
    List<EditTextCondition> listObjects;
    ArrayList<EditTextCondition> arrayList;
    Button saveButton;
    EditIrrigationRule editIrrigationRule;
    View popUpDeleteView;


    public EditTextConditionAdapter(Context context, List<EditTextCondition> listObjects, EditIrrigationRule editIrrigationRule, View popUpDeleteView) {
        this.context = context;
        this.listObjects = listObjects;
        this.arrayList = new ArrayList<EditTextCondition>();
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
            for (EditTextCondition data : arrayList){
                if (data.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    listObjects.add(data);
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
        view = inflate.inflate(R.layout.edit_condition_list, null);
//        saveButton = (Button) view.findViewById(R.id.saveConditons);

        final EditText edit = (EditText) view.findViewById(R.id.editTextEditCondition);
        TextView title = (TextView) view.findViewById(R.id.textVieweEditCondition);
        TextView measure = (TextView) view.findViewById(R.id.measureEditCondition);
        Switch sw = (Switch) view.findViewById(R.id.switchEditCondition);
        Button bt = (Button) view.findViewById(R.id.deleteCondition);

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
                    String urlDelete = "http://10.0.2.2:8080/GarduinoApi/ruleconditions/delete_rule_condition/"+itemId;
                    DeletePopUp popup = new DeletePopUp();
                    popup.showPopUpEnviromentalCondition(editIrrigationRule, popUpDeleteView, EditTextConditionAdapter.this, position, urlDelete);
//                    DoDeleteTask task = new DoDeleteTask();
//                    listObjects.remove(position);
//                    task.execute(new String(urlDelete));
                }
            });
        }

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.editText = edit;
        view.setTag(viewHolder);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.textWatcher != null)
            holder.editText.removeTextChangedListener(holder.textWatcher);


        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listObjects.get(position).setEdit(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        holder.editText.addTextChangedListener(holder.textWatcher);

        title.setText(listObjects.get(position).getTitle());
        edit.setText(listObjects.get(position).getEdit());
        measure.setText(listObjects.get(position).getMeasure());

        return view;
    }

    static class ViewHolder {
        public EditText editText;
        public TextWatcher textWatcher;
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

    private void update(List<EditTextCondition> newlist)
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
