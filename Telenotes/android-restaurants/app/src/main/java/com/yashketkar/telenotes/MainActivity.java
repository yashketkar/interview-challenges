package com.yashketkar.telenotes;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.factual.driver.Factual;
import com.factual.driver.FactualApiException;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static SearchView search;
    static RecyclerView mRecyclerView;
    static TextView emptyTextView;
    static ArrayList<Restaurant> resto;

    protected Factual factual = new Factual("UDaT2LyFxvFrgcwzbVSiNBD9ShiyOKaTEgNYCxWK", "lRqgTVucvgCWYYLuTDLB5eeUx60FRB0ptDYZFErW");
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static String filterRestaurantName;
    private static String filterCuisineType;
    private static String filterRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filterRestaurantName="";
        filterCuisineType="";
        filterRating="0";
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        search=(SearchView) findViewById(R.id.searchView1);
        search.setQueryHint("Enter City");
        search.setIconifiedByDefault(false);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        boolean val=searchManager.getSearchableInfo(getComponentName())==null;
        search.setSubmitButtonEnabled (false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;//return true so that the menu pop up is opened
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.filter:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Set Search Filters");
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_filter, null);
                alert.setView(dialogView);
                final EditText text1= (EditText) dialogView.findViewById(R.id.RestName);
                text1.setText(filterRestaurantName);
                final EditText text2= (EditText) dialogView.findViewById(R.id.CuisineType);
                text2.setText(filterCuisineType);
                final RatingBar rating1 = (RatingBar) dialogView.findViewById(R.id.RatingValue);
                rating1.setRating(Float.parseFloat(filterRating));
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filterRestaurantName=text1.getText().toString();
                        filterCuisineType=text2.getText().toString();
                        filterRating=rating1.getRating()+"";
                        Toast.makeText(getBaseContext(), "Filter criteria set ", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        if(!search.getQuery().toString().equals(""))
                        {
                            doQuery(search.getQuery().toString());
                        }
                    }
                });
                alert.show();
                break;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search.setQuery(query, false);
            try {
                doQuery(query);
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            catch (FactualApiException e)
            {
                Toast.makeText(getBaseContext(), "Factual API Exception (Daily Limit Exceeded): "  + e.getStatusMessage(), Toast.LENGTH_SHORT).show();
                ;
            }
        }
    }

    void doQuery(String query)
    {
        FactualRetrievalTask task = new FactualRetrievalTask();
        Query myquery = new Query()
        .field("locality").isEqual(query)
        .field("name").beginsWith(filterRestaurantName)
        .limit(50);
        if(!filterCuisineType.equals(""))
        {
            myquery.field("cuisine").includes(filterCuisineType);
        }

        if(Float.parseFloat(filterRating)!=0)
        {
            myquery.field("rating").isEqual(Float.parseFloat(filterRating));
        }
        task.execute(myquery);
    }

    protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {

        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_loading);
            pb.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
        }

        @Override
        protected List<ReadResponse> doInBackground(Query... params) {
            List<ReadResponse> results = Lists.newArrayList();
            List<ReadResponse> results2 = Lists.newArrayList();
            for (Query q : params) {
                results.add(factual.fetch("restaurants-us", q));
            }
            for (ReadResponse restaurant: results)
            {
                if (!results2.contains(restaurant))
                {
                    results2.add(restaurant);
                }
            }
            return results2;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(List<ReadResponse> responses) {
            StringBuffer sb = new StringBuffer();
            String cuisine="";
            resto = new ArrayList<Restaurant>();

            Restaurant r;

            for (ReadResponse response : responses) {
                for (Map<String, Object> restaurant : response.getData()) {
                    String name = (String) restaurant.get("name");
                    String address = (String) restaurant.get("address");
                    String rating;
                    if(restaurant.get("rating")!=null)
                    {
                        rating = restaurant.get("rating").toString();
                    }
                    else
                    {
                        rating = "0";
                    }
                    org.json.JSONArray type = (org.json.JSONArray) restaurant.get("cuisine");
                    if(type!=null) {
                            try {
                                sb.append(type.getString(0) + " ");
                                cuisine=type.getString(0);

                            } catch (org.json.JSONException e) {
                            }
                    }
                    String tel= (String) restaurant.get("tel");
                    String email= (String) restaurant.get("email");
                    String website= (String) restaurant.get("website");
                    double latitude = (double) restaurant.get("latitude");
                    double longitude = (double) restaurant.get("longitude");

                    r=new Restaurant();

                    r.setmName(name);
                    r.setmAddress(address);
                    r.setmRating(rating);
                    r.setmType(cuisine);
                    r.setMtel(tel);
                    r.setMemail(email);
                    r.setMwebsite(website);
                    r.setMlatitude(latitude);
                    r.setMlongitude(longitude);
                    resto.add(r);
                }
            }
            if (resto.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
            mAdapter = new MyAdapter(resto,MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_loading);
            pb.setVisibility(View.GONE);
        }
    }
}