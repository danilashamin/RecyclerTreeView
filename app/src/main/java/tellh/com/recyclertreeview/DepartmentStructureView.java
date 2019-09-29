package tellh.com.recyclertreeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import tellh.com.recyclertreeview_lib.SearchWayView;

public class DepartmentStructureView extends View implements SearchWayView {


    private boolean isInSearchWay;
    private boolean isRequiredElement;

    @IntDef(value = {SINGLE, DOUBLE})
    public @interface Mode {
    }

    public static final int SINGLE = 0;
    public static final int DOUBLE = 1;

    private RectF verticalRect;
    private Paint paint;

    @ColorInt
    private int primaryColor;

    @ColorInt
    private int inSearchWayColor;

    private float verticalRectWidth;
    private int viewWidth;
    private float hookBottomMargin;

    private Bitmap hook;

    private Bitmap hookBlue;

    @Mode
    private int mode;

    public DepartmentStructureView(Context context) {
        super(context);
        init(context, null);
    }

    public DepartmentStructureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DepartmentStructureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        verticalRect = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mode = DOUBLE;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DepartmentStructureView);
            mode = ta.getInteger(R.styleable.DepartmentStructureView_usv_mode, DOUBLE);
            ta.recycle();
        }

        primaryColor = UtilResources.getColor(R.color.custom_color_dark_athens_gray, context);
        inSearchWayColor = UtilResources.getColor(R.color.black, context);

        verticalRectWidth = UtilResources.getDimen(R.dimen.d_common_2dp, context);
        viewWidth = getHookBlue().getWidth();
        hookBottomMargin = UtilResources.getDimen(R.dimen.d_common_12dp, context);

        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int top = getTop();
        float hookTop = (float) (getHeight() / 2) + (float) (getHook().getHeight() / 2) - hookBottomMargin;

        float bottom;

        if (mode == SINGLE) {
            bottom = hookTop;
        } else {
            bottom = getBottom();
        }

        if (isInSearchWay) {
            paint.setColor(inSearchWayColor);
        } else {
            paint.setColor(primaryColor);
        }

        verticalRect.set(0, top, verticalRectWidth, bottom);
        canvas.drawRoundRect(verticalRect, 2, 2, paint);
        canvas.drawBitmap(isRequiredElement ? getHookBlue() : getHook(), 0, hookTop, paint);
    }

    public void setMode(@Mode int mode) {
        this.mode = mode;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
        int height = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);//MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        super.onMeasure(width, height);
    }

    private Bitmap getHook() {
        if (hook == null) {
            hook = UtilBitmap.getBitmap(getContext(), R.drawable.ic_hook);
        }
        return hook;
    }

    private Bitmap getHookBlue() {
        if (hookBlue == null) {
            hookBlue = UtilBitmap.getBitmap(getContext(), R.drawable.ic_hook_blue);
        }
        return hookBlue;
    }

    @Override
    public void setInSearchWay(boolean isInSearchWay) {
        this.isInSearchWay = isInSearchWay;
        invalidate();
    }

    @Override
    public void setRequiredElement(boolean isRequiredElement) {
        this.isRequiredElement = isRequiredElement;
        invalidate();
    }

}
