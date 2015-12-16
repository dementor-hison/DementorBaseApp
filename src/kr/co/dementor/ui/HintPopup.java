package kr.co.dementor.ui;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by dementor1 on 15. 11. 20..
 */
public class HintPopup extends LinearLayout
{
	private Context ctx = null;
	private ImageView[] arrIvActionPopupKeys = new ImageView[Defines.MAX_KEY_CAPACITY];
	private ImageView[] arrIvActionPopupArrow = new ImageView[Defines.MAX_KEY_CAPACITY - 1];

	public HintPopup(Context context)
	{
		super(context);
		
		createView(context);
		
		initView();
	}
	
	public HintPopup(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		createView(context);
		
		initView();
	}
	
	private View createView(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(ResourceLoader.getResourseIdByName("hint_popup", "layout", context), this, true);
	}

	private void initView()
	{	
		arrIvActionPopupKeys[Defines.ImagePosition.LOCK] = (ImageView) ResourceLoader.getViewByName("ivHintLock", "id", this);
		arrIvActionPopupKeys[Defines.ImagePosition.KEY1] = (ImageView) ResourceLoader.getViewByName("ivHintKey1", "id", this);
		arrIvActionPopupKeys[Defines.ImagePosition.KEY2] = (ImageView) ResourceLoader.getViewByName("ivHintKey2", "id", this);
		arrIvActionPopupKeys[Defines.ImagePosition.KEY3] = (ImageView) ResourceLoader.getViewByName("ivHintKey3", "id", this);
		
		arrIvActionPopupKeys[Defines.ImagePosition.LOCK].setTag(Defines.RES_ID_DEFAULT_HINT_IMAGE.get(Defines.ImagePosition.LOCK));
		arrIvActionPopupKeys[Defines.ImagePosition.KEY1].setTag(Defines.RES_ID_DEFAULT_HINT_IMAGE.get(Defines.ImagePosition.KEY1));
		arrIvActionPopupKeys[Defines.ImagePosition.KEY2].setTag(Defines.RES_ID_DEFAULT_HINT_IMAGE.get(Defines.ImagePosition.KEY2));
		arrIvActionPopupKeys[Defines.ImagePosition.KEY3].setTag(Defines.RES_ID_DEFAULT_HINT_IMAGE.get(Defines.ImagePosition.KEY3));

		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW1] = (ImageView) ResourceLoader.getViewByName("ivHintArrow1", "id", this);
		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW2] = (ImageView) ResourceLoader.getViewByName("ivHintArrow2", "id", this);
		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW3] = (ImageView) ResourceLoader.getViewByName("ivHintArrow3", "id", this);

		for (int i = 0; i < arrIvActionPopupKeys.length; i++)
		{
			arrIvActionPopupKeys[i].setImageResource(Defines.RES_ID_DEFAULT_HINT_IMAGE.get(i));
			arrIvActionPopupKeys[i].setBackgroundResource(android.R.color.transparent);
		}

		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW1].setSelected(false);
		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW2].setSelected(false);
		arrIvActionPopupArrow[Defines.ArrowImagePosition.ARROW3].setSelected(false);
	}

	public void setHintImage(int position, int resId)
	{
		if (position >= arrIvActionPopupKeys.length)
		{
			LogTrace.e("out of range...Image MaxLength : " + arrIvActionPopupKeys.length + " , try index : " + position);
			return;
		}

		arrIvActionPopupKeys[position].setImageResource(resId);
		arrIvActionPopupKeys[position].setTag(resId);

		for (int i = 0; i < arrIvActionPopupArrow.length; i++)
		{
			arrIvActionPopupArrow[i].setSelected(false);

			if (i == position)
			{
				arrIvActionPopupArrow[position].setSelected(true);
			}
		}

		for (int index = 0; index < arrIvActionPopupKeys.length; index++)
		{
			int currentImgId = (Integer) arrIvActionPopupKeys[index].getTag();

			if (currentImgId != Defines.RES_ID_DEFAULT_HINT_IMAGE.get(index))
			{
				arrIvActionPopupKeys[index].setBackgroundResource(ResourceLoader.getResourseIdByName("grid_item_background", "drawable", ctx));
			}
			else
			{
				arrIvActionPopupKeys[index].setBackgroundResource(android.R.color.transparent);
			}
		}
	}
}
