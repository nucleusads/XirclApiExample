package firstnucleus.xirclapiexample.app.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclapiexample.app.products.CartActivity;
import firstnucleus.xirclplugin.lib.common.OfferDAO;


/**
 * The type Payment activity to get the order total and placed order button.
 */
public class PaymentActivity extends AppCompatActivity {

    // Declaration of UI elements
    private TextView totalAmountHeading, totalAmount, txtConfirmOrder, txtOfferHeading, txtOfferSaveValue;
    private LinearLayout layOfferApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Binding XML to Java
        layOfferApplied = (LinearLayout) findViewById(R.id.layOfferApplied);

        totalAmountHeading = (TextView) findViewById(R.id.totalAmountHeading);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        txtConfirmOrder = (TextView) findViewById(R.id.txtConfirmOrder);
        txtOfferSaveValue = (TextView) findViewById(R.id.txtOfferSaveValue);
        totalAmountHeading.setTypeface(AppController.getDefaultBoldFont(PaymentActivity.this));
        totalAmount.setTypeface(AppController.getDefaultBoldFont(PaymentActivity.this));
        txtConfirmOrder.setTypeface(AppController.getDefaultBoldFont(PaymentActivity.this));
        txtOfferSaveValue.setTypeface(AppController.getDefaultBoldFont(PaymentActivity.this));

        if (AppController.getSharedPref(PaymentActivity.this).getBoolean(getString(R.string.isAppliedOffer), false)) {
            layOfferApplied.setVisibility(View.VISIBLE);
            txtOfferSaveValue.setText("You saved Rs " + AppController.getSharedPref(PaymentActivity.this).getInt(getString(R.string.offerSavingValue), 0));
            totalAmount.setText("Rs. " + AppController.getSharedPref(PaymentActivity.this).getString(getString(R.string.appPriceAfter), "0"));
        } else {
            layOfferApplied.setVisibility(View.GONE);
            totalAmount.setText("Rs. " + AppController.getSharedPref(PaymentActivity.this).getString(getString(R.string.appPriceBefore), "0"));
        }

        txtConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, ThankYouActivity.class));
                finish();
            }
        });

    }
}
