package com.example.garduinoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    String jsonStr;

    ListView listView;
    String[] labelListItems;
    String[] descriptionItems;
    ArrayList<Data> dataArrayList;
    ArrayList<Data> devicesList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crida get a un endpoint per tal d'agafar les dades dels devices (image, device i description)

        listView = (ListView) findViewById(R.id.listData);
        labelListItems = getResources().getStringArray(R.array.devicesArray);
        descriptionItems = getResources().getStringArray(R.array.descriptionArray);

        dataArrayList = new ArrayList<Data>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("https://api.androidhive.info/contacts/");
            }
        });
        // fill the Array using Call Get Https

//        dataArrayList.add(new Data(1, labelListItems[0], descriptionItems[0], R.drawable.plant1 ));
//        dataArrayList.add(new Data(2, labelListItems[1], descriptionItems[1], R.drawable.plant2 ));
//        dataArrayList.add(new Data(3, labelListItems[2], descriptionItems[2], R.drawable.plant3 ));
//        dataArrayList.add(new Data(4, labelListItems[3], descriptionItems[0], R.drawable.plant4 ));
//
//        adapter = new Adapter(getApplicationContext(), dataArrayList);
//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data obj = (Data) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DeviceProfile.class);
                intent.putExtra("object", (Serializable) obj);
                Intent i = new Intent(getApplicationContext(), DeviceProfileStart.class);
                i.putExtra("object", (Serializable) obj);
                startActivity(intent);
            }
        });


    }

    // CREATING MENU AND MANAGING MENU OPTIONS

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search name device...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(newText);
                }
                return  true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_search) return true;

        return super.onOptionsItemSelected(item);

    }

    // CREATING FUNCTIONS AND CLASS IN ORDER TO GET DATA FROM AN ENDPOINT
    // AND MANAGING DATA FROM JSON IN ORDER TO ADAPT TO A LISTVIEW
    private ArrayList<Data> createList(String jsonStr)
    {
        devicesList = new ArrayList<Data>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array
                JSONArray contacts = jsonObject.getJSONArray("contacts");

                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");

                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    String home = phone.getString("home");

                    // tmp hash map for single contact
                    ArrayList<Data> contact = new ArrayList<Data>();

                    // adding each child node to HashMap key => value
                    contact.add(new Data(1, name, email, "https://img-cdn.hipertextual.com/files/2019/03/hipertextual-whatsapp-permitira-realizar-busqueda-inversa-imagenes-recibidas-combatir-fake-news-2019852284.jpg?strip=all&lossy=1&quality=57&resize=740%2C490&ssl=1"));
                    //System.out.println();

                    // adding contact to devicesList
                    devicesList.addAll(contact);

                }
                return devicesList;

               // System.out.println(devicesList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else Toast.makeText(this, "Couldn't get json from file", Toast.LENGTH_SHORT).show();
        return null;
    }

    private class ReadJSON extends AsyncTask<String, Integer, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<Data> result;
            ArrayList<Data> devices = new ArrayList<Data>();
            if(jsonStr != null)
            {
                result = createList(jsonStr);

                adapter = new Adapter(getApplicationContext(), result);
                listView.setAdapter(adapter);

//
//                for(Data dataDevice: result) {
//                    System.out.println(dataDevice.getTitle());
//                    System.out.println("***************");
//                }
//
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }
    }

    private String readURL(String urlContacts)
    {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        InputStreamReader isReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlContacts);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            inputStream = conn.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if(inputStream == null){
                // Nothing to do
                return null;
            }
            isReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(isReader);
            String line;

            while((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
            {
                // Stream was empty. No point in parsing.
                return null;
            }

            jsonStr = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){ conn.disconnect(); }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonStr;
    }
}
