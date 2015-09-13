package com.tcs.tvmilp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
	//this activity is responsible for triggering action and saving values on receive of the push notification.This messageis called
	//from the PushReceiver and ChatPage class.
public class PushChecker extends SQLiteOpenHelper {
	private static final int databaseVersion = 1;
	private static final String databaseName = "dbTest";

	private Context context;

	public PushChecker(Context context) {
		super(context, databaseName, null, databaseVersion);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void insertData(String data) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			String CREATE_IMAGE_TABLE = "CREATE TABLE " + "datatable3" + "("

			+ "number" + " INTEGER PRIMARY KEY," + "data" + " TEXT )";
			
			db.execSQL(CREATE_IMAGE_TABLE);
		values.put("number", 1);
		values.put("data", data);
		
		db.insert("datatable3", null, values);
		db.close();
		} catch (Exception e) {

		}
		System.out.println("values insertion in the pushchecker is successful");

	}

	public String readData() {

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query("datatable3", new String[] {"number" ,"data" },
				"NUMBER LIKE '" + "1" + "%'", null, null, null, null);
		String data=null;
		if (cursor.moveToFirst()) {
			do {
				data=cursor.getString(1);
				System.out.println("the received string is"+data);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		return data;
	}

	public void updateData(String data) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("data", data);
		db.update("datatable3", cv,"number=1", null);
	}

}
