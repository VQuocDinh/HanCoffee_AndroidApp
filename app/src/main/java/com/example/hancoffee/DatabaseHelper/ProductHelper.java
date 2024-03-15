package com.example.hancoffee.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.hancoffee.Domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductHelper extends SQLiteOpenHelper {
    public ProductHelper(@Nullable Context context) {

        super(context, "HanCoffee", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableProduct = "CREATE TABLE IF NOT EXISTS product (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price TEXT, image TEXT)";
        db.execSQL(createTableProduct);
    }

    public List<Product> fetchProduct() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * from product";
        List<Product> data = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Product sp = new Product();
                sp.setName(cursor.getString(1));
                sp.setPrice(cursor.getString(2));
                sp.setPic(cursor.getString(3));
                data.add(sp);
            } while (cursor.moveToNext());  // Thay đổi ở đây từ moveToFirst() sang moveToNext()
        }
        cursor.close();  // Đảm bảo đóng cursor sau khi sử dụng
        return data;
    }

    public void insertProduct(Product sp) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Insert into product Values(?,?,?,?)";
        db.execSQL(sql, new String[]{sp.getId(), sp.getName(), sp.getPrice(), sp.getPic()});
    }

    public void deleteProduct(Product sp) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Delete from " + Constans.TABLE_PRODUCT + " Where id=?";
        db.execSQL(sql, new String[]{sp.getId()});
    }

    public void updateProduct(Product sp) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Update " + Constans.TABLE_PRODUCT + " set name=?,price=?,pic=? where id=?";
        db.execSQL(sql, new String[]{sp.getName(), sp.getPrice(), sp.getPic(), sp.getId()});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
