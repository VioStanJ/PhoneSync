package ht.stanj.kontaksync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {

    private ArrayList<Contact> data = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            DBHelper db = new DBHelper(context);
            SQLiteDatabase database = db.getWritableDatabase();

            for (Contact contact:db.getAll()) {
                if(!contact.isStatus()){
                    Log.d("DATA_ID",""+contact.getId());
                    saveToServer(context,contact,db,database);
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void saveToServer(final Context context, final Contact contact, final DBHelper helper, final SQLiteDatabase database){

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, DBContact.SERVER_URL+"/create", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DATA",response.toString());
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getBoolean("success")){
                            contact.setStatus(true);
                            helper.update(database,contact);
                            context.sendBroadcast(new Intent(DBContact.UI_UPDATE));
                        }
                    } catch (JSONException e) {
                        Log.d("DATA_ERR",e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DATA_ERR_V",error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("name", contact.getName());
                    params.put("phone", contact.getPhone());

                    return params;
                }
            };
            queue.add(request);
            
        Toast.makeText(context,"Saved !",Toast.LENGTH_SHORT).show();
    }
}
