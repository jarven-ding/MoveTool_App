package com.jwding.appbase.component.autolayout.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.jwding.appbase.component.autolayout.utils.ScreenUtils;
import com.jwding.appbase.component.autolayout.utils.L;

/**
 * Created by zhy on 15/11/18.
 */
public class AutoLayoutConifg
{

    private static AutoLayoutConifg sIntance = new AutoLayoutConifg();


    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static final String KEY_DESIGN_HEIGHT = "design_height";
    private static final String KEY_DESIGN_DPI = "design_dpi";

    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDpi;

    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize;


    private AutoLayoutConifg()
    {
    }

    public void checkParams()
    {
        if (mDesignHeight <= 0 || mDesignWidth <= 0)
        {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.");
        }
    }

    public AutoLayoutConifg useDeviceSize()
    {
        useDeviceSize = true;
        return this;
    }


    public static AutoLayoutConifg getInstance()
    {
        return sIntance;
    }


    public int getScreenWidth()
    {
        return mScreenWidth;
    }

    public int getScreenHeight()
    {
        return mScreenHeight;
    }

    public int getDesignWidth()
    {
        return mDesignWidth;
    }

    public int getDesignHeight()
    {
        return mDesignHeight;
    }


    public void init(Context context)
    {
        getMetaData(context);

        int[] screenSize = ScreenUtils.getScreenSize(context, useDeviceSize);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];

        if(mDesignHeight>mDesignWidth&&mScreenHeight<mScreenWidth||mDesignHeight<mDesignWidth&&mScreenHeight>mScreenWidth){
            int temp=(int)(mScreenHeight);
            mScreenHeight=(int)(mScreenWidth);
            mScreenWidth=temp;
        }
        mScreenHeight=(int)(mScreenWidth*mDesignHeight/(float)mDesignWidth);
        L.e(" screenWidth =" + mScreenWidth + " ,screenHeight = " + mScreenHeight+",screenDPI="+mScreenDpi);
    }

    private void getMetaData(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try
        {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null)
            {
                mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
        }

        L.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }


}