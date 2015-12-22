package kr.co.dementor.ui;

import java.util.ArrayList;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import kr.co.dementor.imagedata.IconData;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by dementor1 on 15. 11. 5..
 */
public class CustomGridViewAdapter extends BaseAdapter
{
	private ArrayList<IconData> listItems = new ArrayList<IconData>();
	
	private boolean isLocal = true;

	public void setItemArrayList(ArrayList<IconData> listItem, boolean isLocal)
	{
		listItems.clear();

		if (listItem == null)
		{
			LogTrace.e("listItem null");
			return;
		}
		
		this.isLocal  = isLocal;
		
		listItems.addAll(listItem);
	}
	
	public ArrayList<IconData> getListItems()
	{
		return listItems;
	}

	@Override
	public int getCount()
	{
		return listItems.size();
	}

	@Override
	public IconData getItem(int position)
	{
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SquareImageView iv = (SquareImageView) convertView;

		if (iv == null)
		{
			iv = new SquareImageView(parent.getContext());

			iv.setBackgroundResource(ResourceLoader.getResourseIdByName("grid_item_background", "drawable", parent.getContext()));

			iv.setScaleType(ImageView.ScaleType.FIT_XY);
		}

		if(isLocal)
		{
			iv.setImageBitmap(listItems.get(position).iconBitmap);
		}
		else
		{
			ImageLoader imageLoader = ImageLoader.getInstance();
			String url = listItems.get(position).iconurl;
			imageLoader.displayImage(url, iv, imageLoadingListener);
			url = null;
		}
		
		return iv;
	}
	
	ImageLoadingListener imageLoadingListener = new ImageLoadingListener()
	{
		
		@Override
		public void onLoadingStarted(String arg0, View arg1)
		{
			
		}
		
		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
		{
			
		}
		
		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
		{
			
		}
		
		@Override
		public void onLoadingCancelled(String arg0, View arg1)
		{
			
		}
	};

}
