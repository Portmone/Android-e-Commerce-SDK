package com.portmone.sampleapp_v3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.portmone.sampleapp_v3.R;

/**
 * @author Taras Yurkiv
 * Email TYurkiv1995@gmail.com
 * @since 03.12.2019
 */
public class ChooseColorView extends LinearLayout {
	private TextView txtTitle;
	private TextView txtValue;
	private View colorView;

	private int color = -1;
	private String title;

	public ChooseColorView(Context context) {
		this(context, null);
	}

	public ChooseColorView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		inflate(context, R.layout.layout_choose_color, this);

		setGravity(Gravity.CENTER_VERTICAL);

		txtTitle = findViewById(R.id.tv_choose_color_title);
		txtValue = findViewById(R.id.tv_choose_color_value);
		colorView = findViewById(R.id.choose_color_color);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChooseColorView);
		try {
			color = typedArray.getInt(R.styleable.ChooseColorView_ccv_color,-1);
			title = typedArray.getString(R.styleable.ChooseColorView_android_text);

		} finally {
			typedArray.recycle();
		}

		setTitle(title);
		setColor(color);
	}

	public int getColor() {
		return color;
	}

	public String getTitle() {
		return title;
	}

	public void setColor(int color) {
		this.color = color;

		if (color == -1) {
			txtValue.setText("Not selected");
			colorView.setBackgroundColor(Color.TRANSPARENT);
		} else {
			txtValue.setText(String.format("#%s", Integer.toHexString(color)));
			colorView.setBackgroundColor(color);
		}
	}

	public void setTitle(String title) {
		this.title = title;
		txtTitle.setText(title);
	}
}
