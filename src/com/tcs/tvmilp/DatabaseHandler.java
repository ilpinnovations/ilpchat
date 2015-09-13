package com.tcs.tvmilp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//this is the database class that will save the images in the android internal SQLite databse. This is responsible for
//creating list of images in the android internal database so that that images will not be downloaded again from the cloud database.
//this class is only called from the ChatPage class.
public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int databaseVersion = 1;
	private static final String databaseName = "dbTest";
	private Context context;

	public DatabaseHandler(Context context) {
		super(context, databaseName, null, databaseVersion);
		this.context = context;
	}

	public void saveimage(String imageName, byte[] imageData) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			String CREATE_IMAGE_TABLE = "CREATE TABLE " + "imagetable" + "("

			+ "filename" + " TEXT PRIMARY KEY," + "imagedata" + " TEXT )";
			db.execSQL(CREATE_IMAGE_TABLE);
		} catch (Exception e) {

		}
		values.put("filename", imageName);
		values.put("imagedata", imageData);
		db.insert("imagetable", null, values);
		db.close();
		System.out.println("image saved in the sqlite successful");

	}

	public Bitmap getImage(String filename) {

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query("imagetable", new String[] { "filename",
				"imagedata" }, "filename LIKE '" + filename + "%'", null, null,
				null, null);

		Bitmap bmp = null;
		if (bmp != null)
			System.out.println("there is something in the bmp");
		if (cursor.moveToFirst()) {
			do {
				byte[] cursImage = cursor.getBlob(1);
				bmp = BitmapFactory.decodeByteArray(cursImage, 0,
						cursImage.length);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return bmp;
	}

	public boolean imageChecker(String filename) {
		System.out.println("the image name received is" + filename);
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String CREATE_IMAGE_TABLE = "CREATE TABLE " + "imagetable" + "("

			+ "filename" + " TEXT PRIMARY KEY," + "imagedata" + " TEXT )";
			db.execSQL(CREATE_IMAGE_TABLE);
		} catch (Exception e) {

		}
		boolean isPresent = false;
		
		Cursor cursor2 = db.query("imagetable", new String[] { "filename",
				"imagedata" }, "filename LIKE '" + filename + "%'", null, null,
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

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
