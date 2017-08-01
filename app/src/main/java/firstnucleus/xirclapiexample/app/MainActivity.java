package firstnucleus.xirclapiexample.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.common.AppController;
import firstnucleus.xirclapiexample.app.common.ProductDAO;
import firstnucleus.xirclapiexample.app.products.CartActivity;
import firstnucleus.xirclapiexample.app.products.ProductListingActivity;
import firstnucleus.xirclapiexample.app.setup.SetupActivity;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    // Declaration of UI elements
    private TextView txtResult;
    private Button btnClick, btnProductList, btnGoToCart, btnRemove;

    /**
     * The constant cartId.
     */
    public static ArrayList<Integer> cartId = new ArrayList<Integer>();
    /**
     * The constant isCartUpdate.
     */
    public static boolean isCartUpdate = false;

    /**
     * The Product da os.
     */
    public static ArrayList<ProductDAO> productDAOs;

    /**
     * Method to create a dummy products
     */
    private void createProductData() {
        try {
            productDAOs = new ArrayList<ProductDAO>();
            for (int i = 0; i < 10; i++) {
                productDAOs.add(new ProductDAO("Demo Product " + String.valueOf(i + 1), String.valueOf(i),
                        "https://www.2checkout.com/upload/images/graphic_product_tangible.png",
                        String.valueOf(500 + (int) (Math.random() * 5000))));
            }

        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding XML to Java
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        txtResult = (TextView) findViewById(R.id.txtResult);
        btnClick = (Button) findViewById(R.id.buttonClick);
        btnProductList = (Button) findViewById(R.id.btnProductList);
        btnGoToCart = (Button) findViewById(R.id.btnGoToCart);
        btnRemove = (Button) findViewById(R.id.btnRemove);

        // Set action bar on product listing activity
        setSupportActionBar(toolbar);
        createProductData();

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
            }
        });

        btnProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductListingActivity.class));
            }
        });

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Are you sure? \nYou want to regenerate data.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    createProductData();
                                    MainActivity.cartId.clear();
                                    AppController.getSharedPrefEditor(MainActivity.this).putString(getString(R.string.appSellerRefCode), "").commit();
                                    AppController.getSharedPrefEditor(MainActivity.this).putString(getString(R.string.appPriceBefore), "").commit();
                                    AppController.getSharedPrefEditor(MainActivity.this).putString(getString(R.string.appPriceAfter), "").commit();
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
    }
}
