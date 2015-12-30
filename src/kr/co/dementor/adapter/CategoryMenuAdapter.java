package kr.co.dementor.adapter;

import java.util.ArrayList;

import kr.co.dementor.common.ResourceLoader;
import kr.co.dementor.imagedata.CategoryData;
import kr.co.dementor.imagedata.IconData;
import kr.co.dementor.ui.CustomGridView;
import kr.co.dementor.ui.SquareImageView;
import kr.co.dementor.ui.CustomGridView.OnItemClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryMenuAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<CategoryData> categoryData = null;
	private FragmentCategoryMenu[] fragments;
	
	public CategoryMenuAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index)
	{
		if(fragments[index] == null)
		{
			fragments[index] = new FragmentCategoryMenu(categoryData.get(index)); 
		}
		return fragments[index];
	}

	@Override
	public int getCount()
	{
		return categoryData == null ? 0 : categoryData.size();
	}


	public void setCategoryList(ArrayList<CategoryData> category)
	{
		this.categoryData = category;
		fragments = new FragmentCategoryMenu[categoryData.size()];
	}
	
	private class FragmentCategoryMenu extends Fragment
	{
		private CategoryData categoryData = null;
		private SquareImageView imageView = null;
		
		public FragmentCategoryMenu(CategoryData categoryData)
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
			View view = inflater.inflate(ResourceLoader.getResourseIdByName("item_category_menu", "layout", getContext()), null);
			
			imageView  = (SquareImageView)ResourceLoader.getViewByName("coverflowItemImage", "id", view);
			
			imageView.setImageBitmap(this.categoryData.categoryBitmap);
			
			return view;
		}
		
		OnItemClickListener onItemClickListener = new CustomGridView.OnItemClickListener()
		{
			@Override
			public void OnItemClick(View view, int position, IconData iconData)
			{
				
			}	
					
		};
	}

}
