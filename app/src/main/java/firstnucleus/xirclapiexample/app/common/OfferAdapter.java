package firstnucleus.xirclapiexample.app.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;

import firstnucleus.xirclapiexample.app.R;
import firstnucleus.xirclplugin.lib.common.OfferDAO;
import firstnucleus.xirclplugin.lib.xircls.XirclHelper;



/**
 * Created by Intel on 26-6-17.
 */
public class OfferAdapter extends BaseAdapter
{
	 
	private Activity context;
	private ArrayList<OfferDAO> OfferList;
	private LayoutInflater mLayoutInflater;
	private String productPrice;
	private boolean isDiaplay = false;

	/**
	 * Instantiates a new X offer adapter.
	 *
	 * @param context      the context
	 * @param offerDAOs    the offer da os
	 * @param productPrice the product price
	 * @param isDiaplay    the is diaplay
	 */
	public OfferAdapter(Activity context, ArrayList<OfferDAO> offerDAOs, String productPrice, boolean isDiaplay) {
		this.context = context;
		this.OfferList = offerDAOs;
		this.productPrice = productPrice;
		this.isDiaplay = isDiaplay;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		try {
			if (view == null)
				view = mLayoutInflater.inflate(R.layout.xircls_list_item, null);
			view.setTag(position);
			TextView textViewName = (TextView)view .findViewById(R.id.textViewName);
			TextView textViewDesc = (TextView)view .findViewById(R.id.textViewDesc);
			TextView txtApplyButton = (TextView)view .findViewById(R.id.txtApplyButton);
			txtApplyButton.setTag(position);
			
			textViewName.setText(OfferList.get(position).getOfferName());
			if(isDiaplay){
				txtApplyButton.setVisibility(View.VISIBLE);
				AppController.getCartDiscount(OfferList.get(position).getOfferTypeID(),OfferList.get(position).getOfferSavingValue(),textViewDesc,productPrice);
			} else {
				txtApplyButton.setVisibility(View.GONE);
				AppController.getFinalDiscount(OfferList.get(position).getOfferTypeID(),OfferList.get(position).getOfferSavingValue(),textViewDesc,productPrice);
			}
			txtApplyButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						int pos=(Integer)v.getTag();
						// Create custom dialog object
			            final Dialog dialog = new Dialog(context);
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
			            int displayWidth = context.getWindowManager().getDefaultDisplay().getHeight();
			            imgOffer.getLayoutParams().height = displayWidth / 3;
			           
			            
			            txtDialogTitle.setTypeface(AppController.getDefaultBoldFont(context));
			            txtOutletName.setTypeface(AppController.getDefaultBoldFont(context));
			            txtCouponName.setTypeface(AppController.getDefaultFont(context));
			            txtDescription.setTypeface(AppController.getDefaultFont(context));
			            txtDescriptionTitle.setTypeface(AppController.getDefaultBoldFont(context));
			            txtOfferValue.setTypeface(AppController.getDefaultBoldFont(context));
			            
			            Glide.with(context)
			            .load(OfferList.get(pos).getOfferImage())
			            .dontAnimate()
			            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
			            .into(imgOffer);
			            txtOutletName.setText(OfferList.get(pos).getOfferName());
			            txtCouponName.setText("Get " + XirclHelper.getFormattedString(Float.valueOf(OfferList.get(pos).getOfferSavingValue())) + "% Off");
			            txtDescription.setText(OfferList.get(pos).getOfferDescription());
			            //String offerTypeID, String offerSavingValue, TextView txtFinalText, String productPrice
						AppController.getCartDiscount(OfferList.get(pos).getOfferTypeID(),OfferList.get(pos).getOfferSavingValue(),txtOfferValue, productPrice);
			            dialog.findViewById(R.id.layClose).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
									dialog.dismiss();
							}
						});
			            	
			            dialog.show();
					} catch (Exception e) {
						e.printStackTrace();
					}	
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
 	}
	
	

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return OfferList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
}
