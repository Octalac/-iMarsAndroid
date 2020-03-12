package com.os.imars.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.RemoteMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.os.imars.R;
import com.os.imars.comman.activity.SplashActivity;
import com.os.imars.config.Config;
import com.os.imars.views.BaseView.Env;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by nitesh on 4/26/2017.
 */

public final class Util {

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,20}$";
    private static Pattern pattern;
    private static Matcher matcher;
    private static ProgressDialog progressDialog = null;
    public static Dialog confirmation;
    private static final long[] vPattern = new long[]{1000, 1000, 1000, 1000, 1000};
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final String URL_STORAGE_REFERENCE = "gs://the-agenciz-d377a.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";

    private static Session session;

    public static boolean checkGooglePlayServicesAvailable(Context context) {
        try {
            final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (status == ConnectionResult.SUCCESS) {
                return true;
            }

            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showCenteredToast("Google Play Services Not Available ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    public static void showCenteredToast(String msg) {
        try {
            Toast toast = Toast.makeText(Env.currentActivity, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DisplayImageOptions displayOption(Context mContext) {
        try {
            Bitmap default_bitmap = Util.drawableToBitmap(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
            DisplayImageOptions option = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .showImageOnFail(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
            return option;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        String str = null;

        try {
            Date date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String parseDateToAnyFormat(String inputPattern, String outputPattern, String time) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        String str = null;

        try {
            Date date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static boolean hasInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void noInternet(CoordinatorLayout coordinatorLayout, View.OnClickListener onClickListener, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, context.getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.retry), onClickListener);
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red_500));
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_800));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String msg, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_800));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLocationEnabled(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return !(!gps_enabled && !network_enabled);
    }

    public static void showErrorSnackBar(CoordinatorLayout coordinatorLayout, String msg, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_400));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessSnackBar(CoordinatorLayout coordinatorLayout, String msg, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_500));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isEmailValid(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static boolean isPasswordValidate(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public static Dialog showProDialog(Context context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(context, R.style.NewDialog);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress));
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }


    public static Dialog dismissProDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    public static void openKeyBoard(View textView) {
        try {
            InputMethodManager imm = (InputMethodManager) Env.currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyBoardMethod(final Context con, final View view) {
        try {
            view.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendNotification(Context ctx, RemoteMessage notificationData) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            Integer notifiId = (int) System.currentTimeMillis();
            Intent intent_for_Notification = new Intent();
            Bundle b = new Bundle();
            b.putString("notification_type", notificationData.getData().get("type"));
            b.putString("message", notificationData.getData().get("message"));
            b.putString("title", notificationData.getData().get("title"));
            b.putString("notification_id", notificationData.getData().get("notification_id"));
            b.putString("badge_count", notificationData.getData().get("badge_count"));
            Log.d("1234", "sendNotification: " + notificationData.getData().get("message") + "title:" + notificationData.getData().get("title"));
            intent_for_Notification.putExtras(b);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(""));
            mBuilder.setContentText(notificationData.getData().get("title"));
            mBuilder.setContentTitle(ctx.getResources().getString(R.string.app_name));
            intent_for_Notification.putExtras(b);
            intent_for_Notification.setClass(ctx, SplashActivity.class);
            intent_for_Notification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, notifiId, intent_for_Notification, 0);
            mBuilder.setContentIntent(contentIntent);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setColor(ctx.getResources().getColor(R.color.trans));
            } else {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }
            mBuilder.setVibrate(vPattern);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setAutoCancel(true);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mNotificationManager.notify(notifiId, mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notificationManager(Context ctx, RemoteMessage data) {
        try {

            Log.d("1234","notification"+data.getNotification());
            session = Session.getInstance(ctx);
            String message = data.getNotification().getBody();
            String title = data.getNotification().getTitle();
            PendingIntent pendingIntent = null;
            if (Config.getNotificationManager().equalsIgnoreCase("END")) {
                Intent intent_for_Notification = null;
                if (session.getUserData().getType().equals("0") || session.getUserData().getType().equals("1")) {
                    intent_for_Notification = new Intent(ctx, SplashActivity.class);
                    intent_for_Notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                } else {
                    intent_for_Notification = new Intent(ctx, SplashActivity.class);
                    intent_for_Notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                Bundle b = new Bundle();
                b.putString("message", message);
                b.putString("title", title);
                intent_for_Notification.putExtras(b);
                pendingIntent = PendingIntent.getActivity(ctx, 0, intent_for_Notification, PendingIntent.FLAG_ONE_SHOT);
            }
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, "3")
                    .setSmallIcon(R.mipmap.ic_launcher_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("3", "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(0, notificationBuilder.build());
                }
            } else {
                if (notificationManager != null) {
                    notificationManager.notify(0, notificationBuilder.build());
                }
            }
            notificationBuilder.setAutoCancel(true);
            Notification notification = notificationBuilder.build();
            NotificationManagerCompat.from(Env.currentActivity).notify(0,notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getPath(Uri uri, Activity activity) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {

            if ("content".equals(contentUri.getScheme())) {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return contentUri.getPath();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealPathFromURIinNought(Context context, Uri contentUri) {
        String path = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Cursor metaCursor = cr.query(contentUri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    path = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return path;
    }

    public static Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight, Context context) {
        Bitmap bm = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bm;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;

    }

    public static void showShimmer(ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
    }

    public static void hideShimmer(ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmerAnimation();
    }


    public static Bitmap rotateImageBitmap(String photoPath, Bitmap bitmap) {
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;

                case ExifInterface.ORIENTATION_UNDEFINED:
                    return bitmap;


                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /* public static void setBadgeAppIcon(Context context, int count) {
         try {
             ShortcutBadger.applyCount(context, count); //for 1.1.4+
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     public static void removeBadgeAppIcon(Context context, int count) {
         try {
             ShortcutBadger.applyCount(context, count);
             ShortcutBadger.removeCount(context);//for 1.1.4+
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
    public static boolean checkGps(Activity context) {
        boolean gps_enabled = false;
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return gps_enabled;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gps_enabled;
    }

    public static FragmentTransaction addFragment(FragmentManager fm, Fragment frg, int resource, boolean backStack) {
        FragmentTransaction ft1 = fm.beginTransaction();
        ft1.replace(resource, frg);
        if (backStack) {
            ft1.addToBackStack(null);
        }
        ft1.commit();
        return ft1;
    }


    public static Bitmap imageRound(Bitmap mbitmap) {
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
        return imageRounded;
    }

    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static int getNavBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    public static int getScreenWidth(Context context) {
        int columnWidth = 0;
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            final Point point = new Point();
            try {
                display.getSize(point);
            } catch (java.lang.NoSuchMethodError ignore) { // Older device
                point.x = display.getWidth();
                point.y = display.getHeight();
            }
            return point.x;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columnWidth;
    }

    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static File compressImage(Context context, File actualImage) {
        File compressedImage = null;
        try {
            compressedImage = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(actualImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressedImage;
    }

    public static int dpToPx(Context context, int dp) {
        return Math.round(dp * getPixelScaleFactor(context));
    }

    public static int pxToDp(Context context, int px) {
        return Math.round(px / getPixelScaleFactor(context));
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String convertDateFormat(String upComingDate, String inFormat, String outFormat) {
        String formattedDate = "";
        try {
            if (!upComingDate.isEmpty()) {
                DateFormat inputFormat = new SimpleDateFormat(inFormat);
                DateFormat outputFormat = new SimpleDateFormat(outFormat);
                Date parsed = null;
                parsed = inputFormat.parse(upComingDate);
                formattedDate = outputFormat.format(parsed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    /*public static void doLogout(final CoordinatorLayout coordinatorLayout, final Context context) {

        if (Util.hasInternet(Env.currentActivity)) {
            Util.showProDialog(Env.currentActivity);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put(UserData.KEY_DEVICE_TYPE, UserData.android);
            myHashMap.put(UserData.KEY_USER_ID, Config.getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserData> call = apiService.doLogout(myHashMap);
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        userData = response.body();
                        if (userData.getResponse().isStatus()) {
                            Config.setLogoutStatus(true);
                            Config.setUserId("");
                            Config.setSocialId("");
                            removeBadgeAppIcon(context, 0);
                            Intent intent = new Intent(Env.currentActivity, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            endAct(context);
                        } else {
                            Config.setLogoutStatus(false);
                            Util.showErrorSnackBar(coordinatorLayout, userData.getResponse().getMessage(), Env.currentActivity);

                        }
                    }

                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    Log.e("fail", t.toString());
                    Config.setLogoutStatus(false);
                    Util.dismissProDialog();
                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, context.getResources().getString(R.string.no_internet_connection), Env.currentActivity);
        }
    }*/


    public static void zoomAViewLikeFB(final Context context, View view, final TextView textView, final TextView iconTextView) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("1234", "onTouch: " + event.getAction());
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    textView.setTextColor(Color.parseColor("#198eb4"));
                    iconTextView.setTextColor(Color.parseColor("#198eb4"));
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    iconTextView.startAnimation(animation);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    textView.setTextColor(Color.parseColor("#767672"));
                    iconTextView.setTextColor(Color.parseColor("#767672"));
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                    iconTextView.startAnimation(animation);
                } else if (event.getAction() == KeyEvent.KEYCODE_HOME) {
                    textView.setTextColor(Color.parseColor("#767672"));
                    iconTextView.setTextColor(Color.parseColor("#767672"));
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                    iconTextView.startAnimation(animation);
                }
                return false;
            }
        });
    }


    public static void zoomButtonViewLikeFB(final Context context, View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("1234", "onTouch: " + event.getAction());
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                    view.startAnimation(animation);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                    view.startAnimation(animation);
                } else if (event.getAction() == KeyEvent.KEYCODE_HOME) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                    view.startAnimation(animation);
                }
                return false;
            }
        });
    }


    public static String setZeroBeforeNine(long digit) {
        try {
            if (digit <= 9) {
                return "0" + digit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "" + digit;
    }


    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            if (time > now) {
                return "just now";
            }
            return null;
        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public static long convertDateTimeToMilliSeconds(String dateTime, String comingFormat) throws ParseException {
        long timeInSec = 0;
        try {

            //String dateStr = "Jul 27, 2011 8:35:29 AM";
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            DateFormat writeFormat = new SimpleDateFormat(comingFormat);
            Date date = null;
            try {
                date = readFormat.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                dateTime = writeFormat.format(date);
            }


            SimpleDateFormat sdf = new SimpleDateFormat(comingFormat);
            sdf.setTimeZone(TimeZone.getDefault());
            timeInSec = (sdf.parse(dateTime).getTime());
            return timeInSec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String dataEncode(String textDecode) {
        String base64 = "";
        try {
            byte[] data = textDecode.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return base64;
    }


    public static String dataDecode(String textEncoded) {
        String text = "";
        try {
            byte[] data = Base64.decode(textEncoded, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return text;
    }

    public static double getDateDiff(String dateFormat, String date1, String date2) {
        double diffInDays = 0.0;
        try {
            SimpleDateFormat dfDate = new SimpleDateFormat(dateFormat);
            java.util.Date d = null;
            java.util.Date d1 = null;
            try {
                d = dfDate.parse(date1);
                d1 = dfDate.parse(date2);//Returns 15/10/2012
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(d);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(d1);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if (diffMonth < 0) {
                diffYear = diffYear - 1;
                diffMonth = 12 + diffMonth;
            }
            diffInDays = Double.parseDouble(diffYear + "." + diffMonth);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffInDays;
    }

    public static double getDateDiffTotal(String dateFormat, String date1, String date2) {
        double diffInDays = 0.0;
        try {
            SimpleDateFormat dfDate = new SimpleDateFormat(dateFormat);
            java.util.Date d = null;
            java.util.Date d1 = null;
            try {
                d = dfDate.parse(date1);
                d1 = dfDate.parse(date2);//Returns 15/10/2012
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(d);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(d1);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if (diffMonth < 0) {
                diffYear = diffYear - 1;
                diffMonth = 12 + diffMonth;
            }
            diffInDays = Double.parseDouble(diffYear + "." + diffMonth);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffInDays;
    }

    public static void startAct(Context context) {
        try {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * animation on finishing activity
     *
     * @param context
     */
    public static void endAct(Context context) {
        try {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*public static DisplayImageOptions displayOption(Context mContext) {
        try {
            Bitmap default_bitmap = Util.drawableToBitmap(mContext.getResources().getDrawable(R.drawable.ic_user));
            DisplayImageOptions option = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .showImageOnFail(new BitmapDrawable(mContext.getResources(), default_bitmap))
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
            return option;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static CharSequence converteTimestamp(String mileSegundos) {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]", phone)) {
            if (phone.length() < 10 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}

