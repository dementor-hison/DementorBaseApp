package kr.co.dementor.ui;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dementor1 on 15. 11. 17..
 */
public class TopView extends LinearLayout
{
	//private OnTopViewListener onTopViewListener = null;
	
	/*
	public void onClickBack(View v)
	{
		LogTrace.d("");
		
		if(onTopViewListener != null)
		{
			onTopViewListener.OnBack();	
		}
	}
	
	public void onClickRefresh(View v)
	{
		if(onTopViewListener != null)
		{
			onTopViewListener.OnRefresh();	
		}
	}
	
	public void onClickHelp(View v)
	{
		if(onTopViewListener != null)
		{
			onTopViewListener.OnHelp();	
		}
	}
	
	public void onClickConfirm(View v)
	{
		if(onTopViewListener != null)
		{
			onTopViewListener.OnConfirm();	
		}
	}
	*/
	
	public TopView(Context context)
	{
		super(context);

		createView(context);
	}

	public TopView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		createView(context);
		
		TypedArray array = context.obtainStyledAttributes(attrs, ResourceLoader.getResourceDeclareStyleableIntArray(context,"TopView"));

		setTitle(array.getString(ResourceLoader.getResourseIdByName("TopView_titleText", "styleable", context)));

		array.recycle();
	}

	private View createView(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(ResourceLoader.getResourseIdByName("top_view", "layout", context), this, true);
	}

	private void setTitle(String title)
	{
		((TextView)ResourceLoader.getViewByName("tvTopTitle", "id", this)).setText(title);
	}

	/*
	public void setOnTopViewListener(OnTopViewListener listener)
	{
		onTopViewListener = listener;
	}
*/
	public void setRefreshButtonVisible(boolean isVisible)
	{
		ResourceLoader.getViewByName("ibTopRefresh", "id", this).setVisibility(isVisible == true ? ImageButton.VISIBLE : ImageButton.INVISIBLE);
	}

	public void setConfirmButtonVisible(boolean isVisible)
	{
		ResourceLoader.getViewByName("ibTopConfirm", "id", this).setVisibility(isVisible == true ? ImageButton.VISIBLE : ImageButton.GONE);
	}

	public void setHelpButtonVisible(boolean isVisible)
	{
		ResourceLoader.getViewByName("ibTopHelp", "id", this).setVisibility(isVisible == true ? ImageButton.VISIBLE : ImageButton.GONE);
	}

	public interface OnTopViewListener
	{
		void OnBack();

		void OnRefresh();

		void OnHelp();

		void OnConfirm();
	}
}
