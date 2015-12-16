package kr.co.dementor.ui;

import java.util.ArrayList;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by dementor1 on 15. 11. 5..
 */
public class CustomGridViewAdapter extends BaseAdapter {
    private ArrayList<Integer> listItems = new ArrayList<Integer>();

    public void setItemArrayList(ArrayList<Integer> listItem) {
        listItems.clear();

        if (listItem == null) {
            LogTrace.e("listItem null");
            return;
        }

        listItems.addAll(listItem);
    }

    public ArrayList<Integer> getListItems() {
        return listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView iv = (SquareImageView) convertView;

        if (iv == null) {
            iv = new SquareImageView(parent.getContext());

            iv.setBackgroundResource(ResourceLoader.getResourseIdByName("grid_item_background", "drawable", parent.getContext()));

            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }

      //ImageLoader imageLoader = ImageLoader.getInstance();
        //imageLoader.displayImage(mIconBeans[position].mImageURL, iv, AuthView.this);
        iv.setImageResource(listItems.get(position));

        return iv;
    }

}
