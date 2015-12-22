package kr.co.dementor.adapter;

import java.util.ArrayList;

import kr.co.dementor.common.ResourceLoader;
import kr.co.dementor.imagedata.CategoryData;
import kr.co.dementor.imagedata.IconData;
import kr.co.dementor.ui.CustomGridView;
import kr.co.dementor.ui.CustomGridView.OnItemClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryIconAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<CategoryData> categoryData = null;
	private OnSelectListener selectedListener = null;

	public CategoryIconAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index)
	{
		return new FragmentCategoryIconGrid(categoryData.get(index));
	}

	@Override
	public int getCount()
	{
		return categoryData == null ? 0 : categoryData.size();
	}

	public void setIconList(ArrayList<CategoryData> categorydata)
	{
		this.categoryData  = categorydata;
	}
	
	public void setOnSelectListener(OnSelectListener selectListener)
	{
		this.selectedListener  = selectListener; 
	}
	
	public interface OnSelectListener
	{
		void onSeletedItem(View v, IconData iconData);
	}
	
	private class FragmentCategoryIconGrid extends Fragment
	{
		private CategoryData categoryData = null;
		private CustomGridView customGridView = null;
		
		public FragmentCategoryIconGrid(CategoryData categoryData)
		{
			super();
			
			this.categoryData = categoryData;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View view = inflater.inflate(ResourceLoader.getResourseIdByName("item_category_list", "layout", getContext()), container, true);
			
			customGridView  = (CustomGridView)ResourceLoader.getViewByName("gvGridView", "id", view);
			
			customGridView.setOnItemClickListener(onItemClickListener);
			
			customGridView.setDragable(false);
			
			customGridView.setGridViewItems(categoryData.iconitem);
			
			return view;
		}
		
		OnItemClickListener onItemClickListener = new CustomGridView.OnItemClickListener()
		{
			@Override
			public void OnItemClick(View view, int position, IconData iconData)
			{
				if(selectedListener != null)
				{
					selectedListener.onSeletedItem(view, iconData);
				}
			}	
					
		};
	}
}
