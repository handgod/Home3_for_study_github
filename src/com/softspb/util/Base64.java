package com.softspb.util;

import java.io.UnsupportedEncodingException;

//import com.softspb.util.Base64.Encoder.Decoder;

public class Base64
{
  public static final int CRLF = 4;
  public static final int DEFAULT = 0;
  public static final int NO_CLOSE = 16;
  public static final int NO_PADDING = 1;
  public static final int NO_WRAP = 2;
  public static final int URL_SAFE = 8;
  public static final boolean $assertionsDisabled;
  static
  {
	  boolean flag;
//    if (!Base64.class.desiredAssertionStatus());
//    
//    for (int i = 1; ; i = 0)
//    {
//      $assertionsDisabled = i;
//      return;
//    }
    
    if (!Base64.class.desiredAssertionStatus())
    {
    	flag = true;
    }
	else
		flag = false;
	$assertionsDisabled = flag;
	
  }

//  public static byte[] decode(String paramString, int paramInt)
  {
//    return decode(paramString.getBytes(), paramInt);
  }

//  public static byte[] decode(byte[] paramArrayOfByte, int paramInt)
//  {
//    int i = paramArrayOfByte.length;
// //   return decode(paramArrayOfByte, 0, i, paramInt);
//  }

//  public static byte[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
//  {
//    byte[] arrayOfByte1 = new byte[paramInt2 * 3 / 4];
//    Decoder localDecoder = new Decoder(arrayOfByte1);
//    if (!localDecoder.process(paramArrayOfByte, paramInt1, paramInt2,true))
//      throw new IllegalArgumentException("bad base-64");
//    int i = localDecoder.op;
//    int j = localDecoder.output.length;
//    byte[] arrayOfByte2;
//    if (i == j)
//      arrayOfByte2 = localDecoder.output;
//    else
//    {      
//      arrayOfByte2 = new byte[localDecoder.op];
//      byte[] arrayOfByte3 = localDecoder.output;
//      int k = localDecoder.op;
//      System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 0, k);
//    }
//    return arrayOfByte2;
//  }
//
//  public static byte[] encode(byte[] paramArrayOfByte, int paramInt)
//  {
//    int i = paramArrayOfByte.length;
//    return encode(paramArrayOfByte, 0, i, paramInt);
//  }
//
//  public static byte[] encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
//  {
//    Encoder localEncoder = new Encoder(null);
//    int i = paramInt2 / 3 * 4;
//    int j;
//    if (localEncoder.do_padding)
//    {
//      if (paramInt2 % 3 > 0)
//        i += 4;
//      if ((localEncoder.do_newline) && (paramInt2 > 0))
//      {
//        j = (paramInt2 + -1) / 57 + 1;
//        if (!localEncoder.do_cr)
//          break label185;
//      }
//    }
//    label185: for (int k = 2; ; k = 1)
//    {
//      int m = k * j;
//      i += m;
//      byte[] arrayOfByte = new byte[i];
//      localEncoder.output = arrayOfByte;
//      boolean bool = localEncoder.process(paramArrayOfByte, paramInt1, paramInt2, true);
//      if (($assertionsDisabled) || (localEncoder.op == i))
//        break label191;
//      throw new AssertionError();
//      switch (paramInt2 % 3)
//      {
//      case 0:
//      default:
//        break;
//      case 1:
//        i += 2;
//        break;
//      case 2:
//        i += 3;
//        break;
//      }
//    }
//    label191: return localEncoder.output;
//  }
//
//  public static String encodeToString(byte[] paramArrayOfByte, int paramInt)
//  {
//    try
//    {
//      byte[] arrayOfByte = encode(paramArrayOfByte, paramInt);
//      String str = new String(arrayOfByte, "US-ASCII");
//      return str;
//    }
//    catch (UnsupportedEncodingException localUnsupportedEncodingException)
//    {
//    }
// 
//  }
//
//  public static String encodeToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
//  {
//	  String str = null;
//    try
//    {
//      byte[] arrayOfByte = encode(paramArrayOfByte, paramInt1, paramInt2, paramInt3);
//      str = new String(arrayOfByte, "US-ASCII");
//      return str;
//    }
//    catch (UnsupportedEncodingException localUnsupportedEncodingException)
//    {
//    	return str;
//    }
//  
//  }
//
//  static class Encoder extends Base64.Coder
//  {
//    private static final byte[] ENCODE  ;
//    private static final byte[] ENCODE_WEBSAFE ;
//    public static final int LINE_GROUPS = 19;
//    private final byte[] alphabet;
//    private int count;
//    public final boolean do_cr;
//    public final boolean do_newline;
//    public final boolean do_padding;
//    private final byte[] tail;
//    int tailLen;
//
//    static
//    {
//      if (Base64.class.desiredAssertionStatus())
//      {
//        $assertionsDisabled = true;
//        ENCODE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
//        ENCODE_WEBSAFE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
//        return;
//      }
//    }
//
//    public Encoder(byte[] arg2)
//    {
//      Object localObject;
//      this.output = (byte[]) localObject;
//      int this$1;
//	if ((this$1 & 0x1) == 0)
//      {
//        int j = 1;
//        this.do_padding = true;
//        if ((this$1 & 0x2) != 0)
//          break label114;
//        j = 1;
//        label38: this.do_newline = true;
//        if ((this$1 & 0x4) == 0)
//          break label121;
////        label50: this.do_cr = i;
//        if ((this$1 & 0x8) != 0)
//          break label126;
//        byte[] arrayOfByte1 = ENCODE;
//        label67: this.alphabet = arrayOfByte1;
//        byte[] arrayOfByte3 = new byte[2];
//        this.tail = arrayOfByte3;
//        this.tailLen = 0;
//        if (!this.do_newline)
//          break label134;
//      }
//		int m;
//		for (int k = 19; ; m = -1)
//	      {
//	        this.count = k;
//	        return;
//	        k = 0;
//	        break;
//	        k = 0;
//	        break ;
//
//	        byte[] arrayOfByte2 = ENCODE_WEBSAFE;
//	        break ;
//	      }
//    }
//
//    public int maxOutputSize(int paramInt)
//    {
//      return paramInt * 8 / 5 + 10;
//    }
//
//    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
//    {
////      byte[] arrayOfByte1 = this.alphabet;
////      byte[] arrayOfByte2 = this.output;
////      int i = 0;
////      int j = this.count;
////      int k = paramInt1;
////      paramInt2 += paramInt1;
////      int m = -1;
////      int i12;
////      int i13;
////      switch (this.tailLen)
////      {
////      case 0:
////      default:
////        if (m == -1)
////          break;
////        int n = 0 + 1;
////        int i1 = m >> 18 & 0x3F;
////        int i2 = arrayOfByte1[i1];
////        arrayOfByte2[0] = i2;
////        int i3 = n + 1;
////        int i4 = m >> 12 & 0x3F;
////        int i5 = arrayOfByte1[i4];
////        arrayOfByte2[n] = i5;
////        int i6 = i3 + 1;
////        int i7 = m >> 6 & 0x3F;
////        int i8 = arrayOfByte1[i7];
////        arrayOfByte2[i3] = i8;
////        i = i6 + 1;
////        int i9 = m & 0x3F;
////        int i10 = arrayOfByte1[i9];
////        arrayOfByte2[i6] = i10;
////        j += -1;
////        if (j != 0)
////          break;
////        if (this.do_cr)
////        {
////          int i11 = i + 1;
////          arrayOfByte2[i] = 13;
////          i = i11;
////        }
////        i12 = i + 1;
////        arrayOfByte2[i] = 10;
////        j = 19;
////        i13 = k;
////      case 1:
////      case 2:
////      }
////      while (true)
////      {
////        int i14 = i13 + 3;
////        int i15 = paramInt2;
////        if (i14 <= i15)
////        {
////          int i16 = (paramArrayOfByte[i13] & 0xFF) << 16;
////          int i17 = i13 + 1;
////          int i18 = (paramArrayOfByte[i17] & 0xFF) << 8;
////          int i19 = i16 | i18;
////          int i20 = i13 + 2;
////          int i21 = paramArrayOfByte[i20] & 0xFF;
////          int i22 = i19 | i21;
////          int i23 = i22 >> 18 & 0x3F;
////          int i24 = arrayOfByte1[i23];
////          arrayOfByte2[i12] = i24;
////          int i25 = i12 + 1;
////          int i26 = i22 >> 12 & 0x3F;
////          int i27 = arrayOfByte1[i26];
////          arrayOfByte2[i25] = i27;
////          int i28 = i12 + 2;
////          int i29 = i22 >> 6 & 0x3F;
////          int i30 = arrayOfByte1[i29];
////          arrayOfByte2[i28] = i30;
////          int i31 = i12 + 3;
////          int i32 = i22 & 0x3F;
////          int i33 = arrayOfByte1[i32];
////          arrayOfByte2[i31] = i33;
////          k = i13 + 3;
////          i = i12 + 4;
////          j += -1;
////          if (j == 0)
////          {
////            if (this.do_cr)
////            {
////              int i34 = i + 1;
////              arrayOfByte2[i] = 13;
////              i = i34;
////            }
////            i12 = i + 1;
////            arrayOfByte2[i] = 10;
////            j = 19;
////            i13 = k;
////            continue;
////            int i35 = k + 2;
////            int i36 = paramInt2;
////            if (i35 > i36)
////              break;
////            int i37 = (this.tail[0] & 0xFF) << 16;
////            int i38 = k + 1;
////            int i39 = (paramArrayOfByte[k] & 0xFF) << 8;
////            int i40 = i37 | i39;
////            k = i38 + 1;
////            int i41 = paramArrayOfByte[i38] & 0xFF;
////            m = i40 | i41;
////            this.tailLen = 0;
////            break;
////            int i42 = k + 1;
////            int i43 = paramInt2;
////            if (i42 > i43)
////              break;
////            int i44 = (this.tail[0] & 0xFF) << 16;
////            int i45 = (this.tail[1] & 0xFF) << 8;
////            int i46 = i44 | i45;
////            int i47 = k + 1;
////            int i48 = paramArrayOfByte[k] & 0xFF;
////            m = i46 | i48;
////            this.tailLen = 0;
////            k = i47;
////            break;
////          }
////        }
////        else
////        {
////          if (paramBoolean)
////          {
////            int i49 = this.tailLen;
////            int i50 = i13 - i49;
////            int i51 = paramInt2 + -1;
////            int i52;
////            int i54;
////            if (i50 == i51)
////            {
////              i52 = 0;
////              if (this.tailLen > 0)
////              {
////                byte[] arrayOfByte3 = this.tail;
////                int i53 = i52 + 1;
////                i54 = arrayOfByte3[i52];
////                i52 = i53;
////                k = i13;
////              }
////              while (true)
////              {
////                int i55 = (i54 & 0xFF) << 4;
////                int i56 = this.tailLen - i52;
////                this.tailLen = i56;
////                int i57 = i12 + 1;
////                int i58 = i55 >> 6 & 0x3F;
////                int i59 = arrayOfByte1[i58];
////                arrayOfByte2[i12] = i59;
////                i12 = i57 + 1;
////                int i60 = i55 & 0x3F;
////                int i61 = arrayOfByte1[i60];
////                arrayOfByte2[i57] = i61;
////                if (this.do_padding)
////                {
////                  int i62 = i12 + 1;
////                  arrayOfByte2[i12] = 61;
////                  i12 = i62 + 1;
////                  arrayOfByte2[i62] = 61;
////                }
////                i = i12;
////                if (this.do_newline)
////                {
////                  if (this.do_cr)
////                  {
////                    int i63 = i + 1;
////                    arrayOfByte2[i] = 13;
////                    i = i63;
////                  }
////                  i12 = i + 1;
////                  arrayOfByte2[i] = 10;
////                  i = i12;
////                }
////                label915: if (($assertionsDisabled) || (this.tailLen == 0))
////                  break;
////                throw new AssertionError();
////                k = i13 + 1;
////                i54 = paramArrayOfByte[i13];
////              }
////            }
////            int i64 = this.tailLen;
////            int i65 = i13 - i64;
////            int i66 = paramInt2 + -2;
////            if (i65 == i66)
////            {
////              i52 = 0;
////              label1015: int i68;
////              if (this.tailLen > 1)
////              {
////                byte[] arrayOfByte4 = this.tail;
////                int i67 = i52 + 1;
////                i54 = arrayOfByte4[i52];
////                i52 = i67;
////                k = i13;
////                i68 = (i54 & 0xFF) << 10;
////                if (this.tailLen <= 0)
////                  break label1261;
////                byte[] arrayOfByte5 = this.tail;
////                int i69 = i52 + 1;
////                i54 = arrayOfByte5[i52];
////                i52 = i69;
////              }
////              while (true)
////              {
////                int i70 = (i54 & 0xFF) << 2;
////                int i71 = i68 | i70;
////                int i72 = this.tailLen - i52;
////                this.tailLen = i72;
////                int i73 = i12 + 1;
////                int i74 = i71 >> 12 & 0x3F;
////                int i75 = arrayOfByte1[i74];
////                arrayOfByte2[i12] = i75;
////                int i76 = i73 + 1;
////                int i77 = i71 >> 6 & 0x3F;
////                int i78 = arrayOfByte1[i77];
////                arrayOfByte2[i73] = i78;
////                i = i76 + 1;
////                int i79 = i71 & 0x3F;
////                int i80 = arrayOfByte1[i79];
////                arrayOfByte2[i76] = i80;
////                if (this.do_padding)
////                {
////                  int i81 = i + 1;
////                  arrayOfByte2[i] = 61;
////                  i = i81;
////                }
////                if (!this.do_newline)
////                  break label915;
////                if (this.do_cr)
////                {
////                  int i82 = i + 1;
////                  arrayOfByte2[i] = 13;
////                  i = i82;
////                }
////                i12 = i + 1;
////                arrayOfByte2[i] = 10;
////                break;
////                k = i13 + 1;
////                i54 = paramArrayOfByte[i13];
////                break label1015;
////                label1261: int i83 = k + 1;
////                i54 = paramArrayOfByte[k];
////                k = i83;
////              }
////            }
////            if ((this.do_newline) && (i12 > 0) && (j != 19))
////            {
////              if (!this.do_cr)
////                break label1543;
////              i = i12 + 1;
////              arrayOfByte2[i12] = 13;
////            }
////          }
////          while (true)
////          {
////            i12 = i + 1;
////            arrayOfByte2[i] = 10;
////            k = i13;
////            i = i12;
////            break;
////            if (!$assertionsDisabled)
////            {
////              int i84 = paramInt2;
////              if (k != i84)
////              {
////                throw new AssertionError();
////                int i85 = paramInt2 + -1;
////                if (i13 != i85)
////                  break label1439;
////                byte[] arrayOfByte6 = this.tail;
////                int i86 = this.tailLen;
////                int i87 = i86 + 1;
////                this.tailLen = i87;
////                int i88 = paramArrayOfByte[i13];
////                arrayOfByte6[i86] = i88;
////                int i89 = i13;
////              }
////            }
////            for (i = i12; ; i = i12)
////            {
////              this.op = i;
////              this.count = j;
////              return true;
////              label1439: int i90 = paramInt2 + -2;
////              if (i13 == i90)
////              {
////                byte[] arrayOfByte7 = this.tail;
////                int i91 = this.tailLen;
////                int i92 = i91 + 1;
////                this.tailLen = i92;
////                int i93 = paramArrayOfByte[i13];
////                arrayOfByte7[i91] = i93;
////                byte[] arrayOfByte8 = this.tail;
////                int i94 = this.tailLen;
////                int i95 = i94 + 1;
////                this.tailLen = i95;
////                int i96 = i13 + 1;
////                int i97 = paramArrayOfByte[i96];
////                arrayOfByte8[i94] = i97;
////              }
////              int i98 = i13;
////            }
////            label1543: i = i12;
////          }
////        }
////        i13 = k;
////        i12 = i;
////      }
////    }
//  }
//
// static abstract class Decoder extends Base64.Coder
//  {
//    private static final int[] DECODE  ;
//    private static final int[] DECODE_WEBSAFE  ;
//    private static final int EQUALS = 254;
//    private static final int SKIP = 255;
//    private final int[] alphabet;
//    private int state;
//    private int value;
//
//    public Decoder(byte[] arg2)
//    {
////      Object localObject;
////      this.output = localObject;
////      if ((this$1 & 0x8) == 0);
////      for (int[] arrayOfInt = DECODE; ; arrayOfInt = DECODE_WEBSAFE)
////      {
////        this.alphabet = arrayOfInt;
////        this.state = 0;
////        this.value = 0;
////        return;
//      }
//    }
//
////    public int maxOutputSize(int paramInt)
////    {
////      return paramInt * 3 / 4 + 10;
////    }
//
////    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
////    {
////    	return false;
//////      int i;
//////      if (this.state == 6)
//////      {
//////        i = 0;
//////        return i;
//////      }
//////      int j = paramInt1;
//////      paramInt2 += paramInt1;
//////      int k = this.state;
//////      int m = this.value;
//////      int n = 0;
//////      byte[] arrayOfByte = this.output;
//////      int[] arrayOfInt = this.alphabet;
//////      label49: if (j < paramInt2)
//////        if (k == 0)
//////        {
//////          while (j + 4 <= paramInt2)
//////          {
//////            int i1 = paramArrayOfByte[j] & 0xFF;
//////            int i2 = arrayOfInt[i1] << 18;
//////            int i3 = j + 1;
//////            int i4 = paramArrayOfByte[i3] & 0xFF;
//////            int i5 = arrayOfInt[i4] << 12;
//////            int i6 = i2 | i5;
//////            int i7 = j + 2;
//////            int i8 = paramArrayOfByte[i7] & 0xFF;
//////            int i9 = arrayOfInt[i8] << 6;
//////            int i10 = i6 | i9;
//////            int i11 = j + 3;
//////            int i12 = paramArrayOfByte[i11] & 0xFF;
//////            int i13 = arrayOfInt[i12];
//////            m = i10 | i13;
//////            if (m < 0)
//////              break;
//////            int i14 = n + 2;
//////            int i15 = (byte)m;
//////            arrayOfByte[i14] = i15;
//////            int i16 = n + 1;
//////            int i17 = (byte)(m >> 8);
//////            arrayOfByte[i16] = i17;
//////            int i18 = (byte)(m >> 16);
//////            arrayOfByte[n] = i18;
//////            n += 3;
//////            j += 4;
//////          }
//////          if (j < paramInt2);
//////        }
//////      for (int i19 = n; ; i19 = n)
//////      {
//////        if (!paramBoolean)
//////        {
//////          this.state = k;
//////          this.value = m;
//////          this.op = i19;
//////          i = 1;
//////          break;
//////          int i20 = j + 1;
//////          int i21 = paramArrayOfByte[j] & 0xFF;
//////          int i22 = arrayOfInt[i21];
//////          switch (k)
//////          {
//////          default:
//////          case 0:
//////          case 1:
//////          case 2:
//////          case 3:
//////          case 4:
//////          case 5:
//////          }
//////          label697: 
//////          do
//////          {
//////            do
//////              while (true)
//////              {
//////                j = i20;
//////                break label49;
//////                if (i22 >= 0)
//////                {
//////                  m = i22;
//////                  k += 1;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                  continue;
//////                this.state = 6;
//////                i = 0;
//////                break;
//////                if (i22 >= 0)
//////                {
//////                  m = m << 6 | i22;
//////                  k += 1;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                  continue;
//////                this.state = 6;
//////                i = 0;
//////                break;
//////                if (i22 >= 0)
//////                {
//////                  m = m << 6 | i22;
//////                  k += 1;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                {
//////                  int i23 = n + 1;
//////                  int i24 = (byte)(m >> 4);
//////                  arrayOfByte[n] = i24;
//////                  k = 4;
//////                  n = i23;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                  continue;
//////                this.state = 6;
//////                i = 0;
//////                break;
//////                if (i22 >= 0)
//////                {
//////                  m = m << 6 | i22;
//////                  int i25 = n + 2;
//////                  int i26 = (byte)m;
//////                  arrayOfByte[i25] = i26;
//////                  int i27 = n + 1;
//////                  int i28 = (byte)(m >> 8);
//////                  arrayOfByte[i27] = i28;
//////                  int i29 = (byte)(m >> 16);
//////                  arrayOfByte[n] = i29;
//////                  n += 3;
//////                  k = 0;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                {
//////                  int i30 = n + 1;
//////                  int i31 = (byte)(m >> 2);
//////                  arrayOfByte[i30] = i31;
//////                  int i32 = (byte)(m >> 10);
//////                  arrayOfByte[n] = i32;
//////                  n += 2;
//////                  k = 5;
//////                  continue;
//////                }
//////                if (i22 == -1)
//////                  continue;
//////                this.state = 6;
//////                i = 0;
//////                break;
//////                if (i22 != -1)
//////                  break label697;
//////                k += 1;
//////              }
//////            while (i22 == -1);
//////            this.state = 6;
//////            i = 0;
//////            break;
//////          }
//////          while (i22 == -1);
//////          this.state = 6;
//////          i = 0;
//////          break;
//////        }
//////        switch (k)
//////        {
//////        default:
//////          n = i19;
//////        case 0:
//////        case 1:
//////        case 2:
//////        case 3:
//////          while (true)
//////          {
//////            this.state = k;
//////            this.op = n;
//////            i = 1;
//////            break;
//////            n = i19;
//////            continue;
//////            this.state = 6;
//////            i = 0;
//////            break;
//////            n = i19 + 1;
//////            int i33 = (byte)(m >> 4);
//////            arrayOfByte[i19] = i33;
//////            continue;
//////            int i34 = i19 + 1;
//////            int i35 = (byte)(m >> 10);
//////            arrayOfByte[i19] = i35;
//////            int i36 = i34 + 1;
//////            int i37 = (byte)(m >> 2);
//////            arrayOfByte[i34] = i37;
//////            n = i36;
//////          }
//////        case 4:
//////        }
//////        this.state = 6;
//////        i = 0;
//////        break;
//////      }
////    	return false;
////   }
//  }
//
//  abstract class Coder
//  {
//    public int op;
//    public byte[] output;
//
//    public abstract int maxOutputSize(int paramInt);
//
//    public abstract boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean);
//  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.util.Base64
 * JD-Core Version:    0.6.0
 */