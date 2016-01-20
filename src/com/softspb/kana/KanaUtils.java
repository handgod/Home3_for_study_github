package com.softspb.kana;

import android.util.SparseIntArray;

public class KanaUtils
{
  static final int CODEPOINT_FOR_NULL_STR = 131069;
  static final int VOICED_MARK_1 = 12441;
  static final int VOICED_MARK_2 = 12442;
  static final int VOICED_MARK_3 = 12443;
  static final int VOICED_MARK_4 = 12444;
  static final int VOICED_MARK_5 = 65438;
  static final int VOICED_MARK_6 = 65439;
  static final SparseIntArray voicedSymbols = new SparseIntArray();

  static
  {
    voicedSymbols.put(12364, 12363);
    voicedSymbols.put(12366, 12365);
    voicedSymbols.put(12368, 12367);
    voicedSymbols.put(12370, 12369);
    voicedSymbols.put(12372, 12371);
    voicedSymbols.put(12374, 12373);
    voicedSymbols.put(12376, 12375);
    voicedSymbols.put(12378, 12377);
    voicedSymbols.put(12380, 12379);
    voicedSymbols.put(12382, 12381);
    voicedSymbols.put(12384, 12383);
    voicedSymbols.put(12386, 12385);
    voicedSymbols.put(12389, 12388);
    voicedSymbols.put(12391, 12390);
    voicedSymbols.put(12393, 12392);
    voicedSymbols.put(12400, 12399);
    voicedSymbols.put(12401, 12399);
    voicedSymbols.put(12403, 12402);
    voicedSymbols.put(12404, 12402);
    voicedSymbols.put(12406, 12405);
    voicedSymbols.put(12407, 12405);
    voicedSymbols.put(12409, 12408);
    voicedSymbols.put(12410, 12408);
    voicedSymbols.put(12412, 12411);
    voicedSymbols.put(12413, 12411);
    voicedSymbols.put(12436, 12358);
    voicedSymbols.put(12446, 12445);
    voicedSymbols.put(12447, 12424);
    voicedSymbols.put(12460, 12459);
    voicedSymbols.put(12462, 12461);
    voicedSymbols.put(12464, 12463);
    voicedSymbols.put(12466, 12465);
    voicedSymbols.put(12468, 12467);
    voicedSymbols.put(12470, 12469);
    voicedSymbols.put(12472, 12471);
    voicedSymbols.put(12474, 12473);
    voicedSymbols.put(12476, 12475);
    voicedSymbols.put(12478, 12477);
    voicedSymbols.put(12480, 12479);
    voicedSymbols.put(12482, 12481);
    voicedSymbols.put(12485, 12484);
    voicedSymbols.put(12487, 12486);
    voicedSymbols.put(12489, 12488);
    voicedSymbols.put(12496, 12495);
    voicedSymbols.put(12497, 12495);
    voicedSymbols.put(12499, 12498);
    voicedSymbols.put(12500, 12498);
    voicedSymbols.put(12502, 12501);
    voicedSymbols.put(12503, 12501);
    voicedSymbols.put(12505, 12504);
    voicedSymbols.put(12506, 12504);
    voicedSymbols.put(12508, 12507);
    voicedSymbols.put(12509, 12507);
    voicedSymbols.put(12532, 12454);
    voicedSymbols.put(12535, 12527);
    voicedSymbols.put(12536, 12528);
    voicedSymbols.put(12537, 12529);
    voicedSymbols.put(12538, 12530);
    voicedSymbols.put(12542, 12541);
    voicedSymbols.put(12543, 12467);
  }

	static int GetHiraganaFromHalfwidthKatakana(int i, int j, boolean aflag[])
	{
		if (i >= 65382 && 65439 >= i)
		{
			switch (i)
			{
			default:
				if (65393 <= i && i <= 65397)
				{
					if (i == 65395 && j == 65438)
					{
						if (aflag != null)
							aflag[0] = true;
						i = 12436;
					} else
					{
						i = 12354 + 2 * (i - 65393);
					}
				} else
				if (65398 <= i && i <= 65409)
				{
					if (j == 65438)
					{
						if (aflag != null)
							aflag[0] = true;
						i = 1 + (12363 + 2 * (i - 65398));
					} else
					{
						i = 12363 + 2 * (i - 65398);
					}
				} else
				if (65410 <= i && i <= 65412)
				{
					if (j == 65438)
					{
						if (aflag != null)
							aflag[0] = true;
						i = 1 + (12388 + 2 * (i - 65410));
					} else
					{
						i = 12388 + 2 * (i - 65410);
					}
				} else
				if (65413 <= i && i <= 65417)
					i = 12394 + (i - 65413);
				else
				if (65418 <= i && i <= 65422)
				{
					if (j == 65438)
					{
						if (aflag != null)
							aflag[0] = true;
						i = 1 + (12399 + 3 * (i - 65418));
					} else
					if (j == 65439)
					{
						if (aflag != null)
							aflag[0] = true;
						i = 2 + (12399 + 3 * (i - 65418));
					} else
					{
						i = 12399 + 3 * (i - 65418);
					}
				} else
				if (65423 <= i && i <= 65427)
					i = 12414 + (i - 65423);
				else
				if (65428 <= i && i <= 65430)
					i = 12420 + 2 * (i - 65428);
				else
				if (65431 <= i && i <= 65435)
					i = 12425 + (i - 65431);
				break;

			case 65382: 
				i = 12434;
				break;

			case 65383: 
				i = 12353;
				break;

			case 65384: 
				i = 12355;
				break;

			case 65385: 
				i = 12357;
				break;

			case 65386: 
				i = 12359;
				break;

			case 65387: 
				i = 12361;
				break;

			case 65388: 
				i = 12419;
				break;

			case 65389: 
				i = 12421;
				break;

			case 65390: 
				i = 12423;
				break;

			case 65391: 
				i = 12387;
				break;

			case 65392: 
				i = 12540;
				break;

			case 65436: 
				i = 12431;
				break;

			case 65437: 
				i = 12435;
				break;
			}
		}
		return i;
	} 
  
	static int GetNormalizedCodePoint(int i, int j, boolean aflag[])
	{
		if (aflag != null)
			aflag[0] = false;
		if (i > 32 && i != 12288)
		{
			if (i == 45 || i == 8208 || i == 8213 || i == 12540 || i == 65293)
				i = 65293;
			else
			if (33 <= i && i <= 126 || 65281 <= i && i <= 65374)
			{
				if (65345 <= i && i <= 65370 || 97 <= i && i <= 122)
					i -= 32;
			} else
			if (i == 732 || i == 8764)
				i = 65374;
			else
			if (i > 12352 && (12544 > i || i >= 65280) && i != 0x1fffd)
				i = GetNormalizedKana(i, j, aflag);
		}
		return i;
	}

	static int GetNormalizedHiragana(int i)
	{
		if (i >= 12352 && 12447 >= i)
		{
			switch (i)
			{
			case 12437: 
				i = 12363;
				break;

			case 12438: 
				i = 12367;
				break;
			}
		}
		return i;
	}


	static int GetNormalizedKana(int i, int j, boolean aflag[])
	{
		int k;
		if (12449 <= i && i <= 12534)
			k = i - 96;
		else
			k = GetHiraganaFromHalfwidthKatakana(i, j, aflag);
		return GetNormalizedHiragana(k);
	}
	public static String collapseVoicedMarks(String s)
	{
		int i = s.length();
		StringBuilder stringbuilder = new StringBuilder();
		int j = 0;
		int k = 0;
		while (k < i) 
		{
			int l = s.codePointAt(k);
			if (l == 12441 || l == 12443 || l == 65438)
			{
				if (j != 0)
				{
					stringbuilder.appendCodePoint(j + 1);
					j = 0;
				}
			} else
			if (l == 12442 || l == 12444 || l == 65439)
			{
				if (j != 0)
				{
					stringbuilder.appendCodePoint(j + 2);
					j = 0;
				}
			} else
			{
				if (j != 0)
					stringbuilder.appendCodePoint(j);
				j = l;
			}
			k = s.offsetByCodePoints(k, 1);
		}
		if (j != 0)
			stringbuilder.appendCodePoint(j);
		return stringbuilder.toString();
	}

	public static String discardVoicedMarks(String s)
	{
		int i = s.length();
		StringBuilder stringbuilder = null;
		int j = 0;
		while (j < i) 
		{
			int k = s.codePointAt(j);
			int l = voicedSymbols.get(k, -1);
			if (l != -1)
			{
				if (stringbuilder == null)
				{
					stringbuilder = new StringBuilder();
					stringbuilder.append(s.substring(0, j));
				}
				stringbuilder.append(Character.toChars(l));
			} else
			if (k == 12441 || k == 12442 || k == 12443 || k == 12444 || k == 65438 || k == 65439)
			{
				if (stringbuilder == null)
				{
					stringbuilder = new StringBuilder();
					stringbuilder.append(s.substring(0, j));
				}
			} else
			if (stringbuilder != null)
				stringbuilder.append(Character.toChars(k));
			j = s.offsetByCodePoints(j, 1);
		}
		if (stringbuilder != null)
			s = stringbuilder.toString();
		return s;
	}

	  static boolean getPhoneticallySortableCodePoint(int paramInt1, int paramInt2, StringBuilder paramStringBuilder)
	  {
	    int i = 0;
	    int k = 0;
	    if ((paramInt1 <= 32) || (paramInt1 == 12288))
	    {
	      int j = 65536 + paramInt1;
	      StringBuilder localStringBuilder1 = paramStringBuilder.appendCodePoint(j);
	      k = 0;
	    }
	    while (true)
	    {
	     
	      if (((33 <= paramInt1) && (paramInt1 <= 126)) || ((65281 <= paramInt1) && (paramInt1 <= 65374)))
	      {
	        if ((33 <= paramInt1) && (paramInt1 <= 126))
	          paramInt1 += 65248;
	        if ((65296 <= paramInt1) && (paramInt1 <= 65305))
	        {
	          int m = paramInt1 + 86;
	          StringBuilder localStringBuilder2 = paramStringBuilder.appendCodePoint(m);
	        }
	        while (true)
	        {
	       
	          if ((65345 <= paramInt1) && (paramInt1 <= 65370))
	          {
	            int n = paramInt1 + -32;
	            StringBuilder localStringBuilder3 = paramStringBuilder.appendCodePoint(n);
	            continue;
	          }
	          if ((65281 <= paramInt1) && (paramInt1 <= 65295))
	          {
	            int i1 = paramInt1 + 111;
	            StringBuilder localStringBuilder4 = paramStringBuilder.appendCodePoint(i1);
	            continue;
	          }
	          if ((65306 <= paramInt1) && (paramInt1 <= 65312))
	          {
	            int i2 = paramInt1 + 101;
	            StringBuilder localStringBuilder5 = paramStringBuilder.appendCodePoint(i2);
	            continue;
	          }
	          if ((65339 <= paramInt1) && (paramInt1 <= 65344))
	          {
	            int i3 = paramInt1 + 75;
	            StringBuilder localStringBuilder6 = paramStringBuilder.appendCodePoint(i3);
	            continue;
	          }
	          if ((65371 <= paramInt1) && (paramInt1 <= 65374))
	          {
	            int i4 = paramInt1 + 49;
	            StringBuilder localStringBuilder7 = paramStringBuilder.appendCodePoint(i4);
	            continue;
	          }
	          StringBuilder localStringBuilder8 = paramStringBuilder.appendCodePoint(paramInt1);
	        }
	      }
	      if ((paramInt1 == 732) || (paramInt1 == 8764))
	      {
	        StringBuilder localStringBuilder9 = paramStringBuilder.appendCodePoint(65374);
	        k = 0;
	        continue;
	      }
	      if ((paramInt1 > 12352) && ((12544 > paramInt1) || (paramInt1 >= 65280)) && (paramInt1 != 131069))
	        break;
	      int i5 = 65536 + paramInt1;
	      StringBuilder localStringBuilder10 = paramStringBuilder.appendCodePoint(i5);
	      k = 0;
	    }
	    if (paramInt1 == 12540)
	    {
	      paramInt1 = 65404;
	      label347: switch (paramInt1)
	      {
	      default:
	      case 12353:
	      case 12355:
	      case 12357:
	      case 12359:
	      case 12361:
	      case 12430:
	      case 12437:
	      case 12438:
	      }
	    }
	    else
	    {
	     
	      switch (paramInt1)
	      {
	      default:
	        if ((65393 <= paramInt1) && (paramInt1 <= 65397))
	          if ((paramInt1 == 65395) && (paramInt2 == 65438))
	          {
	            i = 1;
	            paramInt1 = 12358;
	          }
	        break;
	      case 65382:
	        paramInt1 = 12434;
	        break;
	      case 65383:
	        paramInt1 = 12353;
	        break;
	      case 65384:
	        paramInt1 = 12355;
	        break;
	      case 65385:
	        paramInt1 = 12357;
	        break;
	      case 65386:
	        paramInt1 = 12359;
	        break;
	      case 65387:
	        paramInt1 = 12361;
	        break;
	      case 65388:
	        paramInt1 = 12419;
	        break;
	      case 65389:
	        paramInt1 = 12421;
	        break;
	      case 65390:
	        paramInt1 = 12423;
	        break;
	      case 65391:
	        paramInt1 = 12387;
	        break;
	      case 65392:
	        paramInt1 = 12540;
	        break;
	      case 65436:
	        paramInt1 = 12431;
	        break;
	      case 65437:
	        paramInt1 = 12435;
	       
	        if ((65398 <= paramInt1) && (paramInt1 <= 65409))
	        {
	          if (paramInt2 == 65438)
	          {
	            i = 1;
	            paramInt1 = (paramInt1 - 65398) * 2 + 12363 + 1;
	            break;
	          }
	          paramInt1 = (paramInt1 - 65398) * 2 + 12363;
	          break;
	        }
	        if ((65410 <= paramInt1) && (paramInt1 <= 65412))
	        {
	          if (paramInt2 == 65438)
	          {
	            i = 1;
	            paramInt1 = (paramInt1 - 65410) * 2 + 12388 + 1;
	            break;
	          }
	          paramInt1 = (paramInt1 - 65410) * 2 + 12388;
	          break;
	        }
	        if ((65413 <= paramInt1) && (paramInt1 <= 65417))
	        {
	          paramInt1 = paramInt1 - 65413 + 12394;
	          break;
	        }
	        if ((65418 <= paramInt1) && (paramInt1 <= 65422))
	        {
	          if (paramInt2 == 65438)
	          {
	            i = 1;
	            paramInt1 = (paramInt1 - 65418) * 3 + 12399 + 1;
	            break;
	          }
	          if (paramInt2 == 65439)
	          {
	            i = 1;
	            paramInt1 = (paramInt1 - 65418) * 3 + 12399 + 2;
	            break;
	          }
	          paramInt1 = (paramInt1 - 65418) * 3 + 12399;
	          break;
	        }
	        if ((65423 <= paramInt1) && (paramInt1 <= 65427))
	        {
	          paramInt1 = paramInt1 - 65423 + 12414;
	          break;
	        }
	        if ((65428 <= paramInt1) && (paramInt1 <= 65430))
	        {
	          paramInt1 = (paramInt1 - 65428) * 2 + 12420;
	          break;
	        }
	        if ((65431 > paramInt1) || (paramInt1 > 65435))
	          break;
	        paramInt1 = paramInt1 - 65431 + 12425;
	      }
	    }
	    boolean flag ;
	    if(k == 1)
	    	flag = true;
	    else
	    	flag =false;
	    return flag;
	  }
	  
  static int fullwidthToHalfwidth(int paramInt1, int paramInt2, boolean[] paramArrayOfBoolean, int[] paramArrayOfInt)
  {
    if (paramArrayOfBoolean != null)
      paramArrayOfBoolean[0] = false;
    if (paramArrayOfInt != null)
    {
      if (!((paramInt2 != 12443) && (paramInt2 != 65438)))
      {
    	  paramArrayOfInt[0] = 65438;
      }
      if (paramArrayOfBoolean != null)
        paramArrayOfBoolean[0] = true;
    }
    if ((12449 <= paramInt1) && (paramInt1 <= 12542))
      switch (paramInt1)
      {
      case 12460:
      case 12462:
      case 12464:
      case 12466:
      case 12468:
      case 12470:
      case 12472:
      case 12474:
      case 12476:
      case 12478:
      case 12480:
      case 12482:
      case 12485:
      case 12487:
      case 12489:
      case 12496:
      case 12497:
      case 12499:
      case 12500:
      case 12502:
      case 12503:
      case 12505:
      case 12506:
      case 12508:
      case 12509:
      case 12526:
      case 12528:
      case 12529:
      case 12532:
      case 12533:
      case 12534:
      case 12535:
      case 12536:
      case 12537:
      case 12538:
      case 12539:
      case 12540:
      case 12541:
      default:
      case 12449:
      case 12450:
      case 12451:
      case 12452:
      case 12453:
      case 12454:
      case 12455:
      case 12456:
      case 12457:
      case 12458:
      case 12459:
      case 12461:
      case 12463:
      case 12465:
      case 12467:
      case 12469:
      case 12471:
      case 12473:
      case 12475:
      case 12477:
      case 12479:
      case 12481:
      case 12483:
      case 12484:
      case 12486:
      case 12488:
      case 12490:
      case 12491:
      case 12492:
      case 12493:
      case 12494:
      case 12495:
      case 12498:
      case 12501:
      case 12504:
      case 12507:
      case 12510:
      case 12511:
      case 12512:
      case 12513:
      case 12514:
      case 12515:
      case 12516:
      case 12517:
      case 12518:
      case 12519:
      case 12520:
      case 12521:
      case 12522:
      case 12523:
      case 12524:
      case 12525:
      case 12527:
      case 12530:
      case 12531:
      }
    paramInt1 = 65383;
    return paramInt1;
    
//    while (true)
//    {
//      
//      label442: if ((paramInt2 == 12444) || (paramInt2 == 65439))
//      {
//        paramArrayOfInt[0] = 65439;
//        if (paramArrayOfBoolean == null)
//          break;
//        paramArrayOfBoolean[0] = true;
//        break;
//      }
//      paramArrayOfInt[0] = 0;
//      break;
//      paramInt1 = 65383;
//      continue;
//      paramInt1 = 65393;
//      continue;
//      paramInt1 = 65384;
//      continue;
//      paramInt1 = 65394;
//      continue;
//      paramInt1 = 65385;
//      continue;
//      paramInt1 = 65395;
//      continue;
//      paramInt1 = 65386;
//      continue;
//      paramInt1 = 65396;
//      continue;
//      paramInt1 = 65387;
//      continue;
//      paramInt1 = 65397;
//      continue;
//      paramInt1 = 65398;
//      continue;
//      paramInt1 = 65399;
//      continue;
//      paramInt1 = 65400;
//      continue;
//      paramInt1 = 65401;
//      continue;
//      paramInt1 = 65402;
//      continue;
//      paramInt1 = 65403;
//      continue;
//      paramInt1 = 65404;
//      continue;
//      paramInt1 = 65405;
//      continue;
//      paramInt1 = 65406;
//      continue;
//      paramInt1 = 65407;
//      continue;
//      paramInt1 = 65408;
//      continue;
//      paramInt1 = 65409;
//      continue;
//      paramInt1 = 65391;
//      continue;
//      paramInt1 = 65410;
//      continue;
//      paramInt1 = 65411;
//      continue;
//      paramInt1 = 65412;
//      continue;
//      paramInt1 = 65413;
//      continue;
//      paramInt1 = 65414;
//      continue;
//      paramInt1 = 65415;
//      continue;
//      paramInt1 = 65416;
//      continue;
//      paramInt1 = 65417;
//      continue;
//      paramInt1 = 65418;
//      continue;
//      paramInt1 = 65419;
//      continue;
//      paramInt1 = 65420;
//      continue;
//      paramInt1 = 65421;
//      continue;
//      paramInt1 = 65422;
//      continue;
//      paramInt1 = 65423;
//      continue;
//      paramInt1 = 65424;
//      continue;
//      paramInt1 = 65425;
//      continue;
//      paramInt1 = 65426;
//      continue;
//      paramInt1 = 65427;
//      continue;
//      paramInt1 = 65388;
//      continue;
//      paramInt1 = 65428;
//      continue;
//      paramInt1 = 65389;
//      continue;
//      paramInt1 = 65429;
//      continue;
//      paramInt1 = 65390;
//      continue;
//      paramInt1 = 65430;
//      continue;
//      paramInt1 = 65431;
//      continue;
//      paramInt1 = 65432;
//      continue;
//      paramInt1 = 65433;
//      continue;
//      paramInt1 = 65434;
//      continue;
//      paramInt1 = 65435;
//      continue;
//      paramInt1 = 65436;
//      continue;
//      paramInt1 = 65382;
//      continue;
//      paramInt1 = 65437;
//    }
  }

  public static String getHalfwidthForm(String paramString)
  {
    int i = 1;
    int i2 ;
    int i3 ;
    int i4 = 0;
    int i7;

    StringBuilder localStringBuilder1 = new StringBuilder();
    int j = 0;
    if (paramString == null)
      j = 0;
      if (j > 0)
      {
        int k = paramString.codePointAt(0);
        int m = 0;
        boolean[] arrayOfBoolean = new boolean[1];
        arrayOfBoolean[0] = false;
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = 0;
        int n = k;
        int i1 = m;
        i = 1;
        try
        {
          i2 = paramString.offsetByCodePoints(m, i);
          m = i2;
          if ((m == -1) || (m == j))
          {
            i3 = 0;
            while (i1 != -1)
            {
              i4 = 1;
              int i5 = fullwidthToHalfwidth(n, i3, arrayOfBoolean, arrayOfInt);
              if (arrayOfBoolean[0] != false)
                i4 += 1;
              if (i5 != 0)
                localStringBuilder1.appendCodePoint(i5);
              if (arrayOfInt[0] != 0)
              {
                int i6 = arrayOfInt[0];
                StringBuilder localStringBuilder3 = localStringBuilder1.appendCodePoint(i6);
              }
              i7 = i4;
              i4 = i7 + -1;
              if ((i7 <= 0) || (i1 == -1))
                continue;
              n = i3;
              i1 = m;
              i = 1;
            }
          }
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException1)
        {
          try
          {
           
              i2 = paramString.offsetByCodePoints(i1, i);
              m = i2;
              if ((m != -1) && (m != j))
              {
            	  
              }
              else
              {
	              i3 = 0;
	              i7 = i4;
              }
              j = paramString.length();
            i3 = paramString.codePointAt(m);
          }
          catch (IndexOutOfBoundsException localIndexOutOfBoundsException2)
          {
              i3 = paramString.codePointAt(m);
          }
        }
      }
    return localStringBuilder1.toString();
  }

  public static String getNormalizedString(String paramString)
  {
    int i = 1;
    StringBuilder localStringBuilder1 = new StringBuilder();
    int j = 0;
    int i2;
    int i3;
    int i4;
    int i6;
    
    if (paramString == null)
      j = 0;
      if (j > 0)
      {
        int k = paramString.codePointAt(0);
        int m = 0;
        boolean[] arrayOfBoolean = new boolean[1];
        arrayOfBoolean[0] = false;
        int n = k;
        int i1 = m;
        i = 1;
        try
        {
          i2 = paramString.offsetByCodePoints(m, i);
          m = i2;
          if ((m == -1) || (m == j))
          {
            i3 = 0;
            while (i1 != -1)
            {
              i4 = 1;
              int i5 = GetNormalizedCodePoint(n, i3, arrayOfBoolean);
              if (arrayOfBoolean[0] != false)
                i4 += 1;
              StringBuilder localStringBuilder2 = localStringBuilder1.appendCodePoint(i5);
              i6 = i4;
              i4 = i6 + -1;
              if ((i6 <= 0) || (i1 == -1))
                continue;
              n = i3;
              i1 = m;
              i = 1;
            }
          }
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException1)
        {
          try
          {
           
              i2 = paramString.offsetByCodePoints(i1, i);
              m = i2;
              if ((m != -1) && (m != j))
            	  i3 = paramString.codePointAt(m);
          
          }
          catch (IndexOutOfBoundsException localIndexOutOfBoundsException2)
          {
              i3 = paramString.codePointAt(m);
            }
          }
        }
    return localStringBuilder1.toString();
  }
  
  public static String getPhoneticallySortableString(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2;
    int i3 = 0;
    int i4 = 0;
    int i5;

    if (paramString == null)
    {
      i = 0;
      if (i <= 0)
      {}
      else
      {
	      int j = paramString.codePointAt(0);
	      k = 0;
	      m = j;
	      n = k;
	      i1 = 1;
      }
    }
    else
    {

      try
      {
        i2 = paramString.offsetByCodePoints(k, i1);
        k = i2;
        if ((k != -1) && (k != i))
        {}
        else
        {
        i3 = 0;
        }
        if (n != -1)
        {
          i4 = 1;
          if (!getPhoneticallySortableCodePoint(m, i3, localStringBuilder))
          {
	          i5 = i4 + 1;
	          i4 = i5 + -1;
	          if (!((i5 <= 0) || (n == -1)))
	          {
		          m = i3;
		          n = k;
		          i1 = 1;
	          }
          }
        }
        return paramString;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException1)
      {
        try
        {
          i2 = paramString.offsetByCodePoints(n, i1);
          k = i2;
          if ((k != -1) && (k != i))
          {}
          else
          {
	          i3 = 0;
	          i5 = i4;
          }
          i = paramString.length();
          i3 = paramString.codePointAt(k);
          return paramString;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException2)
        {
          k = -1;
          return paramString;
        }
      }
      
    }
	return paramString;
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.kana.KanaUtils
 * JD-Core Version:    0.6.0
 */