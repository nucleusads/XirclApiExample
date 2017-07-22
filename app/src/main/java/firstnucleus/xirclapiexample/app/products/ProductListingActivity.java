package firstnucleus.xirclapiexample.app.products;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.common.ProductAdapter;


/**
 * The type Product listing.
 */
public class ProductListingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    /**
     * The constant productPosition.
     */
    public static int productPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new ProductAdapter(this, MainActivity.productDAOs);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}
