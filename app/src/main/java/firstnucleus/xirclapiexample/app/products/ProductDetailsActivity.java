package firstnucleus.xirclapiexample.app.products;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import firstnucleus.xirclplugin.lib.xircls.Xircls;

/**
 * The type Product Details activity to display cart products and implements Xircls and its method XirclResponse to get offer list and status/message
 * and offerCount to get the count.
 */
public class ProductDetailsActivity extends AppCompatActivity implements Xircls {
    // Declaration of UI elements
    private TextView productName, productPrice, txtProductOffer, txtAddToCart, txtPriceAfterApply, txtSavingValue;
    private ImageView imgProduct;
    private LinearLayout layDiscount;
    // Declaration of variable
    private ArrayList<OfferDAO> offersList;
    private int OfferCount = 0;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        // Binding XML to Java
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

        txtProductOffer.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
        XirclController.xirclsRequest(this).getProductOffers(AppController.getXirclParams(this));

        if (MainActivity.cartId.contains(ProductListingActivity.productPosition)) {
            txtAddToCart.setText("GO TO CART");
        } else {
            txtAddToCart.setText("ADD TO CART");
        }

        txtAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isCartUpdate = false;
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
                        dialogList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        //this line is what you need:
                        dialogList.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        // set values for custom dialog components
                        ImageView imgOffer = (ImageView) dialogList.findViewById(R.id.imgOffer);
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
                                    displayOffer(offersList.get(i));
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

    /**
     * Method to get list of Offer, response, status and message
     *
     * @param offerList       The offer list as ArrayList
     * @param isSuccess       The isSuccess as boolean
     * @param responseMessage The responseMessage as String
     */
    @Override
    public void xirclResponse(final ArrayList<OfferDAO> offerList, boolean isSuccess, String responseMessage) {
        try {
            if (isSuccess) {
                offersList = offerList;
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
        //This method generated by Xircls. It give you Offer count
        //Do your stuff for display Offer count on Product Activity.
        txtProductOffer.setText(String.valueOf(offerCount) + " offer available on product\nView");
        this.OfferCount = offerCount;
    }

    /**
     *
     * Method to display offer details onClick of offer listing items\
     *
     * @param offerData
     */
    private void displayOffer(OfferDAO offerData) {
        try {
            // Create custom dialog object
            final Dialog dialog = new Dialog(ProductDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Include x_offer_dialog.xml file
            dialog.setContentView(R.layout.x_offer_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //this line is what you need:
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // Binding XML to Java
            TextView txtDialogTitle = (TextView) dialog.findViewById(R.id.txtDialogTitle);
            TextView txtOutletName = (TextView) dialog.findViewById(R.id.txtOutletName);
            TextView txtCouponName = (TextView) dialog.findViewById(R.id.txtCouponName);
            TextView txtDescriptionTitle = (TextView) dialog.findViewById(R.id.txtDescriptionTitle);
            TextView txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
            TextView txtOfferValue = (TextView) dialog.findViewById(R.id.txtOfferValue);
            TextView txtMaxOrderTitle = (TextView)dialog.findViewById(R.id.txtMaxOrderTitle);
            TextView txtMaxOrder = (TextView)dialog.findViewById(R.id.txtMaxOrder);
            TextView txtSavingValueTitle = (TextView)dialog.findViewById(R.id.txtSavingValueTitle);
            TextView txtSavingValue = (TextView)dialog.findViewById(R.id.txtSavingValue);

            ImageView imgOffer = (ImageView) dialog.findViewById(R.id.imgOffer);

            // Set Image Height
            AppController.setImageHeight(this,imgOffer);

            // Set font
            txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtOutletName.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtCouponName.setTypeface(AppController.getDefaultFont(ProductDetailsActivity.this));
            txtDescription.setTypeface(AppController.getDefaultFont(ProductDetailsActivity.this));
            txtDescriptionTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtOfferValue.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtMaxOrderTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));
            txtSavingValueTitle.setTypeface(AppController.getDefaultBoldFont(ProductDetailsActivity.this));

            Glide.with(ProductDetailsActivity.this).load(offerData.getOfferImage()).into(imgOffer);

            txtOutletName.setText(offerData.getOfferName());
            txtCouponName.setText("Get " + AppController.getFormattedString(Float.valueOf(offerData.getOfferSavingValue())) + "% Off");
            txtDescription.setText(offerData.getOfferDescription());
            txtMaxOrder.setText("Rs. " + AppController.getFormattedString(Float.valueOf(offerData.getOfferMaxValue())));
            txtSavingValue.setText("Rs. " + AppController.getFormattedString(Float.valueOf(offerData.getOfferSavingValue())));

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

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.cartId.contains(ProductListingActivity.productPosition)) {
            txtAddToCart.setText("GO TO CART");
        } else {
            txtAddToCart.setText("ADD TO CART");
        }
        XirclController.xirclsRequest(this).getProductOffers(AppController.getXirclParams(this));
    }
}
