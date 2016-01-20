// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import com.android.vending.licensing.util.Base64;
import com.android.vending.licensing.util.Base64DecoderException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.*;
import javax.crypto.spec.*;

// Referenced classes of package com.android.vending.licensing:
//			Obfuscator, ValidationException

public class AESObfuscator
	implements Obfuscator
{

	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final byte IV[];
	private static final String KEYGEN_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
	private static final String UTF8 = "UTF-8";
	private static final String header = "com.android.vending.licensing.AESObfuscator-1|";
	private Cipher mDecryptor;
	private Cipher mEncryptor;

	public AESObfuscator(byte abyte0[], String s, String s1)
	{
		try
		{
			SecretKeySpec secretkeyspec = new SecretKeySpec(SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC").generateSecret(new PBEKeySpec((new StringBuilder()).append(s).append(s1).toString().toCharArray(), abyte0, 1024, 256)).getEncoded(), "AES");
			mEncryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			mEncryptor.init(1, secretkeyspec, new IvParameterSpec(IV));
			mDecryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			mDecryptor.init(2, secretkeyspec, new IvParameterSpec(IV));
			return;
		}
		catch (GeneralSecurityException generalsecurityexception)
		{
			throw new RuntimeException("Invalid environment", generalsecurityexception);
		}
	}

	public String obfuscate(String s)
	{
		String s2;
		if (s == null)
		{
			s2 = null;
		} else
		{
			String s1;
			try
			{
				s1 = Base64.encode(mEncryptor.doFinal((new StringBuilder()).append("com.android.vending.licensing.AESObfuscator-1|").append(s).toString().getBytes("UTF-8")));
			}
			catch (UnsupportedEncodingException unsupportedencodingexception)
			{
				throw new RuntimeException("Invalid environment", unsupportedencodingexception);
			}
			catch (GeneralSecurityException generalsecurityexception)
			{
				throw new RuntimeException("Invalid environment", generalsecurityexception);
			}
			s2 = s1;
		}
		return s2;
	}

	public String unobfuscate(String s)
		throws ValidationException
	{
		String s3 = null;
		if (s != null)
		{
			String s1;
			String s2;
			try
			{
				s1 = new String(mDecryptor.doFinal(Base64.decode(s)), "UTF-8");
				if (s1.indexOf("com.android.vending.licensing.AESObfuscator-1|") != 0)
					throw new ValidationException((new StringBuilder()).append("Header not found (invalid data or key):").append(s).toString());
			}
			catch (Base64DecoderException base64decoderexception)
			{
				throw new ValidationException((new StringBuilder()).append(base64decoderexception.getMessage()).append(":").append(s).toString());
			}
			catch (IllegalBlockSizeException illegalblocksizeexception)
			{
				throw new ValidationException((new StringBuilder()).append(illegalblocksizeexception.getMessage()).append(":").append(s).toString());
			}
			catch (BadPaddingException badpaddingexception)
			{
				throw new ValidationException((new StringBuilder()).append(badpaddingexception.getMessage()).append(":").append(s).toString());
			}
			catch (UnsupportedEncodingException unsupportedencodingexception)
			{
				throw new RuntimeException("Invalid environment", unsupportedencodingexception);
			}
			s2 = s1.substring("com.android.vending.licensing.AESObfuscator-1|".length(), s1.length());
			s3 = s2;
	
		}
		return s3;
	}

	static 
	{
		byte abyte0[] = new byte[16];
		abyte0[0] = 16;
		abyte0[1] = 74;
		abyte0[2] = 71;
		abyte0[3] = -80;
		abyte0[4] = 32;
		abyte0[5] = 101;
		abyte0[6] = -47;
		abyte0[7] = 72;
		abyte0[8] = 117;
		abyte0[9] = -14;
		abyte0[10] = 0;
		abyte0[11] = -29;
		abyte0[12] = 70;
		abyte0[13] = 65;
		abyte0[14] = -12;
		abyte0[15] = 74;
		IV = abyte0;
	}
}
