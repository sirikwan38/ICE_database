package ice_pbru.khamhor.sirikwan.ice_database;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    }//Main method

    public class SynJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("SIRIKWAN", "Response ==> " + s);
                JSONArray jsonArray = new JSONArray(s);

                String[] iconStrings = new String[jsonArray.length()];
                String[] titleStrings = new String[jsonArray.length()];
                String[] eBookStrings = new String[jsonArray.length()];

                for (int i=0; i < jsonArray.length();i++ ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    iconStrings[i] = jsonObject.getString("Cover");
                    titleStrings[i] = jsonObject.getString("Name");
                    eBookStrings[i] = jsonObject.getString("Ebook");
                }//for

                ProductAdapter productAdapter = new ProductAdapter(LoginSuscess.this, iconStrings, titleStrings);
                listView.setAdapter(productAdapter);

            } catch (Exception e) {
                Log.d("SIRIKWAN", "onPost -->" + e.toString());
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
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
    }

}//Main Class

