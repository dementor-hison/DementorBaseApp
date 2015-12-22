package kr.co.dementor.jsonformat;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.imagedata.CategoryData;
import kr.co.dementor.imagedata.IconData;
import android.graphics.Bitmap;


public class ReceiveRegisterImageInfo
{
	public String message = null;
	
	public Data data = null;
	
	public String code = null;
	
	public class Data
	{
		public String categoryzip = null;
		public ArrayList<CategoryData> category = null;
	}

	public void setCategoryBitmap(HashMap<String, Bitmap> imageMap)
	{
		if(data == null || data.category == null)
		{
			LogTrace.w("Category data were not yet ready...");
			return;
		}
		
		checkExtractZipMap(imageMap);
		
		String currentCategoryName = null;
		for (int categoryIndex = 0; categoryIndex < data.category.size(); categoryIndex++)
		{
			CategoryData categoryData = data.category.get(categoryIndex);
			
			currentCategoryName = categoryData.categoryname;
			
			if(imageMap.containsKey(currentCategoryName))
			{
				categoryData.categoryBitmap = imageMap.get(currentCategoryName);
				LogTrace.d("currentCategoryName : " + currentCategoryName);
			}
			else
			{
				LogTrace.w("The categoryName not in the compressed file.\n categoryName : " + currentCategoryName);
			}
		}
		
		imageMap.clear();
	}
	
	public void setImageBitmap(HashMap<String, Bitmap> imageMap, String categoryId)
	{
		if(data == null || data.category == null)
		{
			LogTrace.w("Category data was not ready...");
			return;
		}
		
		checkExtractZipMap(imageMap);
		
		for (int categoryIndex = 0; categoryIndex < data.category.size(); categoryIndex++)
		{
			CategoryData categoryData = data.category.get(categoryIndex);
			
			if(categoryData.iconitem == null)
			{
				LogTrace.w("Icon data was not ready...");
				return;
			}
			
			if(categoryData.categoryid.equals(categoryId) == false)
			{
				continue;
			}
			
			String currentIconName = null;
			
			for (int iconIndex = 0; iconIndex < categoryData.iconitem.size(); iconIndex++)
			{
				IconData iconData = categoryData.iconitem.get(iconIndex);
				
				currentIconName = iconData.iconname + ".png";
				
				if(imageMap.containsKey(currentIconName))
				{
					iconData.iconBitmap = imageMap.get(currentIconName);
					LogTrace.d("currentIconName : " + currentIconName + " , Target categoryId : " + categoryId);
				}
				else
				{
					LogTrace.w("The iconName not in the compressed file.\n iconName : " + currentIconName);
				}
			}
		}
		
		imageMap.clear();
	}

	private void checkExtractZipMap(HashMap<String, Bitmap> imageMap)
	{	
		LogTrace.d("zip file size : " +  imageMap.size());
		for (String key : imageMap.keySet())
		{
			LogTrace.i("extract zip file name : " + key);
		}
	}
	
	
	
	
	
}
