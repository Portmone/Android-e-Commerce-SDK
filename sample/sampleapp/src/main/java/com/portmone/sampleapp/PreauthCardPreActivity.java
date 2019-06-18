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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.SaveCardParams;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.ui.preauth_card.PreauthCardActivity;
import com.portmone.ecomsdk.util.Constant$BillCurrency;
import com.portmone.ecomsdk.util.Constant$ExtraKey;
import com.portmone.ecomsdk.util.Constant$Language;
import com.portmone.sampleapp.R;

import java.util.Calendar;

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

public class PreauthCardPreActivity
		extends AppCompatActivity
		implements View.OnClickListener {

	private Spinner spLanguage;
	private EditText etPayeeId;
	private EditText etDescription;
	private TextView tvResult;

	@Constant$BillCurrency
	private String[] currencies = new String[]{
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	@Constant$Language
	private String[] languages = new String[]{
			UK, EN, RU
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preauth_main);
		tvResult = findViewById(R.id.tv_result);
		etPayeeId = findViewById(R.id.et_payee_id);

		etDescription = findViewById(R.id.et_description);

		spLanguage = findViewById(R.id.sp_language);

		ArrayAdapter<String> currencies = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		currencies.addAll(this.currencies);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.addAll(this.languages);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);
	}


	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.btn_open_preauth:
				final int selectedLanguageId = spLanguage.getSelectedItemPosition();
				if (selectedLanguageId != 0) {
					PortmoneSDK.setLanguage(languages[selectedLanguageId - 1]);
				}

				final SaveCardParams bigParams = new SaveCardParams(
						etPayeeId.getText().toString(),
						etDescription.getText().toString(),
						generateNumber()
				);

				PreauthCardActivity.performTransaction(
						this,
						113,
						bigParams
				);
				break;
			case R.id.btn_open_theme_screen:

				startActivityForResult(new Intent(this, ThemeConfigureActivity.class), 112);
				break;

		}
	}

	private String generateNumber() {
		// test bil number generation replace with real one

		return "Test" + Calendar.getInstance().getTime().hashCode();
	}

	@Override
	protected void onActivityResult(
			final int requestCode, final int resultCode, @Nullable final Intent data
	) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 113 && resultCode == RESULT_OK && data != null) {
			final Bill bill = (Bill) data.getSerializableExtra(Constant$ExtraKey.BILL);
			Log.d(
					"PaymentActivity",
					"onActivityResult:" + bill.toString()
			);
			saveCard(bill);
			tvResult.setText("Payment result:\n" + bill.toString());
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