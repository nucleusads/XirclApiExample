package firstnucleus.xirclapiexample.app.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;


/**
 * The type Payment activity.
 */
public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountHeading, totalAmount, txtConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        totalAmountHeading = (TextView) findViewById(R.id.totalAmountHeading);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        txtConfirmOrder = (TextView) findViewById(R.id.txtConfirmOrder);
        totalAmount.setText("Rs. " + AppController.getSharedPref(PaymentActivity.this).getString("X_totalAmountAfter","0"));
        txtConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, ThankYouActivity.class));
            }
        });

    }
}
