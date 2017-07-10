package icapurro.org.smartdns.ui.connection;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.VpnService;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import icapurro.org.smartdns.R;
import icapurro.org.smartdns.data.local.PreferencesHelper;
import icapurro.org.smartdns.data.model.SmartDns;
import icapurro.org.smartdns.utils.CustomVpnService;
import icapurro.org.smartdns.utils.NetUtils;


public class VpnFragment extends Fragment implements View.OnClickListener {

    private VpnStatusReceiver vpnStatusReceiver;
    private IntentFilter filter;
    public CustomVpnService mainService;
    private ConnectionActivity activity;
    private MaterialBetterSpinner spinnerDns1, spinnerDns2;
    private MaterialEditText textServiceId;
    private CheckBox checkUpdateIp;
    private RelativeLayout contentMain;
    private PreferencesHelper preferencesHelper;
    private SmartDns smartDns;
    boolean isBound = false;


    View connectBtn;

    View disconnectBtn;

    TextView textBtnConnected;

    TextView textBtnDisconnected;

    TextView textDonate;

    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vpn, container, false);

        activity = (ConnectionActivity) getActivity();
        connectBtn = (View) v.findViewById(R.id.button_connect);
        disconnectBtn = (View) v.findViewById(R.id.button_disconnect);
        textBtnConnected = (TextView) v.findViewById(R.id.text_button_connected);
        textBtnDisconnected = (TextView) v.findViewById(R.id.text_button_disconnected);
        spinnerDns1 = (MaterialBetterSpinner) v.findViewById(R.id.spinner_dns_1);
        spinnerDns2 = (MaterialBetterSpinner) v.findViewById(R.id.spinner_dns_2);
        textServiceId = (MaterialEditText) v.findViewById(R.id.text_service_id);
        contentMain = (RelativeLayout) v.findViewById(R.id.content_main);

        ((TextView) v.findViewById(R.id.powered_by)).setMovementMethod(LinkMovementMethod.getInstance());

        preferencesHelper = new PreferencesHelper(getContext());

        smartDns = preferencesHelper.loadSmartDns();
        textServiceId.setText(smartDns.getServiceId());
        textServiceId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                smartDns.setServiceId(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, smartDns.getLocationStrings());

        spinnerDns1.setAdapter(adapter);
        spinnerDns1.setText(smartDns.getDns1().toString());
        spinnerDns2.setAdapter(adapter);
        spinnerDns2.setText(smartDns.getDns2().toString());

        spinnerDns1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                smartDns.setDns1(i);
            }
        });

        spinnerDns2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                smartDns.setDns2(i);
            }
        });


        ClickableSpan installSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                Answers.getInstance().logCustom(new CustomEvent("3.0 Homepage Support"));
//
//                Fragment fragment = new DonateFragment();
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        vpnStatusReceiver = new VpnStatusReceiver() {
            @Override
            public void onVpnStartReceived() {
                updateConnectedUI();
            }
        };

        filter = new IntentFilter("vpn.start");
        getActivity().registerReceiver(vpnStatusReceiver, filter);

        if (CustomVpnService.isRunning) {
            this.getActivity().bindService(new Intent(this.getActivity(), CustomVpnService.class), mainConnection, Context.BIND_AUTO_CREATE);
            updateConnectedUI();
        }

        connectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connect(v);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                disconnect(v);
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void updateConnectedUI() {
        setEnabled(false);
        setColor(R.color.green_primary);
        connectBtn.setVisibility(View.GONE);
        disconnectBtn.setVisibility(View.VISIBLE);
        textBtnConnected.setVisibility(View.GONE);
        textBtnDisconnected.setVisibility(View.VISIBLE);
    }

    private void updateDisconnectedUI() {
        setEnabled(true);
        setColor(R.color.blue_primary);
        connectBtn.setVisibility(View.VISIBLE);
        disconnectBtn.setVisibility(View.GONE);
        textBtnConnected.setVisibility(View.VISIBLE);
        textBtnDisconnected.setVisibility(View.GONE);
    }

    public void connect(View view) {
        if (NetUtils.isConnectedWifi(getContext())) {
            startVpn();
        } else {
            alertNoWifi();
        }
    }

    public void setEnabled(Boolean enabled) {
        spinnerDns1.setEnabled(enabled);
        spinnerDns1.setClickable(enabled);
        spinnerDns2.setEnabled(enabled);
        spinnerDns2.setClickable(enabled);
        textServiceId.setEnabled(enabled);
    }

    public void setColor(int color) {
//        contentMain.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        if (activity != null) {
            activity.setBackgroundColor(color);
        }
    }

    public void alertNoWifi() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.no_wifi)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    public void startVpn() {
        preferencesHelper.saveSmartDns(smartDns);
        Intent intent = VpnService.prepare(getActivity());
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, Activity.RESULT_OK, null);
        }
    }

    public void disconnect(View view) {
        if (mainService != null){
            mainService.kill();
        }
        updateDisconnectedUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent i = new Intent(this.getActivity(), CustomVpnService.class);
            this.getActivity().startService(i);
            this.getActivity().bindService(
                    new Intent(this.getActivity(),
                            CustomVpnService.class),
                    mainConnection, Context.BIND_AUTO_CREATE);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ServiceConnection mainConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CustomVpnService.MyLocalBinder binder = (CustomVpnService.MyLocalBinder) service;
            mainService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (isBound) {
            getActivity().unbindService(mainConnection);
            isBound = false;
        }
        getActivity().unregisterReceiver(vpnStatusReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CustomVpnService.isRunning && !isBound) {
            getActivity().bindService(new Intent(getActivity(), CustomVpnService.class), mainConnection, Context.BIND_AUTO_CREATE);
            updateConnectedUI();
        } else {
            updateDisconnectedUI();
        }
        getActivity().registerReceiver(vpnStatusReceiver, filter);
    }
}