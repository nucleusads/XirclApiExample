package firstnucleus.xirclapiexample.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.common.ProductDAO;
import firstnucleus.xirclapiexample.app.products.ProductListingActivity;
import firstnucleus.xirclapiexample.app.setup.SetupActivity;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private TextView txtResult;
    private Button btnClick, btnProductList;

    /**
     * The constant cartId.
     */
    public static ArrayList<Integer> cartId = new ArrayList<Integer>();
    /**
     * The Product da os.
     */
    public static ArrayList<ProductDAO> productDAOs;
    private void initProductData() {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initProductData();
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnClick = (Button) findViewById(R.id.buttonClick);
        btnProductList = (Button) findViewById(R.id.btnProductList);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
            }
        });

        btnProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(XirclController.isSetupDone()){
                    startActivity(new Intent(MainActivity.this, ProductListingActivity.class));
             //   } else {
                    txtResult.setText("Please set profile");
              //  }
            }
        });
    }
}
