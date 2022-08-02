package com.testapp.duplicatefileremover;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.testapp.duplicatefileremover.Model.Duplicate;
import com.testapp.duplicatefileremover.adapter.RecyclerViewType;
import com.testapp.duplicatefileremover.adapter.SectionRecyclerViewAdapter;
import com.testapp.duplicatefileremover.task.DeleteFilesAsyncTask;
import com.testapp.duplicatefileremover.utilts.Utils;

import java.io.File;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class DuplicateActivity extends AppCompatActivity {

    protected static final String RECYCLER_VIEW_TYPE = "recycler_view_type";
    private RecyclerViewType recyclerViewType;
    private RecyclerView recyclerView;
    SectionRecyclerViewAdapter adapter;
    DeleteFilesAsyncTask mDeleteFilesAsyncTask;
    ArrayList<File> listFile = new ArrayList<File>();
    SharedPreferences sharedPreferences;
    String titleToolBar = "";
    Toolbar toolbar;
    View loPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate);
        toolbar = findViewById(R.id.toolbar);
        titleToolBar = getIntent().getStringExtra("title_tool_bar");
        toolbar.setTitle(titleToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loPanel = this.findViewById(R.id.layout_padding);

        loPanel = findViewById(R.id.layout_padding);
        if (loPanel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Utils.getHeightStatusBar(this) > 0) {
                loPanel.setPadding(0, Utils.getHeightStatusBar(this), 0, 0);
            }
            Utils.setStatusBarHomeTransparent(this);
        }
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            recyclerViewType = RecyclerViewType.LINEAR_VERTICAL;
            setUpRecyclerView();
            populateRecyclerView();
            findViewById(R.id.btnRestore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteSelectedItem();
                    if (listFile.size() == 0) {
                        Toast.makeText(DuplicateActivity.this, "Cannot delete, all items are unchecked!", Toast.LENGTH_LONG).show();
                    } else {

                        showDalogConfirmDelete();
                    }

                }
            });
        } catch (Exception e) {

        }

    }

    public void deleteFiles() {
        mDeleteFilesAsyncTask = new DeleteFilesAsyncTask(DuplicateActivity.this, listFile, new DeleteFilesAsyncTask.OnRestoreListener() {
            @Override
            public void onComplete() {
                adapter.notifyDataSetChanged();
//                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
//                intent.putExtra("value", listFile.size());
//                intent.putExtra("title_tool_bar", titleToolBar);
//                startActivityForResult(intent, 200);
                Toast.makeText(DuplicateActivity.this, listFile.size() + " " + getString(R.string.file_removed), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.lubuteam.sellsourcecode.supercleaner", "com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity"));
                intent.putExtra("result deep clean data", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mDeleteFilesAsyncTask.execute();
    }

    public boolean SDCardCheck() {
        File[] storages = ContextCompat.getExternalFilesDirs(this, null);
        if (storages.length <= 1 || storages[0] == null || storages[1] == null) {
            return false;
        }
        return true;
    }

    private void showDalogConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DuplicateActivity.this);
        builder.setTitle(getString(R.string.delete_title));
        builder.setMessage(getString(R.string.delete_content));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!sharedPreferences.getString("sdCardUri", "").equals("")) {
                            deleteFiles();
                        } else if (SDCardCheck()) {

                            SDcardFilesDialog();
                        } else {

                            deleteFiles();
                        }

                        dialog.dismiss();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void SDcardFilesDialog() {

        final Dialog main_dialog2 = new ProgressDialog(this);
        main_dialog2.requestWindowFeature(1);
        main_dialog2.setCancelable(false);
        main_dialog2.setCanceledOnTouchOutside(false);
        main_dialog2.show();
        main_dialog2.setContentView(R.layout.sdcard_dialog_dupicate);
        ((Button) main_dialog2.findViewById(R.id.ok_sd)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (main_dialog2 != null) {
                    main_dialog2.dismiss();
                }
                fileSearch();
            }
        });
    }

    public void fileSearch() {
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 100);
    }

    public void deleteSelectedItem() {
        listFile.clear();
        if (MainActivity.mListData != null) {
            for (int i = 0; i < MainActivity.mListData.size(); i++) {
                ArrayList<Duplicate> listDup = MainActivity.mListData.get(i).getListDuplicate();
                for (int t = listDup.size() - 1; t >= 0; t--) {
                    if (listDup.get(t).isChecked()) {
                        listFile.add(listDup.get(t).getFile());
                        listDup.remove(listDup.get(t));
                    }

                }


            }
        }
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {


        adapter = new SectionRecyclerViewAdapter(this, recyclerViewType, MainActivity.mListData);
        recyclerView.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            boolean f = false;
            if (data != null) {
                Uri uri = data.getData();
                if (Build.VERSION.SDK_INT >= 19 && getContext() != null) {
                    getContentResolver().takePersistableUriPermission(uri, 3);
                }
                if (checkIfSDCardRoot(uri)) {
                    editor.putString("sdCardUri", uri.toString());
                    editor.putBoolean("storagePermission", true);
                    f = true;
                } else {
                    Toast.makeText(this, "Please Select Right SD Card.", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("storagePermission", false);
                    editor.putString("sdCardUri", "");
                }
            } else {
                Toast.makeText(this, "Please Select Right SD Card.", Toast.LENGTH_SHORT).show();
                editor.putString("sdCardUri", "");
            }
            if (editor.commit()) {
                editor.apply();
                if (f) {
                    deleteFiles();
                }
            }
        }
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                finish();
            }

        }
    }

    @TargetApi(21)
    private static boolean checkIfSDCardRoot(Uri uri) {
        return isExternalStorageDocument(uri) && isRootUri(uri) && !isInternalStorage(uri);
    }

    @RequiresApi(api = 21)
    private static boolean isRootUri(Uri uri) {
        return DocumentsContract.getTreeDocumentId(uri).endsWith(":");
    }

    @RequiresApi(api = 21)
    public static boolean isInternalStorage(Uri uri) {
        return isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri).contains("primary");
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
