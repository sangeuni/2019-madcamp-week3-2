package com.example.q.customerapp;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
public class MainTabFragment1 extends Fragment {
    private Button buttonScan;
    //qr code scanner object
    private IntentIntegrator qrScan;
    static Camera camera = null;
    public static final String store = "hi";

    private int INFORMATION_REQUEST = 0;

    @Override
        public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment1, container, false);
        buttonScan = (Button) rootView.findViewById(R.id.buttonScan);
        //View Objects
        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
//                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });
        return rootView;
    }


    public void startInformation(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, requestCode);
        }
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(getActivity().getApplicationContext(), "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(getActivity().getApplicationContext(), "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    String k = result.getContents();
                    Intent intent = new Intent(getActivity().getApplicationContext(), Information.class);
                    intent.putExtra("store",k);
                    startActivityForResult(intent, INFORMATION_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(getActivity().getApplicationContext(), Information.class);
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent(getActivity().getApplicationContext(), Information.class);
            startActivity(intent);
        }
    }
    //Getting the scan results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==INFORMATION_REQUEST) {
            ((MainActivity)getActivity()).phonenumber = data.getStringExtra("phone");
            ((MainActivity)getActivity()).storename = data.getStringExtra("store");
        }
    }
}
