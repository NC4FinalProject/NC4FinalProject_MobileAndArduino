package com.example.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bluetooth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HM10_BLE_Sample";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private List<String> deviceNames = new ArrayList<>();
    private ListView lvDeviceNames;
    private Handler handler = new Handler();
    private EditText etMessage;
    private Button btnSend;

    private static final UUID HM10_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID HM10_CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get permission
        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.INTERNET
        };
        ActivityCompat.requestPermissions(MainActivity.this, permission_list, 1);

        lvDeviceNames = findViewById(R.id.lv_device_names);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        Button scanButton = findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBLEScan();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    etMessage.setText("");
                }
            }
        });

        // BluetoothAdapter 초기화
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // BLE를 지원하는지 확인
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // BLE 활성화 확인
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
        }

        // ListView의 아이템 클릭 이벤트 설정
        lvDeviceNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 기기의 이름을 가져옴
                String deviceName = deviceNames.get(position);
                // BluetoothAdapter가 null이 아니고 Bluetooth 기기가 선택되었을 경우
                if (bluetoothAdapter != null && deviceName != null) {
                    // 페어링된 기기 목록을 가져옴
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    // 페어링된 기기 목록을 순회하면서 선택한 기기와 매칭되는 기기를 찾음
                    for (BluetoothDevice device : pairedDevices) {
                        if (deviceName.equals(device.getName())) {
                            // 선택한 기기와의 블루투스 연결을 시도
                            // 연결하는 코드를 추가해야 함
                            Toast.makeText(MainActivity.this, "Connecting to " + deviceName, Toast.LENGTH_SHORT).show();
                            connectToDevice(device);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void startBLEScan() {
        deviceNames.clear();
        updateDeviceListUI();

        // Register the BroadcastReceiver for Bluetooth discovery
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDiscoveryReceiver, filter);

        // BLE 스캔 시작
        bluetoothAdapter.startDiscovery();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.cancelDiscovery();
                // Unregister the BroadcastReceiver
                unregisterReceiver(bluetoothDiscoveryReceiver);
            }
        }, 10000); // 10초 후에 스캔 중지
    }

    private void updateDeviceListUI() {
        // ArrayAdapter를 사용하여 리스트뷰 갱신
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
        lvDeviceNames.setAdapter(adapter);
    }

    private BroadcastReceiver bluetoothDiscoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    String deviceName = device.getName();
                    if (deviceName != null && !deviceNames.contains(deviceName)) {
                        deviceNames.add(deviceName);
                        updateDeviceListUI();
                    }
                }
            }
        }
    };

    private void connectToDevice(BluetoothDevice device) {
        // 연결을 시도하기 전에 스캔을 중지합니다.
        bluetoothAdapter.cancelDiscovery();

        // GATT 연결을 시도합니다.
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
    }

    // sendMessage 메서드 추가
    private void sendMessage(String message) {
        if (bluetoothGatt == null) {
            Log.e(TAG, "BluetoothGatt is not initialized.");
            return;
        }

        BluetoothGattService service = bluetoothGatt.getService(HM10_SERVICE_UUID);
        if (service == null) {
            Log.e(TAG, "HM-10 service not found.");
            return;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(HM10_CHARACTERISTIC_UUID);
        if (characteristic == null) {
            Log.e(TAG, "HM-10 characteristic not found.");
            return;
        }

        characteristic.setValue(message.getBytes());
        boolean success = bluetoothGatt.writeCharacteristic(characteristic);
        if (success) {
            Log.i(TAG, "Message sent: " + message);
        } else {
            Log.e(TAG, "Failed to send message: " + message);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 연결에 성공한 경우
                Log.i(TAG, "Connected to GATT server.");
                // GATT 서버에 연결되었으므로 서비스를 검색합니다.
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 연결이 끊긴 경우
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 서비스 검색이 성공한 경우
                Log.i(TAG, "Services discovered successfully.");
                // HM-10의 서비스와 특성을 찾습니다.
                BluetoothGattService hm10Service = gatt.getService(HM10_SERVICE_UUID);
                if (hm10Service != null) {
                    // HM-10 서비스가 찾아진 경우
                    Log.i(TAG, "HM-10 service found.");
                    // HM-10의 특성을 찾습니다.
                    BluetoothGattCharacteristic hm10Characteristic = hm10Service.getCharacteristic(HM10_CHARACTERISTIC_UUID);
                    if (hm10Characteristic != null) {
                        // HM-10 특성이 찾아진 경우
                        Log.i(TAG, "HM-10 characteristic found.");
                        // 데이터를 보낼 준비가 완료되었으므로 버튼을 활성화합니다.
                        btnSend.setEnabled(true);
                    } else {
                        Log.e(TAG, "HM-10 characteristic not found.");
                    }
                } else {
                    Log.e(TAG, "HM-10 service not found.");
                }
            } else {
                // 서비스 검색이 실패한 경우
                Log.e(TAG, "Service discovery failed with status: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 데이터 쓰기가 성공한 경우
                Log.i(TAG, "Characteristic write successful.");
            } else {
                // 데이터 쓰기가 실패한 경우
                Log.e(TAG, "Characteristic write failed with status: " + status);
            }
        }
    };
}