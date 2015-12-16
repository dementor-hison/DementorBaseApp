package kr.co.dementor.ui;

import java.util.ArrayList;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by dementor1 on 15. 11. 4..
 */
public class CustomGridView extends FrameLayout {
    private GridView gridView = null;

    private CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter();

    private ImageView ivScrollMore = null;

    private OnDragListener onCustomDragListener = null;
    private OnItemClickListener onCustomItemClickListener = null;

    private boolean isDragable = false;

    private SquareImageView sivDragView = null;
    private int selectedIconPosition = AdapterView.INVALID_POSITION;
    private int targetIconPosition = AdapterView.INVALID_POSITION;

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int position = gridView.pointToPosition((int) x, (int) y);

            int resID = 0;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (position == AdapterView.INVALID_POSITION) {
                        return false;
                    }

                    selectedIconPosition = position;

                    resID = (Integer) gridViewAdapter.getItem(selectedIconPosition);

                    if (onCustomDragListener != null && isDragable) {
                        onCustomDragListener.OnDragStart(selectedIconPosition, resID);

                        makeImage(position, (int) x, (int) y);
                    }

                    break;

                case MotionEvent.ACTION_UP:

                    if (onCustomDragListener != null && isDragable && selectedIconPosition != AdapterView.INVALID_POSITION && targetIconPosition != AdapterView.INVALID_POSITION) {
                        resID = (Integer) gridViewAdapter.getItem(targetIconPosition);

                        onCustomDragListener.OnDragEnd(targetIconPosition, resID);

                        selectedIconPosition = AdapterView.INVALID_POSITION;

                        targetIconPosition = AdapterView.INVALID_POSITION;
                    }

                    Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(200);

                    removeImage();

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (onCustomDragListener != null && isDragable && selectedIconPosition != AdapterView.INVALID_POSITION) {
                        targetIconPosition = position == AdapterView.INVALID_POSITION ? targetIconPosition : position;

                        moveImage(x, y);
                    }

                default:
                    break;
            }

            return false;
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onCustomItemClickListener != null) {
                int imageSrcId = (Integer) gridViewAdapter.getItem(position);
                onCustomItemClickListener.OnItemClick(view, position, imageSrcId);
            }
        }
    };

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                ivScrollMore.setVisibility(ImageView.INVISIBLE);
            } else {
                ivScrollMore.setVisibility(ImageView.VISIBLE);
            }
        }
    };


    public CustomGridView(Context context) {
        super(context);

        createView(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        createView(context);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        createView(context);
    }

    private void makeImage(int position, int x, int y) {
        SquareImageView selectedItem = (SquareImageView) gridView.getChildAt(position);

        if (selectedItem == null) {
            LogTrace.w("notFound item at position : " + position);
            return;
        }

        LayoutParams params = new LayoutParams((int) (selectedItem.getWidth() * 1.5f), (int) (selectedItem.getHeight() * 1.5f));

        params.setMargins(x - sivDragView.getWidth() / 2, y - sivDragView.getHeight() / 2, 0, 0);

        sivDragView.setLayoutParams(params);

        Bitmap b = Bitmap.createBitmap(selectedItem.getMeasuredWidth(),

                selectedItem.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas screenShotCanvas = new Canvas(b);

        selectedItem.draw(screenShotCanvas);

        sivDragView.setImageBitmap(b);

        sivDragView.setVisibility(ImageView.VISIBLE);
    }

    private void moveImage(float x, float y) {
        LayoutParams params = (LayoutParams) sivDragView.getLayoutParams();

        params.setMargins((int) x - sivDragView.getWidth() / 2, (int) y - sivDragView.getHeight() / 2, 0, 0);

        sivDragView.setLayoutParams(params);
    }

    private void removeImage() {
        sivDragView.setVisibility(ImageView.GONE);
    }

    private void createView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(ResourceLoader.getResourseIdByName("custom_grid_view", "layout", context), this, true);
        
        gridView = (GridView) ResourceLoader.getViewByName("gvGridView", "id", this);

        sivDragView = (SquareImageView) ResourceLoader.getViewByName("ivDrag", "id", this);

        ivScrollMore = (ImageView) ResourceLoader.getViewByName("ivScrollMore", "id", this);

        gridViewAdapter = new CustomGridViewAdapter();

        gridView.setAdapter(gridViewAdapter);

        gridView.setOnTouchListener(onTouchListener);

        gridView.setOnItemClickListener(onItemClickListener);

        gridView.setOnScrollListener(onScrollListener);
    }

    public ArrayList<Integer> getGridViewItems() {
        return gridViewAdapter.getListItems();
    }

    public void setGridViewItems(ArrayList<Integer> listBitmaps) {

        if (gridViewAdapter == null) {
            LogTrace.e("GridView adapter not set");
            return;
        }

        gridViewAdapter.setItemArrayList(listBitmaps);

        gridViewAdapter.notifyDataSetChanged();
    }

    public void setOnDragListener(CustomGridView.OnDragListener listener) {
        onCustomDragListener = listener;
    }

    public boolean isDragable() {
        return isDragable;
    }

    public void setDragable(boolean isDragable) {
        this.isDragable = isDragable;
    }

    public void setOnItemClickListener(CustomGridView.OnItemClickListener listener) {
        onCustomItemClickListener = listener;
    }

    public interface OnDragListener {

        void OnDragStart(int position, int resId);

        void OnDragEnd(int position, int resId);
    }

    public interface OnItemClickListener {

        void OnItemClick(View view, int position, int imageSrcId);

    }

}
