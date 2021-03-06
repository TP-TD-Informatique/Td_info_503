package fr.kevin.contact.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import fr.kevin.contact.R;
import fr.kevin.contact.dialog.Updatable;
import fr.kevin.contact.model.Contact;
import fr.kevin.contact.storage.ContactStorage;

public class MainActivity extends AppCompatActivity implements Updatable {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contact = ContactStorage.get(getApplicationContext()).find((Integer) getIntent().getSerializableExtra(ListActivity.EXTRA_CONTACT));

        ((TextView) findViewById(R.id.contact_name)).setText(getString(R.string.contact_name, contact.getFirstName(), contact.getLastName()));
        ((TextView) findViewById(R.id.contact_phone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.contact_home)).setText(contact.getHome());
        ((TextView) findViewById(R.id.contact_email)).setText(contact.getMail());
        ((TextView) findViewById(R.id.contact_location)).setText(contact.getLocation());

        findViewById(R.id.call_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhone()));
                startActivity(intent);
            }
        });
        findViewById(R.id.sms_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + contact.getPhone()));
                if (intent.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.send_sms_error), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.email_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.getMail()));
                startActivity(Intent.createChooser(intent, getString(R.string.send_email_chooser)));
            }
        });
        findViewById(R.id.profil_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    ImageView image = findViewById(R.id.profil_image);
                    image.setImageBitmap((Bitmap) bundle.get("data"));
                    image.setImageTintList(new ColorStateList(new int[0][0], new int[0])); // Enlève le tint
                }
            }
        }
    }

    @Override
    public void update() {
        ((TextView) findViewById(R.id.contact_name)).setText(getString(R.string.contact_name, contact.getFirstName(), contact.getLastName()));
        ((TextView) findViewById(R.id.contact_phone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.contact_home)).setText(contact.getHome());
        ((TextView) findViewById(R.id.contact_email)).setText(contact.getMail());
        ((TextView) findViewById(R.id.contact_location)).setText(contact.getLocation());
    }
}