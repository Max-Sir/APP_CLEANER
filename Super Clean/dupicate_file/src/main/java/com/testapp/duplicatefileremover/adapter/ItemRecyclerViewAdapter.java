package com.testapp.duplicatefileremover.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.testapp.duplicatefileremover.Model.Duplicate;
import com.testapp.duplicatefileremover.Model.TypeFile;
import com.testapp.duplicatefileremover.R;
import com.testapp.duplicatefileremover.utilts.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sonu on 24/07/17.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel;
        private TextView tvSize;
        private TextView tvPath;
        ImageView image,ivPlay;
        CheckBox ivCheckbox;
        private RelativeLayout rlCard;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.name);
            tvSize = (TextView) itemView.findViewById(R.id.size);
            tvPath = (TextView) itemView.findViewById(R.id.path);
            image = (ImageView) itemView.findViewById(R.id.image);
            ivCheckbox = (CheckBox)itemView.findViewById(R.id.checked);
            ivPlay =  (ImageView) itemView.findViewById(R.id.play);
            rlCard = (RelativeLayout) itemView.findViewById(R.id.rlCard);
        }
    }

    private Context context;
    private ArrayList<Duplicate> mDuplicates;

    public ItemRecyclerViewAdapter(Context context, ArrayList<Duplicate> arrayList) {
        this.context = context;
        this.mDuplicates = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_dupicate, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Duplicate mDuplicate = mDuplicates.get(position);
        holder.itemLabel.setText(getFileName(mDuplicate.getFile().getPath()));
        holder.tvSize.setText(Utils.formatSize(mDuplicate.getFile().length()));
        holder.tvPath.setText(mDuplicate.getFile().getPath());
        holder.ivCheckbox.setChecked(mDuplicate.isChecked());
        switch (mDuplicate.getTypeFile()){
            case TypeFile.IMAGE:
                try {
                    Glide.with(context)
                            .load("file://" + mDuplicate.getFile().getPath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case TypeFile.VIDEO:
                holder.ivPlay.setVisibility(View.VISIBLE);
                try {
                    Glide.with(context)
                            .load("file://" + mDuplicate.getFile().getPath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.DOCUMENT:
                try {
                    Glide.with(context)
                            .load(R.drawable.document)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.PDF:
                try {
                    Glide.with(context)
                            .load(R.drawable.ic_pdf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.ZIP:
                try {
                    Glide.with(context)
                            .load(R.drawable.zip)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.APK:
                try {
                    Glide.with(context)
                            .load(R.drawable.android)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.VCF:
                try {
                    Glide.with(context)
                            .load(R.drawable.vcf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
            case TypeFile.AUDIO:
                try {
                    Glide.with(context)
                            .load(R.drawable.audio)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .error(R.drawable.ic_error)
                            .into(holder.image);
                } catch (Exception e){
                    //do nothing
                    Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                break;
                default:
                    try {
                        Glide.with(context)
                                .load(R.drawable.unknown)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                                .centerCrop()
                                .error(R.drawable.ic_error)
                                .into(holder.image);
                    } catch (Exception e){
                        //do nothing
                        Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
        }

        holder.ivCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.ivCheckbox.isChecked()){
                    mDuplicate.setChecked(true);
                }else{
                    mDuplicate.setChecked(false);
                }
            //    Toast.makeText(context,"Check checkbox ne",Toast.LENGTH_LONG).show();
            }
        });
        holder.rlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openFile(mDuplicate);
                }catch (Exception e){

                }



            }
        });
    }
    public void openFile(Duplicate mDuplicate){
        Intent createChooser;
        try {
            File file = mDuplicate.getFile();
            if (mDuplicate.getTypeFile()==1) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");

                if (file.exists()) {
                    if (Build.VERSION.SDK_INT < 24) {
                        intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    } else {
                        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                        context.grantUriPermission(context.getPackageName(), contentUri, 1);
                        intent.setDataAndType(contentUri, "audio/*");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    createChooser = Intent.createChooser(intent, "Complete action using");
                    context.startActivity(createChooser);
                }
                return;
            } else if (mDuplicate.getTypeFile()==2) {
                if(Build.VERSION.SDK_INT<24){
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setDataAndType(Uri.fromFile(mDuplicate.getFile()), "video/*");
                    createChooser = Intent.createChooser(intent2, "Complete action using");
                }else{
                    Intent intent4 = new Intent("android.intent.action.VIEW");
                    Uri contentUri2 = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                    context.grantUriPermission(context.getPackageName(), contentUri2, 1);
                    intent4.setType("*/*");
                    if (Build.VERSION.SDK_INT < 24) {
                        contentUri2 = Uri.fromFile(file);
                    }
                    intent4.setData(contentUri2);
                    intent4.setFlags(1);
                    createChooser = Intent.createChooser(intent4, "Complete action using");
                }


            } else if (Build.VERSION.SDK_INT < 24) {
                Uri fromFile = Uri.fromFile(file);
                Intent intent3 = new Intent("android.intent.action.VIEW");
                String str = "*/*";
                MimeTypeMap singleton = MimeTypeMap.getSingleton();
                if (singleton.hasExtension(MimeTypeMap.getFileExtensionFromUrl(fromFile.toString()))) {
                    str = singleton.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fromFile.toString()));
                }
                intent3.setDataAndType(fromFile, str);
                context.startActivity(intent3);
                return;
            } else {
                Intent intent4 = new Intent("android.intent.action.VIEW");
                Uri contentUri2 = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                context.grantUriPermission(context.getPackageName(), contentUri2, 1);
                intent4.setType("*/*");
                if (Build.VERSION.SDK_INT < 24) {
                    contentUri2 = Uri.fromFile(file);
                }
                intent4.setData(contentUri2);
                intent4.setFlags(1);
                createChooser = Intent.createChooser(intent4, "Complete action using");
            }
            context.startActivity(createChooser);
        } catch (Exception e) {

        }
    }
//    public void openFile(File url) {
//
//        Uri uri = Uri.fromFile(url);
//        String fileName = url.getName();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", url);
//        context.grantUriPermission(context.getPackageName(), contentUri, 1);
////        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
////            // Word document
////            intent.setDataAndType(uri, "application/msword");
////        } else if (fileName.endsWith(".pdf")) {
////            // PDF file
////            intent.setDataAndType(uri, "application/pdf");
////        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
////            // Powerpoint file
////            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
////        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
////            // Excel file
////            intent.setDataAndType(uri, "application/vnd.ms-excel");
////        } else if (fileName.endsWith(".zip") || fileName.endsWith(".rar")) {
////            // WAV audio file
////            intent.setDataAndType(uri, "application/x-wav");
////        } else if (fileName.endsWith(".rtf")) {
////            // RTF file
////            intent.setDataAndType(uri, "application/rtf");
////        } else if (fileName.endsWith(".wav") || fileName.endsWith(".mp3")) {
////            // WAV audio file
////            intent.setDataAndType(uri, "audio/x-wav");
////        } else if (fileName.endsWith(".gif")) {
////            // GIF file
////            intent.setDataAndType(uri, "image/gif");
////        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||fileName.endsWith(".png")) {
////            // JPG file
////            intent.setDataAndType(uri, "image/jpeg");
////        } else if (fileName.endsWith(".txt")) {
////            // Text file
////            intent.setDataAndType(uri, "text/plain");
////        } else if (fileName.endsWith(".3gp") || fileName.endsWith(".mpg") || fileName.endsWith(".mpeg") || fileName.endsWith(".mpe") || fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
////            // Video files
////            intent.setDataAndType(uri, "video/*");
////        } else if (fileName.endsWith(".apk")) {
////            // GIF file
////            intent.setDataAndType(uri, "application/vnd.android.package-archive");
////        }else {
////
////            //if you want you can also define the intent type for any other file
////            //additionally use else clause below, to manage other unknown extensions
////            //in this case, Android will show all applications installed on the device
////            //so you can choose which application to use
////            intent.setDataAndType(uri, "*/*");
////        }
//        intent.setDataAndType(uri, TypeOpen.getType(url));
//        intent.setData(contentUri);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(Intent.createChooser(intent, "Complete action using"));
//
//    }
    public String getFileName(String  path) {
        String filename=path.substring(path.lastIndexOf("/")+1);
        return filename;
    }
    @Override
    public int getItemCount() {
        return mDuplicates.size();
    }


}
