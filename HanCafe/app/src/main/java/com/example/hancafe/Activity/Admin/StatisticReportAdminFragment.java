package com.example.hancafe.Activity.Admin;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.hancafe.Activity.Adapter.CTHDAdapter;
import com.example.hancafe.Model.CTHD;

public class StatisticReportAdminFragment extends Fragment {
    Spinner listTime;
    List<String> data_listTime = new ArrayList<>();
    ArrayAdapter<String> adapter_listTime;
    DatePickerDialog datePickerDialog;
Button dateButton;
    BarDataSet dataSet;
    LinearLayout chartLayout, lengend_layout;
    CardView cardViewChart;
    CardView cvDetails;
    TextView  tvtotal, tvtitleDay, tvtitle ;
    PieChart pieChart;
    RecyclerView rcv_productReport;
    LinearLayout thongke;
    CTHDAdapter CTHDAdapter;
    List<CTHD> listCTHD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_statistic_report, container, false);
        setControl(view);
        initLayoutManager();
        createSampleData();
        setSpinnerEvent();
        dateButton.setText(getTodaysDate());
        getDataFromRealtimeDB(getTodaysDate());
        return view;
    }





    private void setControl(View view) {
        listTime = view.findViewById(R.id.listtime);
        dateButton = view.findViewById(R.id.btnDatePicker);
        pieChart =  view.findViewById(R.id.piechart);
        tvtotal = view.findViewById(R.id.tvtotal);
        cardViewChart = view.findViewById(R.id.cardViewGraph);
        lengend_layout = view.findViewById(R.id.legend_layout);
        chartLayout = view.findViewById(R.id.chartBar);
        cvDetails = view.findViewById(R.id.details);
        rcv_productReport = view.findViewById(R.id.rcv_productReport);
        tvtitleDay = view.findViewById(R.id.tvtitleDay);
        tvtitle = view.findViewById(R.id.tvtitle);
        thongke = view.findViewById(R.id.thongke);

    }

    private void initLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_productReport.setLayoutManager(linearLayoutManager);
        listCTHD = new ArrayList<>();
        CTHDAdapter = new CTHDAdapter(listCTHD);
        rcv_productReport.setAdapter(CTHDAdapter);
    }
    private void getDataFromRealtimeDB(String selectedDate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderManagementRef = database.getReference("Order_Management");
        DatabaseReference orderDetailRef = database.getReference("OrderDetail");

        orderManagementRef.orderByChild("date").equalTo(selectedDate).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCTHD.clear(); // Xóa danh sách cũ trước khi cập nhật

                 final int [] totalAmount = {0};

                if (snapshot.exists()) {
                    for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                        String idBill = billSnapshot.getKey(); // Lấy ID của hóa đơn
                        int totalPrice = billSnapshot.child("price").getValue(Integer.class); // Lấy tổng tiền từ hóa đơn
                        totalAmount[0] += totalPrice; // Cộng tổng tiền


                        orderDetailRef.orderByChild("idOrder").equalTo(idBill).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot detailSnapshot) {
                                for (DataSnapshot detail : detailSnapshot.getChildren()) {
                                    String productId = detail.getKey(); // Lấy ID của sản phẩm
                                    String nameProduct = detail.child("nameProduct").getValue(String.class);
                                    int quantity = detail.child("quantity").getValue(Integer.class);
                                    int priceProduct = detail.child("priceProduct").getValue(Integer.class);

                                    // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chưa, nếu có thì cộng dồn quantity và priceProduct
                                    boolean found = false;
                                    for (CTHD cthd : listCTHD) {
                                        if (cthd.getNameProduct().equals(nameProduct)) {
                                            cthd.setQuantity(cthd.getQuantity() + quantity);
                                            cthd.setPriceProduct(cthd.getPriceProduct() + priceProduct);
                                            found = true;
                                            break;
                                        }
                                    }

                                    // Nếu sản phẩm chưa tồn tại trong danh sách, thêm mới vào danh sách
                                    if (!found) {
                                        CTHD cthd = new CTHD(nameProduct, quantity, priceProduct);
                                        listCTHD.add(cthd);
                                    }
                                }

                                // Cập nhật giao diện sau khi đã duyệt qua tất cả chi tiết hóa đơn
                                pieChart.setVisibility(View.VISIBLE);
                                tvtotal.setText(String.valueOf( totalAmount[0]));
                                SetDataPieChart( totalAmount[0], listCTHD);
                                CTHDAdapter.notifyDataSetChanged();
                                tvtitleDay.setText("Thống kê các sản phẩm đã bán trong ngày:");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to get Bill Detail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Nếu không có dữ liệu, hiển thị thông báo tương ứng
                    tvtitleDay.setText("Không có Doanh Thu trong ngày");
                    pieChart.setVisibility(View.GONE);
                    lengend_layout.removeAllViews();
                    tvtotal.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to get Bill Detail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDataPicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                getDataFromRealtimeDB(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Khởi tạo datePickerDialog
        // Create a ContextThemeWrapper to apply the style directly

// Create the DatePickerDialog with the styled context
        datePickerDialog = new DatePickerDialog(
                requireContext(), // Use the styled context wrapper
                dateSetListener,
                year,
                month,
                day);
    }

    private String makeDateString(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
    }

    private void setSpinnerEvent() {
        KhoiTao();
        adapter_listTime = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data_listTime);
        adapter_listTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listTime.setAdapter(adapter_listTime);

        // Gắn sự kiện mở DatePickerDialog khi nhấn vào button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        listTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItems = data_listTime.get(position);
                if(selectedItems.equals("Ngày")) {
                    cardViewChart.setVisibility(View.VISIBLE);
                    chartLayout.setVisibility(View.GONE);
                    thongke.setVisibility(View.VISIBLE);
                    tvtitle.setVisibility(View.GONE);
                    initDataPicker(); // Gọi phương thức initDataPicker() ở đây để khởi tạo datePickerDialog
                }
                if(selectedItems.equals("Tháng")) {
                    cardViewChart.setVisibility(View.GONE);
                    chartLayout.setVisibility(View.VISIBLE);
                    updateData(12, "Tháng");
                    tvtitle.setVisibility(View.VISIBLE);
                    tvtitle.setText("Thống kê Doanh thu theo từng tháng");
                    thongke.setVisibility(View.GONE);
                }
                if(selectedItems.equals("Năm")) {
                    cardViewChart.setVisibility(View.GONE);
                    chartLayout.setVisibility(View.VISIBLE);
                    updateData(4, "Năm");
                    tvtitle.setVisibility(View.VISIBLE);
                    tvtitle.setText("Thống kê Doanh thu theo 4 năm gần nhất");
                    thongke.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void KhoiTao() {
        data_listTime.add("Ngày");
        data_listTime.add("Tháng");
        data_listTime.add("Năm");
    }

    public void openDatePicker() {
        // Hiển thị datePickerDialog
        datePickerDialog.show();
    }

    private void SetDataPieChart(int totalAmount, List<CTHD> listCTHD) {
        // Clear any existing data in the pie chart
        pieChart.clearChart();
        lengend_layout.removeAllViews();
        // Tạo các mảng để lưu phần trăm và màu sắc tương ứng
        float[] percentages = new float[listCTHD.size()];
        int[] colors = new int[listCTHD.size()];
        String[] legends = new String[listCTHD.size()]; // Chú thích
        // Tính phần trăm của từng loại sản phẩm
        for (int i = 0; i < listCTHD.size(); i++) {
            int amount = listCTHD.get(i).getPriceProduct();
            percentages[i] = ((float) amount / totalAmount) * 100; // Tính phần trăm
            legends[i] = listCTHD.get(i).getNameProduct();
            // Gán màu sắc cho từng loại sản phẩm (có thể thay đổi hoặc mở rộng)
            switch (i) {
                case 0:
                    colors[i] = Color.parseColor("#8B0305");
                    break;
                case 1:
                    colors[i] = Color.parseColor("#F98F00");
                    break;
                case 2:
                    colors[i] = Color.parseColor("#EF5350");
                    break;
                case 3:
                    colors[i] = Color.parseColor("#29B6F6");
                    break;
                case 4:
                    colors[i] = Color.parseColor("#333333");
                    break;
                case 5:
                    colors[i] = Color.parseColor("#47f900");
                    break;
                case 6:
                    colors[i] = Color.parseColor("#8d00f9");
                    break;

            }
            addLegendItem(legends[i], colors[i]);
        }

        // Add new data to the pie chart
        for (int i = 0; i < listCTHD.size(); i++) {
            pieChart.addPieSlice(
                    new PieModel(
                            listCTHD.get(i).getNameProduct(),
                            percentages[i],
                            colors[i]));
        }

        // To animate the pie chart
        pieChart.startAnimation();
    }
    private void addLegendItem(String legendText, int color) {


        // Tạo một LinearLayout mới để chứa mỗi mục chú thích
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout legendItemLayout = new LinearLayout(getContext());
        legendItemLayout.setLayoutParams(layoutParams);
        legendItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        legendItemLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Tạo một hộp màu để hiển thị màu sắc của mục chú thích
        View colorView = new View(getContext());
        LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(
                40, // Độ rộng của hộp màu
                40  // Chiều cao của hộp màu
        );
        colorParams.setMargins(0, 0, 10, 0); // Khoảng cách giữa màu sắc và chữ
        colorView.setLayoutParams(colorParams);
        colorView.setBackgroundColor(color);

        // Tạo một TextView để hiển thị chú thích
        TextView legendTextView = new TextView(getContext());
        legendTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        legendTextView.setText(legendText);
        legendTextView.setTextColor(Color.BLACK); // Màu chữ

        // Thêm hộp màu và chữ vào layout của mỗi mục chú thích
        legendItemLayout.addView(colorView);
        legendItemLayout.addView(legendTextView);

        // Thêm mỗi mục chú thích vào layout chính
        lengend_layout.addView(legendItemLayout);
    }

    private void createSampleData() {
        // Tạo dữ liệu mẫu
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 20));
        dataSet = new BarDataSet(entries, "Sales Report (VND)");
        showChart(dataSet, "Tháng");
    }

    private void updateData(int numDataPoints, String seletedItem) {
        dataSet.clear(); // Xóa dữ liệu hiện tại
        Random random = new Random();

        for (int i = 1; i <= numDataPoints; i++) {
            // Thêm dữ liệu ngẫu nhiên trong khoảng từ 10 đến 1000 cho mỗi ngày/tháng
            float randomValue = random.nextFloat() * (100000000 - 10) + 10;
            dataSet.addEntry(new BarEntry(i, randomValue));
        }

        showChart(dataSet,seletedItem);
    }

    private void showChart(BarDataSet dataSet, String selectedItems) {
        // Hiển thị biểu đồ
        BarChart barChart = new BarChart(getContext());

        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);

        // Set dữ liệu cho biểu đồ cột
        barChart.setData(barData);

        int dataSize = dataSet.getValues().size(); // Lấy kích thước dữ liệu

        // Đặt số lượng cột tối thiểu và tối đa hiển thị
        barChart.setVisibleXRangeMaximum(dataSize);

        // Thêm chú thích dưới các cột
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt vị trí của chú thích dưới biểu đồ
        xAxis.setCenterAxisLabels(true); // Hiển thị nhãn chính giữa cột
        xAxis.setGranularity(1f); // Đảm bảo mỗi cột cách nhau 1 đơn vị

        if(selectedItems.equals("Tháng")) {
            xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthLabels())); // Đặt nhãn cho các cột
        } else if(selectedItems.equals("Năm")) {
            xAxis.setValueFormatter(new IndexAxisValueFormatter(getYearLabels())); // Đặt nhãn cho các cột
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        clearChart();
        chartLayout.addView(barChart, layoutParams);
    }

    // Phương thức để lấy nhãn của các tháng (từ tháng 1 đến tháng 12)
    private ArrayList<String> getMonthLabels() {
        ArrayList<String> monthLabels = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthLabels.add(String.valueOf(i));
        }
        return monthLabels;
    }

    // Phương thức để lấy nhãn của các năm (4 năm gần nhất từ năm 2024)
    // Phương thức để lấy nhãn của các năm (4 năm gần nhất từ năm 2024)
    private ArrayList<String> getYearLabels() {
        ArrayList<String> yearLabels = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Năm hiện tại
        int startYear = currentYear - 3; // Năm thấp nhất trong 4 năm gần nhất
        int endYear = currentYear; // Năm hiện tại

        for (int i = startYear; i <= endYear; i++) {
            yearLabels.add(String.valueOf(i));
        }

        return yearLabels;
    }




    // Phương thức để xóa biểu đồ cũ khi cần thiết
    private void clearChart() {
        chartLayout.removeAllViews();
    }
}
