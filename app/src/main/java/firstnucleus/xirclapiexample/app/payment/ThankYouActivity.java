package firstnucleus.xirclapiexample.app.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclapiexample.app.products.CartActivity;
import firstnucleus.xirclplugin.lib.XirclController;
import firstnucleus.xirclplugin.lib.common.OfferDAO;
import firstnucleus.xirclplugin.lib.common.OfferRedeem;
import firstnucleus.xirclplugin.lib.xircls.Xircls;

/**
 * The type Thankyou activity to display order confirmation message and implements Xircls and its method XirclResponse to get offer list and status/message
 * and offerCount to get the count.
 */
public class ThankYouActivity extends AppCompatActivity implements Xircls {

    // Declaration of UI elements
    private TextView txtXirclMessage, txtMessage;
    private LinearLayout layOfferApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        // Binding XML to Java
        txtXirclMessage = (TextView) findViewById(R.id.txtXirclMessage);
        layOfferApplied =(LinearLayout) findViewById(R.id.layOfferApplied);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        txtXirclMessage.setTypeface(AppController.getDefaultBoldFont(ThankYouActivity.this));
        txtMessage.setTypeface(AppController.getDefaultBoldFont(ThankYouActivity.this));

        MainActivity.cartId.clear();
        MainActivity.isCartUpdate = true;
        // Applied offer on cart it is marked as offer redeem if clicks on placed order
        if (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.isAppliedOffer), false)) {
            XirclController.xirclsRequest(this).redeemOffer(new OfferRedeem(AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pConnectionUrl), getString(R.string.tagDefaultConnectionURL)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pAuthenticationKey), getString(R.string.tagDAuthenticationKey)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pUserMobile), getString(R.string.tagDefaultPhone)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pUserEmail), getString(R.string.tagDefaultEmail)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appSellerRefCode), ""),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserPinCode), getString(R.string.tagDefaultPinCode)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserCity), getString(R.string.tagDefaultCity)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserCountry), getString(R.string.tagDefaultCountry)),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appPriceBefore), ""),
                    AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appPriceAfter), ""),ThankYouActivity.this));
        } else {
            txtXirclMessage.setVisibility(View.GONE);
            layOfferApplied.setVisibility(View.GONE);
        }
    }

    /**
     * Method to get list of Offer, response, status and message
     *
     * @param offerList       The offer list as ArrayList
     * @param isSuccess       The isSuccess as boolean
     * @param responseMessage The responseMessage as String
     */
    @Override
    public void xirclResponse(@NonNull ArrayList<OfferDAO> offerList, @NonNull boolean isSuccess, @NonNull String responseMessage) {
        try {
            if (isSuccess) {
                txtXirclMessage.setVisibility(View.VISIBLE);
                layOfferApplied.setVisibility(View.VISIBLE);
                txtXirclMessage.setText(responseMessage);
            } else {
            }
        } catch (Exception e) {
        }
    }

    /**
     * Method to get issued offer count
     *
     * @param offerCount
     */
    @Override
    public void offerCount(@NonNull int offerCount) {
    }
}
