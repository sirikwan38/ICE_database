package ice_pbru.khamhor.sirikwan.ice_database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //Explicit
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private MySQLite mySQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind widget
        bindwidget();
        mySQLite = new MySQLite(this);

        //Synchronize mySQL
        synAndDelete();


    }//Main Method

    private void synAndDelete() {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name, MODE_PRIVATE, null);

        sqLiteDatabase.delete(MySQLite.user_table, null, null);

        MySynJSON mySynJSON = new MySynJSON();
        mySynJSON.execute();
    }

    public class MySynJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                String strURL = "http://ice.pbru.ac.th/ICE56/sirikwan/php_get_user.php";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                Log.d("SIRIKWAN -->", "doInback" + e.toString());
                return null;
            }

        }//doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("SIRIKWAN -->", "strJSON -->" + s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strName = jsonObject.getString(MySQLite.colum_Name);
                    String strSurname = jsonObject.getString(MySQLite.colum_Surname);
                    String strUser = jsonObject.getString(MySQLite.colum_User);
                    String strPassword = jsonObject.getString(MySQLite.colum_Password);
                    String strEmail = jsonObject.getString(MySQLite.colum_Email);

                    mySQLite.addNewUser(strName, strSurname, strUser, strPassword, strEmail);

                    Log.d("Sirikwan -->", "Name = " + strName.toString());

                }
                Toast.makeText(MainActivity.this,"Synchronize mySQL to SQLite Finish",
                        Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("SIRIKWAN -->", "onPost -->" + e.toString());
            }


        }//onPostExecute
    }//MySynJSON

    public void clickIn(View view) {
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (checkSpace()) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "มีช่องว่าง", "กรุณาตรวจสอบข้อมูล");

        } else {
            //no space

            checkUserAndPassword();

        }
    }//click signin

    private void checkUserAndPassword() {
        try {
            SQLiteDatabase sqliteDatabase = openOrCreateDatabase(MyOpenHelper.database_name, MODE_PRIVATE, null);
            Cursor cursor = sqliteDatabase.rawQuery("SELECT * FROM userTABLE WHERE User = +" +
                    "'" + userString + "'", null);
            cursor.moveToFirst();

            String[] resultStrings = new String[cursor.getColumnCount()];
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                resultStrings[i] = cursor.getString(i);
            }
            cursor.close();
            //check password
            if (passwordString.equals(resultStrings[4])) {
                Toast.makeText(this, "ยินดีต้อนรับ" + resultStrings[1], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginSuscess.class);
                intent.putExtra("Result", resultStrings);
                startActivity(intent);
                finish();

            } else {
                MyAlert myAlert = new MyAlert();
                myAlert.myDialog(this,"Wrong password", "กรุณากรอกรหัสผ่านอีกครั้ง");
            }

        } catch (Exception e) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this,"User error", "ไม่มีชื่อผู้ใช้งานในฐานข้อมูล");

        }
    }//check user and password

    private boolean checkSpace() {
        return
        userString.equals("") ||
                passwordString.equals("");
    }

    public void clickRegis(View view) {
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }

    public void testMyAlert(View view) {
        MyAlert myAlert = new MyAlert();
        myAlert.myDialog(this,"Alert","ทดสอบ");
    }

    private void bindwidget() {
        userEditText = (EditText) findViewById(R.id.editText6);
        passwordEditText = (EditText) findViewById(R.id.editText7);

    }


}//Main class
