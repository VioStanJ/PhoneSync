package ht.stanj.kontaksync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    Context context;
    ArrayList<Contact> contacts;

    public ContactAdapter(Context context,ArrayList<Contact> contacts) {
        super(context,R.layout.contact_row,contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.contact_row,parent,false);

        ImageView status = row.findViewById(R.id.status);
        TextView name = row.findViewById(R.id.name);
        TextView phone = row.findViewById(R.id.phone);

        final Contact contact = contacts.get(position);

        name.setText(contact.getName());
        phone.setText(contact.getPhone());

        if(contact.isStatus() == DBContact.SYNC_STATUS_OK){
            status.setImageResource(R.drawable.ic_check);
        }else {
            status.setImageResource(R.drawable.ic_refresh);
        }

        return row;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }
}
