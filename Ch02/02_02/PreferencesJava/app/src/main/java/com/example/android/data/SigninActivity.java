package com.example.android.data;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class SigninActivity extends AppCompatActivity  {

    public static final String EMAIL_KEY = "email_key";
    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mEmailView = (TextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Use Shared Prefs to check for a previous login and display it,
        // if not, set a default empty string.
        // Retrieve the value from SharedPreferences
        // Create an instance of the SharedPreferences object,
        // this time not using an editor, just referring to the object itself
        SharedPreferences prefs =
                getSharedPreferences(MainActivity.MY_GLOBAL_PREFS, MODE_PRIVATE);

        // Now we have access to those prefs from this Activity
        String email = prefs.getString(
                // key
                EMAIL_KEY,
                // Default value if the key isn't found in the prefs set
                // This will be the case when the code is first called
                "");

        // Check if the value is blank
        if (!TextUtils.isEmpty(email)) {
            // Auto populate the email field with the saved email
            mEmailView.setText(email);
        }
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            // Return the intent that started this activity
            getIntent().putExtra(EMAIL_KEY, email);

            // Call this to set the result that your activity will return to its caller
            setResult(RESULT_OK, getIntent());

            // Call this to end and close the activity
            finish();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}
