package com.shohiebsense.extractzipassets;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by shohiebsense on 05/12/16.
 */

public class Utils {
    private static final int BUFFER_SIZE = 8192 ;//2048;
    private static String SDPath = "zipPath";
    private static String destinationFolder = SDPath + "/unzip/" ;


    private static InputStream getAccessAssets(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.open("zipname.zip");
    }


    public static Boolean unzip(Context context)  {
        ZipInputStream zis = null;

        try {
            zis = new ZipInputStream(new BufferedInputStream(getAccessAssets(context)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((ze = zis.getNextEntry()) != null) {
                String fileName = ze.getName();
                fileName = fileName.substring(fileName.indexOf("/") + 1);
                File file = new File(context.getCacheDir()+File.separator+destinationFolder, fileName); //change thte path
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Invalid path: " + dir.getAbsolutePath());
                if (ze.isDirectory()) continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }

            }
        } catch (IOException ioe){
            //Log.e(TAG,ioe.getMessage());
            return false;
        }  finally {
            if(zis!=null)
                try {
                    zis.close();
                } catch(IOException e) {
                    // Log.e(TAG,e.getMessage());
                }
        }
        File dirFiles = new File(context.getCacheDir()+File.separator+destinationFolder);

       /* for (String strFile : dirFiles.list())
        {
            // strFile is the file name
            Log.e("files in",strFile);
        }*/
        return true;
    }

}
