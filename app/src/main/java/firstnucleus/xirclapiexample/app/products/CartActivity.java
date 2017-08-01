package firstnucleus.xirclapiexample.app.products;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

/**
 * The type Cart activity to display cart products and implements
 * Xircls and its method XirclResponse
 * to get offer list and status/message
 * and offerCount to get the count.
 */
public class CartActivity extends AppCompatActivity implements Xircls {

    // Declaration of UI elements
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private ArrayList<ProductDAO> productCartDAOs;
    private TextView txtPrefix, txtCartPrice, txtAfterApplyCoupon, txtProcced, txtOfferValue, txtCartMsg;
    private LinearLayout layNoCart;
    private RelativeLayout relativeLayout;

    // Declaration of variable
    private int cartTotal = 0;
    private int OfferCount = 0;
    private ArrayList<OfferDAO> offersList;
    private Dialog dialogList = null;

    // Initialize broadcast receiver
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppController.isCartRemove)) {
                cartTotal = 0;
                getCartDetails();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Binding XML to Java
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        txtCartMsg = (TextView) findViewById(R.id.txtCartMsg);
        txtCartMsg.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));
        layNoCart = (LinearLayout) findViewById(R.id.layNoCart);

        AppController.getSharedPrefEditor(CartActivity.this).putBoolean(getString(R.string.isAppliedOffer), false).commit();

        try {
            getCartDetails();
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
                            dialogList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            //this line is what you need:
                            dialogList.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                            // Binding XML to Java
                            TextView txtDialogTitle = (TextView) dialogList.findViewById(R.id.txtDialogTitle);
                            ListView listItems = (ListView) dialogList.findViewById(R.id.listItems);

                            dialogList.findViewById(R.id.layClose).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialogList.dismiss();
                                }
                            });

                            // Set font
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
                                                        AppController.getSharedPrefEditor(CartActivity.this).putBoolean(getString(R.string.isAppliedOffer), true).commit();

                                                        dialogList.dismiss();
                                                        dialog.cancel();
                                                        try {
                                                            AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appSellerRefCode), offersList.get(i).getSellerRefCode()).commit();
                                                            int finalDiscount = 0;
                                                            try {
                                                                String strDiscount = AppController.getFormattedString(Float.valueOf(offersList.get(i).getOfferSavingValue()));
                                                                finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(cartTotal)))) * (Integer.parseInt(strDiscount)) / 100;
                                                                AppController.getSharedPrefEditor(CartActivity.this).putInt(getString(R.string.offerSavingValue), finalDiscount).commit();
                                                            } catch (Exception e) {
                                                            }
                                                            AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appPriceAfter), String.valueOf(cartTotal - finalDiscount)).commit();

                                                            txtAfterApplyCoupon.setText(String.valueOf(cartTotal - finalDiscount));

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

    /**
     * Method to get list of Offer, response, status and message
     *
     * @param offerList       The offer list as ArrayList
     * @param isSuccess       The isSuccess as boolean
     * @param responseMessage The responseMessage as String
     */
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
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, AppController.getintentFilter());
        try {
            if (MainActivity.isCartUpdate) {
                getCartDetails();
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
        txtOfferValue.setText(String.valueOf(offerCount) + " offer available on cart\nView");
        this.OfferCount = offerCount;
    }

    /**
     * Method to get cart details
     */
    public void getCartDetails() {
        if (MainActivity.cartId.size() == 0) {
            relativeLayout.setVisibility(View.GONE);
            layNoCart.setVisibility(View.VISIBLE);
            txtCartMsg.setText("No products in cart");
        } else {
            XirclController.xirclsRequest(this).getCartOffers(AppController.getXirclParams(this));
            relativeLayout.setVisibility(View.VISIBLE);
            layNoCart.setVisibility(View.GONE);

            productCartDAOs = new ArrayList<ProductDAO>();
            for (int i = 0; i < MainActivity.cartId.size(); i++) {
                productCartDAOs.add(MainActivity.productDAOs.get(MainActivity.cartId.get(i)));
                cartTotal = cartTotal + Integer.parseInt(MainActivity.productDAOs.get(MainActivity.cartId.get(i)).getProductPrice());
            }

            // Binding XML to Java
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            txtPrefix = (TextView) findViewById(R.id.txtPrefix);
            txtCartPrice = (TextView) findViewById(R.id.txtCartPrice);
            txtAfterApplyCoupon = (TextView) findViewById(R.id.txtAfterApplyCoupon);
            txtProcced = (TextView) findViewById(R.id.txtProcced);
            txtOfferValue = (TextView) findViewById(R.id.txtOfferValue);
            txtCartPrice.setText(String.valueOf(cartTotal));
            txtOfferValue.setTypeface(AppController.getDefaultBoldFont(CartActivity.this));

            mAdapter = new ProductAdapter(this, productCartDAOs, true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(mAdapter);


            AppController.getSharedPrefEditor(CartActivity.this).putString(getString(R.string.appPriceBefore), String.valueOf(cartTotal)).commit();
            AppController.getSharedPrefEditor(CartActivity.this).putBoolean(getString(R.string.isAppliedOffer), false).commit();

            txtProcced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CartActivity.this, PaymentActivity.class));
                }
            });
        }
    }
}
