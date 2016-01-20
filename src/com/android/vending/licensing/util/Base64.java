// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing.util;


// Referenced classes of package com.android.vending.licensing.util:
//			Base64DecoderException

public class Base64
{

	static  boolean $assertionsDisabled = false;
	private static final byte ALPHABET[];
	private static final byte DECODABET[];
	public static final boolean DECODE = false;
	public static final boolean ENCODE = true;
	private static final byte EQUALS_SIGN = 61;
	private static final byte EQUALS_SIGN_ENC = -1;
	private static final byte NEW_LINE = 10;
	private static final byte WEBSAFE_ALPHABET[];
	private static final byte WEBSAFE_DECODABET[];
	private static final byte WHITE_SPACE_ENC = -5;

	private Base64()
	{
	}

	public static byte[] decode(String s)
		throws Base64DecoderException
	{
		byte abyte0[] = s.getBytes();
		return decode(abyte0, 0, abyte0.length);
	}

	public static byte[] decode(byte abyte0[])
		throws Base64DecoderException
	{
		return decode(abyte0, 0, abyte0.length);
	}

	public static byte[] decode(byte abyte0[], int i, int j)
		throws Base64DecoderException
	{
		return decode(abyte0, i, j, DECODABET);
	}
	public static byte[] decode(byte[] source, int off, int len, byte[] decodabet) 
			throws Base64DecoderException 
	{    
		int len34 = len * 3 / 4;  
		byte[] outBuff = new byte[2 + len34]; // Upper limit on size of output  
		int outBuffPosn = 0;  
		byte[] b4 = new byte[4];    
		int b4Posn = 0;  
		int i = 0;   
		byte sbiCrop = 0; 
		byte sbiDecode = 0;  
		
		for (i = 0; i < len; i++) {  
			sbiCrop = (byte) (source[i + off] & 0x7f); // Only the low seven bits   
			sbiDecode = decodabet[sbiCrop];   
			if (sbiDecode >= WHITE_SPACE_ENC)
			{ // White space Equals sign or better   
				if (sbiDecode >= EQUALS_SIGN_ENC) {
					// An equals sign (for padding) must not occur at position 0 or 1   
					// and must be the last byte[s] in the encoded value    
					if (sbiCrop == EQUALS_SIGN) {    
						int bytesLeft = len - i;    
						byte lastByte = (byte) (source[len - 1 + off] & 0x7f);   
						if (b4Posn == 0 || b4Posn == 1) {   
							throw new Base64DecoderException("invalid padding byte '=' at byte offset " + i);  
							} 
						else if ((b4Posn == 3 && bytesLeft > 2)|| (b4Posn == 4 && bytesLeft > 1)) {
								throw new Base64DecoderException("padding byte '=' falsely signals end of encoded value "+ "at offset " + i);   
								}
						else if (lastByte != EQUALS_SIGN && lastByte != NEW_LINE) {  
									throw new Base64DecoderException("encoded value has invalid trailing byte"); 
							}           
						break;    
						}     
					b4[b4Posn++] = sbiCrop;  
					if (b4Posn == 4) {   
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);   
						b4Posn = 0;   
						}      
					}     
				}
			else {     
				throw new Base64DecoderException("Bad Base64 input character at " + i + ": " + source[i + off] + "(decimal)");   
			}    
		}  // Because web safe encoding allows non padding base64 encodes, we    
		// need to pad the rest of the b4 buffer with equal signs when   
		// b4Posn != 0.  There can be at most 2 equal signs at the end of      
		// four characters, so the b4 buffer must have two or three 
		// characters.  This also catches the case where the input is     
		// padded with EQUALS_SIGN     
		if (b4Posn != 0) {  
			if (b4Posn == 1) {   
				throw new Base64DecoderException("single trailing character at offset " + (len - 1)); 
				}    
			b4[b4Posn++] = EQUALS_SIGN;    
			outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);   
			}       
		byte[] out = new byte[outBuffPosn];    
		System.arraycopy(outBuff, 0, out, 0, outBuffPosn);   
		return out;   
	}   
			
	private static int decode4to3(byte abyte0[], int i, byte abyte1[], int j, byte abyte2[])
	{
		int l;
		if (abyte0[i + 2] == 61)
		{
			abyte1[j] = (byte)(((abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12) >>> 16);
			l = 1;
		} else
		if (abyte0[i + 3] == 61)
		{
			int i1 = (abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12 | (abyte2[abyte0[i + 2]] << 24) >>> 18;
			abyte1[j] = (byte)(i1 >>> 16);
			abyte1[j + 1] = (byte)(i1 >>> 8);
			l = 2;
		} else
		{
			int k = (abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12 | (abyte2[abyte0[i + 2]] << 24) >>> 18 | (abyte2[abyte0[i + 3]] << 24) >>> 24;
			abyte1[j] = (byte)(k >> 16);
			abyte1[j + 1] = (byte)(k >> 8);
			abyte1[j + 2] = (byte)k;
			l = 3;
		}
		return l;
	}

	public static byte[] decodeWebSafe(String s)
		throws Base64DecoderException
	{
		byte abyte0[] = s.getBytes();
		return decodeWebSafe(abyte0, 0, abyte0.length);
	}

	public static byte[] decodeWebSafe(byte abyte0[])
		throws Base64DecoderException
	{
		return decodeWebSafe(abyte0, 0, abyte0.length);
	}

	public static byte[] decodeWebSafe(byte abyte0[], int i, int j)
		throws Base64DecoderException
	{
		return decode(abyte0, i, j, WEBSAFE_DECODABET);
	}

	public static String encode(byte abyte0[])
	{
		return encode(abyte0, 0, abyte0.length, ALPHABET, true);
	}

	public static String encode(byte abyte0[], int i, int j, byte abyte1[], boolean flag)
	{
		byte abyte2[] = encode(abyte0, i, j, abyte1, 0x7fffffff);
		int k = abyte2.length;
		do
		{
			if (flag || k <= 0 || abyte2[k - 1] != 61)
				return new String(abyte2, 0, k);
			k--;
		} while (true);
	}

	public static byte[] encode(byte abyte0[], int i, int j, byte abyte1[], int k)
	{
		int l = 4 * ((j + 2) / 3);
		byte abyte2[] = new byte[l + l / k];
		int i1 = 0;
		int j1 = 0;
		int k1 = j - 2;
		int l1 = 0;
		while (i1 < k1) 
		{
			int i2 = (abyte0[i1 + i] << 24) >>> 8 | (abyte0[i + (i1 + 1)] << 24) >>> 16 | (abyte0[i + (i1 + 2)] << 24) >>> 24;
			abyte2[j1] = abyte1[i2 >>> 18];
			abyte2[j1 + 1] = abyte1[0x3f & i2 >>> 12];
			abyte2[j1 + 2] = abyte1[0x3f & i2 >>> 6];
			abyte2[j1 + 3] = abyte1[i2 & 0x3f];
			if ((l1 += 4) == k)
			{
				abyte2[j1 + 4] = 10;
				j1++;
				l1 = 0;
			}
			i1 += 3;
			j1 += 4;
		}
		if (i1 < j)
		{
			encode3to4(abyte0, i1 + i, j - i1, abyte2, j1, abyte1);
			if (l1 + 4 == k)
			{
				abyte2[j1 + 4] = 10;
				j1++;
			}
			j1 += 4;
		}
		if (!$assertionsDisabled && j1 != abyte2.length)
			throw new AssertionError();
		else
			return abyte2;
	}
	private static byte[] encode3to4(byte[] source, int srcOffset,int numSigBytes, byte[] destination, int destOffset, byte[] alphabet)
	{     
		//           1         2         3      
		// 01234567890123456789012345678901 Bit position    
		// --------000000001111111122222222 Array position from threeBytes      
		// --------|    ||    ||    ||    | Six bit groups to index alphabet      
		//          >>18  >>12  >> 6  >> 0  Right shift necessary     
		//                0x3f  0x3f  0x3f  Additional AND       
		// Create buffer with zero-padding if there are only one or two    
		// significant bytes passed in the array.      
		// We have to shift left 24 in order to flush out the 1's that appear  
		// when Java treats a value as negative that is cast from a byte to an int.    
		int inBuff = 
				(numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
				| (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
				| (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0); 
		switch (numSigBytes) {
		case 3:    
			destination[destOffset] = alphabet[(inBuff >>> 18)]; 
			destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 0x3f];  
			destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 0x3f];   
			destination[destOffset + 3] = alphabet[(inBuff) & 0x3f]; 
			return destination;   
		case 2:    
			destination[destOffset] = alphabet[(inBuff >>> 18)]; 
			destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 0x3f];  
			destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 0x3f];   
			destination[destOffset + 3] = EQUALS_SIGN;       
			return destination;   
		case 1:     
			destination[destOffset] = alphabet[(inBuff >>> 18)];   
			destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 0x3f];  
			destination[destOffset + 2] = EQUALS_SIGN;
			destination[destOffset + 3] = EQUALS_SIGN;    
			return destination;   
		default:   
			return destination;   
			} // end switch   
		} // end encode3to4   

	public static String encodeWebSafe(byte abyte0[], boolean flag)
	{
		return encode(abyte0, 0, abyte0.length, WEBSAFE_ALPHABET, flag);
	}

	static 
	{
		boolean flag;
		byte abyte0[];
		byte abyte1[];
		byte abyte2[];
		byte abyte3[];
		if (!com.android.vending.licensing.util.Base64.class.desiredAssertionStatus())
			flag = true;
		else
			flag = false;
		$assertionsDisabled = flag;
		abyte0 = new byte[64];
		abyte0[0] = 65;
		abyte0[1] = 66;
		abyte0[2] = 67;
		abyte0[3] = 68;
		abyte0[4] = 69;
		abyte0[5] = 70;
		abyte0[6] = 71;
		abyte0[7] = 72;
		abyte0[8] = 73;
		abyte0[9] = 74;
		abyte0[10] = 75;
		abyte0[11] = 76;
		abyte0[12] = 77;
		abyte0[13] = 78;
		abyte0[14] = 79;
		abyte0[15] = 80;
		abyte0[16] = 81;
		abyte0[17] = 82;
		abyte0[18] = 83;
		abyte0[19] = 84;
		abyte0[20] = 85;
		abyte0[21] = 86;
		abyte0[22] = 87;
		abyte0[23] = 88;
		abyte0[24] = 89;
		abyte0[25] = 90;
		abyte0[26] = 97;
		abyte0[27] = 98;
		abyte0[28] = 99;
		abyte0[29] = 100;
		abyte0[30] = 101;
		abyte0[31] = 102;
		abyte0[32] = 103;
		abyte0[33] = 104;
		abyte0[34] = 105;
		abyte0[35] = 106;
		abyte0[36] = 107;
		abyte0[37] = 108;
		abyte0[38] = 109;
		abyte0[39] = 110;
		abyte0[40] = 111;
		abyte0[41] = 112;
		abyte0[42] = 113;
		abyte0[43] = 114;
		abyte0[44] = 115;
		abyte0[45] = 116;
		abyte0[46] = 117;
		abyte0[47] = 118;
		abyte0[48] = 119;
		abyte0[49] = 120;
		abyte0[50] = 121;
		abyte0[51] = 122;
		abyte0[52] = 48;
		abyte0[53] = 49;
		abyte0[54] = 50;
		abyte0[55] = 51;
		abyte0[56] = 52;
		abyte0[57] = 53;
		abyte0[58] = 54;
		abyte0[59] = 55;
		abyte0[60] = 56;
		abyte0[61] = 57;
		abyte0[62] = 43;
		abyte0[63] = 47;
		ALPHABET = abyte0;
		abyte1 = new byte[64];
		abyte1[0] = 65;
		abyte1[1] = 66;
		abyte1[2] = 67;
		abyte1[3] = 68;
		abyte1[4] = 69;
		abyte1[5] = 70;
		abyte1[6] = 71;
		abyte1[7] = 72;
		abyte1[8] = 73;
		abyte1[9] = 74;
		abyte1[10] = 75;
		abyte1[11] = 76;
		abyte1[12] = 77;
		abyte1[13] = 78;
		abyte1[14] = 79;
		abyte1[15] = 80;
		abyte1[16] = 81;
		abyte1[17] = 82;
		abyte1[18] = 83;
		abyte1[19] = 84;
		abyte1[20] = 85;
		abyte1[21] = 86;
		abyte1[22] = 87;
		abyte1[23] = 88;
		abyte1[24] = 89;
		abyte1[25] = 90;
		abyte1[26] = 97;
		abyte1[27] = 98;
		abyte1[28] = 99;
		abyte1[29] = 100;
		abyte1[30] = 101;
		abyte1[31] = 102;
		abyte1[32] = 103;
		abyte1[33] = 104;
		abyte1[34] = 105;
		abyte1[35] = 106;
		abyte1[36] = 107;
		abyte1[37] = 108;
		abyte1[38] = 109;
		abyte1[39] = 110;
		abyte1[40] = 111;
		abyte1[41] = 112;
		abyte1[42] = 113;
		abyte1[43] = 114;
		abyte1[44] = 115;
		abyte1[45] = 116;
		abyte1[46] = 117;
		abyte1[47] = 118;
		abyte1[48] = 119;
		abyte1[49] = 120;
		abyte1[50] = 121;
		abyte1[51] = 122;
		abyte1[52] = 48;
		abyte1[53] = 49;
		abyte1[54] = 50;
		abyte1[55] = 51;
		abyte1[56] = 52;
		abyte1[57] = 53;
		abyte1[58] = 54;
		abyte1[59] = 55;
		abyte1[60] = 56;
		abyte1[61] = 57;
		abyte1[62] = 45;
		abyte1[63] = 95;
		WEBSAFE_ALPHABET = abyte1;
		abyte2 = new byte[128];
		abyte2[0] = -9;
		abyte2[1] = -9;
		abyte2[2] = -9;
		abyte2[3] = -9;
		abyte2[4] = -9;
		abyte2[5] = -9;
		abyte2[6] = -9;
		abyte2[7] = -9;
		abyte2[8] = -9;
		abyte2[9] = -5;
		abyte2[10] = -5;
		abyte2[11] = -9;
		abyte2[12] = -9;
		abyte2[13] = -5;
		abyte2[14] = -9;
		abyte2[15] = -9;
		abyte2[16] = -9;
		abyte2[17] = -9;
		abyte2[18] = -9;
		abyte2[19] = -9;
		abyte2[20] = -9;
		abyte2[21] = -9;
		abyte2[22] = -9;
		abyte2[23] = -9;
		abyte2[24] = -9;
		abyte2[25] = -9;
		abyte2[26] = -9;
		abyte2[27] = -9;
		abyte2[28] = -9;
		abyte2[29] = -9;
		abyte2[30] = -9;
		abyte2[31] = -9;
		abyte2[32] = -5;
		abyte2[33] = -9;
		abyte2[34] = -9;
		abyte2[35] = -9;
		abyte2[36] = -9;
		abyte2[37] = -9;
		abyte2[38] = -9;
		abyte2[39] = -9;
		abyte2[40] = -9;
		abyte2[41] = -9;
		abyte2[42] = -9;
		abyte2[43] = 62;
		abyte2[44] = -9;
		abyte2[45] = -9;
		abyte2[46] = -9;
		abyte2[47] = 63;
		abyte2[48] = 52;
		abyte2[49] = 53;
		abyte2[50] = 54;
		abyte2[51] = 55;
		abyte2[52] = 56;
		abyte2[53] = 57;
		abyte2[54] = 58;
		abyte2[55] = 59;
		abyte2[56] = 60;
		abyte2[57] = 61;
		abyte2[58] = -9;
		abyte2[59] = -9;
		abyte2[60] = -9;
		abyte2[61] = -1;
		abyte2[62] = -9;
		abyte2[63] = -9;
		abyte2[64] = -9;
		abyte2[65] = 0;
		abyte2[66] = 1;
		abyte2[67] = 2;
		abyte2[68] = 3;
		abyte2[69] = 4;
		abyte2[70] = 5;
		abyte2[71] = 6;
		abyte2[72] = 7;
		abyte2[73] = 8;
		abyte2[74] = 9;
		abyte2[75] = 10;
		abyte2[76] = 11;
		abyte2[77] = 12;
		abyte2[78] = 13;
		abyte2[79] = 14;
		abyte2[80] = 15;
		abyte2[81] = 16;
		abyte2[82] = 17;
		abyte2[83] = 18;
		abyte2[84] = 19;
		abyte2[85] = 20;
		abyte2[86] = 21;
		abyte2[87] = 22;
		abyte2[88] = 23;
		abyte2[89] = 24;
		abyte2[90] = 25;
		abyte2[91] = -9;
		abyte2[92] = -9;
		abyte2[93] = -9;
		abyte2[94] = -9;
		abyte2[95] = -9;
		abyte2[96] = -9;
		abyte2[97] = 26;
		abyte2[98] = 27;
		abyte2[99] = 28;
		abyte2[100] = 29;
		abyte2[101] = 30;
		abyte2[102] = 31;
		abyte2[103] = 32;
		abyte2[104] = 33;
		abyte2[105] = 34;
		abyte2[106] = 35;
		abyte2[107] = 36;
		abyte2[108] = 37;
		abyte2[109] = 38;
		abyte2[110] = 39;
		abyte2[111] = 40;
		abyte2[112] = 41;
		abyte2[113] = 42;
		abyte2[114] = 43;
		abyte2[115] = 44;
		abyte2[116] = 45;
		abyte2[117] = 46;
		abyte2[118] = 47;
		abyte2[119] = 48;
		abyte2[120] = 49;
		abyte2[121] = 50;
		abyte2[122] = 51;
		abyte2[123] = -9;
		abyte2[124] = -9;
		abyte2[125] = -9;
		abyte2[126] = -9;
		abyte2[127] = -9;
		DECODABET = abyte2;
		abyte3 = new byte[128];
		abyte3[0] = -9;
		abyte3[1] = -9;
		abyte3[2] = -9;
		abyte3[3] = -9;
		abyte3[4] = -9;
		abyte3[5] = -9;
		abyte3[6] = -9;
		abyte3[7] = -9;
		abyte3[8] = -9;
		abyte3[9] = -5;
		abyte3[10] = -5;
		abyte3[11] = -9;
		abyte3[12] = -9;
		abyte3[13] = -5;
		abyte3[14] = -9;
		abyte3[15] = -9;
		abyte3[16] = -9;
		abyte3[17] = -9;
		abyte3[18] = -9;
		abyte3[19] = -9;
		abyte3[20] = -9;
		abyte3[21] = -9;
		abyte3[22] = -9;
		abyte3[23] = -9;
		abyte3[24] = -9;
		abyte3[25] = -9;
		abyte3[26] = -9;
		abyte3[27] = -9;
		abyte3[28] = -9;
		abyte3[29] = -9;
		abyte3[30] = -9;
		abyte3[31] = -9;
		abyte3[32] = -5;
		abyte3[33] = -9;
		abyte3[34] = -9;
		abyte3[35] = -9;
		abyte3[36] = -9;
		abyte3[37] = -9;
		abyte3[38] = -9;
		abyte3[39] = -9;
		abyte3[40] = -9;
		abyte3[41] = -9;
		abyte3[42] = -9;
		abyte3[43] = -9;
		abyte3[44] = -9;
		abyte3[45] = 62;
		abyte3[46] = -9;
		abyte3[47] = -9;
		abyte3[48] = 52;
		abyte3[49] = 53;
		abyte3[50] = 54;
		abyte3[51] = 55;
		abyte3[52] = 56;
		abyte3[53] = 57;
		abyte3[54] = 58;
		abyte3[55] = 59;
		abyte3[56] = 60;
		abyte3[57] = 61;
		abyte3[58] = -9;
		abyte3[59] = -9;
		abyte3[60] = -9;
		abyte3[61] = -1;
		abyte3[62] = -9;
		abyte3[63] = -9;
		abyte3[64] = -9;
		abyte3[65] = 0;
		abyte3[66] = 1;
		abyte3[67] = 2;
		abyte3[68] = 3;
		abyte3[69] = 4;
		abyte3[70] = 5;
		abyte3[71] = 6;
		abyte3[72] = 7;
		abyte3[73] = 8;
		abyte3[74] = 9;
		abyte3[75] = 10;
		abyte3[76] = 11;
		abyte3[77] = 12;
		abyte3[78] = 13;
		abyte3[79] = 14;
		abyte3[80] = 15;
		abyte3[81] = 16;
		abyte3[82] = 17;
		abyte3[83] = 18;
		abyte3[84] = 19;
		abyte3[85] = 20;
		abyte3[86] = 21;
		abyte3[87] = 22;
		abyte3[88] = 23;
		abyte3[89] = 24;
		abyte3[90] = 25;
		abyte3[91] = -9;
		abyte3[92] = -9;
		abyte3[93] = -9;
		abyte3[94] = -9;
		abyte3[95] = 63;
		abyte3[96] = -9;
		abyte3[97] = 26;
		abyte3[98] = 27;
		abyte3[99] = 28;
		abyte3[100] = 29;
		abyte3[101] = 30;
		abyte3[102] = 31;
		abyte3[103] = 32;
		abyte3[104] = 33;
		abyte3[105] = 34;
		abyte3[106] = 35;
		abyte3[107] = 36;
		abyte3[108] = 37;
		abyte3[109] = 38;
		abyte3[110] = 39;
		abyte3[111] = 40;
		abyte3[112] = 41;
		abyte3[113] = 42;
		abyte3[114] = 43;
		abyte3[115] = 44;
		abyte3[116] = 45;
		abyte3[117] = 46;
		abyte3[118] = 47;
		abyte3[119] = 48;
		abyte3[120] = 49;
		abyte3[121] = 50;
		abyte3[122] = 51;
		abyte3[123] = -9;
		abyte3[124] = -9;
		abyte3[125] = -9;
		abyte3[126] = -9;
		abyte3[127] = -9;
		WEBSAFE_DECODABET = abyte3;
	}
}
