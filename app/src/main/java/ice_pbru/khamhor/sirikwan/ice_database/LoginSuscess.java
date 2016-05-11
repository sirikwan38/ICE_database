package ice_pbru.khamhor.sirikwan.ice_database;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginSuscess extends AppCompatActivity {
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_suscess);

        listView = (ListView) findViewById(R.id.listView);
        SynJSON synJSON = new SynJSON();
        synJSON.execute();

    }//1

    public class SynJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voi) {
            try {
                String strURL = "http://ice.pbru.ac.th/ICE56/sirikwan/php_get_foodtable1.php";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("SIRIKWAN", "doIn --> " + e.toString());
                return null;
            }

        }//doInback

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("SIRIKWAN", "Response ==> " + s);
                JSONArray jsonArray = new JSONArray(s);

                final String[] iconStrings = new String[jsonArray.length()];
                String[] titleStrings = new String[jsonArray.length()];
                final String[] eBookStrings = new String[jsonArray.length()];

                for (int i=0; i < jsonArray.length();i++ ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    iconStrings[i] = jsonObject.getString("Cover");
                    titleStrings[i] = jsonObject.getString("Name");
                    eBookStrings[i] = jsonObject.getString("Ebook");
                }//for

                ProductAdapter productAdapter = new ProductAdapter(LoginSuscess.this, iconStrings, titleStrings);
                listView.setAdapter(productAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(eBookStrings[i]));
                        startActivity(intent);

                    }
                });

            } catch (Exception e) {
                Log.d("SIRIKWAN", "onPost -->" + e.toString());
            }
        }


    }

}//Main Class

