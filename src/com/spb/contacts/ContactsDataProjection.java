// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;


interface ContactsDataProjection
{

	public static final String []CONTACTS_DATA_PROJECTION = new String[]{
		 "contact_id",
		 "display_name",
		 "mimetype",
		 "data1",
		 "data2",
		 "data3",
		 "lookup",
		 "starred",
		 "photo_id",
		 "_id",
		 "data_version"
	};
	public static final int INDEX_CONTACT_ID = 0;
	public static final int INDEX_DATA1 = 3;
	public static final int INDEX_DATA2 = 4;
	public static final int INDEX_DATA3 = 5;
	public static final int INDEX_DATA_ID = 9;
	public static final int INDEX_DATA_VERSION = 10;
	public static final int INDEX_DISPLAY_NAME = 1;
	public static final int INDEX_EVENT_DATE = 3;
	public static final int INDEX_EVENT_LABEL = 5;
	public static final int INDEX_EVENT_TYPE = 4;
	public static final int INDEX_FIRST_NAME = 4;
	public static final int INDEX_LABEL = 5;
	public static final int INDEX_LAST_NAME = 5;
	public static final int INDEX_LOCATION_TYPE = 4;
	public static final int INDEX_LOOKUP_KEY = 6;
	public static final int INDEX_MIMETYPE = 2;
	public static final int INDEX_NUMBER_ADDRESS = 3;
	public static final int INDEX_PHOTO_ID = 8;
	public static final int INDEX_STARRED = 7;
	public static final int INDEX_STRUCTURED_DISPLAY_NAME = 3;
	
}
