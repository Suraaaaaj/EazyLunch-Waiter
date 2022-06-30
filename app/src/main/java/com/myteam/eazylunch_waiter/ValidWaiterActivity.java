package com.myteam.eazylunch_waiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class ValidWaiterActivity extends AppCompatActivity {
    FirebaseFirestore dbroot;
    FirebaseAuth auth;
    TextView phoneError,idError;
    EditText ownerNo,waiterID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_waiter);
        Button nextBtn = (Button) findViewById(R.id.nextBtn);
        ownerNo = findViewById(R.id.ownerNo);;
        waiterID = findViewById(R.id.waiterID);
        phoneError = findViewById(R.id.phoneError);
        idError = findViewById(R.id.idError);
        phoneError.setVisibility(View.INVISIBLE);
        idError.setVisibility(View.INVISIBLE);
        dbroot = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneError.setVisibility(View.INVISIBLE);
                idError.setVisibility(View.INVISIBLE);
                if(TextUtils.isEmpty(ownerNo.getText())){
                    phoneError.setText("Owner no. is required");
                    phoneError.setVisibility(View.VISIBLE);
                }else if(TextUtils.isEmpty(waiterID.getText())){
                    idError.setText("Waiter id is required");
                    idError.setVisibility(View.VISIBLE);
                }else{
                    validateWaiter();
                }
            }
        });
    }

    private void validateWaiter() {
        dbroot.collection("restaurants")
                .document("+91"+ownerNo.getText().toString())
                .collection("waiters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if((documentSnapshot.getId().equals(auth.getCurrentUser().getPhoneNumber()))){
                                    //Logged In users document found
                                    if(documentSnapshot.get("waiterID").toString().equals(waiterID.getText().toString())){
                                        Toast.makeText(ValidWaiterActivity.this,"waiter found",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        idError.setText("Invalid waiter ID");
                                        idError.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }else {

                        }
                        if (task.getResult().isEmpty()){
                            phoneError.setText("Owner number is not registered");
                            phoneError.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ValidWaiterActivity.this,"Something went wrong! Please try again",Toast.LENGTH_LONG).show();
                    }
                });
    }
}