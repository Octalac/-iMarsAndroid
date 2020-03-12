package com.os.imars.comman.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.comman.ChatResponse;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.firebase.MyFirebaseInstanceIDService;
import com.os.imars.operator.activity.UpcomingCustomSurveyDetailsActivity;
import com.os.imars.operator.adapter.ChatRVAdapter;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.utility.FcmNotificationBuilder;
import com.os.imars.utility.ImagePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity implements ImagePickerUtility.ImagePickerCallback {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView rvChat;
    private ChatRVAdapter chatRVAdapter;
    private ImageView imvBack;
    private LinearLayout llAttachFile, llSendMessage, llMessage;
    private EditText edtMessage;
    private String message = "", receiver_id = "", deviceId = "", receiver_name = "", str_Groupname = "", referenceKey = "", survey_status = "", user_id = "", surveyId = "";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private TextView txtUserName, txtNoRecordFound;
    private Session session;
    private ImagePickerUtility imagePickerUtility;
    private List<ChatResponse> chatResponseList;
    private File fileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ChatHistory");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initView();
    }

    private void initView() {
        imagePickerUtility = new ImagePickerUtility(this, this);
        session = Session.getInstance(this);
        chatResponseList = new ArrayList<>();
        receiver_id = getIntent().getStringExtra("receiver_id");
        receiver_name = getIntent().getStringExtra("receiver_name");
        survey_status = getIntent().getStringExtra("status");
        user_id = getIntent().getStringExtra("user_id");
        surveyId = getIntent().getStringExtra("surveyId");
        deviceId = getIntent().getStringExtra("deviceId");

/*        Log.d("1234", "onClick: chat" + receiver_id);
        Log.d("1234", "onClick:1 chat" + receiver_name);
        Log.d("1234", "onClick:2chat " + operator_id);*/

        Log.d("1234", "initView: " + session.getUserData().getUserId() + "deviceId : " + deviceId);


        //  getSurveyDataFrom();


        if (Integer.parseInt(session.getUserData().getUserId()) > Integer.parseInt(receiver_id)) {
            str_Groupname = session.getUserData().getUserId() + "_" + receiver_id;
        } else {
            str_Groupname = receiver_id + "_" + session.getUserData().getUserId();
        }
        imvBack = (ImageView) findViewById(R.id.imvBack);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        llAttachFile = findViewById(R.id.llAttachFile);
        txtUserName = findViewById(R.id.txtUserName);
        llSendMessage = findViewById(R.id.llSendMessage);
        llMessage = findViewById(R.id.llMessage);
        txtNoRecordFound = findViewById(R.id.txtNoRecordFound);
        edtMessage = findViewById(R.id.edtMessage);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(mLayoutManager);
        rvChat.setItemAnimator(new DefaultItemAnimator());
        chatRVAdapter = new ChatRVAdapter(Env.currentActivity, chatResponseList, session.getUserData().getUserId());
        rvChat.setAdapter(chatRVAdapter);
        imvBack.setOnClickListener(this);
        llAttachFile.setOnClickListener(this);
        llSendMessage.setOnClickListener(this);
        if (survey_status.equals("2")) {
            llMessage.setVisibility(View.GONE);
        } else {
            if (!user_id.equals("")) {
                if (!user_id.equals(session.getUserData().getUserId())) {
                    llMessage.setVisibility(View.GONE);
                } else {
                    llMessage.setVisibility(View.VISIBLE);
                }
            }
        }
        myRef.child(surveyId).child(str_Groupname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatResponseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatResponse chatResponse = snapshot.getValue(ChatResponse.class);
                    chatResponseList.add(chatResponse);
                }

                if (chatResponseList.size() == 0) {
                    txtNoRecordFound.setVisibility(View.VISIBLE);
                    rvChat.setVisibility(View.GONE);
                } else {
                    rvChat.setVisibility(View.VISIBLE);
                    txtNoRecordFound.setVisibility(View.GONE);
                }
                rvChat.smoothScrollToPosition(chatResponseList.size());
                chatRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("1234", "onCancelled: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.llSendMessage:
                sendMessage();
                break;
            case R.id.llAttachFile:
                showImageSelectionDialog();
                break;
        }

    }

    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        imagePickerUtility.captureImageFromCamera();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    try {
                        imagePickerUtility.chooseImageFromGallary();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePickerUtility.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onImagePickSuccess() {
        fileImage = imagePickerUtility.getImageFile();
        sendImageMessage();

    }

    private void uploadImage() {
        StorageReference riversRef = mStorageRef.child("images/" + System.currentTimeMillis());
        Uri file = Uri.fromFile(fileImage);
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                message = uri.toString();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("message", message);
                                myRef.child(surveyId).child(str_Groupname).child(referenceKey).updateChildren(childUpdates);
                                chatRVAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });


    }

    private void sendImageMessage() {
        if (fileImage != null) {
            referenceKey = myRef.push().getKey();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String currentDateAndTime = df.format(new Date());
            Long tsLong = System.currentTimeMillis() / 1000;
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setChatType("Image");
            chatResponse.setDate(currentDateAndTime);
            chatResponse.setMessage("");
            chatResponse.setReceiverName(receiver_name);
            chatResponse.setTimestamp(tsLong.toString());
            chatResponse.setReceiverId(receiver_id);
            chatResponse.setSenderImage("");
            chatResponse.setSenderId(session.getUserData().getUserId());
            chatResponse.setSenderName(session.getUserData().getFirstName() + " " + session.getUserData().getLastName());
            myRef.child(surveyId).child(str_Groupname).child(referenceKey).setValue(chatResponse);
            FcmNotificationBuilder.initialize()
                    .message(message)
                    .username(session.getUserData().getFirstName() + " " + session.getUserData().getLastName() + " sent a new message").setCustomData("notification_type", "chat")
                    .setCustomData("user_id", session.getUserData().getUserId())
                    .setCustomData("device_id", deviceId)
                    .setCustomData("user_name", session.getUserData().getFirstName()).receiverFirebaseToken(deviceId).send();
            edtMessage.setText(null);
            uploadImage();
            String json = "";
            Bundle bundle = new Bundle();
            bundle.putString("chatMessageBundle", json);
            RemoteMessage remoteMessage = new RemoteMessage(bundle);
            Util.notificationManager(ChatActivity.this, remoteMessage);
        }

    }

    @Override
    public void onImagePickError(String message) {

    }

    private void sendMessage() {
        message = edtMessage.getText().toString();
        String newMessage = message.trim();
        if (newMessage.equals("")) {
            Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show();
        } else {
            referenceKey = myRef.push().getKey();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String currentDateAndTime = df.format(new Date());
            Long tsLong = System.currentTimeMillis() / 1000;
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setChatType("Text");
            chatResponse.setDate(currentDateAndTime);
            chatResponse.setMessage(message);
            chatResponse.setReceiverName(receiver_name);
            chatResponse.setTimestamp(tsLong.toString());
            chatResponse.setReceiverId(receiver_id);
            chatResponse.setSenderImage("");
            chatResponse.setSenderId(session.getUserData().getUserId());
            chatResponse.setSenderName(session.getUserData().getFirstName() + " " + session.getUserData().getLastName());
            myRef.child(surveyId).child(str_Groupname).child(referenceKey).setValue(chatResponse);
            edtMessage.setText(null);
            Log.d("1234", "deviceId: " + deviceId);
            FcmNotificationBuilder.initialize()
                    .message(message)
                    .username(session.getUserData().getFirstName() + " " + session.getUserData().getLastName() + " sent a new message").setCustomData("notification_type", "chat")
                    .setCustomData("user_id", session.getUserData().getUserId())
                    .setCustomData("device_id", deviceId)
                    .setCustomData("user_name", session.getUserData().getFirstName()).receiverFirebaseToken(deviceId).send();
            chatRVAdapter.notifyDataSetChanged();
            callApi(message);
        }
    }

    private void callApi(String message) {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("receiver_id", receiver_id);
        hashMap.put("sender_id", session.getUserData().getUserId());
        hashMap.put("message", message);
        Call<CommonResponse> call = apiService.chat_email(hashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
            }
        });
    }


}
