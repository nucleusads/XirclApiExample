package firstnucleus.xirclapiexample.app.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclplugin.lib.common.UserDetails;


/**
 * The type App controller.
 */
public class AppController extends Application {

    /**
     * The constant storeIDs as ArrayList.
     */
    public static List<String> storeIDs = new ArrayList<String>();

    private static AppController mInstance;

    /**
     * The constant TAG as String.
     */
    public static final String TAG = AppController.class.getSimpleName();

    /**
     * The Editor.
     */
    static SharedPreferences.Editor editor;

    /**
     * The Prefs.
     */
    static SharedPreferences prefs;

    /**
     * Gets shared pref.
     *
     * @param pContext the p context
     * @return the shared pref
     */
    public static SharedPreferences getSharedPref(Context pContext) {
        if (prefs == null)
            prefs = pContext.getSharedPreferences(pContext.getString(R.string.AppData), 0);
        return prefs;
    }

    /**
     * Gets shared pref editor.
     *
     * @param pContext the p context
     * @return the shared pref editor
     */
    public static SharedPreferences.Editor getSharedPrefEditor(Context pContext) {
        if (editor == null)
            editor = pContext.getSharedPreferences(pContext.getString(R.string.AppData), 0).edit();
        return editor;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    //Setting application font Bold,Normal,Regular

    /**
     * Gets default bold font.
     *
     * @param context the context
     * @return the default bold font
     */
    public static Typeface getDefaultBoldFont(Context context) {
        AssetManager am = context.getApplicationContext().getAssets();
        return Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "proxima-nova-alt-bold.ttf"));
    }

    /**
     * Gets default font.
     *
     * @param context the context
     * @return the default font
     */
    public static Typeface getDefaultFont(Context context) {
        AssetManager am = context.getApplicationContext().getAssets();
        return Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "OpenSans-Regular.ttf"));
    }

    /**
     * Sets image height.
     *
     * @param context the context
     * @param img     the img
     */
    // Set Image Height
    public static void setImageHeight(Context context, ImageView img) {
        try {
            int displayWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
            img.getLayoutParams().height = displayWidth / 3;
        } catch (Exception e) {
        }
    }

    /**
     * The constant cartCountUpdate.
     */
    // Set Brodcast listener

    public static String isCartRemove = "com.broadcast.cart.changed";
    private static IntentFilter mIntentFilter;

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public static IntentFilter getintentFilter() {
        try {
            mIntentFilter = new IntentFilter();
             mIntentFilter.addAction(AppController.isCartRemove);
        } catch (Exception e) {
        }
        return mIntentFilter;
    }

    /**
     * Gets formatted string.
     *
     * @param value the value
     * @return the formatted string
     */
    public static String getFormattedString(float value) {
        try {
            return String.format("%.0f", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00";
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

     /* Start of XirclController Functions */

    /**
     * Get final discount.
     *
     * @param offerTypeID      the offer type id
     * @param offerSavingValue the offer saving value
     * @param txtFinalText     the txt final text
     * @param productPrice     the product price
     */
   // Set offer Value (1-FDO, 2-PDO, 3-FPSV, 4-BOGO)
    public static void getFinalDiscount(String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice) {
        try {
            if (offerTypeID.equals("1")) {
                txtFinalText.setText("Get Rs. " + AppController.getFormattedString(Float.valueOf(offerSavingValue)) + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)));
            } else if (offerTypeID.equals("2")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs. " + finalDiscount + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)));
            } else if (offerTypeID.equals("3")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs. " + finalDiscount + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)));
            } else if (offerTypeID.equals("4")) {
                txtFinalText.setText("Buy One Get One Free");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get cart discount.
     *
     * @param offerTypeID      the offer type id
     * @param offerSavingValue the offer saving value
     * @param txtFinalText     the txt final text
     * @param productPrice     the product price
     */
    // Set offer Value (1-FDO, 2-PDO, 3-FPSV, 4-BOGO)
    public static void getCartDiscount(String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice) {
        try {
            if (offerTypeID.equals("1")) {
                txtFinalText.setText("Get Rs." + AppController.getFormattedString(Float.valueOf(offerSavingValue)) + " OFF on your cart");
            } else if (offerTypeID.equals("2")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs." + finalDiscount + " OFF on your cart");
            } else if (offerTypeID.equals("3")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount = (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs." + finalDiscount + " OFF on your cart");
            } else if (offerTypeID.equals("4")) {
                txtFinalText.setText("Buy One Get One Free");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send userdetails to call API
     *
     * @param context
     * @return UserDetails
     */
    public static UserDetails getXirclParams(Context context) {
        UserDetails userDetails = null;
        try {
            userDetails = new UserDetails(
                    AppController.getSharedPref(context).getString(context.getString(R.string.pConnectionUrl), context.getString(R.string.tagDefaultConnectionURL)),
                    AppController.getSharedPref(context).getString(context.getString(R.string.pAuthenticationKey), context.getString(R.string.tagDAuthenticationKey)),
                    AppController.getSharedPref(context).getString(context.getString(R.string.pUserMobile), context.getString(R.string.tagDefaultPhone)),
                    AppController.getSharedPref(context).getString(context.getString(R.string.pUserEmail), context.getString(R.string.tagDefaultEmail)),
                    AppController.getSharedPref(context).getString(context.getString(R.string.pMarchantRefCode), context.getString(R.string.tagDefaultRefCode)),
                    context);

        } catch (Exception e) {
        }
        return userDetails;
    }
    /*end of XirclController Functions*/
}
