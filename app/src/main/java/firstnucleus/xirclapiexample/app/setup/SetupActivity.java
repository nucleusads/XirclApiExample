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

/**
 * The type Setup activity to set credential.
 */
public class SetupActivity extends AppCompatActivity {

    // Declaration of UI elements
    private Button btnSaveProfile;
    private TextView txtUserHeading, txtConnectionHeading;
    private EditText edtMobileNumber, edtEmailId, edtPinCode, edtCity, edtCountry, edtConnectionUrl, edtAuthenticationKey, edtReferenceCode;
    private LinearLayout layConnectionDetails;
    private CheckBox ckhUseDefault;

    // Declaration of variable
    private boolean isDefaultCredential = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        
        isDefaultCredential = AppController.getSharedPref(SetupActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false);

        // Binding XML to Java
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

        // Set font
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
                isDefaultCredential = isChecked;
                if (isChecked) {
                    edtMobileNumber.setText(getString(R.string.tagDefaultPhone));
                    edtEmailId.setText(getString(R.string.tagDefaultEmail));
                    edtPinCode.setText(getString(R.string.tagDefaultPinCode));
                    edtCity.setText(getString(R.string.tagDefaultCity));
                    edtCountry.setText(getString(R.string.tagDefaultCountry));
                    edtConnectionUrl.setText(getString(R.string.tagDefaultConnectionURL));
                    edtAuthenticationKey.setText(getString(R.string.tagDAuthenticationKey));
                    edtReferenceCode.setText(getString(R.string.tagDefaultRefCode));

                    edtMobileNumber.setEnabled(false);
                    edtEmailId.setEnabled(false);
                    edtPinCode.setEnabled(false);
                    edtCity.setEnabled(false);
                    edtCountry.setEnabled(false);
                    edtConnectionUrl.setEnabled(false);
                    edtAuthenticationKey.setEnabled(false);
                    edtReferenceCode.setEnabled(false);
                } else {
                    edtMobileNumber.setText("");
                    edtEmailId.setText("");
                    edtPinCode.setText("");
                    edtCity.setText("");
                    edtCountry.setText("");
                    edtConnectionUrl.setText(getString(R.string.tagDefaultConnectionURL));
                    edtAuthenticationKey.setText("");
                    edtReferenceCode.setText("");

                    edtMobileNumber.setEnabled(true);
                    edtEmailId.setEnabled(true);
                    edtPinCode.setEnabled(true);
                    edtCity.setEnabled(true);
                    edtCountry.setEnabled(true);
                    edtConnectionUrl.setEnabled(false);
                    edtAuthenticationKey.setEnabled(true);
                    edtReferenceCode.setEnabled(true);
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

                if (isCorrectInfo) {
                    Toast.makeText(SetupActivity.this, "Information filled correctly", Toast.LENGTH_SHORT).show();
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsProfileSetup), true).commit();

                    if (isDefaultCredential) {
                        AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), true).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserMobile), getString(R.string.tagDefaultPhone)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserEmail), getString(R.string.tagDefaultEmail)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserPinCode), getString(R.string.tagDefaultPinCode)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCity), getString(R.string.tagDefaultCity)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCountry), getString(R.string.tagDefaultCountry)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pConnectionUrl), getString(R.string.tagDefaultConnectionURL)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pAuthenticationKey), getString(R.string.tagDAuthenticationKey)).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pMarchantRefCode), getString(R.string.tagDefaultRefCode)).commit();
                    } else {
                        AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), false).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserMobile), edtMobileNumber.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pUserEmail), edtEmailId.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserPinCode), edtPinCode.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCity), edtCity.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.appUserCountry), edtCountry.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pConnectionUrl), edtConnectionUrl.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pAuthenticationKey), edtAuthenticationKey.getText().toString().trim()).commit();
                        AppController.getSharedPrefEditor(SetupActivity.this).putString(getString(R.string.pMarchantRefCode), edtReferenceCode.getText().toString().trim()).commit();
                    }
                    startActivity(new Intent(SetupActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
        saveCredential();
    }

    /**
     * Method to display credentail
     */
    private void saveCredential() {
        try {
            if (AppController.getSharedPref(SetupActivity.this).getBoolean(getString(R.string.pIsProfileSetup), false)) {
                if (isDefaultCredential) {
                    ckhUseDefault.setChecked(true);
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), true).commit();
                    edtMobileNumber.setText(getString(R.string.tagDefaultPhone));
                    edtEmailId.setText(getString(R.string.tagDefaultEmail));
                    edtPinCode.setText(getString(R.string.tagDefaultPinCode));
                    edtCity.setText(getString(R.string.tagDefaultCity));
                    edtCountry.setText(getString(R.string.tagDefaultCountry));
                    edtConnectionUrl.setText(getString(R.string.tagDefaultConnectionURL));
                    edtAuthenticationKey.setText(getString(R.string.tagDAuthenticationKey));
                    edtReferenceCode.setText(getString(R.string.tagDefaultRefCode));
                } else {
                    ckhUseDefault.setChecked(false);
                    AppController.getSharedPrefEditor(SetupActivity.this).putBoolean(getString(R.string.pIsDefaultUse), false).commit();
                    edtConnectionUrl.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pConnectionUrl), getString(R.string.tagDefaultConnectionURL)));
                    edtAuthenticationKey.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pAuthenticationKey), getString(R.string.tagDAuthenticationKey)));
                    edtReferenceCode.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pMarchantRefCode), getString(R.string.tagDefaultRefCode)));
                    edtMobileNumber.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pUserMobile), getString(R.string.tagDefaultPhone)));
                    edtEmailId.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.pUserEmail), getString(R.string.tagDefaultEmail)));
                    edtPinCode.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserPinCode), getString(R.string.tagDefaultPinCode)));
                    edtCity.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserCity), getString(R.string.tagDefaultCity)));
                    edtCountry.setText(AppController.getSharedPref(SetupActivity.this).getString(getString(R.string.appUserCountry), getString(R.string.tagDefaultCity)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
