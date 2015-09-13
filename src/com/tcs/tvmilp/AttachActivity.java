package com.tcs.tvmilp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import paul.arian.fileselector.FileSelectionActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
//this is the class for attaching videos and images.
public class AttachActivity extends Activity {
	private static final String FILES_TO_UPLOAD = "upload";
	ArrayList<File> Files;
	String tableName,uploadFileType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.attachactivity);

		Button image = (Button) findViewById(R.id.battachimage);
		Button video = (Button) findViewById(R.id.battachvideo);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			tableName = extras.getString("tablename");
		}
		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// This is the intent that will take you to the file explorer
				// api interface
				Intent intent = new Intent(getBaseContext(),
						FileSelectionActivity.class);
				//sending table name and uploadfiletype to the parian file selector activity
				intent.putExtra("tablename", tableName);
				intent.putExtra("uploadfiletype", "image");
				startActivityForResult(intent, 0);

			}
		});

		video.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// This is the intent that will take you to the file explorer
				// api interface
				Intent intent = new Intent(getBaseContext(),
						FileSelectionActivity.class);
				intent.putExtra("tablename", tableName);
				intent.putExtra("uploadfiletype", "video");

				startActivityForResult(intent, 0);

			}
		});

	}
//this method is the result received from the parian file selector dialog.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {

			tableName = data.getStringExtra("tablename");
			uploadFileType=data.getStringExtra("uploadfiletype");
			
			System.out
					.println("the value of table from parian received in the attach class is"
							+ tableName);
			Files = (ArrayList<File>) data
					.getSerializableExtra(FILES_TO_UPLOAD); // file array list
			String[] files_paths = null; // string array
			int i = 0;

			for (File file : Files) {
				// String fileName = file.getName();
				String uri = file.getAbsolutePath();
				System.out.println("the path is" + uri.toString());
				Intent sendPath = new Intent(getApplicationContext(),
						ChatPage.class);
				sendPath.putExtra("filepath", uri.toString());
				System.out
						.println("the table name finally sent is" + tableName);
				sendPath.putExtra("tablename", tableName);
				sendPath.putExtra("uploadfiletype", uploadFileType);
				startActivity(sendPath);
				// files_paths[i] = uri.toString(); //storing the selected
				// file's paths to string array files_paths
				// i++;
			}
		} else {
		}

	}

}
