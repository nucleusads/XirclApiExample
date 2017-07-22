package firstnucleus.xirclapiexample.app.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.products.ProductDetailsActivity;
import firstnucleus.xirclapiexample.app.products.ProductListingActivity;


/**
 * The type Product adapter.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.GridViewHolder> implements Serializable {

    private List<Integer> selecionados;
    private static int SELECIONADO = 1;
    private static Context currentContext;
    private ArrayList<ProductDAO> productList;

    /**
     * Instantiates a new Product adapter.
     *
     * @param context     the context
     * @param productList the product list
     */
    public ProductAdapter(Context context, ArrayList<ProductDAO> productList) {
        this.currentContext = context;
       this.productList = productList;
        selecionados = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (selecionados.contains(position)) {
            return SELECIONADO;
        }
        return 2;
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
         try {
             ProductDAO productDAO = productList.get(position);
             holder.productName.setText(productDAO.getProductName());
             holder.productPrice.setText("Price : Rs. "+ productDAO.getProductPrice());
             Glide.with(currentContext).load(productDAO.getProductImage()).into(holder.imgProduct);

             holder.imgProduct.setTag(position);

             holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     ProductListingActivity.productPosition = (Integer)v.getTag();
                     currentContext.startActivity(new Intent(currentContext, ProductDetailsActivity.class));
                 }
             });
         } catch (Exception e) {
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    /**
     * The type Grid view holder.
     */
    public class GridViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice;
        private ImageView imgProduct;

        private GridViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.txtProductName);
            productPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
        }
    }
}