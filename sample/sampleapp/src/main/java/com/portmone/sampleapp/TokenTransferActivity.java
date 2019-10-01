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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.TokenTransferParams;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.util.Constant$ExtraKey;
import com.portmone.ecomsdk.util.Constant$Language;

import static com.portmone.ecomsdk.util.Constant$Language.EN;
import static com.portmone.ecomsdk.util.Constant$Language.RU;
import static com.portmone.ecomsdk.util.Constant$Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant$Language.UK;

public class TokenTransferActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spLanguage;
	private EditText etPayeeId;
	private EditText etAttribute1;
	private EditText etAttribute2;
	private EditText etAttribute3;
	private EditText etAttribute4;
	private EditText etAmount;
	private TextView tvResult;

	@Constant$Language
	private String[] languages = new String[] {
			SYSTEM, UK, EN, RU
	};
	private String card;
	private String token;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token_transfer);

		SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
		id = prefs.getString("id", null);
		card = prefs.getString("card", null);
		token = prefs.getString("token", null);

		tvResult = findViewById(R.id.tv_result);
		etPayeeId = findViewById(R.id.et_payee_id);
		etAttribute1 = findViewById(R.id.et_attribute_1);
		etAttribute2 = findViewById(R.id.et_attribute_2);
		etAttribute3 = findViewById(R.id.et_attribute_3);
		etAttribute4 = findViewById(R.id.et_attribute_4);
		etAmount = findViewById(R.id.et_amount);
		spLanguage = findViewById(R.id.sp_language);


		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.add(this.languages[1]);
		languages.add(this.languages[2]);
		languages.add(this.languages[3]);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);

//		etPayeeId.setText(id);
//		etPayeeId.setEnabled(false);
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.btn_open_payment_screen:
				if (card == null) {
					Toast.makeText(this, "No saved card", Toast.LENGTH_SHORT).show();
					return;
				}
				final int selectedLanguageId = spLanguage.getSelectedItemPosition();
				PortmoneSDK.setLanguage(languages[selectedLanguageId]);


				double billAmount = 0;
				try {
					billAmount = Double.parseDouble(etAmount.getText().toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				final TokenTransferParams bigParams = new TokenTransferParams(
						etPayeeId.getText().toString(),
						etAttribute1.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						billAmount,
						card,
						token
				);

				com.portmone.ecomsdk.ui.token.transfer.TokenTransferActivity.performTransaction(
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
			Log.d(
					"PaymentActivity",
					"onActivityResult:" + bill.toString()
			);
			tvResult.setText("Payment result:\n" + bill.toString());
		}
		if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.hasExtra(ThemeConfigureActivity.APP_STYLE)) {
			PortmoneSDK.setAppStyle((AppStyle) data.getSerializableExtra(ThemeConfigureActivity.APP_STYLE));
		}
	}
}