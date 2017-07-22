package firstnucleus.xirclapiexample.app.payment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclplugin.lib.XirclController;
import firstnucleus.xirclplugin.lib.common.OfferDAO;
import firstnucleus.xirclplugin.lib.common.OfferRedeem;
import firstnucleus.xirclplugin.lib.xircls.Xircls;

public class ThankYouActivity extends AppCompatActivity implements Xircls {

    private TextView txtXirclMessage, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        txtXirclMessage = (TextView) findViewById(R.id.txtXirclMessage);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        XirclController.xirclsRequest(ThankYouActivity.this).redeemOffer(new OfferRedeem(
                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.connectionURL) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pConnectionUrl), "")),
                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagAuthenticationKey) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pAuthenticationKey), "")),
                 AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pUserMobile), ""),
                 AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pUserEmail), ""),
                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagDefaultRefCode) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.pMarchantRefCode), "")),

                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagDefaultPinCode) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserPinCode), "")),
                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagDefaultCity) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserCity), "")),
                (AppController.getSharedPref(ThankYouActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagDefaultCountry) : AppController.getSharedPref(ThankYouActivity.this).getString(getString(R.string.appUserCountry), "")),
                 AppController.getSharedPref(this).getString(getString(R.string.appPriceBefore),""),
                 AppController.getSharedPref(this).getString(getString(R.string.appPriceAfter),""), ThankYouActivity.this));
    }

    @Override
    public void xirclResponse(@NonNull ArrayList<OfferDAO> offerList, @NonNull boolean isSuccess, @NonNull String responseMessage) {
        try {
            if (isSuccess) {
                txtXirclMessage.setText(responseMessage);
            } else {
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void offerCount(@NonNull int offerCount) {
    }
}
