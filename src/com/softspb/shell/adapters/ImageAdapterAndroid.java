package com.softspb.shell.adapters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.softspb.shell.data.WidgetsContract;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.IOHelper;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class ImageAdapterAndroid extends ImageAdapter
{
  private static final String ASSETS_PREFIX = "@assets/";
  private static final String CONTENT_PREFIX = "content";
  private static final float HIGH_DENSITY_DPI = 1.5F;
  private static final int PixelFormat_A8 = 6;
  private static final int PixelFormat_B8G8R8 = 3;
  private static final int PixelFormat_B8G8R8A8 = 5;
  private static final int PixelFormat_L8 = 7;
  private static final int PixelFormat_L8A8 = 8;
  private static final int PixelFormat_R5G6B5 = 1;
  private static final int PixelFormat_R8G8B8 = 2;
  private static final int PixelFormat_R8G8B8A8 = 4;
  private static final String RESOURCE_PREFIX = "android.resource";
  private static final Logger logger = Loggers.getLogger(ImageAdapterAndroid.class.getName());
  private Context context;
  private String externalStorageRootPath;
  String widgetsAuthority;

  final static class ImageAdapterAndroid$1 extends InputStream
  {
	  private ByteBuffer val$buf;
    public ImageAdapterAndroid$1(ByteBuffer paramByteBuffer) {
		this.val$buf =paramByteBuffer;
	}

	public int available()
      throws IOException
    {
      return this.val$buf.remaining();
    }

    /** @deprecated */
    public synchronized int read()
      throws IOException
    {
     int i = -1;
      try
      {
        boolean bool = this.val$buf.hasRemaining();
        if(bool)
        {
        	byte byte0 = this.val$buf.get();
			i = byte0 & 0xff;
        }
        return i;
      }
      finally
      {

      }
  
    }

    /** @deprecated */
    public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      int i=-1;
      try
      {
        boolean bool = this.val$buf.hasRemaining();
        if(bool)
        {
        	 int j = this.val$buf.remaining();
             paramInt2 = Math.min(paramInt2, j);
             ByteBuffer localByteBuffer = this.val$buf.get(paramArrayOfByte, paramInt1, paramInt2);
             i = paramInt2;
        }
        else
        {
        	
        }
        return i;       
      }
      finally
      {
      
      }
 
    }

    public long skip(long paramLong)
      throws IOException
    {
      long l1 = 0L;
      if (paramLong >=l1)
      {
        long l2 = this.val$buf.remaining();
        l1 = Math.min(paramLong, l2);
        ByteBuffer localByteBuffer = this.val$buf;
        int i = (int)(this.val$buf.position() + l1);
        Buffer localBuffer = localByteBuffer.position(i);
      }
      return l1;
    }
  }
  
  public ImageAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  private Bitmap decodeAsset(String paramString)
  {
    Logger localLogger1 = logger;
    String str1 = "decodeAsset: path=" + paramString;
    localLogger1.d(str1);
    Object localObject1 = null;
    InputStream localInputStream = null;
    if (paramString != null);
    while (true)
    {
      try
      {
        String str2 = paramString.toLowerCase();
        localInputStream = this.context.getAssets().open(str2);
        Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream);
        localObject1 = localBitmap;
        IOHelper.closeSilent(localInputStream);
        Logger localLogger2 = logger;
        StringBuilder localStringBuilder = new StringBuilder().append("Asset is null?: ");
        if (localObject1 == null)
        {
          Object localObject2 = localObject1;
          return (Bitmap)localObject2;
        }
      }
      catch (Exception localException)
      {
        Logger localLogger3 = logger;
        String str4 = "decodeAsset failed: " + localException;
        localLogger3.e(str4, localException);
        IOHelper.closeSilent(localInputStream);
        continue;
      }
      finally
      {
        IOHelper.closeSilent(localInputStream);
      }
    }
  }

  private Bitmap decodeFile(String paramString)
  {
    Logger localLogger1 = logger;
    String str1 = "decodeFile: path=" + paramString;
    localLogger1.d(str1);
    Object localObject = null;
    try
    {
      Bitmap localBitmap = BitmapFactory.decodeFile(paramString);
      localObject = localBitmap;
      Logger localLogger2 = logger;
      StringBuilder localStringBuilder = new StringBuilder().append("File is null? ");
        return (Bitmap)localObject;
    }
    catch (Exception localException)
    { 
    	return (Bitmap)localObject;
    }
  }

	private Bitmap decodeFromExternalStorage(String s)
	{
		File file;
		Object obj;
		Bitmap bitmap;
		logger.d((new StringBuilder()).append("decodeFromExternalStorage: path=").append(s).toString());
		file = new File(s);
		obj = null;
		bitmap = null;
		FileInputStream fileinputstream = null;
		try {
			fileinputstream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap1 = BitmapFactory.decodeStream(fileinputstream);
		bitmap = bitmap1;
		IOHelper.closeSilent(fileinputstream);
		return bitmap;
	}
  public static Bitmap decodeResourceUri(Context paramContext, String paramString)
  {
    Logger localLogger1 = logger;
    String str1 = "decodeResourceUri: " + paramString;
    localLogger1.d(str1);
    Bitmap localBitmap = null ;
    try
    {
      Uri localUri = Uri.parse(paramString);
      String str2 = localUri.getHost();
      int i = Integer.valueOf(localUri.getLastPathSegment()).intValue();
      Resources localResources = paramContext.createPackageContext(str2, 2).getResources();
      Drawable localDrawable = null;
      if (isSW600dp(localResources.getDisplayMetrics()))
        localDrawable = getDrawableForDensity(localResources, i, 240, 1.5F);
      if (localDrawable == null)
        localDrawable = localResources.getDrawable(i);
      localBitmap = resizeDrawable(paramContext, localDrawable);
      Logger localLogger2 = logger;
      Object[] arrayOfObject = new Object[2];
      Integer localInteger1 = Integer.valueOf(localBitmap.getWidth());
      arrayOfObject[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(localBitmap.getHeight());
      arrayOfObject[1] = localInteger2;
      localLogger2.d("resulting bitmap width %d height: %d", arrayOfObject);
      return localBitmap;
    }
    catch (Exception localException)
    { 
    	return localBitmap;
    }
  }

  public static Drawable getDrawableForDensity(Resources paramResources, int paramInt1, int paramInt2, float paramFloat)
  {
    Configuration localConfiguration = paramResources.getConfiguration();
    DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
    float f = localDisplayMetrics.density;
    int i = localDisplayMetrics.densityDpi;
    localDisplayMetrics.density = paramFloat;
    localDisplayMetrics.densityDpi = paramInt2;
    paramResources.updateConfiguration(localConfiguration, localDisplayMetrics);
    Drawable localDrawable = paramResources.getDrawable(paramInt1);
    localDisplayMetrics.density = f;
    localDisplayMetrics.densityDpi = i;
    paramResources.updateConfiguration(localConfiguration, localDisplayMetrics);
    return localDrawable;
  }
	private static boolean isSW600dp(DisplayMetrics displaymetrics)
	{
		boolean flag;
		if ((float)displaymetrics.widthPixels >= 600F * displaymetrics.density)
			flag = true;
		else
			flag = false;
		return flag;
	}
  public static InputStream newInputStream(ByteBuffer paramByteBuffer)
  {
    return new  ImageAdapterAndroid$1(paramByteBuffer);
  }

	private static byte[] readByteBuffer(ByteBuffer bytebuffer)  
	{
		ByteArrayOutputStream bytearrayoutputstream;
		InputStream inputstream = null;
		bytearrayoutputstream = new ByteArrayOutputStream();
		
		inputstream = newInputStream(bytebuffer);
		byte abyte1[] = new byte[1024];
		do
		{
			int i = 0;
			try {
				i = inputstream.read(abyte1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i == -1)
				break;
			bytearrayoutputstream.write(abyte1, 0, i);
		} while (true);
		
		byte abyte0[];
		byte abyte2[];
		
		if (inputstream != null)
		{
			try
			{
				inputstream.close();
			}
			catch (IOException ioexception1) { }
		}	
		abyte0 = null;
		
		abyte2 = bytearrayoutputstream.toByteArray();
		abyte0 = abyte2;
		return abyte0;
	}

  public static Bitmap resizeDrawable(Context paramContext, Drawable paramDrawable)
  {
	  Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
	  Bitmap localBitmap = null;
    try
    {
      int i = paramDrawable.getIntrinsicWidth();
      int j = paramDrawable.getIntrinsicHeight();
      Resources localResources = paramContext.getResources();
      int k = (int)localResources.getDimension(17104896);
      int m = (int)localResources.getDimension(17104896);
      DisplayMetrics localDisplayMetrics = localResources.getDisplayMetrics();
      if (isSW600dp(localDisplayMetrics))
      {
        float f1 = localDisplayMetrics.density;
        k = (int)(72.0F * f1);
        float f2 = localDisplayMetrics.density;
        m = (int)(72.0F * f2);
      }
      if ((paramDrawable instanceof PaintDrawable))
      {
        PaintDrawable localPaintDrawable = (PaintDrawable)paramDrawable;
        localPaintDrawable.setIntrinsicWidth(k);
        localPaintDrawable.setIntrinsicHeight(m);
      }
      if ((k > 0) && (m > 0))
      {
        float f3 = i;
        float f4 = j;
        float f5 = f3 / f4;
        if (i > j)
        {
          m = (int)(k / f5);
          
        }
        else
        {
        	 k = (int)(m * f5);
        }
        if (paramDrawable.getOpacity() == -1)
        {
      	    localBitmap = Bitmap.createBitmap(k, m, localConfig);
            Canvas localCanvas = new Canvas(localBitmap);
            PaintFlagsDrawFilter localPaintFlagsDrawFilter = new PaintFlagsDrawFilter(4, 0);
            localCanvas.setDrawFilter(localPaintFlagsDrawFilter);
            Rect localRect1 = paramDrawable.getBounds();
            Rect localRect2 = new Rect(localRect1);
            paramDrawable.setBounds(0, 0, k, m);
            paramDrawable.draw(localCanvas);
            paramDrawable.setBounds(localRect2);
//            return localBitmap; 
        }   
      }
    }
    catch (Exception localException)
    {
        localBitmap = null;      
    }
    finally
    {
    	 return localBitmap; 
    }
  }

  private static void writeToSDCard(Context context1, byte abyte0[], String s)
	{
		File file;
		logger.d((new StringBuilder()).append("writeToSDCard >>> filename=").append(s).append(" data size=").append(abyte0.length).toString());
		file = context1.getFilesDir();
		if (file != null)
		{
			File file1;
			FileOutputStream fileoutputstream;
			file1 = new File(file, s);
			fileoutputstream = null;
			FileOutputStream fileoutputstream1 = null;
			try {
				fileoutputstream1 = new FileOutputStream(file1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fileoutputstream1.write(abyte0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (fileoutputstream1 != null)
				try
				{
					fileoutputstream1.close();
				}
			catch (IOException ioexception3) { }
			logger.d((new StringBuilder()).append("writeToSDCard <<< data stored to SD card: ").append(file1.getPath()).toString());
		}
		else 
		{
		   logger.w("writeToSDCard <<< failed to store file on SD card: not mounted?");
		}
		return;
	}
  
  public boolean SaveCompressed(ByteBuffer bytebuffer, int i, int j, int k, String s)
	{
	  boolean flag;
	  android.graphics.Bitmap.Config config = null;
	  switch (k) {
		case 1:
			config = android.graphics.Bitmap.Config.RGB_565;
			break;
		case 2:
			logger.e((new StringBuilder()).append("SaveCompressed: Unsupported pixel format ").append(k).toString());
			flag = false;
			break;
		case 3:
			logger.e((new StringBuilder()).append("SaveCompressed: Unsupported pixel format ").append(k).toString());
			flag = false;
			break;
		case 4:
			config = android.graphics.Bitmap.Config.ARGB_8888;
			break;
		case 5:
			logger.e((new StringBuilder()).append("SaveCompressed: Unsupported pixel format ").append(k).toString());
			flag = false;	
			break;
		case 6:
			config = android.graphics.Bitmap.Config.ALPHA_8; 		
			break;
		default:
			logger.e((new StringBuilder()).append("SaveCompressed: Unsupported pixel format ").append(k).toString());
			flag = false;
			break;
		}
	  	Object obj;
		Bitmap bitmap;
		obj = null;
		bitmap = null;
		FileOutputStream fileoutputstream = null;
		try {
			fileoutputstream = new FileOutputStream(new File(s));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean flag1;
		bitmap = Bitmap.createBitmap(i, j, config);
		bitmap.copyPixelsFromBuffer(bytebuffer);
		flag1 = bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 75, fileoutputstream);
		flag = flag1;
		IOHelper.closeSilent(fileoutputstream);
		if (bitmap != null)
			bitmap.recycle();
		return flag;
	}
  
  public Bitmap decodeContentUri(String paramString)
  {
    Logger localLogger1 = logger;
    String str1 = "decodeContentUri: " + paramString;
    localLogger1.d(str1);
    InputStream localInputStream = null;
    Bitmap localObject1;
	try
    {
      ContentResolver localContentResolver = this.context.getContentResolver();
      Uri localUri = Uri.parse(paramString);
      localInputStream = localContentResolver.openInputStream(localUri);
      localObject1 = BitmapFactory.decodeStream(localInputStream);
      if (((Bitmap)localObject1).getConfig() == null)
      {
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = ((Bitmap)localObject1).copy(localConfig, false);
        ((Bitmap)localObject1).recycle();
        localObject1 = localBitmap;
      }
      return localObject1;
    }
    catch (Exception localException)
    {
        Logger localLogger2 = logger;
        String str2 = "Failed to get content URI: " + localException;
        localLogger2.e(str2, localException);
        IOHelper.closeSilent(localInputStream);
        localObject1 = null;
        return localObject1;
    }
    finally
    {
      IOHelper.closeSilent(localInputStream);  
    }
  }

  public Bitmap decodeShortcutContentUri(String paramString)
  {
    Object localObject1 = null;
    ContentResolver localContentResolver = this.context.getContentResolver();
    Cursor localCursor = context.getContentResolver().query(Uri.parse(paramString), null, null, null, null);
    if (localCursor != null)
    {   
	      int i = localCursor.getColumnIndex("icon");
	      if (i != -1 && localCursor.moveToFirst())
	  		{
		  		 byte[] arrayOfByte = localCursor.getBlob(i);
		  		if (arrayOfByte != null)
		  		{
		  			 Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
		  			 localCursor.close();
		  		     localObject1 = localBitmap;
		  		}
	  		}
    }
    return (Bitmap)localObject1;
  }

  /** @deprecated */
  public synchronized Bitmap getByBuffer(ByteBuffer paramByteBuffer)
  {
    try
    {
      logger.d("getByBuffer >>>");
      Bitmap localBitmap = BitmapFactory.decodeStream(newInputStream(paramByteBuffer));
      if (localBitmap == null)
        logger.w("getByBuffer <<< decoding bitmap failed");
      else
      {
  		logger.d((new StringBuilder()).append("getByBuffer <<< decoded bitmap w=").append(localBitmap.getWidth()).append(" h=").append(localBitmap.getHeight()).toString());
      }
      return localBitmap;
    }
    finally
    {

    }
  
  }

  public Bitmap getByBuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    Object localObject = BitmapFactory.decodeStream(newInputStream(paramByteBuffer));
    int i = ((Bitmap)localObject).getWidth();
    int j = paramInt1;
    if (i <= j)
    {
      int k = ((Bitmap)localObject).getHeight();
      int m = paramInt2;
      if (k <= m)
        return (Bitmap) localObject;
    }
    double d1 = ((Bitmap)localObject).getWidth();
    double d2 = ((Bitmap)localObject).getHeight();
    double d3 = paramInt1;
    double d4 = paramInt2;
    double d5 = d1 / d3;
    double d6 = d2 / d4;
    double d7 = 0;
    double d8 = 0;
    if (d5 > d6)
    {
      d7 = d1 / d5;
      d8 = d2 / d5;
    }
    else
    {
	  d7 = d1 / d6;
      d8 = d2 / d6;
    }
    {
      Logger localLogger = logger;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Scaling image ");
      int n = ((Bitmap)localObject).getWidth();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(n).append("x");
      int i1 = ((Bitmap)localObject).getHeight();
      String str = i1 + " downto " + d7 + "x" + d8;
      localLogger.d(str);
      int i2 = (int)d7;
      int i3 = (int)d8;
      int i4 = i2;
      int i5 = i3;
      int i6 = 1;
      Bitmap localBitmap = Bitmap.createScaledBitmap((Bitmap)localObject, i4, i5, true);
      ((Bitmap)localObject).recycle();
      localObject = localBitmap;
    }
    return  (Bitmap) localObject;
  }
	public Bitmap getByPath(String s)
	{
		logger.d((new StringBuilder()).append("getByPath: path=").append(s).toString());
		Bitmap bitmap;
		if (s.startsWith((new StringBuilder()).append("content://").append(widgetsAuthority).toString()))
			bitmap = decodeShortcutContentUri(s);
		else
		if (s.startsWith("android.resource"))
			bitmap = decodeResourceUri(context, s);
		else
		if (s.startsWith("content"))
			bitmap = decodeContentUri(s);
		else
		if (s.startsWith("@assets/"))
			bitmap = decodeAsset(s.substring("@assets/".length()));
		else
		if (s.startsWith(externalStorageRootPath))
			bitmap = decodeFromExternalStorage(s);
		else
			bitmap = decodeFile(s);
		return bitmap;
	}
//  public Bitmap getByPath(String paramString)
//  {
//    Logger localLogger = logger;
//    String str1 = "getByPath: path=" + paramString;
//    localLogger.d(str1);
//    StringBuilder localStringBuilder = new StringBuilder().append("content://");
//    String str2 = this.widgetsAuthority;
//    String str3 = str2;
//    Bitmap localBitmap;
//    if (paramString.startsWith(str3))
//      localBitmap = decodeShortcutContentUri(paramString);
//    while (true)
//    {
//      return localBitmap;
//      if (paramString.startsWith("android.resource"))
//      {
//        localBitmap = decodeResourceUri(this.context, paramString);
//        continue;
//      }
//      if (paramString.startsWith("content"))
//      {
//        localBitmap = decodeContentUri(paramString);
//        continue;
//      }
//      if (paramString.startsWith("@assets/"))
//      {
//        int i = "@assets/".length();
//        String str4 = paramString.substring(i);
//        localBitmap = decodeAsset(str4);
//        continue;
//      }
//      String str5 = this.externalStorageRootPath;
//      if (paramString.startsWith(str5))
//      {
//        localBitmap = decodeFromExternalStorage(paramString);
//        continue;
//      }
//      localBitmap = decodeFile(paramString);
//    }
//  }

  public void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    this.context = paramContext;
    String str1 = WidgetsContract.getAuthority(paramContext);
    this.widgetsAuthority = str1;
    String str2 = Environment.getExternalStorageDirectory().getPath();
    this.externalStorageRootPath = str2;
    Logger localLogger = logger;
    StringBuilder localStringBuilder = new StringBuilder().append("Created ImageAdapterAndroid: external storage root: ");
    String str3 = this.externalStorageRootPath;
    String str4 = str3;
    localLogger.d(str4);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ImageAdapterAndroid
 * JD-Core Version:    0.6.0
 */