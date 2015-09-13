package com.tcs.tvmilp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
//this page will save the entries of the already downloaded videos in teh android SQLIte databse.This is because that videos should not 
//be downloaded again from teh database.
public class VideoDatabaseHandler extends SQLiteOpenHelper {

	private static final int databaseVersion = 1;
	private static final String databaseName = "dbTest";
	private Context context;

	public VideoDatabaseHandler(Context context) {
		super(context, databaseName, null, databaseVersion);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void saveVideo(String videoName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			String CREATE_IMAGE_TABLE = "CREATE TABLE " + "videotable" + "("

			+ "videoname" + " TEXT PRIMARY KEY)";
			db.execSQL(CREATE_IMAGE_TABLE);
		} catch (Exception e) {
		}
		values.put("videoname", videoName);
		db.insert("videotable", null, values);
		db.close();

	}
	public boolean videoChecker(String videoName)
	{
		System.out.println("the image name received is" + videoName);
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String CREATE_IMAGE_TABLE = "CREATE TABLE " + "videotable" + "("

			+ "videoname" + " TEXT PRIMARY KEY)";
			db.execSQL(CREATE_IMAGE_TABLE);
		} catch (Exception e) {

		}
		boolean isPresent = false;
		
		Cursor cursor2 = db.query("videotable", new String[] { "videoname"
			}, "videoname LIKE '" + videoName + "%'", null, null,
				null, null);
		if(cursor2==null)
			System.out.println("the cursor is null now");
		Bitmap bmp = null;
		if (bmp != null)
			System.out.println("there is something in the bmp");
		if (cursor2.moveToFirst()) {
			do {
				String innerFileName = cursor2.getString(0);
				
				
				if (innerFileName != null)
				{
					isPresent = true;
				}
				else
				{
					System.out.println("is file is null now");
				}
				System.out.println("the filename present in the cursor is"
						+ innerFileName);
			} while (cursor2.moveToNext());
		}
		cursor2.close();
		db.close();

		return isPresent;
		
		
	}

}
