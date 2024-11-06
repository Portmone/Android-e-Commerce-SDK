package com.portmone.sampleapp_v3;


import static com.portmone.ecomsdk.util.Constant.Language.EN;
import static com.portmone.ecomsdk.util.Constant.Language.RU;
import static com.portmone.ecomsdk.util.Constant.Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant.Language.UK;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.TokenTransferParams;
import com.portmone.ecomsdk.data.contract_params.TokenTransferContractParams;
import com.portmone.ecomsdk.ui.token.transfer.TokenTransferContract;

//import static com.portmone.ecomsdk.util.Constant.Language.EN;
//import static com.portmone.ecomsdk.util.Constant.Language.RU;
//import static com.portmone.ecomsdk.util.Constant.Language.SYSTEM;
//import static com.portmone.ecomsdk.util.Constant.Language.UK;


public class TokenTransferActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spLanguage;
	private EditText etUID;
	private EditText etPayeeId;
	private EditText etShopBillId;
	//private EditText etAttribute1;
	private EditText etAttribute2;
	private EditText etAttribute3;
	private EditText etAttribute4;
	private EditText etAmount;
	private CheckBox cbReceipt;
	private CheckBox cbDisableReturnToDetails;
	private TextView tvResult;

	//@Constant$Language
	private String[] languages = new String[] {
			SYSTEM, UK, EN, RU
	};
	private String card;
	private String token;
	private String id;

	private ActivityResultLauncher<TokenTransferContractParams> resultLauncher = registerForActivityResult(
			new TokenTransferContract(),
			result -> {
				result.handleResult(
						(successResult) -> {
							tvResult.setText("Payment result:\n" + successResult.getBill().toString());
							return null;
							},
						(failureResult) -> {
							tvResult.setText("Payment error:\n" + "Code" + failureResult.getCode() +
									"\n" + failureResult.getMessage());
							return null;
						},
						() -> {
							tvResult.setText("Payment has been cancelled");
							return null;
						});
			}
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token_transfer);

		SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
		id = prefs.getString("id", null);
		card = prefs.getString("card", null);
		token = prefs.getString("token", null);

		tvResult = findViewById(R.id.tv_result);
		etUID = findViewById(R.id.et_uid);
		etPayeeId = findViewById(R.id.et_payee_id);
		etShopBillId = findViewById(R.id.et_shop_bill_id);
		//etAttribute1 = findViewById(R.id.et_attribute_1);
		etAttribute2 = findViewById(R.id.et_attribute_2);
		etAttribute3 = findViewById(R.id.et_attribute_3);
		etAttribute4 = findViewById(R.id.et_attribute_4);
		cbReceipt = findViewById(R.id.cb_receipt);
		cbDisableReturnToDetails = findViewById(R.id.cb_return_to_details);
		etAmount = findViewById(R.id.et_amount);
		spLanguage = findViewById(R.id.sp_language);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.add(this.languages[1]);
		languages.add(this.languages[2]);
		languages.add(this.languages[3]);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);

	}

	@Override
	public void onClick(final View v) {
		if(v.getId() == R.id.btn_open_payment_screen) {
				if (id == null) {
					Toast.makeText(this, "No saved card", Toast.LENGTH_SHORT).show();
					return;
				}
				String uid = etUID.getText().toString();
				if (TextUtils.isEmpty(uid)) uid = null;
				PortmoneSDK.setUid(uid);

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
						etShopBillId.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						billAmount,
						card,
						token
				);

				resultLauncher.launch(new TokenTransferContractParams(
						bigParams,
						cbReceipt.isChecked(),
						!cbDisableReturnToDetails.isChecked())
				);
		}
	}
}