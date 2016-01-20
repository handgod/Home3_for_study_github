package com.softspb.kana;

public abstract class Characters
{
  private static final char FIRST_FULL_WIDTH_CHAR_IN_ASCII = '!';
  public static final char FULL_WIDTH_LATIN_MAX = '～';
  public static final char FULL_WIDTH_LATIN_MIN = '！';

  public static char fullWidthLatinToAsciiLatin(char paramChar)
  {
    if ((paramChar >= 65281) && (paramChar <= 65374))
      paramChar = (char)(paramChar - 65281 + 33);
    return paramChar;
  }

  public static CharSequence fullWidthLatinToAsciiLatin(CharSequence paramCharSequence)
  {
    int i = paramCharSequence.length();
    StringBuilder localStringBuilder1 = new StringBuilder(i);
    int j = 0;
    while (j < i)
    {
      char c = fullWidthLatinToAsciiLatin(paramCharSequence.charAt(j));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(c);
      j += 1;
    }
    return localStringBuilder1;
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.kana.Characters
 * JD-Core Version:    0.6.0
 */