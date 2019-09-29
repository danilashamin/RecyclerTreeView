package tellh.com.recyclertreeview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

public class UtilResources {

    // Получение ресурсов по ID

    public static int getColor(@ColorRes int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        }
        else {
            return ContextCompat.getColor(context, id);
        }
    }

    public static Drawable getDrawable(@DrawableRes int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        }
        else {
            return ContextCompat.getDrawable(context, id);
        }
    }

    public static float getDimen(@DimenRes int id, Context context){
        return context.getResources().getDimension(id);
    }

    // Получение ID ресурсов по имени

    public static int getResourceByName(String name, String type, String defPackage, Context context) {
        if (defPackage == null) defPackage = context.getPackageName();
        return context.getResources().getIdentifier(name, type, defPackage);
    }

    public static int getResId(String name, String defPackage, Context context) {
        return getResourceByName(name, "id", defPackage, context);
    }

    public static int getResId(String name, Context context) {
        return getResourceByName(name, "id", null, context);
    }

    public static int getResIdColor(String name, String defPackage, Context context) {
        return getResourceByName(name, "color", defPackage, context);
    }

    public static int getResIdString(String name, String defPackage, Context context) {
        return getResourceByName(name, "string", defPackage, context);
    }

    public static int getResIdDrawable(String name, String defPackage, Context context) {
        return getResourceByName(name, "drawable", defPackage, context);
    }

    public static int getAndroidResourceByName(String name, String type, Context context) {
        return context.getResources().getIdentifier(name, type, "android");
    }

    // Получение ресурсов по имени

    public static int getColor(String name, String defPackage, Context context) {
        return getColor(getResIdColor(name, defPackage, context), context);
    }

    public static String getString(String name, String defPackage, Context context) {
        return context.getString(getResIdString(name, defPackage, context), context);
    }

    public static Drawable getDrawable(String name, String defPackage, Context context) {
        return getDrawable(getResIdDrawable(name, defPackage, context), context);
    }

    public static int getColor(String name, Context context) {
        return getColor(getResIdColor(name, null, context), context);
    }

    public static String getString(String name, Context context) {
        return context.getString(getResIdString(name, null, context), context);
    }

    public static Drawable getDrawable(String name, Context context) {
        return getDrawable(getResIdDrawable(name, null, context), context);
    }

    public static int getAndroidDimenPx(String name, Context context) {
        return context.getResources().getDimensionPixelSize(getAndroidResourceByName(name, "dimen", context));
    }
}
