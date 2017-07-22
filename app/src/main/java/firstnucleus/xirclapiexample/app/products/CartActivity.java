package firstnucleus.xirclapiexample.app.products;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import firstnucleus.xirclapiexample.app.common.ProductAdapter;
import firstnucleus.xirclapiexample.app.common.ProductDAO;
import firstnucleus.xirclapiexample.app.payment.PaymentActivity;
import firstnucleus.xirclplugin.lib.XirclController;
import firstnucleus.xirclplugin.lib.common.OfferDAO;
import firstnucleus.xirclplugin.lib.common.UserDetails;
import firstnucleus.xirclplugin.lib.xircls.Xircls;


// Cart Activity

public class CartActivity extends AppCompatActivity implements Xircls {

    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private ArrayList<ProductDAO> productCartDAOs;
    private int cartTotal = 0;
    private TextView txtPrefix, txtCartPrice, txtAfterApplyCoupon, txtProcced, txtOfferValue;
    private int OfferCount = 0;
    private ArrayList<OfferDAO> offersList;
    private Dialog dialogList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        try {


        /*Calling XirclController method getCartOffers for get offers of user
        * Tt required UserDetails object with value of
        * User Mobile as String
        * User EmailId as String
        * Seller reference code as String (comma separated )
        * Activity reference
        * */

        XirclController.xirclsRequest(CartActivity.this).getCartOffers(new UserDetails(
                (AppController.getSharedPref(CartActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.connectionURL) : AppController.getSharedPref(CartActivity.this).getString(getString(R.string.pConnectionUrl), "")),
                (AppController.getSharedPref(CartActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagAuthenticationKey) : AppController.getSharedPref(CartActivity.this).getString(getString(R.string.pAuthenticationKey), "")),
                AppController.getSharedPref(CartActivity.this).getString(getString(R.string.pUserMobile), ""),
                AppController.getSharedPref(CartActivity.this).getString(getString(R.string.pUserEmail), ""),
                (AppController.getSharedPref(CartActivity.this).getBoolean(getString(R.string.pIsDefaultUse), false) ? getString(R.string.tagDefaultRefCode) : AppController.getSharedPref(CartActivity.this).getString(getString(R.string.pMarchantRefCode), "")),
                CartActivity.this));

            productCartDAOs = new ArrayList<ProductDAO>();
            for (int i = 0; i < MainActivity.cartId.size(); i++) {
                productCartDAOs.add(MainActivity.productDAOs.get(MainActivity.cartId.get(i)));
                cartTotal = cartTotal + Integer.parseInt(MainActivity.productDAOs.get(MainActivity.cartId.get(i)).getProductPrice());
            }
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mAdapter = new ProductAdapter(this, productCartDAOs);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(mAdapter);
            txtPrefix = (TextView) findViewById(R.id.txtPrefix);
            txtCartPrice = (TextView) findViewById(R.id.txtCartPrice);
            txtAfterApplyCoupon = (TextView) findViewById(R.id.txtAfterApplyCoupon);
            txtProcced = (TextView) findViewById(R.id.txtProcced);
            txtOfferValue = (TextView) findViewById(R.id.txtOfferValue);
            txtCartPrice.setText(String.valueOf(cartTotal));
            txtProcced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CartActivity.this, PaymentActivity.class));
                }
            });

            txtOfferValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (OfferCount > 0) {
                            // Create custom dialog object
                            dialogList = new Dialog(CartActivity.this);
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

                            txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));

                            OfferAdapter customList = new OfferAdapter(CartActivity.this, offersList, String.valueOf(cartTotal), true);
                            listItems.setAdapter(customList);

                            listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                                    try {
                                       // diaplayOffer(offersList.get(i));
                                      AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
                                    builder1.setMessage("Are you sure? \nApply this coupon on Cart.");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialogList.dismiss();
                                                    dialog.cancel();
                                                    try {
                                                       AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appSellerRefCode), offersList.get(i).getSellerRefCode()).commit();
                                                       AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appPriceBefore), String.valueOf(cartTotal)).commit();

                                                        int finalDiscount = 0;
                                                        try {
                                                            String strDiscount = AppController.getFormattedString(Float.valueOf(offersList.get(i).getOfferSavingValue()));
                                                            finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(cartTotal)))) * (Integer.parseInt(strDiscount)) / 100;
                                                        } catch (Exception e) {
                                                        }
                                                        AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appPriceAfter), String.valueOf(cartTotal - finalDiscount)).commit();

                                                        txtAfterApplyCoupon.setText(String.valueOf(cartTotal - finalDiscount));
                                                       // txtSavingValue.setText(String.valueOf(finalDiscount));
                                                        try {
                                                            txtCartPrice.setTextColor(getResources().getColor(R.color.gray_light));
                                                            txtCartPrice.setText(String.valueOf(cartTotal));
                                                            txtCartPrice.setPaintFlags(txtCartPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }

                                }
                            });
                            dialogList.show();
                        } else {
                            Toast.makeText(CartActivity.this, "Offers not available.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void diaplayOffer(OfferDAO offerData) {
        try {
            // Create custom dialog object
            final Dialog dialog = new Dialog(CartActivity.this);
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


            txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));
            txtOutletName.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));
            txtCouponName.setTypeface(AppController.getDefaultFont(CartActivity.this));
            txtDescription.setTypeface(AppController.getDefaultFont(CartActivity.this));
            txtDescriptionTitle.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));
            txtOfferValue.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));

            Glide.with(CartActivity.this)
                    .load(offerData.getOfferImage())
                    .dontAnimate()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(imgOffer);
            txtOutletName.setText(offerData.getOfferName());
            txtCouponName.setText("Get " + AppController.getFormattedString(Float.valueOf(offerData.getOfferSavingValue())) + "% Off");
            txtDescription.setText(offerData.getOfferDescription());
            //String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice
            AppController.getFinalDiscount(offerData.getOfferTypeID(), offerData.getOfferSavingValue(), txtOfferValue, String.valueOf(cartTotal));
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
    public void xirclResponse(ArrayList<OfferDAO> offerList, boolean isSuccess, String responseMessage) {
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
        txtOfferValue.setText("Offer(" + String.valueOf(offerCount) + ")");
        this.OfferCount = offerCount;
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
