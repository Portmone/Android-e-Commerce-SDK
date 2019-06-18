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
import android.view.View;
import android.widget.EditText;

import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.data.style.BlockTitleTextStyle;
import com.portmone.ecomsdk.data.style.ButtonStyle;
import com.portmone.ecomsdk.data.style.DialogInfoStyle;
import com.portmone.ecomsdk.data.style.EditTextStyle;
import com.portmone.ecomsdk.data.style.TextStyle;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeConfigureActivity
		extends AppCompatActivity
		implements View.OnClickListener {
	public static String APP_STYLE = "APP_STYLE";
	EditText etBackground;
	EditText etToolbar;
	EditText etBackgroundSecondary;
	EditText etBtnBackground;
	EditText etBtnBackgroundPressed;
	EditText etBtnText;
	EditText etBtnTextPressed;
	EditText etInputText;
	EditText etInputHint;
	EditText etInputError;
	EditText etBlockTitle;
	EditText etTitle;
	EditText etDescription;
	EditText etAddInfo;
	EditText etDialogTitle;
	EditText etDialogDescription;
	private AppStyle appStyle = new AppStyle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_configure);

		etBackground = findViewById(R.id.et_background);
		etToolbar = findViewById(R.id.et_toolbar);
		etBackgroundSecondary = findViewById(R.id.et_background_input);
		etBtnBackground = findViewById(R.id.et_btn_back);
		etBtnBackgroundPressed = findViewById(R.id.et_btn_back_pressed);
		etBtnText = findViewById(R.id.et_btn_text);
		etBtnTextPressed = findViewById(R.id.et_btn_text_pressed);
		etInputText = findViewById(R.id.et_input_txt);
		etInputHint = findViewById(R.id.et_input_hint);
		etInputError = findViewById(R.id.et_input_error);
		etBlockTitle = findViewById(R.id.et_block_title_txt);
		etTitle = findViewById(R.id.et_title_txt);
		etDescription = findViewById(R.id.et_description_txt);
		etAddInfo = findViewById(R.id.et_additional_info);
		etDialogTitle = findViewById(R.id.et_dialog_title);
		etDialogDescription = findViewById(R.id.et_dialog_description);
	}

	@Override
	public void onClick(final View v) {
		if (v.getId() == R.id.btn_submit) {
			appStyle.setBackground(getColor(etBackground));
			appStyle.setToolbarColor(getColor(etToolbar));
			fillEditTextStyle();
			fillButtonStyle();
			fillTextBlockStyle();
			fillTextTitleStyle();
			fillTextStyle();
			fillAddInfoTextStyle();
			fillDialogTextStyle();

			Intent intent = new Intent();
			intent.putExtra(APP_STYLE, appStyle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private int getColor(EditText et) {
		try {
			return Color.parseColor(et.getText().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void fillEditTextStyle() {
		EditTextStyle editTextStyle = new EditTextStyle();
		editTextStyle.setTextColor(getColor(etInputText));
		editTextStyle.setHintTextColor(getColor(etInputHint));
		editTextStyle.setErrorTextColor(getColor(etInputError));

		appStyle.setEditTextStyle(editTextStyle);
	}

	private void fillButtonStyle() {
		ButtonStyle btnStyle = new ButtonStyle();
		btnStyle.setTextColor(getColor(etBtnText));
		btnStyle.setTextColorPressed(getColor(etBtnTextPressed));
		btnStyle.setBackgroundColor(getColor(etBtnBackground));
		btnStyle.setBackgroundColorPressed(getColor(etBtnBackgroundPressed));

		appStyle.setButtonStyle(btnStyle);
	}

	private void fillTextBlockStyle() {
		BlockTitleTextStyle txtStyle = new BlockTitleTextStyle();
		txtStyle.setTextColor(getColor(etBlockTitle));
		txtStyle.setBackgroundColor(getColor(etBackgroundSecondary));

		appStyle.setBlockTitleTextStyle(txtStyle);
	}

	private void fillTextTitleStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(getColor(etTitle));

		appStyle.setTitleTextStyle(txtStyle);
	}

	private void fillAddInfoTextStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(getColor(etAddInfo));

		appStyle.setAdditionalInfoTextStyle(txtStyle);
	}

	private void fillDialogTextStyle() {
		DialogInfoStyle dialogInfoStyle = new DialogInfoStyle();
		TextStyle txtTitle = new TextStyle();
		txtTitle.setTextColor(getColor(etDialogTitle));

		TextStyle txtDescription = new TextStyle();
		txtDescription.setTextColor(getColor(etDialogDescription));
		dialogInfoStyle.setTitle(txtTitle);
		dialogInfoStyle.setDescription(txtDescription);

		appStyle.setDialogInfoStyle(dialogInfoStyle);
	}

	private void fillTextStyle() {
		TextStyle txtStyle = new TextStyle();
		txtStyle.setTextColor(getColor(etDescription));

		appStyle.setDescriptionTextStyle(txtStyle);
	}
}
