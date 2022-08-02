package com.security.applock.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FileUtil {

    public static File createFolder(String folderName) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), folderName);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        return mediaStorageDir;
    }

    public static String getFolderName(String name) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), name);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }    

    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getNewFile(Context context, String folderName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String timeStamp = simpleDateFormat.format(new Date());
        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return new File(path);
    }

    public static String saveImageToGallery(@NonNull Context context, @NonNull File file, @NonNull Bitmap bitmap) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("saveImageToGallery: the path of bmp is ");
        stringBuilder.append(file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public static void openFile(Context context, File file) throws IOException {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (file.toString().endsWith(".doc") || file.toString().endsWith(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (file.toString().endsWith(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (file.toString().endsWith(".ppt") || file.toString().endsWith(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (file.toString().endsWith(".xls") || file.toString().endsWith(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (file.toString().endsWith(".zip") || file.toString().endsWith(".rar")) {
            // zip file
            intent.setDataAndType(uri, "application/zip");
        } else if (file.toString().endsWith(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (file.toString().endsWith(".wav") || file.toString().endsWith(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (file.toString().endsWith(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (file.toString().endsWith(".jpg") || file.toString().endsWith(".jpeg") || file.toString().endsWith(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (file.toString().endsWith(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (file.toString().endsWith(".3gp") ||
                file.toString().endsWith(".mpg") ||
                file.toString().endsWith(".mpeg") ||
                file.toString().endsWith(".mpe") ||
                file.toString().endsWith(".mp4") ||
                file.toString().endsWith(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        try{
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean saveImageToStorage(
            byte[] bytes,
            String filename,
            String mimeType,
            String directory,
            Uri mediaContentUri,
            Context context
    ) {
        OutputStream imageOutStream = null;
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                contentValues.put("relative_path", directory);
           Uri mUri = context.getContentResolver().insert(mediaContentUri,contentValues);
            try {
                 imageOutStream = context.getContentResolver().openOutputStream(mUri);
            } catch (FileNotFoundException e) {
                return false;
            }
        } else {
            String imagePath = Environment.getExternalStoragePublicDirectory(directory).getAbsolutePath();
            File image = new  File(imagePath, filename);
            try {
                imageOutStream = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        try {
            imageOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                imageOutStream.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public static File[] getAllImagesInDirectory(Context context,String directory) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = "relative_path" + " =?";
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " ASC";

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, new String[]{directory}, orderBy);
        File[] lstFile = new File[cursor.getCount()];
        if (cursor != null) {
            int i = 0;
            while (cursor.moveToNext()) {
                lstFile[i] = new File(cursor.getString(cursor.getColumnIndex(projection[0])));
                i++;
            }
            cursor.close();
        }
        return lstFile;
    }

    public static String saveImageToInternalStorage(byte[] imageBytes, String fileName, Context context) {
        File directory = new File(context.getFilesDir(), Config.INTRUDER_SELFIE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mFile = new File(directory, fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFile);
            fos.write(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mFile.getAbsolutePath();
    }

    public static File[] getAllImageFromInternal(Context context) {
        File directory = new File(context.getFilesDir(), Config.INTRUDER_SELFIE);
        if (!directory.exists()) {
            return new File[0];
        } else {
            return directory.listFiles();
        }
    }
}
