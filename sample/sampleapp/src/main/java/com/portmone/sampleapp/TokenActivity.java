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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.TokenPaymentParams;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.ui.token_payment.TokenPaymentActivity;
import com.portmone.ecomsdk.util.Constant$BillCurrency;
import com.portmone.ecomsdk.util.Constant$ExtraKey;
import com.portmone.ecomsdk.util.Constant$Language;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.portmone.ecomsdk.util.Constant$BillCurrency.BYN;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.EUR;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.GBP;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.KZT;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.RUB;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.UAH;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.USD;
import static com.portmone.ecomsdk.util.Constant$Language.EN;
import static com.portmone.ecomsdk.util.Constant$Language.RU;
import static com.portmone.ecomsdk.util.Constant$Language.UK;

public class TokenActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spCurrency;
	private Spinner spLanguage;
	private EditText etPayeeId;
	private EditText etDescription;
	private EditText etBillNumber;
	private EditText etAttribute1;
	private EditText etAttribute2;
	private EditText etAttribute3;
	private EditText etAttribute4;
	private EditText etAmount;
	private CheckBox cbPreauth;
	private TextView tvResult;

	@Constant$BillCurrency
	private String[] currencies = new String[]{
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	@Constant$Language
	private String[] languages = new String[]{
			UK, EN, RU
	};
	private String card;
	private String token;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
		id = prefs.getString("id", null);
		card = prefs.getString("card", null);
		token = prefs.getString("token", null);

		tvResult = findViewById(R.id.tv_result);
		etPayeeId = findViewById(R.id.et_payee_id);
		etDescription = findViewById(R.id.et_description);
		etBillNumber = findViewById(R.id.et_bill_number);
		etAttribute1 = findViewById(R.id.et_attribute_1);
		etAttribute2 = findViewById(R.id.et_attribute_2);
		etAttribute3 = findViewById(R.id.et_attribute_3);
		etAttribute4 = findViewById(R.id.et_attribute_4);
		etAmount = findViewById(R.id.et_amount);
		spCurrency = findViewById(R.id.sp_currency);
		spLanguage = findViewById(R.id.sp_language);
		cbPreauth = findViewById(R.id.cb_preauth);

		ArrayAdapter<String> currencies = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		currencies.addAll(this.currencies);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.addAll(this.languages);

		spCurrency.setAdapter(currencies);
		spCurrency.setSelection(0);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);

		etPayeeId.setText(id);
		etPayeeId.setEnabled(false);
	}


	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.btn_open_payment_screen:
				if (id == null) {
					Toast.makeText(this, "No saved card", Toast.LENGTH_SHORT).show();
					return;
				}
				final int selectedLanguageId = spLanguage.getSelectedItemPosition();
				if (selectedLanguageId != 0) {
					PortmoneSDK.setLanguage(languages[selectedLanguageId - 1]);
				}
				if (etAmount.getText().toString().equals("")) {
					Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
					return;
				}

				final TokenPaymentParams bigParams = new TokenPaymentParams(
						etPayeeId.getText().toString(),
						etBillNumber.getText().toString(),
						cbPreauth.isChecked(),
						currencies[spCurrency.getSelectedItemPosition()],
						etAttribute1.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						Double.parseDouble(etAmount.getText().toString()),
						card,
						token,
						etDescription.getText().toString()
				);

				TokenPaymentActivity.performTransaction(
						this,
						114,
						bigParams
				);
				break;
			case R.id.btn_open_theme_screen:

				startActivityForResult(new Intent(this, ThemeConfigureActivity.class), 112);
				break;

		}
	}

	@Override
	protected void onActivityResult(
			final int requestCode, final int resultCode, @Nullable final Intent data
	) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 114 && resultCode == RESULT_OK && data != null) {
			final Bill bill = (Bill) data.getSerializableExtra(Constant$ExtraKey.BILL);
			tvResult.setText("Payment result:\n" + bill.toString());
		}
		if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.hasExtra(ThemeConfigureActivity.APP_STYLE)) {
			PortmoneSDK.setAppStyle((AppStyle) data.getSerializableExtra(ThemeConfigureActivity.APP_STYLE));
		}
	}
}