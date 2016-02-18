package com.sponia.foundationmoudle.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.sponia.foundationmoudle.common.Common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description
 * @date 15/10/10
 */
public class FileUtil {
    /**
     * 创建文件 创建文件时，如果文件的父目录不存在 则会自动创建 创建完成后，会校验是否创建成功 若失败则抛出异常
     *
     * @param file
     * @throws CreateFileFailedException
     * @throws IOException
     */
    public static void createFile(File file) throws CreateFileFailedException,
            IOException {
        if (null == file || file.exists()) {
            return;
        }
        // 如果是文件，先检查父目录是否存在
        File directoryFile = file.getParentFile();
        if (directoryFile != null && !directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        file.createNewFile();
        // 校验，如果创建失败则抛异常
        if (!file.exists()) {
            throw new CreateFileFailedException("Failed to create file! ["
                    + file.getPath() + "]");
        }
    }

    /**
     * 创建文件 创建文件时，如果文件的父目录不存在 则会自动创建 创建完成后，会校验是否创建成功 若失败则抛出异常
     * @param filePath
     * @throws CreateFileFailedException
     * @throws IOException
     */
    public static void createFile(String filePath)
            throws CreateFileFailedException, IOException {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        createFile(file);
    }

    /**
     * 删除文件或者文件夹 删除完成后，会校验是否删除成功 若失败则抛出异常
     *
     * @param file
     * @throws DeleteFileFailedException
     */
    public static void deleteFile(File file) throws DeleteFileFailedException {
        if (null == file || !file.exists()) {
            return;
        }
        //如果是一个文件
        if (file.isFile()) {
            file.delete();
        } else {//如果是文件夹
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {//如果是空文件夹
                file.delete();//直接删除空文件夹
                return;
            }//假如不是空文件夹
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);//递归删除文件夹中的每一个文件
            }
            file.delete();//然后再把文件夹删除
        }

        // 校验，如果创建失败则抛异常
        if (file.exists()) {
            throw new DeleteFileFailedException("Failed to delete file! ["
                    + file.getPath() + "]");
        }
    }

    /**
     * 删除文件或者文件夹 删除完成后，会校验是否删除成功 若失败则抛出异常
     *
     * @param filePath
     * @throws DeleteFileFailedException
     */
    public static void deleteFile(String filePath)
            throws DeleteFileFailedException {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        deleteFile(file);
    }

    /**
     * 拷贝文件或者文件夹
     *
     * @param srcFile
     * @param desFile
     * @throws IOException
     * @throws CreateFileFailedException
     */
    public static void copyFile(File srcFile, File desFile) throws IOException,
            CreateFileFailedException {
        copyFileCore(srcFile, desFile, false);
    }

    /**
     * 拷贝文件或者文件夹
     *
     * @param srcFilePath
     * @param desFilePath
     * @throws IOException
     * @throws CreateFileFailedException
     */
    public static void copyFile(String srcFilePath, String desFilePath)
            throws IOException, CreateFileFailedException {
        if (TextUtils.isEmpty(srcFilePath)
                || TextUtils.isEmpty(desFilePath)) {
            return;
        }
        File srcFile = new File(srcFilePath);
        File desFile = new File(desFilePath);
        copyFile(srcFile, desFile);
    }

    /**
     * 使用通道拷贝文件
     *
     * @param srcFile
     * @param desFile
     * @throws IOException
     * @throws CreateFileFailedException
     */
    public static void copyFileByChannel(File srcFile, File desFile)
            throws IOException, CreateFileFailedException {
        copyFileCore(srcFile, desFile, true);
    }

    /**
     * 使用通道拷贝文件
     *
     * @param srcFilePath
     * @param desFilePath
     * @throws IOException
     * @throws CreateFileFailedException
     */
    public static void copyFileByChannel(String srcFilePath, String desFilePath)
            throws IOException, CreateFileFailedException {
        if (TextUtils.isEmpty(srcFilePath)
                || TextUtils.isEmpty(desFilePath)) {
            return;
        }
        File srcFile = new File(srcFilePath);
        File desFile = new File(desFilePath);
        copyFileCore(srcFile, desFile, true);
    }

    /**
     * 从文件读取string
     *
     * @param file
     * @return String
     *
     */
    public static String readStringFromFile(File file) {
        if (null == file) {
            return null;
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            char[] cTemp = new char[1024];
            int i = -1;
            StringBuilder strBuilder = new StringBuilder();
            while ((i = fileReader.read(cTemp)) != -1) {
                strBuilder.append(cTemp, 0, i);
            }
            return strBuilder.toString();
        } catch (Exception e) {
            LogUtil.defaultLog(e);
            return null;
        } finally {
            if (null != fileReader) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    LogUtil.defaultLog(e);
                }
            }
        }
    }

    /**
     * 从文件读取string
     * @param filePath
     * @return
     */
    public static String readStringFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return readStringFromFile(new File(filePath));
    }

    /**
     * 向文件写入string
     *
     * @param filePath
     * @param inputStr
     * @param append
     */
    public static final void writeStringToFile(String filePath,
                                               String inputStr, boolean append) {
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.write(inputStr);
            fileWriter.flush();
        } catch (Exception e) {
            LogUtil.defaultLog(e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                LogUtil.defaultLog(e);
            }
        }
    }

    /**
     * 从文件读取图片
     *
     * @param file
     * @return Bitmap
     * @throws FileNotFoundException
     */
    public static final Bitmap readBitmapFromFile(File file)
            throws FileNotFoundException {
        FileInputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            return bitmap;
        } catch (Exception e) {
            LogUtil.defaultLog(e);
            return null;
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LogUtil.defaultLog(e);
            }
        }
    }

    /**
     * 从文件读取图片
     *
     * @param filePath
     * @return Bitmap
     */
    public static final Bitmap readBitmapFromFile(String filePath)
            throws FileNotFoundException {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return readBitmapFromFile(new File(filePath));
    }

    // 默认图片质量
    private static final int DEFAULT_QUALITY = 100;

    /**
     * 将图片写入文件
     *
     * @param bitmap
     * @param file
     * @throws CreateFileFailedException
     * @throws IOException
     *
     */
    public static final void writeBitmapToFile(Bitmap bitmap, File file)
            throws CreateFileFailedException, IOException {
        if (null == bitmap || null == file) {
            return;
        }
        // 删除原文件
        if (file.exists()) {
            file.delete();
        }
        createFile(file);

        OutputStream outStream = null;
        try {
            // 选择压缩算法
            CompressFormat format = null;
            if (file.getPath().endsWith(".png")) {
                format = CompressFormat.PNG;
            } else {
                format = CompressFormat.JPEG;
            }
            outStream = new FileOutputStream(file);
            if (!bitmap.compress(format, DEFAULT_QUALITY, outStream)) {
                return;
            }
        } catch (Exception e) {
            LogUtil.defaultLog(e);
            return;
        } finally {
            try {
                if (null != outStream) {
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException e) {
                LogUtil.defaultLog(e);
            }
        }
    }

    /**
     * 将图片写入文件
     *
     * @param bitmap
     * @param filePath
     * @throws CreateFileFailedException
     * @throws IOException
     */
    public static final void writeBitmapToFile(Bitmap bitmap, String filePath)
            throws CreateFileFailedException, IOException {
        if (null == bitmap || TextUtils.isEmpty(filePath)) {
            return;
        }
        writeBitmapToFile(bitmap, new File(filePath));
    }

    /**
     * 从文件读取drawable
     *
     * @param file
     * @return Drawable
     * @throws FileNotFoundException
     */
    public static final Drawable readDrawableFromFile(File file)
            throws FileNotFoundException {
        Drawable drawable = null;
        Bitmap bitmap = readBitmapFromFile(file);
        if (bitmap != null) {
            bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
            drawable = new BitmapDrawable(bitmap);
        }
        return drawable;
    }

    /**
     * 从文件读取drawable
     * （.9图片有问题，已作废）
     * @param filePath
     * @return Drawable
     * @throws FileNotFoundException
     */
    @Deprecated
    public static final Drawable readDrawableFromFile(String filePath)
            throws FileNotFoundException {

        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return readDrawableFromFile(new File(filePath));
    }

    /***
     * 从文件读取drawable(支持.9图片)
     * @param path 图片的绝对路径
     * 			如“/res/drawable-xhdpi/icon_cmb_keyboard_upper.png”
     * 			如“/res/drawable-xhdpi/icon_cmb_keyboard_upper.9.png”
     * @return Drawable
     */
    public static Drawable getDrawableFromFile(String path) {
        try {
            Drawable drawable = null;
            InputStream inputStream = FileUtil.class.getResourceAsStream(path);
            if (path.endsWith("9.png")) {
                TypedValue value = new TypedValue();
                value.density = TypedValue.DENSITY_DEFAULT;
                drawable = NinePatchDrawable.createFromStream(
                        FileUtil.class.getResourceAsStream(path), null);
            } else {
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                if (bm != null) {
                    bm.setDensity(DisplayMetrics.DENSITY_DEFAULT);
                    drawable = new BitmapDrawable(bm);
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            }
            return drawable;
        } catch (Exception e) {
            return NinePatchDrawable.createFromStream(
                    FileUtil.class.getResourceAsStream(path), null);
        }
    }

    /**
     * 将drawable写入文件
     *
     * @param drawable
     * @param filePath
     */
    public static final void writeDrawableToFile(Drawable drawable,
                                                 String filePath) throws IOException {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        writeDrawableToFile(drawable, new File(filePath));
    }

    /**
     * 将drawable写入文件
     *
     * @param drawable
     * @param file
     */
    public static final void writeDrawableToFile(Drawable drawable, File file)
            throws IOException {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        writeBitmapToFile(bitmap, file);
    }

    /**
     * 将异常日志输入至指定文件夹/指定文件
     *
     * @param Path
     * @param throwable
     */
    public static final void writeLogInfoToFile(String Path, Throwable throwable) {
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            File file = new File(Path);
            if (!file.exists() && file.getParentFile() != null
                    && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file, true);
            ps = new PrintStream(fos);
            throwable.printStackTrace(ps);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcFile
     * @param desFile
     * @param isUseChannel
     */
    private static void copyFileCore(File srcFile, File desFile,
                                     boolean isUseChannel) throws IOException, CreateFileFailedException {
        if (null == srcFile || null == desFile || !srcFile.exists()) {
            return;
        }

        if (srcFile.isDirectory()) {
            // 如果是目录，则进行循环遍历

            // 递归
            File[] srcFiles = srcFile.listFiles();
            for (int t = 0; t < srcFiles.length; t++) {
                File tempSrcFile = srcFiles[t];
                copyFileCore(tempSrcFile, new File(desFile.getPath()
                        + File.separator + tempSrcFile.getName()), isUseChannel);
            }

        } else {
            // 如果是文件，则直接复制
            createFile(desFile);
            if (isUseChannel) {
                copyFileByChannelCore(srcFile, desFile);
            } else {
                copyFileByStreamCore(srcFile, desFile);
            }
        }
    }

    /**
     * 使用流拷贝文件
     *
     * @param srcFile
     * @param desFile
     */
    private static void copyFileByStreamCore(File srcFile, File desFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(desFile);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
        } catch (IOException e) {
            LogUtil.defaultLog(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.defaultLog(e);
            }
        }
    }

    /**
     * 使用通道贝文件
     *
     * @param srcFile
     * @param desFile
     */
    private static void copyFileByChannelCore(File srcFile, File desFile) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(srcFile);
            fo = new FileOutputStream(desFile);
            in = fi.getChannel();
            out = fo.getChannel();
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            LogUtil.defaultLog(e);
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.defaultLog(e);
            }
        }
    }

    /**
     * 创建文件失败异常
     *
     * @author wangyuanjie
     */
    public static class CreateFileFailedException extends RuntimeException {

        public CreateFileFailedException(String errorMsg) {
            super(errorMsg);
        }

        public CreateFileFailedException(Throwable e) {
            super(e);
        }
    }

    /**
     * 删除文件失败异常
     *
     * @author wangyuanjie
     */
    public static class DeleteFileFailedException extends RuntimeException {

        public DeleteFileFailedException(String errorMsg) {
            super(errorMsg);
        }

        public DeleteFileFailedException(Throwable e) {
            super(e);
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     *            File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static double getFolderSize(File file) throws Exception {
        double size = 0;
        File[] fileList = file.listFiles();
        if (null == fileList) {
            return 0;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        size = size / 1048576;
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(size));
    }

    /**
     * 向android文件系统写入位图信息，返回文件绝对路径,
     *
     * @param bitmap
     * @param parentPath
     * @return 返回写入成功后的文件绝对路径，写入失败等情况返回null
     */
    public static final String writeBitmap2FileSysJpeg(Bitmap bitmap,
                                                       String parentPath) {
        String path = null;
        if (bitmap == null || parentPath == null || parentPath.length() == 0)
            return null;
        File file = new File(parentPath);
        if (file.exists() && file.isDirectory()) {
            String fileName = (new StringBuilder(String.valueOf(UUID
                    .randomUUID().toString().replaceAll("-", "")))).append(
                    ".jpg").toString();
            path = (new StringBuilder(String.valueOf(parentPath))).append("/")
                    .append(fileName).toString();
            File fileBitmap = new File(path);
            if (fileBitmap.exists()) {
                fileBitmap.delete();
            }
            try {
                fileBitmap.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            CompressFormat format = CompressFormat.JPEG;
            int quality = 100;
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(path);
                if (!bitmap.compress(format, quality, stream))
                    path = null;
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                path = null;
            }
        }
        return path;
    }

    public static final boolean writeResDrawableToSdPng ( Bitmap bitmap,
                                                          String filePath ) {
        if (bitmap == null || filePath == null || filePath.length() == 0)
            return false;
        try {
            File file = new File(filePath);
            if (file.exists() ) {
                file.delete();
            }
            file.createNewFile();
            CompressFormat format = CompressFormat.PNG;
            int quality = 100;
            OutputStream stream = null;
            stream = new FileOutputStream(filePath);
            if (!bitmap.compress(format, quality, stream))
                filePath = null;
            stream.flush();
            stream.close();
        } catch (Exception e) {
            return false ;
        }
        return true ;

    }

    private final static String FOLDER_NAME = "/sponia/openplayer";
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    private static String mDataRootPath = Common.application.getApplicationContext().getCacheDir().getPath();


    public static String getStorageDirectory() {
        String root = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;

        if (root.contains("emulated/0/")) {
            root = root.replace("emulated/0/", "emulated/legacy/");
        }
        if (root.contains("emulated/1/")) {
            root = root.replace("emulated/1/", "emulated/legacy/");
        }
        if (root.contains("emulated/2/")) {
            root = root.replace("emulated/2/", "emulated/legacy/");
        }

        File file = new File(root);
        if (!file.exists()) {
            file.mkdirs();
        }

        return root;
    }
}
