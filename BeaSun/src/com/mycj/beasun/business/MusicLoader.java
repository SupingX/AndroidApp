package com.mycj.beasun.business;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;




import com.mycj.beasun.R;
import com.mycj.beasun.bean.Music;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

public class MusicLoader {
	private static MusicLoader musicLoader;
	private static List<Music> musicList = new ArrayList<>();
	// Uri，指向external的database
	private static Uri contentUri = Media.EXTERNAL_CONTENT_URI;
	// 选择的列
	private static String[] projection = new String[] { Media._ID, Media.DISPLAY_NAME, Media.DATA, Media.ALBUM, Media.ARTIST, Media.DURATION, Media.SIZE, Media.ALBUM_ID };
	// 过滤条件
	private String where = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
	private static String sortOrder = Media.DATA;
	private static ContentResolver contentResolver;

	public static MusicLoader instance(ContentResolver pContentResolver) {
		if (musicLoader == null) {
			contentResolver = pContentResolver;
			return new MusicLoader();
		}
		return musicLoader;
	}
	
	
	public static String getTitleFromId(long ids){
		// 利用ContentResolver的query函数来查询数据，然后将得到的结果放到MusicInfo对象中，最后放到数组中
		
		Cursor cursor = contentResolver.query(contentUri, projection, null, null, sortOrder);
		try {
			if (cursor == null) {
			} else if (!cursor.moveToFirst()) {

			} else {
				int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);
				int idCol = cursor.getColumnIndex(Media._ID);
				do {
					String title = cursor.getString(displayNameCol);
					long id = cursor.getLong(idCol);
					if (id==ids) {
						return title;
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}
		return null;
	}
	
	private MusicLoader() {
		// 利用ContentResolver的query函数来查询数据，然后将得到的结果放到MusicInfo对象中，最后放到数组中
		Cursor cursor = contentResolver.query(contentUri, projection, null, null, sortOrder);
		try {
			if (cursor == null) {
			} else if (!cursor.moveToFirst()) {

			} else {
				int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);
				int albumCol = cursor.getColumnIndex(Media.ALBUM);
				int idCol = cursor.getColumnIndex(Media._ID);
				int durationCol = cursor.getColumnIndex(Media.DURATION);
				int sizeCol = cursor.getColumnIndex(Media.SIZE);
				int artistCol = cursor.getColumnIndex(Media.ARTIST);
				int urlCol = cursor.getColumnIndex(Media.DATA);
				int albumid = cursor.getColumnIndex(Media.ALBUM_ID);
				do {
					String title = cursor.getString(displayNameCol);
					String album = cursor.getString(albumCol);
					long id = cursor.getLong(idCol);
					int duration = cursor.getInt(durationCol);
					long size = cursor.getLong(sizeCol);
					String artist = cursor.getString(artistCol);
					String url = cursor.getString(urlCol);
					long albumId = cursor.getLong(albumid);

					Music musicInfo = new Music(id, title);
					musicInfo.setAlbum(album);
					musicInfo.setDuration(duration);
					musicInfo.setSize(size);
					musicInfo.setArtist(artist);
					musicInfo.setUrl(url);
					musicInfo.setAlbumId(albumId);
					musicList.add(musicInfo);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			cursor.close();
		}

	}

	public static List<Music> getMusicList() {
		return musicList;
	}

	public static Uri getMusicUriById(long id) {
		Uri uri = ContentUris.withAppendedId(contentUri, id);
		return uri;
	}

	public static Bitmap getArt(Context context, long song_id, long album_id, boolean allowdefault) {
		Bitmap bm = null;
//		if (album_id < 0) {
//			if (song_id >= 0) {
//				bm = getArtworkFromFile(context, song_id, -1);
//			}
//			if (allowdefault) {
//				bm = getDefaultArtwork(context);
//			}
//			return bm;
//		}

		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				options.inSampleSize = computeSampleSize(options, 30);
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = res.openInputStream(uri);
				bm = BitmapFactory.decodeStream(in, null, options);
			} catch (FileNotFoundException ex) {
				// The album art thumbnail does not actually exist. Maybe
				// the
				// user deleted it, or
				// maybe it never existed to begin with.
//				bm = getArtworkFromFile(context, song_id, album_id);
//				if (bm != null) {
//					if (bm.getConfig() == null) {
//						bm = bm.copy(Bitmap.Config.RGB_565, false);
//						if (bm == null && allowdefault) {
//							bm = getDefaultArtwork(context);
//						}
//					}
//				} else if (allowdefault) {
//					bm = getDefaultArtwork(context);
//				}

			} finally {
				try {
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException ex) {
				}
			}
		}
		return bm;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		//Log.v("ADW", "candidate:" + candidate);
		return candidate;
	}

	public static Bitmap getDefaultArtwork(Context context) {
		Bitmap bitmap = null;
		InputStream input = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			input = context.getResources().openRawResource(R.drawable.ic_acquiesce_img);
			bitmap = BitmapFactory.decodeStream(input, null, opts);

			return bitmap;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
				input = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}

	public static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
		Bitmap bm = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException("Must specify an album or a song id");
		}
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			FileDescriptor fd = null;

			ContentResolver contentResolver = context.getContentResolver();

			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
				ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm =
					// BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm =
					// BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			}
			//Log.v("ADW", "getArtworkFromFile");
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			options.inSampleSize = 500;// computeSampleSize(options, 800);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (FileNotFoundException ex) {

		} finally {
			// try {
			// pfd.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		if (bm != null) {
			mCachedBit = bm;
		}
		return bm;
	}

	private final static Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static Bitmap mCachedBit = null;

}
