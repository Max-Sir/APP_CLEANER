package com.testapp.duplicatefileremover.task;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.testapp.duplicatefileremover.R;
import com.testapp.duplicatefileremover.dialog.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteFilesAsyncTask extends AsyncTask<String, Integer, String> {
    private final String TAG = getClass().getName();
    private ArrayList<File> listFile;
    private Context mContext;
    private LoadingDialog progressDialog;
    private OnRestoreListener onRestoreListener;
    TextView tvNumber;
    int count = 0;

    public DeleteFilesAsyncTask(Context context, ArrayList<File> mList, OnRestoreListener mOnRestoreListener) {
        this.mContext = context;
        this.listFile = mList;
        this.onRestoreListener = mOnRestoreListener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new LoadingDialog(this.mContext);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    protected String doInBackground(String... strAr) {

        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int strArr = 0; strArr < this.listFile.size(); strArr++) {

            if ( this.listFile.get(strArr).exists()) {
                boolean flag = isOnExtSdCard(this.listFile.get(strArr), mContext);
                if (Build.VERSION.SDK_INT < 21 || !isOnExtSdCard(this.listFile.get(strArr), mContext)) {
                    try {
                        this.listFile.get(strArr).delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getDocumentFile( this.listFile.get(strArr), false, mContext).delete();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            }

            count = strArr+1;
            publishProgress(count);

        }
        try {

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        try{
            if (this.progressDialog != null&&progressDialog.isShowing()) {
                this.progressDialog.cancel();
                this.progressDialog = null;
            }
        }catch (Exception e){

        }

        if (null != onRestoreListener) {
            onRestoreListener.onComplete();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        tvNumber = (TextView)progressDialog.findViewById(R.id.tvNumber);
        try {

        }catch (Exception e){
            tvNumber.setText(String.format(mContext.getString(R.string.deleted_number_format), values[0]));
        }


    }

    public interface OnRestoreListener {
        void onComplete();
    }
    @Nullable
    private DocumentFile getDocumentFile( File file, boolean isDirectory, Context context) {
        String sdUri = PreferenceManager.getDefaultSharedPreferences(context).getString("sdCardUri", "");
        Log.i(TAG, "getDocumentFile: " + sdUri);
        String baseFolder = getExtSdCardFolder(file, context);
        boolean originalDirectory = false;
        if (baseFolder == null) {
            return null;
        }
        String relativePath = null;
        try {
            String fullPath = file.getCanonicalPath();
            if (baseFolder.equals(fullPath)) {
                originalDirectory = true;
            } else {
                relativePath = fullPath.substring(baseFolder.length() + 1);
            }
        } catch (IOException e) {
            return null;
        } catch (Exception e2) {
            originalDirectory = true;
        }
        Uri treeUri = null;
        if (!(sdUri == null || sdUri.equals(""))) {
            treeUri = Uri.parse(sdUri);
        }
        if (treeUri == null) {
            return null;
        }
        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);
        if (originalDirectory) {
            return document;
        }
        String[] parts = relativePath.split("\\/");
        for (int i = 0; i < parts.length; i++) {
            DocumentFile nextDocument = document.findFile(parts[i]);
            if (nextDocument == null) {
                if (i < parts.length - 1 || isDirectory) {
                    nextDocument = document.createDirectory(parts[i]);
                } else {
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    Log.i(TAG, "getDocumentFile: " + fileExtension);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    if (mimeType == null || TextUtils.isEmpty(mimeType)) {
                        nextDocument = document.createFile(mimeType, parts[i]);

                    } else {
                        nextDocument = document.createFile(mimeType, parts[i]);
                    }
                }
            }
            document = nextDocument;
        }
        return document;
    }
    @TargetApi(19)
    private static String getExtSdCardFolder(File file, Context context) {
        String[] extSdPaths = getExtSdCardPaths(context);
        int i = 0;
        while (i < extSdPaths.length) {
            try {
                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
                    return extSdPaths[i];
                }
                i++;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }
    @TargetApi(19)
    private static String[] getExtSdCardPaths(Context context) {
        List<String> paths = new ArrayList();
        for (File file : context.getExternalFilesDirs("external")) {
            if (!(file == null || file.equals(context.getExternalFilesDir("external")))) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w("log", "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                    }
                    paths.add(path);
                }
            }
        }
        if (paths.isEmpty()) {
            paths.add("/storage/sdcard1");
        }
        return (String[]) paths.toArray(new String[0]);
    }
    @TargetApi(19)
    public static boolean isOnExtSdCard(File file, Context c) {
        return getExtSdCardFolder(file, c) != null;
    }
    public String getType(Context c,File file){
        ContentResolver cR = c.getContentResolver();
       return cR.getType(Uri.fromFile(file));
    }
}
