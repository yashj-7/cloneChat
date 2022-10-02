package android.example.clonechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.example.clonechat.databinding.ActivitySignUpBinding;
import android.example.clonechat.models.Users;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        pd = new ProgressDialog(signUp.this);
        pd.setTitle("creating account");
        pd.setMessage("Account creation under progress");

        mAuth = FirebaseAuth.getInstance();
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                if(binding.username.getText().toString().isEmpty() || binding.email.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()){
                    Toast.makeText(signUp.this, "enter credentials", Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if(task.isSuccessful()){

                                Users u = new Users(binding.username.getText().toString(),binding.email.getText().toString(),binding.password.getText().toString());
                                String id = task.getResult().getUser().getUid();

                                db = FirebaseDatabase.getInstance();
                                db.getReference().child("users").child(id).setValue(u);

                            }else{
                                Toast.makeText(signUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this,signIn.class);
                startActivity(intent);
            }
        });



    }
}