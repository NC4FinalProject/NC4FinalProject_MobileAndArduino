package com.bitcamp.nc4_all;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bitcamp.nc4_all.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    // 하단 네비게이션 바 설정
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainMenuFragment fragmentMain = new MainMenuFragment();
    private MainMenuBluetoothFragment fragmentBluetooth = new MainMenuBluetoothFragment();
    private MainMenuProfileFragment fragmentProfile = new MainMenuProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //onCreate되는 시점에 MainActivity의 원래 테마가 나타나도록 MainActivity에 아래의 내용을 추가.
        setTheme(R.style.Theme_Nc4_all);

        // 하단 네비게이션 바 설정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

        // Get permission
        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.INTERNET
        };
        ActivityCompat.requestPermissions(MainActivity.this, permission_list, 1);

    }

    // 네비게이션 바 로직
    class ItemSelectedListener implements BottomNavigationView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            int itemId = menuItem.getItemId();
            if (itemId == R.id.fragment_home) {
                transaction.replace(R.id.menu_frame_layout, fragmentMain).commitAllowingStateLoss();
            } else if (itemId == R.id.fragment_bluetooth) {
                transaction.replace(R.id.menu_frame_layout, fragmentBluetooth).commitAllowingStateLoss();
            } else if (itemId == R.id.fragment_profile) {
                transaction.replace(R.id.menu_frame_layout, fragmentProfile).commitAllowingStateLoss();
            }

            return true;
        }
    }
}