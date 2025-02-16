package com.mradzinski.casterexample;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public final class NyMimeTypes {

    //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types

    public static final String ALL_MIME_TYPES = "*/*";
    private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();
    public static final String BASIC_MIME_TYPE = "application/octet-stream";
    public static final String BASIC_MIME_TYPE2 = "application/x-mpegURL";

    private NyMimeTypes() {
    }

    static {
        MIME_TYPES.put("asm", "text/x-asm");
        MIME_TYPES.put("def", "text/plain");
        MIME_TYPES.put("in", "text/plain");
        MIME_TYPES.put("rc", "text/plain");
        MIME_TYPES.put("list", "text/plain");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("pl", "text/plain");
        MIME_TYPES.put("prop", "text/plain");
        MIME_TYPES.put("properties", "text/plain");
        MIME_TYPES.put("rc", "text/plain");

        MIME_TYPES.put("epub", "application/epub+zip");
        MIME_TYPES.put("ibooks", "application/x-ibooks+zip");

        MIME_TYPES.put("ifb", "text/calendar");
        MIME_TYPES.put("eml", "message/rfc822");
        MIME_TYPES.put("msg", "application/vnd.ms-outlook");

        MIME_TYPES.put("ace", "application/x-ace-compressed");
        MIME_TYPES.put("bz", "application/x-bzip");
        MIME_TYPES.put("bz2", "application/x-bzip2");
        MIME_TYPES.put("cab", "application/vnd.ms-cab-compressed");
        MIME_TYPES.put("gz", "application/x-gzip");
        MIME_TYPES.put("lrf", BASIC_MIME_TYPE);
        MIME_TYPES.put("jar", "application/java-archive");
        MIME_TYPES.put("xz", "application/x-xz");
        MIME_TYPES.put("Z", "application/x-compress");

        MIME_TYPES.put("bat", "application/x-msdownload");
        MIME_TYPES.put("ksh", "text/plain");
        MIME_TYPES.put("sh", "application/x-sh");

        MIME_TYPES.put("db", BASIC_MIME_TYPE);
        MIME_TYPES.put("db3", BASIC_MIME_TYPE);

        MIME_TYPES.put("otf", "application/x-font-otf");
        MIME_TYPES.put("ttf", "application/x-font-ttf");
        MIME_TYPES.put("psf", "application/x-font-linux-psf");

        MIME_TYPES.put("cgm", "image/cgm");
        MIME_TYPES.put("btif", "image/prs.btif");
        MIME_TYPES.put("dwg", "image/vnd.dwg");
        MIME_TYPES.put("dxf", "image/vnd.dxf");
        MIME_TYPES.put("fbs", "image/vnd.fastbidsheet");
        MIME_TYPES.put("fpx", "image/vnd.fpx");
        MIME_TYPES.put("fst", "image/vnd.fst");
        MIME_TYPES.put("mdi", "image/vnd.ms-mdi");
        MIME_TYPES.put("npx", "image/vnd.net-fpx");
        MIME_TYPES.put("xif", "image/vnd.xiff");
        MIME_TYPES.put("pct", "image/x-pict");
        MIME_TYPES.put("pic", "image/x-pict");

        MIME_TYPES.put("adp", "audio/adpcm");
        MIME_TYPES.put("au", "audio/basic");
        MIME_TYPES.put("snd", "audio/basic");
        MIME_TYPES.put("m2a", "audio/mpeg");
        MIME_TYPES.put("m3a", "audio/mpeg");
        MIME_TYPES.put("oga", "audio/ogg");
        MIME_TYPES.put("spx", "audio/ogg");
        MIME_TYPES.put("aac", "audio/x-aac");
        MIME_TYPES.put("mka", "audio/x-matroska");

        //TODO 2021-8-11
        MIME_TYPES.put("jpgv", "video/jpeg");
        MIME_TYPES.put("jpgm", "video/jpm");
        MIME_TYPES.put("jpm", "video/jpm");
        MIME_TYPES.put("mj2", "video/mj2");
        MIME_TYPES.put("mjp2", "video/mj2");
        MIME_TYPES.put("mpa", "video/mpeg");
        MIME_TYPES.put("ogv", "video/ogg");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("mkv", "video/x-matroska");
        MIME_TYPES.put("mts", "video/mts");
        MIME_TYPES.put("m2ts", "video/m2ts");
        MIME_TYPES.put("dat", "video/dat");
        MIME_TYPES.put("vob", "video/vob");
        MIME_TYPES.put("avi", "video/avi");
        MIME_TYPES.put("webm", "video/webm");

    }

    public static String getMimeTypeFromPath(String url) {
        String ext = getFileExtension(url);
        if (ext != null && getMimeTypeFromExtension(ext)!=null) return getMimeTypeFromExtension(ext);
            //TODO originally return ext;
        else return BASIC_MIME_TYPE2;
    }


    public static String getMimeTypeFromExtension(String extension) {
        String type = null;

        if (!TextUtils.isEmpty(extension)) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionLowerCase);
            //TODO 2022-5-2 //extra mime types map
            if (type == null || type.equals("application/octet-stream")) {
                type = MIME_TYPES.get(extensionLowerCase);
            }
        }
        return type;
    }

    /**
     * Get Mime Type of a file
     *
     * @param file the file of which mime type to get
     * @return Mime type in form of String
     */
    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return null;
        }

        String type = ALL_MIME_TYPES;
        final String extension = getFileExtension(file.getName());

        // mapping extension to system mime types
        if (extension != null && !extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase();
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
            if (type == null) {
                type = MIME_TYPES.get(extensionLowerCase);
            }
        }
        if (type == null) type = ALL_MIME_TYPES;
        return type;
    }


    public static @Nullable
    String[] splitMimeType(String mimeType) {
        final String[] groups = mimeType.split("/");

        if (groups.length != 2 || groups[0].isEmpty() || groups[1].isEmpty()) {
            return null;
        }

        return groups;
    }

    public static String findCommonMimeType(List<String> mimeTypes) {
        String[] commonType = splitMimeType(mimeTypes.get(0));
        if (commonType == null) {
            return ALL_MIME_TYPES;
        }

        for (int i = 1; i < mimeTypes.size(); i++) {
            String[] type = mimeTypes.get(i).split("/");
            if (type.length != 2) continue;

            if (!commonType[1].equals(type[1])) {
                commonType[1] = "*";
            }

            if (!commonType[0].equals(type[0])) {
                commonType[0] = "*";
                commonType[1] = "*";
                break;
            }
        }

        return commonType[0] + "/" + commonType[1];
    }

    public static String getFileExtension(String fileName) {
        String ext = null;
        if (fileName != null && fileName.lastIndexOf('.') != -1)
            ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        return ext;
    }

    public static String getLastSegmentFromString(String path) {
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path.contains("/")) {
            path = path.substring(path.lastIndexOf('/') + 1);
        }
        return path;
    }

    public static String getFileNameWithoutExtFromPath(String path) {

        path = getLastSegmentFromString(path);
        if (path.contains(".")) {
            path = path.substring(0, path.indexOf('.'));
        }
        return path;
    }
}