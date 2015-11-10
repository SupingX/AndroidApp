package com.mycj.beasun.service.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

public class ImageUtil {
	  /** 
     * 旋转Bitmap 
     * @param b 
     * @param rotateDegree 
     * @return 
     */  
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){  
        Matrix matrix = new Matrix();  
        matrix.postRotate((float)rotateDegree);  
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);  
        return rotaBitmap;  
    } 
    
    
//    private void getAllSDImageFolder(Context context) {
//        String[] projection = new String[] { MediaStore.Images.Media._ID,
//                        MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
//                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
//                        MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
//                        MediaStore.Images.Media.DATA, // 图片绝对路径
//                        "count("+MediaStore.Images.Media._ID+")"//统计当前文件夹下共有多少张图片
//        };
//        String selection = " 0==0) group by bucket_display_name --(";
//         
//        ContentResolver cr = context.getContentResolver();
//         
//        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,null, "");
//         
//        if(null != cursor){
//            while(cursor.moveToNext()){
//                String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
//                folderIDS[folderIndex] = folderId;
//                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
//                folderNames[folderIndex] = folder;
//                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
//                String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
//                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                int count = cursor.getInt(5);//该文件夹下一共有多少张图片
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                Bitmap bitmap = Thumbnails.getThumbnail(cr, fileId, Thumbnails.MICRO_KIND, options);//获取指定图片缩略片
//                thumbS.add(bitmap);
//                folderIndex++;
//            }
//            if(!cursor.isClosed()){
//                cursor.close();
//            }
//             
//        }
//    }
}
