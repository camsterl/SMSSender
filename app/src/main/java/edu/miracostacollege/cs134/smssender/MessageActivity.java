package edu.miracostacollege.cs134.smssender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.miracostacollege.cs134.smssender.model.Contact;
import edu.miracostacollege.cs134.smssender.model.ContactsAdapter;
import edu.miracostacollege.cs134.smssender.model.DBHelper;

public class MessageActivity extends AppCompatActivity {

    private ArrayList<Contact> contactsList;
   private ContactsAdapter contactsAdapter;
    private DBHelper db;
    private ListView contactsListView;
    private EditText messageEditText;
    private Button sendTextMessageButton;

    public static final int REQUEST_CONTACT = 404;
    public static final int REQUEST_SMS = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        db = new DBHelper(this);
        contactsList = db.getAllContacts();
        contactsAdapter = new ContactsAdapter(this, R.layout.contact_list_item, contactsList);
        contactsListView = findViewById(R.id.contactsListView);
        contactsListView.setAdapter(contactsAdapter);

        messageEditText = findViewById(R.id.messageEditText);
        sendTextMessageButton = findViewById(R.id.sendTextMessageButton);
    }

    public void addContacts(View view) {
        // TODO: Start an activity for intent to pick a contact from the device.
        Intent contactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        startActivityForResult(contactsIntent, REQUEST_CONTACT);

    }

    // TODO: Overload (create) the onActivityResult() method, get the contactData,


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CONTACT)
        {
            if(resultCode == RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);

                if(cursor.moveToFirst())
                {
                    // TODO: resolve the content and create a new Contact object from the name and phone number.
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact newContact = new Contact(name, number);
                    // TODO: Add the new contact to the database and the contactsAdapter.
                    db.addContact(newContact);
                    contactsAdapter.add(newContact);
                }
            }
        }
    }





    public void deleteContact(View view) {
        // TODO: Delete the selected contact from the database and remove the contact from the contactsAdapter.

    }

    public void sendTextMessage(View view) {

        // TODO: Get the default SmsManager, then send a text message to each of the contacts in the list.
        // TODO: Be sure to check for permissions to SEND_SMS and request permissions if necessary.
        SmsManager manager;

        //check permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
        else
        {
            String message = messageEditText.getText().toString();
            manager = SmsManager.getDefault();
            String phoneNumber;
            //loop thru all contacts and send message

            for(Contact c : contactsList)
            {
             phoneNumber = c.getPhone();
             manager.sendTextMessage(phoneNumber, "1300-655-506", message, null, null);
                Toast.makeText(this, "Text message sent to: " + c.getName(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
