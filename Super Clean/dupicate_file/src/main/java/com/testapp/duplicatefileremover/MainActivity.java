package com.testapp.duplicatefileremover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.testapp.duplicatefileremover.Model.Cont;
import com.testapp.duplicatefileremover.Model.DataModel;
import com.testapp.duplicatefileremover.Model.Duplicate;
import com.testapp.duplicatefileremover.Model.TypeFile;
import com.testapp.duplicatefileremover.utilts.Config;
import com.testapp.duplicatefileremover.utilts.Utils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
//import com.skyfishjy.library.RippleBackground;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
//    Shimmer shimmer;
//    ShimmerTextView stvScan;
//    ImageButton btnScan;
    TextView tvNumber;
//    LottieAnimationView ivSearch;
    View loPanel;
    private static final int REQUEST_PERMISSIONS = 100;
    //  public static ArrayList<AlbumFileModel> mAlbumPhotos = new ArrayList<>();
    ScanImagesAsyncTask mScanImagesAsyncTask;
//    RippleBackground rippleBackground;
    private ArrayList<String> arrPermission;
    CardView cvImage, cvAudio, cvVideo, cvDoc, cvOther;

    private HashMap<String, ArrayList<File>> requiredContent;
    HashMap<String, ArrayList<File>> mListDoc = new HashMap<>();
    HashMap<String, ArrayList<File>> mListImage = new HashMap<>();
    HashMap<String, ArrayList<File>> mListVideo = new HashMap<>();
    HashMap<String, ArrayList<File>> mListAudio = new HashMap<>();
    HashMap<String, ArrayList<File>> mListOtherFile = new HashMap<>();
    public static ArrayList<DataModel> mListData = new ArrayList<DataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocale(this);
        setContentView(R.layout.activity_dupicate_main);
        intDrawer();
        intView();
        intData();
        intEvent();
        checkPermission();
        requiredContent = new HashMap();
    }

    public void intDrawer() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.deep_clean));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_previous));
//        toolbar.setNavigationIcon(R.drawable.ic_previous);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void intView() {
//        stvScan = this.findViewById(R.id.stvScan);
//        btnScan = this.findViewById(R.id.btnScan);
        tvNumber = this.findViewById(R.id.tvNumber);
//        ivSearch = this.findViewById(R.id.ivSearch);
//        rippleBackground = this.findViewById(R.id.im_scan_bg);
        cvImage = this.findViewById(R.id.cvImage);
        cvAudio = this.findViewById(R.id.cvAudio);
        cvVideo = this.findViewById(R.id.cvVideo);
        cvDoc = this.findViewById(R.id.cvDoc);
        cvOther = this.findViewById(R.id.cvOther);
        loPanel = this.findViewById(R.id.layout_padding);

        loPanel = findViewById(R.id.layout_padding);
        if (loPanel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Utils.getHeightStatusBar(this) > 0) {
                loPanel.setPadding(0, Utils.getHeightStatusBar(this), 0, 0);
            }
            Utils.setStatusBarHomeTransparent(this);
        }
    }

    public void intData() {
//        shimmer = new Shimmer();
//        shimmer.start(stvScan);
    }

    public void intEvent() {
//        btnScan.setOnClickListener(this);
        cvImage.setOnClickListener(this);
        cvAudio.setOnClickListener(this);
        cvVideo.setOnClickListener(this);
        cvDoc.setOnClickListener(this);
        cvOther.setOnClickListener(this);
    }

    private void collectRequiredFilesContent() {
        requiredContent.put(getString(R.string.apk), new ArrayList());
        requiredContent.put(getString(R.string.zip), new ArrayList());
        requiredContent.put(getString(R.string.vcf), new ArrayList());
        requiredContent.put(getString(R.string.mp3), new ArrayList());
        requiredContent.put(getString(R.string.aac), new ArrayList());
        requiredContent.put(getString(R.string.amr), new ArrayList());
        requiredContent.put(getString(R.string.m4a), new ArrayList());
        requiredContent.put(getString(R.string.ogg), new ArrayList());
        requiredContent.put(getString(R.string.wav), new ArrayList());
        requiredContent.put(getString(R.string.flac), new ArrayList());
        requiredContent.put(getString(R.string._3gp), new ArrayList());
        requiredContent.put(getString(R.string.mp4), new ArrayList());
        requiredContent.put(getString(R.string.mkv), new ArrayList());
        requiredContent.put(getString(R.string.webm), new ArrayList());
        requiredContent.put(getString(R.string.jpg), new ArrayList());
        requiredContent.put(getString(R.string.jpeg), new ArrayList());
        requiredContent.put(getString(R.string.png), new ArrayList());
        requiredContent.put(getString(R.string.bmp), new ArrayList());
        requiredContent.put(getString(R.string.gif), new ArrayList());
        requiredContent.put(getString(R.string.doc), new ArrayList());
        requiredContent.put(getString(R.string.docx), new ArrayList());
        requiredContent.put(getString(R.string.html), new ArrayList());
        requiredContent.put(getString(R.string.pdf), new ArrayList());
        requiredContent.put(getString(R.string.txt), new ArrayList());
        requiredContent.put(getString(R.string.xml), new ArrayList());
        requiredContent.put(getString(R.string.xlsx), new ArrayList());
        requiredContent.put(getString(R.string.js), new ArrayList());
        requiredContent.put(getString(R.string.css), new ArrayList());
        requiredContent.put(getString(R.string.dat), new ArrayList());
        requiredContent.put(getString(R.string.cache), new ArrayList());
        requiredContent.put(getString(R.string.nomedia), new ArrayList());
        requiredContent.put(getString(R.string.emptyshow), new ArrayList());

    }

    private void detectFileTypeAndAddInCategory(File file) {

        String fileName = file.getName();
        ArrayList audios;
        ArrayList videos;
        ArrayList images;
        ArrayList documents;
        ArrayList files;

        if (fileName.endsWith(".apk")) {
            ArrayList<File> apk = (ArrayList) requiredContent.get(getString(R.string.apk));
            if (apk != null) {
                apk.add(file);
            }
        } else if (fileName.endsWith(".zip")) {
            ArrayList<File> zip = (ArrayList) requiredContent.get(getString(R.string.zip));
            if (zip != null) {
                zip.add(file);
            }
        } else if (fileName.endsWith(".vcf")) {
            ArrayList<File> vcf = (ArrayList) requiredContent.get(getString(R.string.vcf));
            if (vcf != null) {
                vcf.add(file);
            }
        } else if (fileName.endsWith(".mp3")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.mp3));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".aac")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.aac));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".amr")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.amr));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".m4a")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.m4a));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".ogg")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.ogg));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".wav")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.wav));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".flac")) {
            audios = (ArrayList) requiredContent.get(getString(R.string.flac));
            if (audios != null) {
                audios.add(file);
            }
        } else if (fileName.endsWith(".3gp")) {
            videos = (ArrayList) requiredContent.get(getString(R.string._3gp));
            if (videos != null) {
                videos.add(file);
            }
        } else if (fileName.endsWith(".mp4")) {
            videos = (ArrayList) requiredContent.get(getString(R.string.mp4));
            if (videos != null) {
                videos.add(file);
            }
        } else if (fileName.endsWith(".mkv")) {
            videos = (ArrayList) requiredContent.get(getString(R.string.mkv));
            if (videos != null) {
                videos.add(file);
            }
        } else if (fileName.endsWith(".webm")) {
            videos = (ArrayList) requiredContent.get(getString(R.string.webm));
            if (videos != null) {
                videos.add(file);
            }
        } else if (fileName.endsWith(".jpg")) {
            images = (ArrayList) requiredContent.get(getString(R.string.jpg));
            if (images != null) {
                images.add(file);
            }
        } else if (fileName.endsWith(".jpeg")) {
            images = (ArrayList) requiredContent.get(getString(R.string.jpeg));
            if (images != null) {
                images.add(file);
            }
        } else if (fileName.endsWith(".png")) {
            images = (ArrayList) requiredContent.get(getString(R.string.png));
            if (images != null) {
                images.add(file);
            }
        } else if (fileName.endsWith(".bmp")) {
            images = (ArrayList) requiredContent.get(getString(R.string.bmp));
            if (images != null) {
                images.add(file);
            }
        } else if (fileName.endsWith(".gif")) {
            images = (ArrayList) requiredContent.get(getString(R.string.gif));
            if (images != null) {
                images.add(file);
            }
        } else if (fileName.endsWith(".doc")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.doc));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".docx")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.docx));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".html")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.html));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".pdf")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.pdf));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".txt")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.txt));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".xml")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.xml));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".xlsx")) {
            documents = (ArrayList) requiredContent.get(getString(R.string.xlsx));
            if (documents != null) {
                documents.add(file);
            }
        } else if (fileName.endsWith(".js")) {
            files = (ArrayList) requiredContent.get(getString(R.string.js));
            if (files != null) {
                files.add(file);
            }
        } else if (fileName.endsWith(".css")) {
            files = (ArrayList) requiredContent.get(getString(R.string.css));
            if (files != null) {
                files.add(file);
            }
        } else if (fileName.endsWith(".dat")) {
            files = (ArrayList) requiredContent.get(getString(R.string.dat));
            if (files != null) {
                files.add(file);
            }
        } else if (fileName.endsWith(".cache")) {
            files = (ArrayList) requiredContent.get(getString(R.string.cache));
            if (files != null) {
                files.add(file);
            }
        } else if (fileName.endsWith(".nomedia")) {
            files = (ArrayList) requiredContent.get(getString(R.string.nomedia));
            if (files != null) {
                files.add(file);
            }
        } else if (fileName.endsWith(".emptyshow")) {
            files = (ArrayList) requiredContent.get(getString(R.string.emptyshow));
            if (files != null) {
                files.add(file);
            }
        }
    }


    public void requestPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs();
            }
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
//        if (id == R.id.btnScan) {
//        } else if (id == R.id.cvImage) {
        if (id == R.id.cvImage) {
            scanFile(Cont.IMAGE);
        } else if (id == R.id.cvAudio) {
            scanFile(Cont.AUDIO);
        } else if (id == R.id.cvVideo) {
            scanFile(Cont.VIDEO);
        } else if (id == R.id.cvDoc) {
            scanFile(Cont.DOCCUNMENT);
        } else if (id == R.id.cvOther) {
            scanFile(Cont.OTHERFILE);
        }

    }

    public void scanFile(int type) {
        if (this.mScanImagesAsyncTask != null && this.mScanImagesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(MainActivity.this, getString(R.string.scan_wait), Toast.LENGTH_LONG).show();
        } else {
            mListData.clear();
//            shimmer.cancel();
//            stvScan.setVisibility(View.GONE);
//            ivSearch.setVisibility(View.VISIBLE);
            tvNumber.setVisibility(View.VISIBLE);
            tvNumber.setText(getString(R.string.analyzing));
            tvNumber.startAnimation(AnimationUtils.loadAnimation(this, R.anim.analyzing));

//            ivSearch.playAnimation();
//            rippleBackground.startRippleAnimation();
            mScanImagesAsyncTask = new ScanImagesAsyncTask(type);
            mScanImagesAsyncTask.execute();

        }
    }

    public class ScanImagesAsyncTask extends AsyncTask<String, Integer, ArrayList<DataModel>> {

        int typeScan = 0;

        public ScanImagesAsyncTask(int type) {
            typeScan = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<DataModel> model_images) {
            super.onPostExecute(model_images);
//            rippleBackground.stopRippleAnimation();
//            ivSearch.pauseAnimation();
//            ivSearch.setProgress(0);

            tvNumber.setText("");
            tvNumber.setVisibility(View.INVISIBLE);
            tvNumber.clearAnimation();

            if (mListData.size() == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.no_file_found), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.lubuteam.sellsourcecode.supercleaner", "com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity"));
                intent.putExtra("result deep clean data", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), DuplicateActivity.class);
                intent.putExtra("title_tool_bar", getTitleToolBar(typeScan));
                startActivity(intent);
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            tvNumber.setText(String.valueOf(values[0]));


        }

        @Override
        protected ArrayList<DataModel> doInBackground(String... strings) {

            String strArr;
            strArr = Environment.getExternalStorageDirectory().getAbsolutePath();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("root = ");
            stringBuilder.append(strArr);
            //Day la tat ca thu muc trong may
            requiredContent.clear();
            collectRequiredFilesContent();

            checkFileOfDirectory(Utils.getFileList(strArr));
            getSdCard();
            //nhom Image lai voi nhau
            HashMap<String, ArrayList<File>> duplicateResult = getMediaGroup(typeScan);
            ArrayList<String> keys = new ArrayList(duplicateResult.keySet());
            int group = 1;
            for (int i = 0; i < keys.size(); i++) {

                //Lay list file theo key jpg,png,gif cac kieu
                ArrayList<File> mListFile = duplicateResult.get(keys.get(i));

                //tim danh sach file double
                HashMap<Long, ArrayList<File>> extractDouble = findExactDuplicates(mListFile);

                //loc va setchecked

                if (extractDouble != null && extractDouble.size() > 0) {

                    Iterator it = new ArrayList(extractDouble.keySet()).iterator();

                    while (it.hasNext()) {
                        DataModel mDataModel = new DataModel();
                        ArrayList<File> files = (ArrayList) extractDouble.get((Long) it.next());
                        if (files != null && files.size() > 0) {
                            mDataModel.setTitleGroup("Group: " + Integer.valueOf(group));
                            ArrayList<Duplicate> list = new ArrayList();
                            for (int j = 0; j < files.size(); j++) {

                                Duplicate duplicate = new Duplicate();
                                duplicate.setFile((File) files.get(j));
                                duplicate.setTypeFile(TypeFile.getType(files.get(j).getPath()));
                                if (j == 0) {
                                    duplicate.setChecked(false);
                                }
                                list.add(duplicate);
                            }
                            mDataModel.setListDuplicate(list);
                            publishProgress(group);
                            group++;

                        }
                        mListData.add(mDataModel);

                    }


                }


            }


            return null;
        }


    }

    public void checkFileOfDirectory(File[] fileArr) {
        if (fileArr != null) {
            for (int i = 0; i < fileArr.length; i++) {
                if (fileArr[i].isDirectory()) {
                    checkFileOfDirectory(Utils.getFileList(fileArr[i].getPath()));
                } else {
                    detectFileTypeAndAddInCategory(fileArr[i]);

                }
            }
        }


    }

    private HashMap<Long, ArrayList<File>> findExactDuplicates(ArrayList<File> files) {
        HashMap<Long, ArrayList<File>> exactDuplicates = new HashMap();
        if (files != null) {
            HashMap<Long, ArrayList<File>> duplicatesBySize = findDuplicatesBySize(files);
            ArrayList<Long> keys = new ArrayList(duplicatesBySize.keySet());
            for (int i = 0; i < keys.size(); i++) {
                ArrayList<File> sameLengthFiles = (ArrayList) duplicatesBySize.get(keys.get(i));
                int countOfSameLengthFiles = sameLengthFiles.size();
                int j = 0;

                while (j < countOfSameLengthFiles) {

                    int k = 0;

                    while (k < countOfSameLengthFiles) {

                        if (j != k && j < countOfSameLengthFiles && k < countOfSameLengthFiles) {

                            try {
                                if (contentEquals((File) sameLengthFiles.get(j), (File) sameLengthFiles.get(k))) {

                                    File candidateFile = (File) sameLengthFiles.get(j);
                                    ArrayList<File> candidateFiles;

                                    if (exactDuplicates.containsKey(Long.valueOf(candidateFile.length()))) {

                                        candidateFiles = (ArrayList) exactDuplicates.get(Long.valueOf(candidateFile.length()));

                                        if (!candidateFiles.contains(candidateFile)) {
                                            candidateFiles.add(candidateFile);
                                        }

                                    } else {

                                        candidateFiles = new ArrayList();
                                        candidateFiles.add(candidateFile);
                                        exactDuplicates.put(Long.valueOf(candidateFile.length()), candidateFiles);
                                    }
                                }
                            } catch (Exception e) {
                                //    LogHelper.logE(TAG, "Looking for duplicates: " + e.getLocalizedMessage());
                            }
                        }
                        k++;
                    }
                    j++;
                }
            }
        }
        return exactDuplicates;
    }

    private HashMap<Long, ArrayList<File>> findDuplicatesBySize(ArrayList<File> files) {
        HashMap<Long, ArrayList<File>> duplicatesBySize = new HashMap();
        Iterator it = files.iterator();
        while (it.hasNext()) {
            File file = (File) it.next();
            long length = file.length();
            if (duplicatesBySize.containsKey(Long.valueOf(length))) {
                ((ArrayList) duplicatesBySize.get(Long.valueOf(length))).add(file);
            } else {
                ArrayList<File> candidateFiles = new ArrayList();
                candidateFiles.add(file);
                duplicatesBySize.put(Long.valueOf(length), candidateFiles);
            }
        }
        ArrayList<Long> keys = new ArrayList(duplicatesBySize.keySet());
        for (int i = 0; i < keys.size(); i++) {
            try {
                if (((ArrayList) duplicatesBySize.get(keys.get(i))).size() == 1) {
                    duplicatesBySize.remove(keys.get(i));

                }
            } catch (Exception e) {

            }
        }
        return duplicatesBySize;
    }

    private boolean contentEquals(File file1, File file2) throws IOException {
        if (!file1.exists()) {
            return false;
        }
        if (!file2.exists()) {
            return false;
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        if (file1.length() <= 3000) {
            try {
                return FileUtils.contentEquals(file1, file2);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            if (file1.exists()) {
                InputStream input1 = new FileInputStream(file1);
                InputStream input2 = new FileInputStream(file2);

                try {
                    byte[] startBufferFile1 = new byte[512];
                    IOUtils.read(input1, startBufferFile1, 0, 512);
                    String str = new String(startBufferFile1);
                    IOUtils.skip(input1, (file1.length() / 2) - 256);
                    byte[] midBufferFile1 = new byte[512];
                    IOUtils.read(input1, midBufferFile1, 0, 512);
                    String midTextFile1 = new String(midBufferFile1);
                    IOUtils.skip(input1, file1.length() - 512);
                    byte[] endBufferFile1 = new byte[512];
                    IOUtils.read(input1, endBufferFile1, 0, 512);
                    String endTextFile1 = new String(endBufferFile1);
                    byte[] startBufferFile2 = new byte[512];
                    IOUtils.read(input2, startBufferFile2, 0, 512);
                    str = new String(startBufferFile2);
                    IOUtils.skip(input2, (file2.length() / 2) - 256);
                    byte[] midBufferFile2 = new byte[512];
                    IOUtils.read(input2, midBufferFile2, 0, 512);
                    str = new String(midBufferFile2);
                    IOUtils.skip(input2, file2.length() - 512);
                    byte[] endBufferFile2 = new byte[512];
                    IOUtils.read(input2, endBufferFile2, 0, 512);
                    String endTextFile2 = new String(endBufferFile2);
                    if (str.equals(str) && midTextFile1.equals(str) && endTextFile1.equals(endTextFile2)) {

                        return true;
                    }

                    input1.close();
                    input2.close();
                    input1.close();
                    input2.close();
                    return false;
                } catch (IOException e2) {

                } finally {
                    input1.close();
                    input2.close();
                }
            }
        } catch (FileNotFoundException e3) {

        }
        return false;
    }

    public String getTitleToolBar(int typeGroup) {
        switch (typeGroup) {
            case Cont.IMAGE:

                return getString(R.string.scan_image_file);

            case Cont.AUDIO:

                return getString(R.string.scan_audio_file);
            case Cont.DOCCUNMENT:

                return getString(R.string.scan_doc_file);
            case Cont.OTHERFILE:

                return getString(R.string.scan_all_file);
            case Cont.VIDEO:

                return getString(R.string.scan_video_file);
            default:
                return getString(R.string.app_name);
        }

    }

    public HashMap<String, ArrayList<File>> getMediaGroup(int typeGroup) {
        switch (typeGroup) {
            case Cont.IMAGE:
                mListImage.put(getString(R.string.jpg), (ArrayList) requiredContent.get(getString(R.string.jpg)));
                mListImage.put(getString(R.string.jpeg), (ArrayList) requiredContent.get(getString(R.string.jpeg)));
                mListImage.put(getString(R.string.png), (ArrayList) requiredContent.get(getString(R.string.png)));
                mListImage.put(getString(R.string.bmp), (ArrayList) requiredContent.get(getString(R.string.bmp)));
                mListImage.put(getString(R.string.gif), (ArrayList) requiredContent.get(getString(R.string.gif)));
                return mListImage;

            case Cont.AUDIO:
                mListAudio.put(getString(R.string.mp3), (ArrayList) requiredContent.get(getString(R.string.mp3)));
                mListAudio.put(getString(R.string.aac), (ArrayList) requiredContent.get(getString(R.string.aac)));
                mListAudio.put(getString(R.string.amr), (ArrayList) requiredContent.get(getString(R.string.amr)));
                mListAudio.put(getString(R.string.m4a), (ArrayList) requiredContent.get(getString(R.string.m4a)));
                mListAudio.put(getString(R.string.ogg), (ArrayList) requiredContent.get(getString(R.string.ogg)));
                mListAudio.put(getString(R.string.wav), (ArrayList) requiredContent.get(getString(R.string.wav)));
                mListAudio.put(getString(R.string.flac), (ArrayList) requiredContent.get(getString(R.string.flac)));
                return mListAudio;
            case Cont.DOCCUNMENT:
                mListDoc.put(getString(R.string.doc), (ArrayList) requiredContent.get(getString(R.string.doc)));
                mListDoc.put(getString(R.string.docx), (ArrayList) requiredContent.get(getString(R.string.docx)));
                mListDoc.put(getString(R.string.html), (ArrayList) requiredContent.get(getString(R.string.html)));
                mListDoc.put(getString(R.string.pdf), (ArrayList) requiredContent.get(getString(R.string.pdf)));
                mListDoc.put(getString(R.string.txt), (ArrayList) requiredContent.get(getString(R.string.txt)));
                mListDoc.put(getString(R.string.xml), (ArrayList) requiredContent.get(getString(R.string.xml)));
                mListDoc.put(getString(R.string.xlsx), (ArrayList) requiredContent.get(getString(R.string.xlsx)));
                return mListDoc;
            case Cont.OTHERFILE:
                mListOtherFile.put(getString(R.string.zip), (ArrayList) requiredContent.get(getString(R.string.zip)));
                mListOtherFile.put(getString(R.string.apk), (ArrayList) requiredContent.get(getString(R.string.apk)));
                mListOtherFile.put(getString(R.string.vcf), (ArrayList) requiredContent.get(getString(R.string.vcf)));

                mListOtherFile.put(getString(R.string.js), (ArrayList) requiredContent.get(getString(R.string.js)));
                mListOtherFile.put(getString(R.string.css), (ArrayList) requiredContent.get(getString(R.string.css)));
                mListOtherFile.put(getString(R.string.dat), (ArrayList) requiredContent.get(getString(R.string.dat)));
                mListOtherFile.put(getString(R.string.cache), (ArrayList) requiredContent.get(getString(R.string.cache)));
//                mListOtherFile.put(getString(R.string.nomedia),(ArrayList) requiredContent.get(getString(R.string.nomedia)));
//                mListOtherFile.put(getString(R.string.emptyshow),(ArrayList) requiredContent.get(getString(R.string.emptyshow)));
                return mListOtherFile;
            case Cont.VIDEO:
                mListVideo.put(getString(R.string._3gp), (ArrayList) requiredContent.get(getString(R.string._3gp)));
                mListVideo.put(getString(R.string.mp4), (ArrayList) requiredContent.get(getString(R.string.mp4)));
                mListVideo.put(getString(R.string.mkv), (ArrayList) requiredContent.get(getString(R.string.mkv)));
                mListVideo.put(getString(R.string.webm), (ArrayList) requiredContent.get(getString(R.string.webm)));
                return mListVideo;
            default:
                return requiredContent;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
                        if (!fileDirectory.exists()) {
                            fileDirectory.mkdirs();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }

    //    private void showNotFoundDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setMessage(getString(R.string.not_found));
//
//        String positiveText = getString(android.R.string.ok);
//        builder.setPositiveButton(positiveText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        // display dialog
//        dialog.show();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//
//        return true;

        return false;
    }


    private void checkPermission() {
        arrPermission = new ArrayList();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Utils.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                arrPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!Utils.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                arrPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!arrPermission.isEmpty()) {
                requestPermissions(arrPermission.toArray(new String[0]), REQUEST_PERMISSIONS);
            } else {
                // scan


            }
        } else {
            //can

        }

    }

    public void getSdCard() {

        String[] externalStoragePaths = getExternalStorageDirectories();

        if (externalStoragePaths != null && externalStoragePaths.length > 0) {

            for (String path : externalStoragePaths) {
                File file = new File(path);
                if (file.exists()) {
                    File[] subFiles = file.listFiles();
                    checkFileOfDirectory(subFiles);
                }
            }
        }
    }

    public String[] getExternalStorageDirectories() {
        List<String> results = new ArrayList();
        if (Build.VERSION.SDK_INT >= 19) {
            File[] externalDirs = getExternalFilesDirs(null);
            if (externalDirs != null && externalDirs.length > 0) {
                for (File file : externalDirs) {
                    if (file != null) {
                        String[] paths = file.getPath().split("/Android");
                        if (paths != null && paths.length > 0) {
                            boolean addPath;
                            String path = paths[0];
                            if (Build.VERSION.SDK_INT >= 21) {
                                addPath = Environment.isExternalStorageRemovable(file);
                            } else {
                                addPath = "mounted".equals(EnvironmentCompat.getStorageState(file));
                            }
                            if (addPath) {
                                results.add(path);
                            }
                        }
                    }
                }
            }
        }

        if (results.isEmpty()) {
            String output = "";
            InputStream is = null;
            try {
                Process process = new ProcessBuilder(new String[0]).command(new String[]{"mount | grep /dev/block/vold"}).redirectErrorStream(true).start();
                process.waitFor();
                is = process.getInputStream();
                byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (Exception e) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {

                    }
                }

            }
            if (!output.trim().isEmpty()) {
                String[] devicePoints = output.split(IOUtils.LINE_SEPARATOR_UNIX);
                if (devicePoints.length > 0) {
                    for (String voldPoint : devicePoints) {
                        results.add(voldPoint.split(" ")[2]);
                    }
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            storageDirectories[i] = (String) results.get(i);
        }
        return storageDirectories;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
