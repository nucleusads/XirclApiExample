package firstnucleus.xirclapiexample.app.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclapiexample.app.common.Validation;


public class SetupActivity extends AppCompatActivity {

    private Button btnSaveProfile;
    private TextView txtUserHeading, txtConnectionHeading;
    private EditText edtMobileNumber, edtEmailId, edtPinCode, edtCity, edtCountry, edtConnectionUrl, edtAuthenticationKey, edtReferenceCode;
    private LinearLayout layConnectionDetails;
    private CheckBox ckhUseDefault;
    private boolean isDefaultCerdentials = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        txtUserHeading = (TextView) findViewById(R.id.txtUserHeading);
        txtConnectionHeading = (TextView) findViewById(R.id.txtConnectionHeading);

        edtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtPinCode = (EditText) findViewById(R.id.edtPinCode);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        edtConnectionUrl = (EditText) findViewById(R.id.edtConnectionUrl);
        edtAuthenticationKey = (EditText) findViewById(R.id.edtAuthenticationKey);
        edtReferenceCode = (EditText) findViewById(R.id.edtReferenceCode);
        layConnectionDetails = (LinearLayout) findViewById(R.id.layConnectionDetails);
        ckhUseDefault = (CheckBox) findViewById(R.id.ckhUseDefault);

        //Set font
        btnSaveProfile.setTypeface(AppController.getDefaultBoldFont(this));
        txtUserHeading.setTypeface(AppController.getDefaultBoldFont(this));
        txtConnectionHeading.setTypeface(AppController.getDefaultBoldFont(this));
        edtMobileNumber.setTypeface(AppController.getDefaultFont(this));
        edtEmailId.setTypeface(AppController.getDefaultFont(this));
        edtPinCode.setTypeface(AppController.getDefaultFont(this));
        edtCity.setTypeface(AppController.getDefaultFont(this));
        edtCountry.setTypeface(AppController.getDefaultFont(this));
        edtConnectionUrl.setTypeface(AppController.getDefaultFont(this));
        edtAuthenticationKey.setTypeface(AppController.getDefaultFont(this));
        edtReferenceCode.setTypeface(AppController.getDefaultFont(this));
        ckhUseDefault.setTypeface(AppController.getDefaultFont(this));


        ckhUseDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultCerdentials = isChecked;
                if (isChecked) {
                    layConnectionDetails.setVisibility(View.GONE);
                } else {
                    layConnectionDetails.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrectInfo = true;
                if (!Validation.hasMobileText(edtMobileNumber)) {
                    isCorrectInfo = false;
                }

                if (!Validation.isEmailAddress(edtEmailId, true)) {
                    isCorrectInfo = false;
                }

                if (!Validation.hasPinCodeText(edtPinCode)) {
                    edtPinCode.setError("Enter proper pin code.");
                    isCorrectInfo = false;
                }

                if (!Validation.hasText(edtCity)) {
                    isCorrectInfo = false;
                }

                if (!Validation.hasText(edtCountry)) {
                    isCorrectInfo = false;
                }

                if (!isDefaultCerdentials) {

                    if (!Validation.hasText(edtConnectionUrl) ||
                        (!URLUtil.isValidUrl(edtConnectionUrl.getText().toString().trim()))) {
                        edtConnectionUrl.setError("Enter valid URL.");
                        isCorrectInfo = false;
                    }

                    if (!Validation.hasAuthText(edtAuthenticationKey)) {
                        edtAuthenticationKey.setError("Enter valid authentication key.");
                        isCorrectInfo = false;
                    }

                    if (!Validation.hasText(edtReferenceCode)) {
                        isCorrectInfo = false;
                    }
                }

                if (isCorrectInfo) {
                    Toast.makeText(SetupActivity.this, "Information filled correctly", Toast.LENGTH_SHORT).show();
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsProfileSetup), true).commit();

                    AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserMobile), edtMobileNumber.getText().toString().trim()).commit();
                    AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserEmail), edtEmailId.getText().toString().trim()).commit();
                    AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserPinCode), edtPinCode.getText().toString().trim()).commit();
                    AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCity), edtCity.getText().toString().trim()).commit();
                    AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCountry), edtCountry.getText().toString().trim()).commit();

                    if (isDefaultCerdentials) {
                        AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), true).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pConnectionUrl), "").commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pAuthenticationKey), "").commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pMarchantRefCode), "").commit();
                    } else {
                        AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), false).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pConnectionUrl), edtConnectionUrl.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pAuthenticationKey), edtAuthenticationKey.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pMarchantRefCode), edtReferenceCode.getText().toString().trim()).commit();
                    }
                    startActivity(new Intent(SetupActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
        setData();
    }

    private void setData() {
        try {
            if (AppController.getSharedPref(SetupActivity.this).getBoolean(getString(R.string.pIsProfileSetup), false)) {
                if (isDefaultCerdentials) {
                    ckhUseDefault.setChecked(true);
                    layConnectionDetails.setVisibility(View.GONE);
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), true).commit();
                    edtConnectionUrl.setText("");
                    edtAuthenticationKey.setText("");
                    edtReferenceCode.setText("");
                } else {
                    ckhUseDefault.setChecked(false);
                    layConnectionDetails.setVisibility(View.VISIBLE);
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), false).commit();
                    edtConnectionUrl.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pConnectionUrl), ""));
                    edtAuthenticationKey.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pAuthenticationKey), ""));
                    edtReferenceCode.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pMarchantRefCode), ""));
                }

                edtMobileNumber.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pUserMobile), ""));
                edtEmailId.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pUserEmail), ""));
                edtPinCode.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserPinCode), ""));
                edtCity.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserCity), ""));
                edtCountry.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserCountry), ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
