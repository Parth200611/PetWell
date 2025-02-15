package com.mountreachsolution.petwell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);
        Button btnCallDoctor = findViewById(R.id.btnCallDoctor);
        btnCallDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doctorPhoneNumber = "+9198222 31923"; // Change this to the vet's actual number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + doctorPhoneNumber));
                startActivity(intent);
            }
        });
    }
}