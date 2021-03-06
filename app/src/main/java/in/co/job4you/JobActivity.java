package in.co.job4you;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://job4you.co.in";
    private RecyclerView mRecyclerView;
    private ArrayList<JobGetterSetter> mArrayList;
    private DataAdapter mAdapter;
    LinearLayout nointernet; SwipeRefreshLayout   mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        if (isNetworkAvailable(this)) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nointernet.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
            loadJSON();
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorPrimaryDark));
        if(isNetworkAvailable(this)) {
            new SendIPAddress().execute(new String[]{Utils.getPublicIPAddress(this)});
        }
    }
    void refreshItems() {
            onItemsLoadComplete();
        }
        void onItemsLoadComplete() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    public void tryagain(View v)
    {
        if(isNetworkAvailable(this)) {
            nointernet.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(true);
            loadJSON();
        }
        else{
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private void initViews() {
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.mSwipeRefreshLayout);
        nointernet=(LinearLayout)findViewById(R.id.nointernet);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadJSON() {

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getJob()));
                mAdapter = new DataAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(isNetworkAvailable(JobActivity.this)) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }
    class SendIPAddress extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            BufferedReader inBuffer = null;
            String url =BASE_URL+"/api/tracking";
            String result = "fail";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("userIp", params[0]));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                httpClient.execute(request);
                result="got it";
            } catch(Exception e) {
                // Do something about exceptions
                result = e.getMessage();
            } finally {
                if (inBuffer != null) {

                }
            }
            return  result;
        }

        protected void onPostExecute(String page)
        {
            //textView.setText(page);
            Log.e("Response",page.toString());
            Toast toast = Toast.makeText(getApplicationContext(), page, Toast.LENGTH_SHORT);
            //toast.show();
        }
    }
}