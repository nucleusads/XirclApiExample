package firstnucleus.xirclapiexample.app.products;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclapiexample.app.common.OfferAdapter;
import firstnucleus.xirclapiexample.app.common.ProductDAO;
import firstnucleus.xirclplugin.lib.XirclController;
import firstnucleus.xirclplugin.lib.common.OfferDAO;
import firstnucleus.xirclplugin.lib.common.UserDetails;
import firstnucleus.xirclplugin.lib.xircls.Xircls;


public class ProductDetailsActivity extends AppCompatActivity implements Xircls {

    private TextView productName, productPrice, txtProductOffer, txtAddToCart, txtPriceAfterApply, txtSavingValue;
    private ImageView imgProduct;
    private ProductDAO productDAO;
     private ArrayList<OfferDAO> offersList;
    private int OfferCount = 0;
    private LinearLayout layDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        productName = (TextView) findViewById(R.id.txtProductName);
        productPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtProductOffer = (TextView) findViewById(R.id.txtProductOffer);
        txtAddToCart = (TextView) findViewById(R.id.txtAddToCart);
        txtPriceAfterApply = (TextView) findViewById(R.id.txtPriceAfterApply);
        txtSavingValue = (TextView) findViewById(R.id.txtSavingValue);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        layDiscount = (LinearLayout) findViewById(R.id.layDiscount);

        productDAO = MainActivity.productDAOs.get(ProductListingActivity.productPosition);
        productName.setText(productDAO.getProductName());
        productPrice.setText(productDAO.getProductPrice());
        Glide.with(this).load(productDAO.getProductImage()).into(imgProduct);

        /** Calling XirclController method getProductOffers for get offers of user
         * Tt required UserDetails object with value of
         * User Mobile as String
         * User EmailId as String
         * Seller reference code as String (comma separated )
         * Activity reference
         *
         * */


        XirclController.xirclsRequest(this).getProductOffers(new UserDetails(
                (AppController.getSharedPref(this).getBoolean(getString(R.string.pIsDefaultUse), false)
                 ? getString(R.string.connectionURL)
                 : AppController.getSharedPref(this).getString(getString(R.string.pConnectionUrl), "")),
                (AppController.getSharedPref(this).getBoolean(getString(R.string.pIsDefaultUse), false)
                 ? getString(R.string.tagAuthenticationKey)
                 : AppController.getSharedPref(this).getString(getString(R.string.pAuthenticationKey), "")),
                AppController.getSharedPref(this).getString(getString(R.string.pUserMobile), ""),
                AppController.getSharedPref(this).getString(getString(R.string.pUserEmail), ""),
                (AppController.getSharedPref(this).getBoolean(getString(R.string.pIsDefaultUse), false)
                 ? getString(R.string.tagDefaultRefCode)
                 : AppController.getSharedPref(this).getString(getString(R.string.pMarchantRefCode), "")),
                this));


        if (MainActivity.cartId.contains(ProductListingActivity.productPosition)) {
            txtAddToCart.setText("GO TO CART");
        } else {
            txtAddToCart.setText("ADD TO CART");
        }


        txtAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.cartId.contains(ProductListingActivity.productPosition)) {
                    startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                } else {
                    MainActivity.cartId.add(ProductListingActivity.productPosition);
                    txtAddToCart.setText("GO TO CART");
                }
            }
        });

        txtProductOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (OfferCount > 0) {
                        // Create custom dialog object
                        dialogList = new Dialog(ProductDetailsActivity.this);
                        dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        // Include dialog.xml file
                        dialogList.setContentView(R.layout.x_custom_dialoglist);

                        // set values for custom dialog components
                        TextView txtDialogTitle = (TextView) dialogList.findViewById(R.id.txtDialogTitle);
                        ListView listItems = (ListView) dialogList.findViewById(R.id.listItems);
                        dialogList.findViewById(R.id.layClose).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialogList.dismiss();
                            }
                        });

                        txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));

                        OfferAdapter customList = new OfferAdapter(ProductDetailsActivity.this, offersList, productDAO.getProductPrice(), false);
                        listItems.setAdapter(customList);

                        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                try {
                                  //  diaplayOffer(offersList.get(i));
                                } catch (Exception e) {
                                }
                            }
                        });
                        dialogList.show();
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "Offers not available.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private Dialog dialogList = null;


    @Override
    public void xirclResponse(final ArrayList<OfferDAO> offerList, boolean isSuccess, String responseMessage) {

        //This method generated by Xircls. It give you List of Offer, response status and message.
        //Do your stuff for display Offer list on Product Activity.

        try {
            if (isSuccess) {
                offersList = offerList;
            } else {
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void offerCount(@NonNull int offerCount) {

        //This method generated by Xircls. It give you Offer count
        //Do your stuff for display Offer count on Product Activity.
        txtProductOffer.setText("Offer(" + String.valueOf(offerCount) + ")");
        this.OfferCount = offerCount;
    }

    private void diaplayOffer(OfferDAO offerData) {
        try {
            // Create custom dialog object
            final Dialog dialog = new Dialog(ProductDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Include x_offer_dialog.xml file
            dialog.setContentView(R.layout.x_offer_dialog);

            // set values for custom dialog components
            TextView txtDialogTitle = (TextView) dialog.findViewById(R.id.txtDialogTitle);
            TextView txtOutletName = (TextView) dialog.findViewById(R.id.txtOutletName);
            TextView txtCouponName = (TextView) dialog.findViewById(R.id.txtCouponName);
            TextView txtDescriptionTitle = (TextView) dialog.findViewById(R.id.txtDescriptionTitle);
            TextView txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
            TextView txtOfferValue = (TextView) dialog.findViewById(R.id.txtOfferValue);


            ImageView imgOffer = (ImageView) dialog.findViewById(R.id.imgOffer);
            //Set Image Height
            setImageHeight(imgOffer);


            txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtOutletName.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtCouponName.setTypeface(AppController.getDefaultFont(ProductDetailsActivity.this));
            txtDescription.setTypeface(AppController.getDefaultFont(ProductDetailsActivity.this));
            txtDescriptionTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtOfferValue.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));

            Glide.with(ProductDetailsActivity.this)
                    .load(offerData.getOfferImage())
                    .dontAnimate()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(imgOffer);
            txtOutletName.setText(offerData.getOfferName());
            txtCouponName.setText("Get " +
                                  AppController.getFormattedString(Float.valueOf(offerData.getOfferSavingValue())) +
                                  "% Off");
            txtDescription.setText(offerData.getOfferDescription());
            //String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice
            AppController.getFinalDiscount(offerData.getOfferTypeID(), offerData.getOfferSavingValue(), txtOfferValue, productDAO.getProductPrice());
            dialog.findViewById(R.id.layClose).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
        }
    }

    //Set Image Height
    private void setImageHeight(ImageView img) {
        try {
            int displayWidth = getWindowManager().getDefaultDisplay().getHeight();
            img.getLayoutParams().height = displayWidth / 3;
        } catch (Exception e) {
        }
    }
}
