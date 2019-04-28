package com.app.pao.ui.widget.tagview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Tag Entity
 */
public class Tag {

	public int     id;
	public CharSequence  text;
	public CharSequence textPlus;
	public int     tagTextColor;
	public int tagPlusTextColor;
	public float   tagTextSize;
	public float tagPlusTextSize;
	public int     layoutColor;
	public int     layoutColorPress;
	public boolean isDeletable;
	public boolean isShowImg;
	public int     deleteIndicatorColor;
	public float   deleteIndicatorSize;
	public float   radius;
	public String  deleteIcon;
	public float   layoutBorderSize;
	public int     layoutBorderColor;
	public Drawable background;
	public int imgDrawableRes;
	public int mTempValue;



	public Tag(CharSequence text) {
		init(0, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_LAYOUT_COLOR, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
				Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);
	}

	public Tag(CharSequence text,CharSequence textPlus){
		init(0, text,textPlus, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_LAYOUT_COLOR, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
			Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);

	}

	public Tag(CharSequence text, int color) {
		init(0, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, color, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS, Constants.DEFAULT_TAG_IS_DELETABLE,
				Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);

	}

//	public Tag(CharSequence text, String color) {
//		init(0, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, Color.parseColor(color), Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
//				Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);
//
//	}

	public Tag(int id, CharSequence text) {
		init(id, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_LAYOUT_COLOR, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
				Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);
	}

	public Tag(int id, CharSequence text, int color) {
		init(id, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, color, Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS, Constants.DEFAULT_TAG_IS_DELETABLE,
				Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);

	}

	public Tag(int id, CharSequence text, String color) {
		init(id, text,Constants.DEFAULT_TAG_TEXT, Constants.DEFAULT_TAG_TEXT_COLOR,Constants.DEFAULT_TAG_TEXT_COLOR, Constants.DEFAULT_TAG_TEXT_SIZE, Constants.DEFAULT_TAG_TEXT_SIZE, Color.parseColor(color), Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
				Constants.DEFAULT_TAG_IS_DELETABLE, Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR, Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE, Constants.DEFAULT_TAG_RADIUS, Constants.DEFAULT_TAG_DELETE_ICON, Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE, Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR,Constants.DEFAULT_SHOW_TAG_IMG);

	}

	private void init(int id, CharSequence text, CharSequence textPlus,int tagTextColor,int tagPlusTextColor,float tagPlusTextSize ,float tagTextSize, int layout_color, int layout_color_press, boolean isDeletable, int deleteIndicatorColor,
	                  float deleteIndicatorSize, float radius, String deleteIcon, float layoutBorderSize, int layoutBorderColor,boolean isShowImg) {
		this.id = id;
		this.text = text;
		this.textPlus = textPlus;
		this.tagTextColor = tagTextColor;
		this.tagPlusTextColor = tagPlusTextColor;
		this.tagPlusTextSize = tagPlusTextSize;
		this.tagTextSize = tagTextSize;
		this.layoutColor = layout_color;
		this.layoutColorPress = layout_color_press;
		this.isDeletable = isDeletable;
		this.deleteIndicatorColor = deleteIndicatorColor;
		this.deleteIndicatorSize = deleteIndicatorSize;
		this.radius = radius;
		this.deleteIcon = deleteIcon;
		this.layoutBorderSize = layoutBorderSize;
		this.layoutBorderColor = layoutBorderColor;
		this.isShowImg = isShowImg;
	}
}
