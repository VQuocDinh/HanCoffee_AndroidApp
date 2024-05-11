package com.example.hancafe.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hancafe.Activity.User.OrderStatus.CancelOrderFragment;
import com.example.hancafe.Activity.User.OrderStatus.CompleteOrderFragment;
import com.example.hancafe.Activity.User.OrderStatus.ConfirmOrderFragment;
import com.example.hancafe.Activity.User.OrderStatus.DeliveryOrderFragment;
import com.example.hancafe.R;


public class Orders extends AppCompatActivity {
    private TextView confirm, delivering, canceled, received;

    private ImageView btnBack;
    ConfirmOrderFragment confirmOrderFragment = new ConfirmOrderFragment();
    CancelOrderFragment cancelOrderFragment = new CancelOrderFragment();
    DeliveryOrderFragment deliveryOrderFragment = new DeliveryOrderFragment();
    CompleteOrderFragment completeOrderFragment = new CompleteOrderFragment();

    private Boolean confirmIsActive, deliveringIsActive, canceledIsActive, receivedIsActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        setControl();
        setEvent();
    }

    private void setEvent() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, confirmOrderFragment).commit();
        confirm.setTextColor(getResources().getColor(R.color.mainColor));
        delivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, deliveryOrderFragment).commit();
//                delivering.setTextColor(getResources().getColor(R.color.mainColor));
                updateButtonState(delivering);

            }
        });

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, completeOrderFragment).commit();
//                received.setTextColor(getResources().getColor(R.color.mainColor));
                updateButtonState(received);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, confirmOrderFragment).commit();
//                confirm.setTextColor(getResources().getColor(R.color.mainColor));
                updateButtonState(confirm);

            }
        });
        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, cancelOrderFragment).commit();
//                canceled.setTextColor(getResources().getColor(R.color.mainColor));
                updateButtonState(canceled);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Orders.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateButtonState(TextView click) {
        confirmIsActive = click == confirm;
        canceledIsActive = click == canceled;
        deliveringIsActive = click == delivering;
        receivedIsActive = click == received;

        confirm.setTextColor(confirmIsActive ? getResources().getColor(R.color.mainColor) : getResources().getColor(R.color.black));
        delivering.setTextColor(deliveringIsActive ? getResources().getColor(R.color.mainColor) : getResources().getColor(R.color.black));
        canceled.setTextColor(canceledIsActive ? getResources().getColor(R.color.mainColor) : getResources().getColor(R.color.black));
        received.setTextColor(receivedIsActive ? getResources().getColor(R.color.mainColor) : getResources().getColor(R.color.black));
    }


    private void setControl() {

        btnBack = findViewById(R.id.btnBack);
        confirm = findViewById(R.id.confirm);
        canceled = findViewById(R.id.canceled);
        delivering = findViewById(R.id.delivering);
        received = findViewById(R.id.received);

    }


}