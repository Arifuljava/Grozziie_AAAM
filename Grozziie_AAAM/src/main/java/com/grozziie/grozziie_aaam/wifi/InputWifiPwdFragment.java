package com.grozziie.grozziie_aaam.wifi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.grozziie.grozziie_aaam.R;

import static com.grozziie.grozziie_aaam.wifi.Constants.KEY_WIFI_SSID;


/**
 *
 */

public class InputWifiPwdFragment extends DialogFragment {


    TextView mNameView;
    EditText mPwdView;
    private WifiPwdInputListener mListener;
    private ScanResult mScanResult;

    public static InputWifiPwdFragment newInstance(ScanResult result) {
        InputWifiPwdFragment fragment = new InputWifiPwdFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_WIFI_SSID, result);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogFragment show(FragmentActivity activity, ScanResult result) {
        DialogFragment dialogFragment = newInstance(result);
        dialogFragment.show(activity.getSupportFragmentManager(), InputWifiPwdFragment.class.getSimpleName());
        return dialogFragment;
    }


    void connect() {
        String pwd = mPwdView.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(contentView.getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contentView.getContext(), ""+pwd, Toast.LENGTH_SHORT).show();
            mListener.onWifiPwdInputCallback(mScanResult, pwd);
        }
    }
    View contentView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
         contentView = inflater.inflate(R.layout.fragment_wifi_pwd, container, false);
        mNameView = contentView.findViewById(R.id.name);
        mPwdView = contentView.findViewById(R.id.pwd);

        contentView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               connect();
            }
        });

        if (getArguments() != null) {
            mScanResult = getArguments().getParcelable(KEY_WIFI_SSID);
            if (mScanResult != null) {
                mNameView.setText(mScanResult.SSID);
            }
        }
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WifiPwdInputListener) {
            mListener = (WifiPwdInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WifiPwdInputListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface WifiPwdInputListener {
        void onWifiPwdInputCallback(ScanResult result, String pwd);
    }
}
