package firstnucleus.xirclapiexample.app.common;

import android.support.annotation.NonNull;

/**
 * The type Product dao.
 */
public class ProductDAO {
    /**
     * Instantiates a new Product dao.
     */
    public ProductDAO() {
    }

    /**
     * Gets product name.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets product name.
     *
     * @param productName the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets product id.
     *
     * @param productId the product id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Gets product image.
     *
     * @return the product image
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * Sets product image.
     *
     * @param productImage the product image
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * Gets product price.
     *
     * @return the product price
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * Sets product price.
     *
     * @param productPrice the product price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    private String productName;
    private String productId;

    /**
     * Instantiates a new Product dao.
     *
     * @param productName  the product name
     * @param productId    the product id
     * @param productImage the product image
     * @param productPrice the product price
     */

    public ProductDAO(@NonNull String productName, @NonNull String productId, @NonNull String productImage, @NonNull String productPrice) {
        this.productName = productName;
        this.productId = productId;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    private String productImage;
    private String productPrice;

}
