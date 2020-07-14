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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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

    public void saveData(){

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Contact contact = new Contact(0,name.getText().toString(),phone.getText().toString(),false);

        if(checkNetwork()){

        }else{
            dbHelper.save(database,contact);
        }

        loadData();
        database.close();
        dbHelper.close();
        dialog.dismiss();

        Toast.makeText(getApplicationContext(),"Saved !",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.open:
                loadData();
                dialog();
            break;
            case R.id.save:
                saveData();
                break;
        }
    }
}
