package firstnucleus.xirclapiexample.app.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import firstnucleus.xirclapiexample.app.MainActivity;
import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclapiexample.app.products.CartActivity;
import firstnucleus.xirclapiexample.app.products.ProductDetailsActivity;
import firstnucleus.xirclapiexample.app.products.ProductListingActivity;


/**
 * Product adapter for single product.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.GridViewHolder> implements Serializable {
    // Declaration of variable
    private List<Integer> selecionados;
    private static Context currentContext;
    private ArrayList<ProductDAO> productList;
    // Initialize variable
    private static int SELECIONADO = 1;
    private boolean isDisplay = false;

    /**
     * Instantiates a new Product adapter.
     *
     * @param context     the context
     * @param productList the product list
     */
    public ProductAdapter(Context context, ArrayList<ProductDAO> productList, boolean isDisplay) {
        this.currentContext = context;
        this.productList = productList;
        this.isDisplay = isDisplay;
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
            if (isDisplay) {
                holder.btnDeleteCart.setVisibility(View.VISIBLE);
            } else {
                holder.btnDeleteCart.setVisibility(View.GONE);
            }

            holder.productName.setText(productDAO.getProductName());
            holder.productPrice.setText("Price : Rs. " + productDAO.getProductPrice());
            Glide.with(currentContext).load(productDAO.getProductImage()).into(holder.imgProduct);

            holder.imgProduct.setTag(position);
            holder.btnDeleteCart.setTag(position);

            holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductListingActivity.productPosition = (Integer) v.getTag();
                    currentContext.startActivity(new Intent(currentContext, ProductDetailsActivity.class));
                }
            });

            holder.btnDeleteCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int positionId = (Integer) v.getTag();
                    try {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(currentContext);
                        builder1.setMessage("Are you sure? \nYou want to remove product from cart.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        MainActivity.cartId.remove(positionId);
                                        Intent broadcastIntent = new Intent();
                                        broadcastIntent.setAction(AppController.isCartRemove);
                                        currentContext.sendBroadcast(broadcastIntent);
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
        private ImageView btnDeleteCart;

        private GridViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.txtProductName);
            productPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
            btnDeleteCart = (ImageView) view.findViewById(R.id.btnDeleteCart);
        }
    }
}