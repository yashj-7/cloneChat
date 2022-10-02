package android.example.clonechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.example.clonechat.databinding.ActivitySignInBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog pd;
    FirebaseDatabase db;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        pd=new ProgressDialog(signIn.this);
        pd.setTitle("Login");
        pd.setMessage("Trying to log in");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.email.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()){
                    Toast.makeText(signIn.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    mAuth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(signIn.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(signIn.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        binding.newAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signIn.this,signUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(signIn.this,MainActivity.class);
            startActivity(intent);
        }
    }
}