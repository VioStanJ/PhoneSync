package ht.stanj.kontaksync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView contacts;
    private FloatingActionButton saveButton;

    private ArrayList<Contact> data;
    ContactAdapter adapter;

    EditText name;
    EditText phone;
    Button save;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);

        contacts = findViewById(R.id.list_contacts);
        saveButton = findViewById(R.id.open);

        data = new ArrayList<>();

        adapter = new ContactAdapter(getApplicationContext(),data);
        contacts.setAdapter(adapter);

        loadData();

        saveButton.setOnClickListener(this);

    }

    public boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void dialog(){
        dialog.setContentView(R.layout.contact_modal);
        dialog.setTitle("Contact");

        name = dialog.findViewById(R.id.name);
        phone = dialog.findViewById(R.id.phone);
        save = dialog.findViewById(R.id.save);

        save.setOnClickListener(this);

        dialog.show();
    }

    public void loadData(){

        data.clear();

        DBHelper db = new DBHelper(this);
        for (Contact contact:db.getAll()) {
            data.add(contact);
        }
    }

    public void saveToServer(){

        final Contact contact = new Contact(0,name.getText().toString(),phone.getText().toString(),false);

        if(checkNetwork()){

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://gestionah.com/api/contact/create", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DATA",response.toString());
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String data = jsonObject.getString("data");
//                        Log.d("DATA",data);
//                        Toast.makeText(getApplicationContext(),"Tes",Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(),"Err String",Toast.LENGTH_SHORT).show();
//
//                    }
                    Toast.makeText(getApplicationContext(),"Response",Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error Saved",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("name",contact.getName());
                    params.put("phone",contact.getPhone());
                    Log.d("DATA_PARAM",params.toString());
                    return super.getParams();
                }
            };
            queue.add(stringRequest);
        }else{
            saveLocal(contact);
        }

        loadData();

        dialog.dismiss();

//        Toast.makeText(getApplicationContext(),"Saved !",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.open:
                loadData();
                dialog();
            break;
            case R.id.save:
                saveToServer();
                break;
        }
    }

    public void saveLocal(Contact contact){

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        dbHelper.save(database,contact);

        database.close();
        dbHelper.close();
    }
}
