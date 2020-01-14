//  Created on 2/18/19.
//  Copyright © 2019 Portmone. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
package com.portmone.sampleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.data.style.BlockTitleTextStyle;
import com.portmone.ecomsdk.data.style.ButtonStyle;

import com.portmone.ecomsdk.data.style.DialogStyle;
import com.portmone.ecomsdk.data.style.EditTextStyle;
import com.portmone.ecomsdk.data.style.TextStyle;
import com.portmone.sampleapp.widget.ChooseColorView;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeConfigureActivity
		extends AppCompatActivity
		implements View.OnClickListener, ColorPickerDialogListener {
	public static String APP_STYLE = "APP_STYLE";
	private ChooseColorView ccvBackground;
	private ChooseColorView ccvToolbar;
	private ChooseColorView ccvBtnBackground;
	private ChooseColorView ccvBtnBackgroundPressed;
	private ChooseColorView ccvBtnText;
	private ChooseColorView ccvBtnTextPressed;
	private EditText etBtnCornerRadius;
	private ChooseColorView ccvFPBtnText;
	private ChooseColorView ccvPaymentDivider;
	private ChooseColorView ccvInputText;
	private ChooseColorView ccvInputHint;
	private ChooseColorView ccvInputError;
	private ChooseColorView ccvBlockTitle;
	private ChooseColorView ccvBackgroundSecondary;
	private ChooseColorView ccvTitle;
	private ChooseColorView ccvDescription;
	private ChooseColorView ccvAddInfo;
	private ChooseColorView ccvPaymentSuccessDownload;
	private ChooseColorView ccvDialogTitle;
	private ChooseColorView ccvDialogDescription;
	private ChooseColorView ccvDialogButton;

	private ChooseColorView chooseColorView;

	private AppStyle appStyle;
	private ColorPickerDialog colorPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appStyle = PortmoneSDK.getAppStyle();
		if (appStyle == null) appStyle = new AppStyle();
		else appStyle = new AppStyle(appStyle);
		setContentView(R.layout.activity_theme_configure);

		ccvBackground = findViewById(R.id.ccv_background);
		ccvToolbar = findViewById(R.id.ccv_toolbar);

		ccvBtnBackground = findViewById(R.id.ccv_btn_color);
		ccvBtnBackgroundPressed = findViewById(R.id.ccv_btn_color_pressed);
		ccvBtnText = findViewById(R.id.ccv_btn_text);
		ccvBtnTextPressed = findViewById(R.id.ccv_btn_text_pressed);
		etBtnCornerRadius = findViewById(R.id.et_btn_corner_radius);

		ccvPaymentDivider = findViewById(R.id.ccv_payment_divider);
		ccvFPBtnText = findViewById(R.id.ccv_fp_btn_text);
		ccvInputText = findViewById(R.id.ccv_input_txt);
		ccvInputHint = findViewById(R.id.ccv_input_hint);
		ccvInputError = findViewById(R.id.ccv_input_error);
		ccvBlockTitle = findViewById(R.id.ccv_block_title_txt);
		ccvBackgroundSecondary = findViewById(R.id.ccv_background_input);
		ccvTitle = findViewById(R.id.ccv_title_txt);
		ccvDescription = findViewById(R.id.ccv_description_txt);
		ccvAddInfo = findViewById(R.id.ccv_additional_info);
		ccvPaymentSuccessDownload = findViewById(R.id.ccv_payment_success_download);
		ccvDialogTitle = findViewById(R.id.ccv_dialog_title);
		ccvDialogDescription = findViewById(R.id.ccv_dialog_description);
		ccvDialogButton = findViewById(R.id.ccv_dialog_button);

		ccvBackground.setOnClickListener(this);
		ccvToolbar.setOnClickListener(this);
		ccvBtnBackground.setOnClickListener(this);
		ccvBtnBackgroundPressed.setOnClickListener(this);
		ccvBtnText.setOnClickListener(this);
		ccvBtnTextPressed.setOnClickListener(this);
		ccvPaymentDivider.setOnClickListener(this);
		ccvFPBtnText.setOnClickListener(this);
		ccvInputText.setOnClickListener(this);
		ccvInputHint.setOnClickListener(this);
		ccvInputError.setOnClickListener(this);
		ccvBlockTitle.setOnClickListener(this);
		ccvBackgroundSecondary.setOnClickListener(this);
		ccvTitle.setOnClickListener(this);
		ccvDescription.setOnClickListener(this);
		ccvAddInfo.setOnClickListener(this);
		ccvPaymentSuccessDownload.setOnClickListener(this);
		ccvDialogTitle.setOnClickListener(this);
		ccvDialogDescription.setOnClickListener(this);
		ccvDialogButton.setOnClickListener(this);

		configureUIForStyle(appStyle);
	}

	private void configureUIForStyle(AppStyle appStyle) {
		ccvBackground.setColor(appStyle.getBackground());
		ccvToolbar.setColor(appStyle.getToolbarColor());

		ButtonStyle buttonStyle = appStyle.getButtonStyle();
		if (buttonStyle == null) buttonStyle = new ButtonStyle();
		ccvBtnBackground.setColor(buttonStyle.getBackgroundColor());
		ccvBtnBackgroundPressed.setColor(buttonStyle.getBackgroundColorPressed());
		ccvBtnText.setColor(buttonStyle.getTextColor());
		ccvBtnTextPressed.setColor(buttonStyle.getTextColorPressed());
		etBtnCornerRadius.setText(buttonStyle.getCornerRadius() == -1 ? null : Float.toString(px2Dp(buttonStyle.getCornerRadius())));

		setTextColor(ccvPaymentDivider, appStyle.getPaymentDivider());

		TextStyle fingerprintButtonStyle = appStyle.getFingerprintButton();
		if (fingerprintButtonStyle == null) fingerprintButtonStyle = new TextStyle();
		ccvFPBtnText.setColor(fingerprintButtonStyle.getTextColor());

		EditTextStyle editTextStyle = appStyle.getEditTextStyle();
		if (editTextStyle == null) editTextStyle = new EditTextStyle();
		ccvInputText.setColor(editTextStyle.getTextColor());
		ccvInputHint.setColor(editTextStyle.getHintTextColor());
		ccvInputError.setColor(editTextStyle.getErrorTextColor());

		BlockTitleTextStyle blockStyle = appStyle.getBlockTitleTextStyle();
		if (blockStyle == null) blockStyle = new BlockTitleTextStyle();
		ccvBlockTitle.setColor(blockStyle.getTextColor());
		ccvBackgroundSecondary.setColor(blockStyle.getBackgroundColor());

		setTextColor(ccvTitle, appStyle.getTitleTextStyle());
		setTextColor(ccvDescription, appStyle.getDescriptionTextStyle());
		setTextColor(ccvAddInfo, appStyle.getAdditionalInfoTextStyle());
		setTextColor(ccvPaymentSuccessDownload, appStyle.getPaymentSuccessDownload());

		DialogStyle dialogStyle = appStyle.getDialogStyle();
		if (dialogStyle == null) dialogStyle = new DialogStyle();

		setTextColor(ccvDialogTitle, dialogStyle.getTitle());
		setTextColor(ccvDialogDescription, dialogStyle.getDescription());
		setTextColor(ccvDialogButton, dialogStyle.getButton());
	}

	private void setTextColor(ChooseColorView ccv, TextStyle style) {
		if (style == null) style = new TextStyle();
		ccv.setColor(style.getTextColor());
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.btn_clear:
				appStyle = new AppStyle();
				configureUIForStyle(appStyle);
				break;
			case R.id.btn_submit:
				appStyle.setBackground(ccvBackground.getColor());
				appStyle.setToolbarColor(ccvToolbar.getColor());
				fillEditTextStyle();
				fillButtonStyle();
				fillTextBlockStyle();
				fillTextTitleStyle();
				fillPaymentDividerTextStyle();
				fillTextStyle();
				fillFPButtonStyle();
				fillAddInfoTextStyle();
				fillPaymentSuccessDownload();
				fillDialogTextStyle();

				PortmoneSDK.setAppStyle(appStyle);

				setResult(RESULT_OK);
				finish();
				break;
			case R.id.ccv_background:
			case R.id.ccv_toolbar:
			case R.id.ccv_background_input:
			case R.id.ccv_btn_color:
			case R.id.ccv_btn_color_pressed:
			case R.id.ccv_btn_text:
			case R.id.ccv_btn_text_pressed:
			case R.id.ccv_payment_divider:
			case R.id.ccv_fp_btn_text:
			case R.id.ccv_input_txt:
			case R.id.ccv_input_hint:
			case R.id.ccv_input_error:
			case R.id.ccv_block_title_txt:
			case R.id.ccv_title_txt:
			case R.id.ccv_description_txt:
			case R.id.ccv_additional_info:
			case R.id.ccv_payment_success_download:
			case R.id.ccv_dialog_title:
			case R.id.ccv_dialog_description:
			case R.id.ccv_dialog_button:
				chooseColorView = (ChooseColorView) v;
				if (colorPicker != null) {
					colorPicker.dismissAllowingStateLoss();
				}
				colorPicker = ColorPickerDialog.newBuilder()
						.setAllowPresets(true)
						.setDialogType(ColorPickerDialog.TYPE_PRESETS)
						.setColor(chooseColorView.getColor())
						.setShowAlphaSlider(true)
						.setShowColorShades(true)
						.create();
				colorPicker.setColorPickerDialogListener(this);
				colorPicker.show(getSupportFragmentManager(), ColorPickerDialog.class.getSimpleName());
				break;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (colorPicker != null) {
			colorPicker.dismissAllowingStateLoss();
		}
	}

	private void fillFPButtonStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvFPBtnText.getColor());

		appStyle.setFingerprintButton(txtStyle);
	}

	private void fillEditTextStyle() {
		EditTextStyle editTextStyle = new EditTextStyle();
		editTextStyle.setTextColor(ccvInputText.getColor());
		editTextStyle.setHintTextColor(ccvInputHint.getColor());
		editTextStyle.setErrorTextColor(ccvInputError.getColor());

		appStyle.setEditTextStyle(editTextStyle);
	}

	private void fillButtonStyle() {
		ButtonStyle btnStyle = new ButtonStyle();
		btnStyle.setTextColor(ccvBtnText.getColor());
		btnStyle.setTextColorPressed(ccvBtnTextPressed.getColor());
		btnStyle.setBackgroundColor(ccvBtnBackground.getColor());
		btnStyle.setBackgroundColorPressed(ccvBtnBackgroundPressed.getColor());
		try {
			btnStyle.setCornerRadius(dp2Px(Float.parseFloat(etBtnCornerRadius.getText().toString())));
		} catch (Exception ignore) {
		}

		appStyle.setButtonStyle(btnStyle);
	}

	public  float dp2Px(float dp) {
		return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	public float px2Dp(float px) {
		return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	private void fillTextBlockStyle() {
		BlockTitleTextStyle txtStyle = new BlockTitleTextStyle();
		txtStyle.setTextColor(ccvBlockTitle.getColor());
		txtStyle.setBackgroundColor(ccvBackgroundSecondary.getColor());

		appStyle.setBlockTitleTextStyle(txtStyle);
	}

	private void fillTextTitleStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvTitle.getColor());

		appStyle.setTitleTextStyle(txtStyle);
	}

	private void fillPaymentDividerTextStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvPaymentDivider.getColor());

		appStyle.setPaymentDivider(txtStyle);
	}

	private void fillAddInfoTextStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvAddInfo.getColor());

		appStyle.setAdditionalInfoTextStyle(txtStyle);
	}

	private void fillPaymentSuccessDownload() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvPaymentSuccessDownload.getColor());

		appStyle.setPaymentSuccessDownload(txtStyle);
	}

	private void fillDialogTextStyle() {
		DialogStyle dialogStyle = new DialogStyle();

		TextStyle txtTitle = new TextStyle();
		txtTitle.setTextColor(ccvDialogTitle.getColor());

		TextStyle txtDescription = new TextStyle();
		txtDescription.setTextColor(ccvDialogDescription.getColor());

		TextStyle txtButton = new TextStyle();
		txtButton.setTextColor(ccvDialogButton.getColor());

		dialogStyle.setTitle(txtTitle);
		dialogStyle.setDescription(txtDescription);
		dialogStyle.setButton(txtButton);

		appStyle.setDialogStyle(dialogStyle);
	}

	private void fillTextStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(ccvDescription.getColor());

		appStyle.setDescriptionTextStyle(txtStyle);
	}

	@Override
	public void onColorSelected(int dialogId, int color) {
		if (chooseColorView != null) {
			chooseColorView.setColor(color);
		}
	}

	@Override
	public void onDialogDismissed(int dialogId) {

	}
}
