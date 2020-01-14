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
import android.text.TextUtils;
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
import com.portmone.ecomsdk.ui.token.payment.TokenPaymentActivity;
import com.portmone.ecomsdk.util.Constant$BillCurrency;
import com.portmone.ecomsdk.util.Constant$ExtraKey;
import com.portmone.ecomsdk.util.Constant$Language;
import com.portmone.ecomsdk.util.Constant$Type;

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
import static com.portmone.ecomsdk.util.Constant$Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant$Language.UK;

public class TokenActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spCurrency;
	private Spinner spLanguage;
	private Spinner spTypes;
	private EditText etPayeeId;
	private EditText etUID;
	private EditText etDescription;
	private EditText etAttribute1;
	private EditText etAttribute2;
	private EditText etAttribute3;
	private EditText etAttribute4;
	private EditText etAmount;
	private EditText etBillNumber;
	private CheckBox cbGPayEnabled;
	private CheckBox cbOnlyGooglePay;
	private CheckBox cbGoogleTest;
	private CheckBox cbPreauth;
	private CheckBox cbFingerprint;
	private TextView tvResult;

	@Constant$BillCurrency
	private String[] currencies = new String[]{
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	@Constant$Language
	private String[] languages = new String[] {
			SYSTEM, UK, EN, RU
	};

	@Constant$Type
	private int[] types = new int[] {
			Constant$Type.DEFAULT,
			Constant$Type.PHONE
	};

	private String[] typesTxt = new String[] {
			"DEFAULT",
			"PHONE"
	};

	private String card;
	private String token;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);

		SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
		id = prefs.getString("id", null);
		card = prefs.getString("card", null);
		token = prefs.getString("token", null);

		tvResult = findViewById(R.id.tv_result);
		etUID = findViewById(R.id.et_uid);
		etPayeeId = findViewById(R.id.et_payee_id);
		etBillNumber = findViewById(R.id.et_bill_number);
		etDescription = findViewById(R.id.et_description);
		etAttribute1 = findViewById(R.id.et_attribute_1);
		etAttribute2 = findViewById(R.id.et_attribute_2);
		etAttribute3 = findViewById(R.id.et_attribute_3);
		etAttribute4 = findViewById(R.id.et_attribute_4);
		etAmount = findViewById(R.id.et_amount);
		cbGPayEnabled = findViewById(R.id.cb_google_pay);
		cbOnlyGooglePay = findViewById(R.id.cb_only_google_pay);
		cbGoogleTest = findViewById(R.id.cb_test_google_pay);
		spCurrency = findViewById(R.id.sp_currency);
		spLanguage = findViewById(R.id.sp_language);
		spTypes = findViewById(R.id.sp_types);
		cbPreauth = findViewById(R.id.cb_preauth);
		cbFingerprint = findViewById(R.id.cb_fingerprint);

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

		etPayeeId.setText(id);
		//etPayeeId.setEnabled(false);

		spTypes.setAdapter(types);
		spTypes.setSelection(0);


		cbGPayEnabled.setOnCheckedChangeListener((compoundButton, checked) -> {
			if (checked) {
				cbOnlyGooglePay.setEnabled(true);
				cbGoogleTest.setEnabled(true);
				cbFingerprint.setEnabled(false);
				cbFingerprint.setChecked(false);
			} else {
				cbOnlyGooglePay.setChecked(false);
				cbGoogleTest.setChecked(false);
				cbOnlyGooglePay.setEnabled(false);
				cbGoogleTest.setEnabled(false);
				cbFingerprint.setEnabled(true);
			}
		});

		cbOnlyGooglePay.setOnCheckedChangeListener((buttonView, isChecked) -> {
			cbFingerprint.setEnabled(!isChecked);
			if (isChecked) {
				cbFingerprint.setChecked(false);
			}
		});
	}

	@Override
	public void onClick(final View v) {
		if (v.getId() == R.id.btn_open_payment_screen) {
			if (id == null&&!cbOnlyGooglePay.isChecked()) {
				Toast.makeText(this, "No saved card", Toast.LENGTH_SHORT).show();
				return;
			}
			String uid = etUID.getText().toString();
			if (TextUtils.isEmpty(uid)) uid = null;
			PortmoneSDK.setUid(uid);

			final int selectedLanguageId = spLanguage.getSelectedItemPosition();
			final int selectedType = spTypes.getSelectedItemPosition();
			PortmoneSDK.setLanguage(languages[selectedLanguageId]);

			PortmoneSDK.setFingerprintPaymentEnable(cbFingerprint.isChecked());

			AppStyle appStyle = PortmoneSDK.getAppStyle();
			if (appStyle == null) {
				appStyle = new AppStyle();
			}
			appStyle.setType(types[selectedType]);
			PortmoneSDK.setAppStyle(appStyle);

			if (etAmount.getText().toString().equals("")) {
				Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
				return;
			}

			if (etAmount.getText().toString().equals("")) {
				Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
				return;
			}

			final TokenPaymentParams bigParams;
			if (cbGPayEnabled.isChecked()) {
				bigParams = new TokenPaymentParams(
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
						etDescription.getText().toString(),
						cbOnlyGooglePay.isChecked(),
						cbGoogleTest.isChecked()
				);
			} else {
				bigParams = new TokenPaymentParams(
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
			}
			TokenPaymentActivity.performTransaction(
					this,
					114,
					bigParams
			);
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
	}
}