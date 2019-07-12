package com.app.eventify.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.eventify.AdminActivity;
import com.app.eventify.MainActivity;
import com.app.eventify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private TextInputEditText editTextEmail,editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Intent intent;

    public LoginFragment() {

    }
    private boolean checkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    private void loginUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("email required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("password required");
            editTextPassword.requestFocus();
            return;
        }
        if(!checkConnection())
        {
            Toast.makeText(getActivity(),"Please Check your Internet Connection...",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information

                              FirebaseUser user = mAuth.getCurrentUser();
                              if(user.getUid().equals("lSf5e78AhAgIWaTXLe2Lzv4RyDv1"))
                              {
                                  intent = new Intent(getActivity(), AdminActivity.class);
                                  startActivity(intent);
                                  getActivity().finish();
                              }
                              else
                              {
                                  intent = new Intent(getActivity(), MainActivity.class);
                                  intent.putExtra("login","Yload");
                                  startActivity(intent);
                                  getActivity().finish();
                              }
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void hideKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editTextEmail = view.findViewById(R.id.login_email);
        editTextPassword = view.findViewById(R.id.login_password);
        Button linkToRegister = view.findViewById(R.id.btn_link_register);
        Button linkToReset = view.findViewById(R.id.btn_link_reset);
        progressBar = view.findViewById(R.id.progressBar_login);
        mAuth = FirebaseAuth.getInstance();
        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.OpeningContainer,registerFragment,"REGISTER_FRAGMENT")
                                .addToBackStack(null)
                                .commit();
            }
        });

        linkToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.OpeningContainer,resetPasswordFragment,"RESET_PASSWORD_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button btn = view.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideKeyboard(getActivity());
                loginUser();
            }
        });
        return view;
    }

}
