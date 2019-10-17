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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.CardPaymentParams;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.ui.card.CardPaymentActivity;
import com.portmone.ecomsdk.util.Constant$BillCurrency;
import com.portmone.ecomsdk.util.Constant$ExtraKey;
import com.portmone.ecomsdk.util.Constant$Language;
import com.portmone.ecomsdk.util.Constant$Type;

import static com.portmone.ecomsdk.util.Constant$BillCurrency.BYN;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.EUR;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.GBP;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.KZT;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.RUB;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.UAH;
import static com.portmone.ecomsdk.util.Constant$BillCurrency.USD;
import static com.portmone.ecomsdk.util.Constant$Language.EN;
import static com.portmone.ecomsdk.util.Constant$Language.RU;
import static com.portmone.ecomsdk.util.Constant$Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant$Language.UK;


public class CardActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spCurrency;
	private Spinner spLanguage;
	private Spinner spTypes;
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
	private String[] currencies = new String[] {
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	@Constant$Language
	private String[] languages = new String[] {
			SYSTEM,
			UK,
			EN,
			RU
	};

	@Constant$Type
	private int[] types = new int[]{
			Constant$Type.DEFAULT,
			Constant$Type.PHONE
	};

	private String[] typesTxt = new String[]{
			"DEFAULT",
			"PHONE"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
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
		spTypes = findViewById(R.id.sp_types);
		cbPreauth = findViewById(R.id.cb_preauth);

		ArrayAdapter<String> currencies = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		currencies.addAll(this.currencies);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.add(this.languages[1]);
		languages.add(this.languages[2]);
		languages.add(this.languages[3]);

		ArrayAdapter<String> types = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		types.addAll(this.typesTxt);

		spCurrency.setAdapter(currencies);
		spCurrency.setSelection(0);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);

		spTypes.setAdapter(types);
		spTypes.setSelection(0);
	}


	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.btn_open_payment_screen:
				final int selectedLanguageId = spLanguage.getSelectedItemPosition();
				final int selectedType = spTypes.getSelectedItemPosition();

				PortmoneSDK.setLanguage(languages[selectedLanguageId]);
				if (etAmount.getText().toString().equals("")) {
					Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
					return;
				}

				AppStyle appStyle = PortmoneSDK.getAppStyle();
				if (appStyle == null) {
					appStyle = new AppStyle();
				}
				appStyle.setType(types[selectedType]);
				PortmoneSDK.setAppStyle(appStyle);

				final CardPaymentParams bigParams = new CardPaymentParams(
						etPayeeId.getText().toString(),
						etBillNumber.getText().toString(),
						cbPreauth.isChecked(),
						currencies[spCurrency.getSelectedItemPosition()],
						etAttribute1.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						Double.parseDouble(etAmount.getText().toString()),
						etDescription.getText().toString()
				);
				CardPaymentActivity.performTransaction(
						this,
						111,
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

		if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
			final Bill bill = (Bill) data.getSerializableExtra(Constant$ExtraKey.BILL);
			tvResult.setText("Payment result:\n" + bill.toString());
			saveCard(bill);
		}
		if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.hasExtra(ThemeConfigureActivity.APP_STYLE)) {
			PortmoneSDK.setAppStyle((AppStyle) data.getSerializableExtra(ThemeConfigureActivity.APP_STYLE));
		}
	}

	private void saveCard(Bill shopBill) {
		SharedPreferences.Editor editor = getSharedPreferences("test", MODE_PRIVATE).edit();

		editor.putString("id", etPayeeId.getText().toString());
		editor.putString("card", shopBill.getCardMask());
		editor.putString("token", shopBill.getToken());
		editor.apply();
	}
}