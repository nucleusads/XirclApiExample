package firstnucleus.xirclapiexample.app.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import firstnucleus.xirclapiexample.app.R;


/**
 * The type App controller.
 */
public class AppController extends Application {

    /**
     * The constant storeIDs.
     */
    public static List<String> storeIDs = new ArrayList<String>();

    private static AppController mInstance;
    /**
     * The constant TAG.
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

    /**
     * Gets systen version.
     *
     * @return the systen version
     */
//Get the current version
    public static boolean getSystenVersion() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * Parse values array list.
     *
     * @param line      the line
     * @param separator the separator
     * @return the array list
     */
//Convert String to array groupList
    public static ArrayList<String> ParseValues(String line, String separator) {
        try {
            ArrayList<String> aList = new ArrayList<String>();
            int X;
            String sVal = "", sChar;
            int strLength = line.length();
            for (X = 0; X < strLength; X++) {
                sChar = line.substring(X, X + 1);
                if (sChar.equals(separator) || X == strLength - 1) {
                    // new one
                    if (X == strLength - 1 && !separator.equals(sChar))
                        sVal = sVal + sChar;
                    aList.add(sVal);
                    sVal = "";
                } else
                    sVal = sVal + sChar;
            }
            return aList;
        } catch (Exception ex) {
            return null;
        }
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
     * Gets default light font.
     *
     * @param context the context
     * @return the default light font
     */
    public static Typeface getDefaultLightFont(Context context) {
        AssetManager am = context.getApplicationContext().getAssets();
        return Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "OpenSans-Light.ttf"));
    }

    /**
     * Sets image height.
     *
     * @param context the context
     * @param img     the img
     */
//Set Image Height
    public static void setImageHeight(Context context, ImageView img) {
        try {
            int displayWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
            img.getLayoutParams().height = displayWidth / 3;
        } catch (Exception e) {
        }
    }

    /**
     * Sets list view height based on children c.
     *
     * @param listView the list view
     */
    //Set ListView Child item size
    public static void setListViewHeightBasedOnChildrenC(ListView listView) {
        try {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
                return;

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
        }
    }

    /**
     * Is connecting to internet boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isConnectingToInternet(Context context) {
        boolean wifiDataAvailable = false;
        boolean mobileDataAvailable = false;
        try {
            ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {
                if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (netInfo.isConnected())
                        wifiDataAvailable = true;
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (netInfo.isConnected())
                        mobileDataAvailable = true;
            }
        } catch (Exception e) {
        }
        return wifiDataAvailable || mobileDataAvailable;
    }


    /**
     * The constant cartCountUpdate.
     */
//Set Brodcast listener
    public static String cartCountUpdate = "com.broadcast.cartCount.update";
    /**
     * The constant isCPUpdated.
     */
    public static String isCPUpdated = "com.broadcast.coupon.coupanbank.update";
    /**
     * The constant isSMSReceived.
     */
    public static String isSMSReceived = "com.broadcast.sms.received";
    /**
     * The constant isCartUpdated.
     */
    public static String isCartUpdated = "com.broadcast.cartcount.changed";
    private static IntentFilter mIntentFilter;

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public static IntentFilter getintentFilter() {
        try {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(AppController.cartCountUpdate);
            mIntentFilter.addAction(AppController.isSMSReceived);
            mIntentFilter.addAction(AppController.isCartUpdated);
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


    /**
     * Gets device imei.
     *
     * @param context the context
     * @return the device imei
     */
    public static String getDeviceIMEI(Context context) {
        try {
            String imei = null;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null)
                imei = tm.getDeviceId();
            if (imei == null)
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return imei;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

     /*Start of XirclController Functions*/


    /**
     * Get final discount.
     *
     * @param offerTypeID      the offer type id
     * @param offerSavingValue the offer saving value
     * @param txtFinalText     the txt final text
     * @param productPrice     the product price
     */
//Set offer Value (1-FDO, 2-PDO, 3-FPSV, 4-BOGO)
    public static void getFinalDiscount(String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice){
        try {
            if (offerTypeID.equals("1")) {
                txtFinalText.setText("Get Rs. " + AppController.getFormattedString(Float.valueOf(offerSavingValue)) + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)));
            } else if (offerTypeID.equals("2")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount= (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs. " + finalDiscount + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)));
            } else if (offerTypeID.equals("3")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount= (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs. " + finalDiscount + " OFF,\nOn buying product of Rs." + AppController.getFormattedString(Float.valueOf(productPrice)) );
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
        //Set offer Value (1-FDO, 2-PDO, 3-FPSV, 4-BOGO)
    public static void getCartDiscount(String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice){
        try {
            if (offerTypeID.equals("1")) {
                txtFinalText.setText("Get Rs." + AppController.getFormattedString(Float.valueOf(offerSavingValue)) + " OFF on your cart");
            } else if (offerTypeID.equals("2")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount= (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs." + finalDiscount + " OFF on your cart");
            } else if (offerTypeID.equals("3")) {
                String strDiscount = AppController.getFormattedString(Float.valueOf(offerSavingValue));
                int finalDiscount= (Integer.parseInt(AppController.getFormattedString(Float.valueOf(productPrice)))) * (Integer.parseInt(strDiscount)) / 100;
                txtFinalText.setText("Get Rs." + finalDiscount + " OFF on your cart");
            } else if (offerTypeID.equals("4")) {
                txtFinalText.setText("Buy One Get One Free");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ProductDetailsActivity response value boolean.
     *
     * @param strStatus the str status
     * @return the boolean
     */
//get the response code from json
    public static boolean bResponseValue(@NonNull String strStatus) {
        boolean bStatus = false;
        try {
            int sCode = Integer.parseInt(strStatus);
            switch (sCode) {
                case 200:
                    bStatus = true;
                    break;
                case 201:
                    bStatus = false;
                    break;
                case 400:
                    bStatus = false;
                    break;
                case 401:
                    bStatus = false;
                    break;
                case 403:
                    bStatus = false;
                    break;
                case 404:
                    bStatus = false;
                    break;
                case 500:
                    bStatus = false;
                    break;
                case 503:
                    bStatus = false;
                    break;
                default:
            }
        } catch (Exception e) {
        }
        return bStatus;
    }
    /*end of XirclController Functions*/

}
