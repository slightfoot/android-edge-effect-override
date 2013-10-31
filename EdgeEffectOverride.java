package com.demondevelopers.eeo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;


/**
 * <h1>Edge Effect Override</h1>
 * 
 * <p>Enables you to override the color of the edge effects used in your application.</p>
 * 
 * <p>Can be applied with.</p>
 * <ul>
 * <li>{@code EdgeEffectOverride.createContextWrapper(context, color); }</li>
 * <li>{@code EdgeEffectOverride.createContextThemeWrapper(context, themeResId, color); }</li>
 * </ul>
 * 
 * @see {@link android.widget.EdgeEffect}
 * 
 * @author  Simon Lightfoot <simon@demondevelopers.com>
 * @since   30/10/2013
 * @version 1.4
 * 
 */
public class EdgeEffectOverride
{
	private static int mOverscrollEdge;
	private static int mOverscrollGlow;
	
	
	/**
	 * Use this as you would create a new ContextWrapper
	 * 
	 * @param base  Context to wrap
	 * @param color Color to apply to EdgeEffect drawables
	 * 
	 * @return ContextWrapper with color applied to edge drawables
	 */
	public static Context createContextWrapper(Context base, int color)
	{
		if(!resolvePlatformDrawables(base)){
			return base;
		}
		return new EdgeEffectContextWrapper(base, color);
	}
	
	
	/**
	 * Use this as you would create a new ContextThemeWrapper
	 * 
	 * @param base       Context to wrap
	 * @param themeResId Theme resource style id
	 * @param color      Color to apply to EdgeEffect drawables
	 * 
	 * @return ContextThemeWrapper with color applied to edge drawables
	 */
	public static Context createContextThemeWrapper(Context base, int themeResId, int color)
	{
		if(!resolvePlatformDrawables(base)){
			return base;
		}
		return new EdgeEffectContextThemeWrapper(base, themeResId, color);
	}
	
	/**
	 * Used to resolve internal platform resources by name rather than id.
	 */
	private static boolean resolvePlatformDrawables(Context context)
	{
		Resources res = context.getResources();
		if(mOverscrollEdge == 0){
			mOverscrollEdge = res.getIdentifier("android:drawable/overscroll_edge", null, null);
		}
		if(mOverscrollGlow == 0){
			mOverscrollGlow = res.getIdentifier("android:drawable/overscroll_glow", null, null);
		}
		return (mOverscrollEdge != 0 && mOverscrollGlow != 0);
	}
	
	
	public static class EdgeEffectResources extends Resources
	{
		private ColorMatrixColorFilter mColorMatrixFilter;
		
		
		private EdgeEffectResources(Resources res, int color)
		{
			super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
			mColorMatrixFilter = new ColorMatrixColorFilter(new ColorMatrix(new float[ ] {
				0, 0, 0, 0, Color.red(color),
				0, 0, 0, 0, Color.green(color),
				0, 0, 0, 0, Color.blue(color),
				0, 0, 0, 1, 0,
			}));
		}
		
		@Override
		public Drawable getDrawable(int id) throws NotFoundException
		{
			Drawable drawable = super.getDrawable(id);
			if(drawable != null && (id == mOverscrollEdge || id == mOverscrollGlow)){
				drawable.setColorFilter(mColorMatrixFilter);
			}
			return drawable;
		}
		
		@Override
		@TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		public Drawable getDrawableForDensity(int id, int density) throws NotFoundException
		{
			Drawable drawable = super.getDrawableForDensity(id, density);
			if(drawable != null && (id == mOverscrollEdge || id == mOverscrollGlow)){
				drawable.setColorFilter(mColorMatrixFilter);
			}
			return drawable;
		}
	}
	
	public static class EdgeEffectContextWrapper extends ContextWrapper
	{
		private EdgeEffectResources mResources;
		private LayoutInflater mInflater;
		
		
		private EdgeEffectContextWrapper(Context base, int color)
		{
			super(base);
			mResources = new EdgeEffectResources(super.getResources(), color);
		}
		
		@Override
		public Resources getResources()
		{
			return mResources;
		}
		
		/**
		 * Required so that the LayoutInflater uses this context to resolve resources.
		 */
		@Override
		public Object getSystemService(String name)
		{
			if(LAYOUT_INFLATER_SERVICE.equals(name)){
				if(mInflater == null){
					mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
				}
				return mInflater;
			}
			return super.getSystemService(name);
		}
	}
	
	public static class EdgeEffectContextThemeWrapper extends ContextThemeWrapper
	{
		private EdgeEffectResources mResources;
		
		
		private EdgeEffectContextThemeWrapper(Context base, int themeResId, int color)
		{
			super(base, themeResId);
			mResources = new EdgeEffectResources(super.getResources(), color);
		}
		
		@Override
		public Resources getResources()
		{
			return mResources;
		}
	}
}
