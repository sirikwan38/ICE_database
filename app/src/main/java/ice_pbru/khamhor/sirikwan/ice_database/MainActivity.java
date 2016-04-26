package ice_pbru.khamhor.sirikwan.ice_database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //Explicit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind widget
        bindwidget();


    }//Main Method
    public void clickRegis(View view) {
        startActivity(new Intent(MainActivity.this,SignUp.class));
    }

    private void bindwidget() {

    }


}//Main class
