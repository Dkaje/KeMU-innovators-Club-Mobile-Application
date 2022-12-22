package com.example.kemussit.Orggen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kemussit.Kitendawili.Connect;
import com.example.kemussit.Kitendawili.PrintF;
import com.example.kemussit.Kitendawili.SahauAda;
import com.example.kemussit.Kitendawili.SahauMode;
import com.example.kemussit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ViewOldage extends AppCompatActivity {
    SahauAda eventAda;
    SahauMode evenMdel;
    ArrayList<SahauMode> evenMdelArrayList = new ArrayList<>();
    ListView listView;
    SearchView searchView;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Past Events Participation");
        setContentView(R.layout.activity_view_oldage);
        listView = findViewById(R.id.listView);
        listView.setTextFilterEnabled(true);
        searchView = findViewById(R.id.seacrhed);
        getCard();
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            evenMdel = (SahauMode) adapterView.getItemAtPosition(i);
            startActivity(new Intent(getApplicationContext(), OldageView.class).putExtra("theme", evenMdel.getTheme()).putExtra("venue", evenMdel.getVenue()).putExtra("club", evenMdel.getClub()));
        });
    }

    private void getCard() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(new StringRequest(Request.Method.POST, Connect.general,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        Log.e("response ", response);
                        int success = jsonObject.getInt("trust");
                        if (success == 1) {
                            jsonArray = jsonObject.getJSONArray("victory");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                evenMdel = new SahauMode(jsonObject.getString("theme"),
                                        jsonObject.getString("venue"), jsonObject.getString("club"), jsonObject.getString("counter"));
                                evenMdelArrayList.add(evenMdel);
                            }
                            eventAda = new SahauAda(ViewOldage.this, R.layout.russia, evenMdelArrayList);
                            listView.setAdapter(eventAda);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String text) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    eventAda.getFilter().filter(newText);
                                    return false;
                                }
                            });
                            findViewById(R.id.btnPrint).setVisibility(View.VISIBLE);
                            findViewById(R.id.btnPrint).setOnClickListener(view -> {
                                PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
                                printManager.print(getString(R.string.app_name), new PrintF(this, findViewById(R.id.Printer)), null);
                            });
                        } else if (success == 0) {
                            String msg = jsonObject.getString("mine");
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
            Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
        }));
    }
}