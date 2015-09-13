package com.tcs.tvmilp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class ChatPage extends Activity {

	Boolean isSent = false;
	Boolean isRecieved = false;
	ArrayList<ParseUser> pu;
	String msg;
	EditText message;
	int i = 0;
	ParseUser currentUser;
	QueryAdapter qadap;
	ArrayList<ParseObject> assoclist;
	String tableName;
	String toggle = "text";
	Bitmap bmp = null;
	ArrayList<String> actionList;

	String action = "text";
	String filePath;
	String fileName;
	ArrayList<String> checker;
	boolean isAddItem = true;
	QueryAdapter qa;
	int counter = 0;
	Handler handler;
	boolean isRunning = false;
	Runnable r;
	ArrayList<byte[]> imageData;
	DatabaseHandler dbhandler;
	SQLiteDatabase db;
	SharedPreferences classPreferences;
	Timer t;
	PushChecker pushchecker;
	Bitmap innerBitmap;
	String actionType, uploadFileType;
	VideoDatabaseHandler videoDatabaseHandler;
	InternetChecker internetChecker;
	boolean onScreen=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int i = 0;
		actionList = new ArrayList<String>();
		actionList.add("text");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatmain);
		System.out.println("ON CREATE IS FIRED");
		pushchecker = new PushChecker(this);
		pushchecker.insertData("true");
		videoDatabaseHandler = new VideoDatabaseHandler(this);
		String data = pushchecker.readData();
		System.out.println("the values retrieved from the push checker is"
				+ data);
		pushchecker.updateData("false");
		System.out.println("the data in the table after the update is" + data);
		classPreferences = getSharedPreferences("MyPreferences",
				Context.MODE_PRIVATE);
		String stringIsRunning = classPreferences.getString("isRunning", null);
		System.out.println("the value of the shared preferecne is"
				+ stringIsRunning);
		imageData = new ArrayList<byte[]>();
		checker = new ArrayList<String>();
		message = (EditText) findViewById(R.id.msgedittext);
		Button send = (Button) findViewById(R.id.btnsend);
		// Button logout = (Button) findViewById(R.id.blogout);
		currentUser = new ParseUser();
		handler = new Handler();

		String role = ParseUser.getCurrentUser().getString("role");
		dbhandler = new DatabaseHandler(this);
		// dbhandler.onCreate(db);
		Bundle extras = getIntent().getExtras();
		if (role.equals("trainee")) {
			tableName = ParseUser.getCurrentUser().getString("L_G");
		}
		if (extras != null) {
			tableName = extras.getString("tablename");
			System.out
					.println("Table name received from intent is" + tableName);

			filePath = extras.getString("filepath");
			uploadFileType = extras.getString("uploadfiletype");
			System.out.println("the uploadfiletype received is"
					+ uploadFileType);
			String pushAction = extras.getString("pushaction");
			// if (pushAction.equals("yes"))
			// isRunning = true;

		}

		// this is the code to subscribe the user to a channel. The push
		// notification will be sent to user through this channel.
		
		ParseUser newuser=ParseUser.getCurrentUser();
		String userrole=newuser.getString("role");
		if(!userrole.equals("superadmin"))
		{
		try {
			ParseInstallation.getCurrentInstallation().saveInBackground();
			ParseInstallation.getCurrentInstallation().put("username",
					ParseUser.getCurrentUser().getUsername());
			System.out.println("the value of the channel is " + tableName);
			PushService.setDefaultPushCallback(this, ChatPage.class);
			ParsePush.subscribeInBackground(tableName, new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					// TODO Auto-generated method stub
					System.out.println("channel successfully subscribed");
				}
			});
		} catch (Exception e) {

		}
		}
		// PushService.setDefaultPushCallback(this, PushReceiver.class);

		String username = ParseUser.getCurrentUser().getString("L_G");
		Button userlist = (Button) findViewById(R.id.blist);
		// Button imageUpload = (Button) findViewById(R.id.btnimg);
		Button attachImage = (Button) findViewById(R.id.battach);

		// this is the method that will attah imageand videos to the list view
		attachImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// uploadImage();
				Intent selectFile = new Intent(getApplicationContext(),
						AttachActivity.class);
				selectFile.putExtra("tablename", tableName);
				startActivity(selectFile);
			}
		});
		/*
		 * imageUpload.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { try { // fetchImage();
		 * fetchQuery(); } catch (Exception e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 * 
		 * } // ChatPage cp=new ChatPage();
		 * 
		 * });
		 */

		try {
			// fetchQuery();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// this method will take you to the user list page
		userlist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						UserList.class);
				intent.putExtra("tablename", tableName);
				startActivity(intent);

			}
		});
		// this is the method responsible for the send button
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// i++;
				msg = message.getText().toString();
				message.setText("");
				// isAddItem=true;
				try {

					saveData();
					qa = null;
					fetchQuery();

					// pushQuery();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pushQuery();

				try {
					fetchQuery();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * try { fetchQuery(); } catch (Exception e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

				// lv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				// lv.setStackFromBottom(true);
			}
		});

		if (filePath != null) {

			if (uploadFileType.equals("image")) {
				uploadImage();
			} else {
				uploadVideo();
			}

		}
		try {
			// fetchImage();
			fetchQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void subscribe() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onScreen=true;
		internetChecker = new InternetChecker(getApplicationContext());
		if (!internetChecker.isConnectingToInternet()) {
			System.out.println("INTERNET CRASHED");
			Toast.makeText(getApplicationContext(), "Connect to Internet",
					Toast.LENGTH_LONG).show();
		}
		try {
			fetchQuery();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//pushQuery();
		isRunning = false;
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {

				String innerData = pushchecker.readData();

				if (innerData.equals("true")) {
					try {
						fetchQuery();
						pushchecker.updateData("false");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}, 0, 1000);
		try {
			fetchQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void uploadImage() {

		actionType = "image";
		MyAsyncTask task = new MyAsyncTask();
		task.execute();

	}

	// this method fetches images from bort the sqlite tables and the parse
	// cloud databse
	public Bitmap fetchImage(ParseObject parseObj) throws Exception {

		// final Bitmap innerBitmap=null;
		final ParseObject innerpo = parseObj;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// Do the processing.
				ParseQuery<ParseObject> imageQuery = new ParseQuery<ParseObject>(
						tableName);

				ParseObject po = innerpo;
				if (po.getString("filetype").toString().equals("image")) {
					ParseFile fileObject = (ParseFile) po.get("file");
					String fname = po.getString("filename");
					byte[] data = null;
					try {
						data = fileObject.getData();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					innerBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					System.out.println("image is in phone now");

					try {
						dbhandler.saveimage(fname, data);
					} catch (Exception e) {

					}

					System.out.println("coming to checker in the fetchImage"
							+ fname + "boolean is"
							+ dbhandler.imageChecker(fname));
					if (dbhandler.imageChecker(fname)) {

						System.out
								.println("image is getting saved in the receivers sqlitedatabse");

					}

				}
				qa.notifyDataSetChanged();
			}
		});
		System.out.println("returning the value of bitmap");
		return innerBitmap;
	}

	public void uploadVideo() {

		actionType = "video";
		MyVideoAsyncTask videoTask = new MyVideoAsyncTask();
		videoTask.execute();

	}

	//this is the method to download the video from the cloud.
	public void downloadVideo(String videoObjectId) {
		final ProgressDialog dia = ProgressDialog.show(ChatPage.this, null,
				"Downloading Video...");
		
		final String vobjid = videoObjectId;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				System.out.println("download video method is fired");
				ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
				query.whereEqualTo("objectId", vobjid);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> arg0, ParseException arg1) {
						// TODO Auto-generated method stub
						ParseObject videoObject = arg0.get(0);
						String videoFileName = videoObject
								.getString("filename");
						System.out.println("the name of the video file is"
								+ videoFileName);
						ParseFile parseVideoFile = (ParseFile) videoObject
								.get("file");

						byte[] videoData = null;
						try {
							videoData = parseVideoFile.getData();
							System.out
									.println("video file is successfully received");
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						File videoFile = new File(Environment
								.getExternalStorageDirectory() + "/tvmilp");
						videoFile.mkdirs();
						videoFile = new File(videoFile.getPath() + "/"
								+ videoFileName);
						try {
							videoFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							FileOutputStream fileOutputStream = new FileOutputStream(
									videoFile);
							fileOutputStream.write(videoData);
							fileOutputStream.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("the saved video filename is"
								+ fileName);
						videoDatabaseHandler.saveVideo(videoFileName);

						dia.dismiss();
						try {
							fetchQuery();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
			}
		}).start();

	}

	

	// this is the method responsible for saving the text data in the parse
	// database
	public void saveData() {
		ParseObject po = new ParseObject(tableName);
		po.put("username", ParseUser.getCurrentUser().getUsername().toString());
		po.put("text", msg);
		po.put("filetype", "text");

		po.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				// TODO Auto-generated method stub

				if (arg0 == null) {

				}

			}
		});

	}

	
	// this is the method for setting the value of the handler to true
	public void setValue(boolean value) {

		if (value)
			isRunning = true;
		System.out.println("setValue method is fired" + value + isRunning);

		// SharedPreferences
		// sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
		// SharedPreferences.Editor editor=sharedPreferences.edit();
		// editor.putString("isRunning", "true");
		// editor.commit();
	}

	// this is the method that sends the push notification to the users
	public void pushQuery() {
		isSent = true;

		ParsePush parsePush = new ParsePush();

		ParseQuery pQuery = ParseInstallation.getQuery();
		pQuery.whereNotEqualTo("username", ParseUser.getCurrentUser()
		.getUsername());
		pQuery.whereEqualTo("channels", tableName);

		if (msg != null) {
			parsePush.sendMessageInBackground(msg, pQuery);
		} else {
			parsePush.sendMessageInBackground("image/video", pQuery);
		}
		// saveData();
		try {
			// fetchQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// this is the method responsible for the updation of the list view after
	// the update or send of the message.all the methods are called by thiis
	// method
	public void fetchQuery() throws Exception {
		qadap = new QueryAdapter();
		String lgname;

		// this code will receive the values send from the createGroup page
		/*
		 * Bundle extras = getIntent().getExtras(); if (extras != null) {
		 * System.out.println("This is coming from the create group page"); }
		 * else { System.out
		 * .println("This is coming from the User page and this is not admin");
		 * } System.out.println("The table searched is" + tableName);
		 */
		ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> list, ParseException arg1) {

				if (arg1 != null) {

					Toast.makeText(getApplicationContext(), arg1.toString(),
							Toast.LENGTH_LONG).show();

				}
				if (list == null) {

					Toast.makeText(getApplicationContext(),
							"No user in database", Toast.LENGTH_LONG).show();
				} else {

					assoclist = new ArrayList<ParseObject>(list);

					// listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					// listview.setTranscriptMode(mode);

					qa = new QueryAdapter();
					ListView listview = (ListView) findViewById(R.id.chatmainlist);
					listview.setStackFromBottom(true);
					// listview.setScrollingCacheEnabled(true);
					if (qa.isEmpty())
						System.out.println("THe value is empty");
					qa.notifyDataSetChanged();
					Drawable draw = getResources().getDrawable(
							R.drawable.transparent);

					listview.setDivider(draw);
					listview.setDividerHeight(10);
					listview.setAdapter(qa);

				}
				
				
				
			}
		});
		//this notifies the adapter for the change in values. 
		qadap.notifyDataSetChanged();

	}
	//this method switchs off the timer thread when the activity is paused
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunning = false;
		System.out.println("application minimized");

		t.cancel();
		// handler.removeCallbacks(r);

	}

	// this is the adapter class responsible for creating the customized list
	// view.
	private class QueryAdapter extends BaseAdapter {

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub

			return super.getItemViewType(position);
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return assoclist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return assoclist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			try {
				counter++;
				ParseObject currentObject = assoclist.get(arg0);

				String user = currentObject.getString("name");
				int maxValue = assoclist.size();
				if (currentObject != null) {
					if (currentObject.getString("filetype") != null)

						action = currentObject.getString("filetype");

					int itemType = getItemViewType(arg0);

					if (arg1 == null) {
						try {
							//here we are creating two different layouts for sending and receiving the messages.
							if (currentObject
									.getString("username")
									.toString()
									.equals(ParseUser.getCurrentUser()
											.getUsername().toString())) {

								System.out.println("sent");
								arg1 = getLayoutInflater().inflate(
										R.layout.chatitemsent, null);
								//if(action.equals("text"))
								//arg1.setBackgroundResource(R.drawable.send);

							} else {
								
								
								System.out.println("received");
								
								arg1 = getLayoutInflater().inflate(
										R.layout.chatitem, null);
								//arg1.setLayoutDirection(1);
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (isAddItem) {
							LinearLayout layout = (LinearLayout) arg1;
							layout.setLayoutParams(new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							//this is when the message is of type text
							if (action.equals("text")) {
								System.out.println("main text added");
								//layout.setBackgroundResource(R.drawable.send);
								ParseObject ponew = assoclist.get(arg0);
								if (ponew != null) {
									try {
										if (ponew.getString("filetype").equals(
												"text")) {
											TextView username = new TextView(
													getApplicationContext());
											username.setText(ponew.get("text")
													.toString());
											TextView textText = new TextView(
													getApplicationContext());
											textText.setText(user + " :");
											textText.setTextColor(getResources()
													.getColor(R.color.Black));
											username.setTextColor(getResources()
													.getColor(R.color.Black));
											if (currentObject
													.getString("username")
													.toString()
													.equals(ParseUser
															.getCurrentUser()
															.getUsername()
															.toString()))
												layout.setGravity(Gravity.RIGHT);
												textText.setTextColor(getResources()
														.getColor(R.color.Black));
											username.setTextColor(getResources()
													.getColor(R.color.Black));
											textText.setTypeface(null,
													Typeface.BOLD_ITALIC);

											TextView objectIdtv = new TextView(
													getApplicationContext());
											final String objectId = ponew
													.getObjectId();
											System.out
													.println("the objectid received by the chat apge is"
															+ objectId);
											objectIdtv.setText(objectId);
											objectIdtv.setTextSize(0);
											objectIdtv
													.setTextColor(getResources()
															.getColor(
																	android.R.color.transparent));
											//this is the method to delete the messages.
											layout.setOnLongClickListener(new View.OnLongClickListener() {

												@Override
												public boolean onLongClick(
														View v) {
													// TODO Auto-generated
													// method stub
													LinearLayout innerLayout = (LinearLayout) v;
													TextView tv = (TextView) innerLayout
															.getChildAt(2);
													System.out
															.println("the data present in the child is"
																	+ tv.getText()
																			.toString());
													Intent delete = new Intent(
															getApplicationContext(),
															DeleteDialog.class);
													delete.putExtra(
															"tablename",
															tableName);
													delete.putExtra("objectid",
															tv.getText()
																	.toString());
													ParseUser pu=ParseUser.getCurrentUser();
													String role=pu.getString("role");
													if(!role.equals("trainee"))
													startActivity(delete);

													return false;
												}
											});

											layout.addView(textText, 0);
											layout.addView(username, 1);
											layout.addView(objectIdtv, 2);
											checker.add(ponew.getString("text")
													.toString());

										}
									} catch (Exception e) {

									}
								}
							}

							if (action.equals("image")) {

								System.out.println("main image added");

								ParseObject ponew = assoclist.get(arg0);
								if (ponew != null) {
									try {
										if (ponew.getString("filetype").equals(
												"image")) {
											ImageView imageview = new ImageView(
													getApplicationContext());

											TextView imageText = new TextView(
													getApplicationContext());
											imageText.setText(user + " :");
											imageText
													.setTextColor(getResources()
															.getColor(
																	R.color.Black));
											Bitmap innerBitmap;
											imageText.setTypeface(null,
													Typeface.BOLD_ITALIC);
											String innerFileName = ponew
													.getString("filename");
											TextView objectIdtv = new TextView(
													getApplicationContext());
											final String objectId = ponew
													.getObjectId();

											System.out
													.println("the objectid received by the chat apge is"
															+ objectId);
											objectIdtv.setText(objectId);
											objectIdtv.setTextSize(0);
											objectIdtv
													.setTextColor(getResources()
															.getColor(
																	android.R.color.transparent));
											boolean isPresent = dbhandler
													.imageChecker(innerFileName);
											if (isPresent) {
												System.out
														.println("image is already present");
												innerBitmap = dbhandler
														.getImage(innerFileName);
											} else {
												System.out
														.println("image is  not present");
												innerBitmap = fetchImage(ponew);
											}
											// dbhandler.imageChecker(innerFileName);

											imageview
													.setImageBitmap(innerBitmap);

											int count = Collections.frequency(
													checker,
													ponew.getString("filename")
															.toString());

											layout.addView(imageText, 0);
											layout.addView(imageview, 1);
											layout.addView(objectIdtv, 2);
											layout.setOnLongClickListener(new View.OnLongClickListener() {

												@Override
												public boolean onLongClick(
														View v) {
													// TODO Auto-generated
													// method stub
													LinearLayout innerLayout = (LinearLayout) v;
													TextView tv = (TextView) innerLayout
															.getChildAt(2);
													System.out
															.println("the data present in the child is"
																	+ tv.getText()
																			.toString());
													Intent delete = new Intent(
															getApplicationContext(),
															DeleteDialog.class);
													delete.putExtra(
															"tablename",
															tableName);
													delete.putExtra("objectid",
															tv.getText()
																	.toString());
													ParseUser pu=ParseUser.getCurrentUser();
													String role=pu.getString("role");
													if(!role.equals("trainee"))
													startActivity(delete);

													return false;
												}
											});

											checker.add(ponew.getString(
													"filename").toString());

										}
									} catch (Exception ex) {

										ex.printStackTrace();
									}

								}
							}
							if (action.equals("video")) {

								ParseObject ponew = assoclist.get(arg0);
								if (ponew != null) {
									try {
										if (ponew.getString("filetype").equals(
												"video")) {

											TextView textText = new TextView(
													getApplicationContext());
											textText.setText(user + " :");
											TextView username = new TextView(
													getApplicationContext());
											TextView filePathTv = new TextView(
													getApplicationContext());
											TextView actionTv = new TextView(
													getApplicationContext());

											final String innerVideoFileName = ponew
													.getString("filename");
											if (videoDatabaseHandler
													.videoChecker(innerVideoFileName)) {
												Bitmap thumbnail=ThumbnailUtils.createVideoThumbnail("/storage/sdcard0/tvmilp/"+ innerVideoFileName,0 );
												Bitmap playbit=BitmapFactory.decodeResource(getResources(), R.drawable.play);
												
												BitmapDrawable ob = new BitmapDrawable(getResources(), thumbnail);
												BitmapDrawable ob1=new BitmapDrawable(getResources(),playbit);
												Drawable[] layers=new Drawable[2];
												Drawable dr=(Drawable)ob;
												Drawable dr1=(Drawable)ob1;
												layers[0]=dr;
												layers[1]=dr1;
												LayerDrawable layer=new  LayerDrawable(layers);
												actionTv.setBackgroundDrawable(layer);
											} else {
												actionTv.setBackgroundResource(R.drawable.download);
											}
											if (currentObject
													.getString("username")
													.toString()
													.equals(ParseUser
															.getCurrentUser()
															.getUsername()
															.toString())) {
												// layout.setGravity(Gravity.RIGHT);
												// textText.setGravity(Gravity.RIGHT);
												// username.setGravity(Gravity.RIGHT);
												System.out
														.println("right worked");

											}
											textText.setTextColor(getResources()
													.getColor(R.color.Black));

											textText.setTypeface(null,
													Typeface.BOLD_ITALIC);

											username.setText(innerVideoFileName);
											username.setTextColor(getResources()
													.getColor(R.color.Black));
											TextView objectIdtv = new TextView(
													getApplicationContext());
											final String objectId = ponew
													.getObjectId();
											System.out
													.println("the objectid received by the chat apge is"
															+ objectId);
											objectIdtv.setText(objectId);
											objectIdtv.setTextSize(0);
											objectIdtv
													.setTextColor(getResources()
															.getColor(
																	android.R.color.transparent));
											filePathTv.setText(ponew
													.getString("filepath"));
											filePathTv.setTextSize(0);
											filePathTv
													.setTextColor(getResources()
															.getColor(
																	android.R.color.transparent));

											layout.setOnLongClickListener(new View.OnLongClickListener() {

												@Override
												public boolean onLongClick(
														View v) {
													// TODO Auto-generated
													// method stub
													LinearLayout innerLayout = (LinearLayout) v;
													TextView tv = (TextView) innerLayout
															.getChildAt(2);
													System.out
															.println("the data present in the child is"
																	+ tv.getText()
																			.toString());
													Intent delete = new Intent(
															getApplicationContext(),
															DeleteDialog.class);
													delete.putExtra(
															"tablename",
															tableName);
													delete.putExtra("objectid",
															tv.getText()
																	.toString());
													ParseUser pu=ParseUser.getCurrentUser();
													String role=pu.getString("role");
													if(!role.equals("trainee"))
													startActivity(delete);

													return false;
												}
											});

											layout.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													// TODO Auto-generated
													// method stub
													LinearLayout innerLayout = (LinearLayout) arg0;
													System.out
															.println("VIDEO IS CLICKED");
													TextView tv = (TextView) innerLayout
															.getChildAt(2);
													TextView tvPath = (TextView) innerLayout
															.getChildAt(3);
													String objid = tv.getText()
															.toString();
													// String
													// objid=tv.getText().toString();
													System.out
															.println("the video name sent for checking is"
																	+ innerVideoFileName);

													if (videoDatabaseHandler
															.videoChecker(innerVideoFileName)) {
														System.out
																.println("video is already present in the phone");
														Intent intent = new Intent(
																Intent.ACTION_VIEW,
																Uri.parse(tvPath
																		.getText()
																		.toString()));
														intent.setDataAndType(
																Uri.parse("/storage/sdcard0/tvmilp/"
																		+ innerVideoFileName),
																"video/*");
														startActivity(intent);
													} else {
														System.out
																.println("video is not present in the phone");
														downloadVideo(objid);
													}

												}
											});

											layout.addView(textText, 0);
											layout.addView(username, 1);
											layout.addView(objectIdtv, 2);
											layout.addView(filePathTv, 3);
											layout.addView(actionTv, 3);
											System.out
													.println("main video added");

											checker.add(ponew.getString("text")
													.toString());

										}
									} catch (Exception e) {

									}
								}
							}
						}
						//writing the above inflators in the else part because the recycler of the list view was only loading 
						// two views at a time and repeating that two views.In else part i have counted the views received from the 
						//cloud and then inflated till the counter equals to 0.
					} else {

						if (arg0 != 1) {
							try {
								if (currentObject.getString("username").equals(
										ParseUser.getCurrentUser()
												.getUsername().toString())) {
									arg1 = getLayoutInflater().inflate(
											R.layout.chatitemsent, null);
								} else {

									arg1 = getLayoutInflater().inflate(
											R.layout.chatitem, null);
								}
							} catch (Exception e) {

							}
							if (isAddItem) {
								LinearLayout layout = (LinearLayout) arg1;

								layout.setLayoutParams(new LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));

								if (action.equals("text")) {
									System.out.println("sec text added");
									ParseObject ponew = assoclist.get(arg0);
									if (ponew != null) {
										try {
											if (ponew.getString("filetype")
													.equals("text")) {

												TextView textText = new TextView(
														getApplicationContext());
												textText.setText(user + " :");
												TextView username = new TextView(
														getApplicationContext());
												if (currentObject
														.getString("username")
														.toString()
														.equals(ParseUser
																.getCurrentUser()
																.getUsername()
																.toString())) {
													// layout.setGravity(Gravity.RIGHT);
													// textText.setGravity(Gravity.RIGHT);
													// username.setGravity(Gravity.RIGHT);
													System.out
															.println("right worked");

												}
												textText.setTextColor(getResources()
														.getColor(R.color.Black));

												textText.setTypeface(null,
														Typeface.BOLD_ITALIC);

												username.setText(ponew.get(
														"text").toString());
												username.setTextColor(getResources()
														.getColor(R.color.Black));
												TextView objectIdtv = new TextView(
														getApplicationContext());
												final String objectId = ponew
														.getObjectId();
												System.out
														.println("the objectid received by the chat apge is"
																+ objectId);
												objectIdtv.setText(objectId);
												objectIdtv.setTextSize(0);
												objectIdtv
														.setTextColor(getResources()
																.getColor(
																		android.R.color.transparent));
												layout.setOnLongClickListener(new View.OnLongClickListener() {

													@Override
													public boolean onLongClick(
															View v) {
														// TODO Auto-generated
														// method stub
														LinearLayout innerLayout = (LinearLayout) v;
														TextView tv = (TextView) innerLayout
																.getChildAt(2);
														System.out
																.println("the data present in the child is"
																		+ tv.getText()
																				.toString());
														Intent delete = new Intent(
																getApplicationContext(),
																DeleteDialog.class);
														delete.putExtra(
																"tablename",
																tableName);
														delete.putExtra(
																"objectid",
																tv.getText()
																		.toString());
														ParseUser pu=ParseUser.getCurrentUser();
														String role=pu.getString("role");
														if(!role.equals("trainee"))
														startActivity(delete);

														return false;
													}
												});

												layout.addView(textText, 0);
												layout.addView(username, 1);
												layout.addView(objectIdtv, 2);
												checker.add(ponew.getString(
														"text").toString());

											}
										} catch (Exception e) {

										}
									}
								}
								// this is the second image snippet.
								if (action.equals("image")) {
									System.out.println("sec image added");
									ParseObject ponew = assoclist.get(arg0);
									if (ponew != null) {
										try {
											if (ponew.getString("filetype")
													.equals("image")) {
												ImageView imageview = new ImageView(
														getApplicationContext());
												TextView imageText = new TextView(
														getApplicationContext());
												String innerFileName = ponew
														.getString("filename");

												TextView objectIdTv = new TextView(
														getApplicationContext());
												final String objectId = ponew
														.getObjectId();

												System.out
														.println("the objectid received by the chat apge is"
																+ objectId);
												objectIdTv
														.setText(innerFileName);
												objectIdTv.setTextSize(0);
												objectIdTv
														.setTextColor(getResources()
																.getColor(
																		android.R.color.transparent));
												imageText.setText(user + " :");
												imageText
														.setTextColor(getResources()
																.getColor(
																		R.color.Black));
												Bitmap innerBitmap;
												imageText.setTypeface(null,
														Typeface.BOLD_ITALIC);

												boolean isPresent = dbhandler
														.imageChecker(innerFileName);
												if (isPresent) {
													innerBitmap = dbhandler
															.getImage(innerFileName);
													System.out
															.println("image is already present");
												} else {
													System.out
															.println("image is not present");
													innerBitmap = fetchImage(ponew);
												}

												imageview
														.setImageBitmap(innerBitmap);
												layout.setOnLongClickListener(new View.OnLongClickListener() {

													@Override
													public boolean onLongClick(
															View v) {
														// TODO Auto-generated
														// method stub
														LinearLayout innerLayout = (LinearLayout) v;
														TextView tv = (TextView) innerLayout
																.getChildAt(2);
														System.out
																.println("the data present in the child is"
																		+ tv.getText()
																				.toString());
														Intent delete = new Intent(
																getApplicationContext(),
																DeleteDialog.class);
														delete.putExtra(
																"tablename",
																tableName);
														delete.putExtra(
																"objectid",
																tv.getText()
																		.toString());
														ParseUser pu=ParseUser.getCurrentUser();
														String role=pu.getString("role");
														if(!role.equals("trainee"))
														startActivity(delete);

														return false;
													}
												});

												int count = Collections
														.frequency(
																checker,
																ponew.getString(
																		"filename")
																		.toString());

												layout.addView(imageText, 0);
												layout.addView(imageview, 1);
												layout.addView(objectIdTv, 2);

												checker.add(ponew.getString(
														"filename").toString());

											}
										} catch (Exception ex) {

											ex.printStackTrace();
										}

									}
								}
								if (action.equals("video")) {

									ParseObject ponew = assoclist.get(arg0);
									if (ponew != null) {
										try {
											if (ponew.getString("filetype")
													.equals("video")) {

												TextView textText = new TextView(
														getApplicationContext());
												textText.setText(user + " :");
												TextView username = new TextView(
														getApplicationContext());
												TextView actionTv = new TextView(
														getApplicationContext());
												final String innerVideoFileName = ponew
														.getString("filename");
												String videoUri;
												if (videoDatabaseHandler
														.videoChecker(innerVideoFileName)) {
													
													Bitmap thumbnail=ThumbnailUtils.createVideoThumbnail("/storage/sdcard0/tvmilp/"+ innerVideoFileName,0 );
													Bitmap playbit=BitmapFactory.decodeResource(getResources(), R.drawable.play);
													
													BitmapDrawable ob = new BitmapDrawable(getResources(), thumbnail);
													BitmapDrawable ob1=new BitmapDrawable(getResources(),playbit);
													Drawable[] layers=new Drawable[2];
													Drawable dr=(Drawable)ob;
													Drawable dr1=(Drawable)ob1;
													layers[0]=dr;
													layers[1]=dr1;
													LayerDrawable layer=new  LayerDrawable(layers);
													actionTv.setBackgroundDrawable(layer);
													//actionTv.setBackgroundDrawable(ob1);

												} else {
													actionTv.setBackgroundResource(R.drawable.download);
												}
												if (currentObject
														.getString("username")
														.toString()
														.equals(ParseUser
																.getCurrentUser()
																.getUsername()
																.toString())) {
													// layout.setGravity(Gravity.RIGHT);
													// textText.setGravity(Gravity.RIGHT);
													// username.setGravity(Gravity.RIGHT);
													System.out
															.println("right worked");

												}
												textText.setTextColor(getResources()
														.getColor(R.color.Black));

												textText.setTypeface(null,
														Typeface.BOLD_ITALIC);

												username.setText(innerVideoFileName);
												username.setTextColor(getResources()
														.getColor(R.color.Black));
												TextView objectIdtv = new TextView(
														getApplicationContext());
												final String objectId = ponew
														.getObjectId();
												System.out
														.println("the objectid received by the chat apge is"
																+ objectId);
												objectIdtv.setText(objectId);
												objectIdtv.setTextSize(0);
												objectIdtv
														.setTextColor(getResources()
																.getColor(
																		android.R.color.transparent));
												layout.setOnLongClickListener(new View.OnLongClickListener() {

													@Override
													public boolean onLongClick(
															View v) {
														// TODO Auto-generated
														// method stub
														LinearLayout innerLayout = (LinearLayout) v;
														TextView tv = (TextView) innerLayout
																.getChildAt(2);
														System.out
																.println("the data present in the child is"
																		+ tv.getText()
																				.toString());
														Intent delete = new Intent(
																getApplicationContext(),
																DeleteDialog.class);
														delete.putExtra(
																"tablename",
																tableName);
														delete.putExtra(
																"objectid",
																tv.getText()
																		.toString());
														ParseUser pu=ParseUser.getCurrentUser();
														String role=pu.getString("role");
														if(!role.equals("trainee"))
														startActivity(delete);

														return false;
													}
												});
												layout.setOnClickListener(new View.OnClickListener() {

													@Override
													public void onClick(
															View arg0) {
														// TODO Auto-generated
														// method stub
														System.out
																.println("VIDEO IS CLICKED");
														LinearLayout innerLayout = (LinearLayout) arg0;
														TextView tv = (TextView) innerLayout
																.getChildAt(2);
														String objid = tv
																.getText()
																.toString();
														System.out
																.println("the video name sent for checking is"
																		+ innerVideoFileName);

														if (videoDatabaseHandler
																.videoChecker(innerVideoFileName)) {
															System.out
																	.println("video is already present in the phone");
															TextView tvPath = (TextView) innerLayout
																	.getChildAt(3);
															Intent intent = new Intent(
																	Intent.ACTION_VIEW,
																	Uri.parse(tvPath
																			.getText()
																			.toString()));
															intent.setDataAndType(
																	Uri.parse("/storage/sdcard0/tvmilp/"
																			+ innerVideoFileName),
																	"video/*");
															startActivity(intent);
														} else {
															System.out
																	.println("video is not present in the phone");
															downloadVideo(objid);
														}

													}
												});

												layout.addView(textText, 0);
												layout.addView(username, 1);
												layout.addView(objectIdtv, 2);
												layout.addView(actionTv, 3);
												System.out
														.println("sec video added");

												checker.add(ponew.getString(
														"text").toString());

											}
										} catch (Exception e) {

										}
									}
								}
							}

						}
					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return arg1;
		}

	}
	//this is the asynctack private class for uploading the images
	private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dia = ProgressDialog.show(ChatPage.this, null,
				"Uploading Image...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			if (filePath != null) {
				File file = new File(filePath);
				String uploadFileType = filePath.substring(filePath
						.lastIndexOf(".") + 1);
				System.out.println("FILETYPE FILETYPE FILETYPE"
						+ uploadFileType);
				ByteArrayOutputStream bos = null;

				try {

					FileInputStream fis = new FileInputStream(file);

					bos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						bos.write(buf, 0, readNum); // no doubt here is 0

						// System.out.println("read " + readNum + " bytes,");
					}
				} catch (IOException ex) {

				}
				byte[] image = bos.toByteArray();

				// byte[] image = FileUtils.toByteArray(new
				// FileInputStream(file));
				ParseFile imageFile = new ParseFile("icon." + uploadFileType,
						image);
				imageFile.saveInBackground();
				ParseObject po = new ParseObject(tableName);
				po.put("username", ParseUser.getCurrentUser().getUsername()
						.toString());
				fileName = file.getName();
				po.put("filename", fileName);
				long fileSize = file.getTotalSpace();
				System.out.println("the Size of the file is" + fileSize
						/ (1024 * 1024));
				po.put("file", imageFile);
				po.put("filetype", "image");
				try {
					po.save();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// this code will save the image in teh database along with its
				// name
				// in the databse

				dbhandler.saveimage(fileName, image);
				// dia.dismiss();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				fetchQuery();
				pushQuery();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			dia.dismiss();
		}

	}
	//this is the asynctask private class for uploading the video.
	private class MyVideoAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog diaVideo = ProgressDialog.show(ChatPage.this, null,
				"Uploading Video...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			if (filePath != null) {
				File file = new File(filePath);
				long size = file.length() / (1024 * 1024);
				System.out.println("the size of the video is mb" + size);
				String uploadFileType = filePath.substring(filePath
						.lastIndexOf(".") + 1);
				ByteArrayOutputStream bos = null;

				try {

					FileInputStream fis = new FileInputStream(file);

					bos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						bos.write(buf, 0, readNum); // no doubt here is 0

						// System.out.println("read " + readNum + " bytes,");
					}
				} catch (IOException ex) {

				}
				byte[] image = bos.toByteArray();
				fileName = file.getName();
				File videoFile = new File(
						Environment.getExternalStorageDirectory() + "/tvmilp");
				videoFile.mkdirs();
				videoFile = new File(videoFile.getPath() + "/" + fileName);
				try {
					videoFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(
							videoFile);
					fileOutputStream.write(image);
					fileOutputStream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// byte[] image = FileUtils.toByteArray(new
				// FileInputStream(file));
				ParseFile imageFile = new ParseFile("icon." + uploadFileType,
						image);
				try {
					imageFile.save();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ParseObject po = new ParseObject(tableName);
				po.put("username", ParseUser.getCurrentUser().getUsername()
						.toString());

				po.put("filename", fileName);
				po.put("filepath", filePath);
				long fileSize = file.length();
				System.out.println("the Size of the file is" + fileSize
						/ (1024 * 1024));
				po.put("file", imageFile);
				po.put("filetype", "video");
				try {
					po.save();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// this code will save the image in teh database along with its
				// name
				// in the databse

				videoDatabaseHandler.saveVideo(fileName);
				// dia.dismiss();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				fetchQuery();
				pushQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

			diaVideo.dismiss();
		}

	}
	boolean doubleBackToExitPressedOnce = false;

	@Override
	public void onBackPressed() {
		
		 ParseUser pu = ParseUser.getCurrentUser();
		String role = pu.getString("role");
		if (role.equals("trainee")) {
			
		if (doubleBackToExitPressedOnce) {
			 super.onBackPressed();
			
			
			

				
				Intent i=new Intent(getApplicationContext(),Launcher.class);
				i.putExtra("exit", 0);
				startActivity(i);
				//android.os.Process.killProcess(android.os.Process.myPid());
				//super.finish();
				//Intent intent = new Intent(Intent.ACTION_MAIN);
				//intent.addCategory(Intent.CATEGORY_HOME);
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//startActivity(intent);
				//finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
			}
		 this.doubleBackToExitPressedOnce = true;
		
		if(role.equals("trainee"))
		 Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
	        return;
	    }
	    if (role.equals("superadmin")) {
			Intent intent = new Intent(getApplicationContext(),
					SelectGroup.class);
			startActivity(intent);
		}
		if (role.equals("admin")) {
			Intent i=new Intent(getApplicationContext(),CreateGroup.class);
			startActivity(i);
		}

	   
	   

	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	
	

}
