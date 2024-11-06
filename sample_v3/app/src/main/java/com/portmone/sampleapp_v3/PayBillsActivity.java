package com.portmone.sampleapp_v3;

import static com.portmone.ecomsdk.util.Constant.Language.EN;
import static com.portmone.ecomsdk.util.Constant.Language.RU;
import static com.portmone.ecomsdk.util.Constant.Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant.Language.UK;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.MassPayBill;
import com.portmone.ecomsdk.data.PayBillsParams;
import com.portmone.ecomsdk.ui.masspay.MassPayContract;

import java.util.ArrayList;

public class PayBillsActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    String token;
    String contractNumber;
    String credentials;

    private TextView tvResult;
    private TextInputEditText et_id_1;
    private TextInputEditText et_amount_1;
    private TextInputEditText et_id_2;
    private TextInputEditText et_amount_2;
    private TextInputEditText et_id_3;
    private TextInputEditText et_amount_3;
    private TextInputEditText et_id_4;
    private TextInputEditText et_amount_4;

    private CheckBox chGPay;

    private Spinner spLanguage;

    //@Constant$Language
    private String[] languages = new String[]{
            SYSTEM, UK, EN, RU
    };

    private ActivityResultLauncher<PayBillsParams> resultLauncher =
            registerForActivityResult(new MassPayContract(),
                    result ->
                            result.handleResult((successResult) -> {
                                        tvResult.setText("Payment result, count payed bills:\n" + successResult.getBills().size());
                                        for (int i = 0; i < successResult.getBills().size(); i++) {
                                            tvResult.append("\nBillId = " + successResult.getBills().get(i).getBillId());
                                            tvResult.append("\nBillAmount = " + successResult.getBills().get(i).getBillAmount());
                                            tvResult.append("\nCommission = " + successResult.getBills().get(i).getCommission());
                                            tvResult.append("\nStatus = " + successResult.getBills().get(i).getStatus());
                                            tvResult.append("\nErrorCode = " + successResult.getBills().get(i).getErrorCode());
                                            tvResult.append("\nErrorMessage = " + successResult.getBills().get(i).getErrorMessage());
                                            tvResult.append("\n========================================");
                                        }
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
                                    }));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);

        SharedPreferences sharedPreferences = getSharedPreferences("testBills", MODE_PRIVATE);

        token = sharedPreferences.getString("token", "");
        contractNumber = sharedPreferences.getString("contractNumber", "");
        credentials = sharedPreferences.getString("credentials", "");

        TextView tvToken = findViewById(R.id.tv_token);
        TextView tvContractNumber = findViewById(R.id.tv_contractNumber);
        TextView tvCredentials = findViewById(R.id.tv_credentials);
        tvResult = findViewById(R.id.tv_status);

        chGPay = findViewById(R.id.checkBox);
        et_id_1 = findViewById(R.id.et_id_1);
        et_id_1.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_id_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_amount_1 = findViewById(R.id.et_amount_1);
        //et_amount_1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_amount_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_id_2 = findViewById(R.id.et_id_2);
        et_id_2.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_id_2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_amount_2 = findViewById(R.id.et_amount_2);
        //et_amount_2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_amount_2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_id_3 = findViewById(R.id.et_id_3);
        et_id_3.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_id_3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_amount_3 = findViewById(R.id.et_amount_3);
        //et_amount_3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_amount_3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_id_4 = findViewById(R.id.et_id_4);
        et_id_4.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_id_4.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_amount_4 = findViewById(R.id.et_amount_4);
        //et_amount_4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_amount_4.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        tvToken.setText("Token: " + token);
        tvContractNumber.setText("ContractNumber: " + contractNumber);
        tvCredentials.setText("Credentials: " + credentials);

        spLanguage = findViewById(R.id.sp_language);

        ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
        languages.add("System");
        languages.add(this.languages[1]);
        languages.add(this.languages[2]);
        languages.add(this.languages[3]);

        spLanguage.setAdapter(languages);
        spLanguage.setSelection(0);
    }

    private ArrayList<MassPayBill> getBills() {
        ArrayList<MassPayBill> bills = new ArrayList<>();

        if ((et_id_1.getText() != null &&
                !et_id_1.getText().toString().isEmpty()) &&
                (et_amount_1.getText() != null &&
                        !et_amount_1.getText().toString().isEmpty()))
            bills.add(new MassPayBill(et_id_1.getText().toString(), et_amount_1.getText().toString()));

        if ((et_id_2.getText() != null &&
                !et_id_2.getText().toString().isEmpty()) &&
                (et_amount_2.getText() != null &&
                        !et_amount_2.getText().toString().isEmpty()))
            bills.add(new MassPayBill(et_id_2.getText().toString(), et_amount_2.getText().toString()));

        if ((et_id_3.getText() != null &&
                !et_id_3.getText().toString().isEmpty()) &&
                (et_amount_3.getText() != null &&
                        !et_amount_3.getText().toString().isEmpty()))
            bills.add(new MassPayBill(et_id_3.getText().toString(), et_amount_3.getText().toString()));

        if ((et_id_4.getText() != null &&
                !et_id_4.getText().toString().isEmpty()) &&
                (et_amount_4.getText() != null &&
                        !et_amount_4.getText().toString().isEmpty()))
            bills.add(new MassPayBill(et_id_4.getText().toString(), et_amount_4.getText().toString()));

        return bills;
    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.btn_mass_pay_bills) {

            final int selectedLanguageId = spLanguage.getSelectedItemPosition();
            PortmoneSDK.setLanguage(languages[selectedLanguageId]);

            if (token != null && !token.isEmpty()
                    && contractNumber != null && !contractNumber.isEmpty()
                    && credentials != null && !credentials.isEmpty()) {
                final PayBillsParams payBillsParams = new PayBillsParams(
                        getBills(),
                        token,
                        contractNumber,
                        credentials,
                        getBills().size() == 1 && chGPay.isChecked()
                );
                tvResult.setText("");
                resultLauncher.launch(payBillsParams);
            } else {
                Toast.makeText(
                        this,
                        "PortmoneSDK: empty list of bills or empty token/credentials/contractNumber", Toast.LENGTH_LONG
                ).show();
            }
        }

    }
}
