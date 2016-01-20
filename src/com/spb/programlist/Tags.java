// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.Intent;
import android.net.Uri;
import java.util.Collection;
import java.util.LinkedList;

class Tags
	implements TagSources.TagSource
{

	Tags()
	{
	}

	public Collection getTags(TagSources.ITagFactory itagfactory)
	{
		LinkedList linkedlist = new LinkedList();
		LinkedList linkedlist1 = new LinkedList();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.POWER_USAGE_SUMMARY");
		linkedlist1.add(itagfactory.getPatternBuilder(intent).create());
		linkedlist.add(itagfactory.create("settings_battery", linkedlist1));
		LinkedList linkedlist2 = new LinkedList();
		Intent intent1 = new Intent();
		intent1.setAction("android.intent.action.VIEW");
		intent1.setData(Uri.parse("market://search"));
		IntentPattern.PatternBuilder patternbuilder = itagfactory.getPatternBuilder(intent1);
		patternbuilder.matchPackage(true);
		linkedlist2.add(patternbuilder.create());
		linkedlist.add(itagfactory.create("market", linkedlist2));
		LinkedList linkedlist3 = new LinkedList();
		Intent intent2 = new Intent();
		intent2.setAction("android.intent.action.VIEW");
		intent2.setData(Uri.parse("vnd.youtube.launch://"));
		linkedlist3.add(itagfactory.getPatternBuilder(intent2).create());
		linkedlist.add(itagfactory.create("youtube", linkedlist3));
		LinkedList linkedlist4 = new LinkedList();
		Intent intent3 = new Intent();
		intent3.setClassName("com.htc.newsreader", "");
		linkedlist4.add(itagfactory.getPatternBuilder(intent3).create());
		Intent intent4 = new Intent();
		intent4.setClassName("com.newspaperdirect.pressreader.android.samsung", "");
		linkedlist4.add(itagfactory.getPatternBuilder(intent4).create());
		Intent intent5 = new Intent();
		intent5.setClassName("com.google.android.apps.genie.geniewidget", "");
		linkedlist4.add(itagfactory.getPatternBuilder(intent5).create());
		linkedlist.add(itagfactory.create("news", linkedlist4));
		LinkedList linkedlist5 = new LinkedList();
		Intent intent6 = new Intent();
		intent6.setAction("android.intent.action.SEND");
		intent6.setPackage("com.google.android.gm");
		intent6.setType("*/*");
		linkedlist5.add(itagfactory.getPatternBuilder(intent6).create());
		linkedlist.add(itagfactory.create("gmail_new", linkedlist5));
		LinkedList linkedlist6 = new LinkedList();
		Intent intent7 = new Intent();
		intent7.setClassName("com.android.calculator2", "");
		linkedlist6.add(itagfactory.getPatternBuilder(intent7).create());
		Intent intent8 = new Intent();
		intent8.setClassName("com.sec.android.app.calculator", "");
		linkedlist6.add(itagfactory.getPatternBuilder(intent8).create());
		linkedlist.add(itagfactory.create("calculator", linkedlist6));
		LinkedList linkedlist7 = new LinkedList();
		Intent intent9 = new Intent();
		intent9.setClassName("com.google.android.talk", "");
		linkedlist7.add(itagfactory.getPatternBuilder(intent9).create());
		linkedlist.add(itagfactory.create("gtalk", linkedlist7));
		LinkedList linkedlist8 = new LinkedList();
		Intent intent10 = new Intent();
		intent10.setAction("android.intent.action.VIEW");
		intent10.setType("vnd.android.cursor.dir/contact");
		linkedlist8.add(itagfactory.getPatternBuilder(intent10).create());
		Intent intent11 = new Intent();
		intent11.setClassName("com.android.contacts", "com.sec.android.app.contacts.PhoneBookTopMenuActivity");
		linkedlist8.add(itagfactory.getPatternBuilder(intent11).create());
		Intent intent12 = new Intent();
		intent12.setClassName("com.android.contacts", "com.sec.android.app.contacts.ContactsEntryActivity");
		linkedlist8.add(itagfactory.getPatternBuilder(intent12).create());
		Intent intent13 = new Intent();
		intent13.setClassName("com.sonyericsson.android.socialphonebook", "com.sonyericsson.android.socialphonebook.LaunchActivity");
		linkedlist8.add(itagfactory.getPatternBuilder(intent13).create());
		Intent intent14 = new Intent();
		intent14.setClassName("com.android.htccontacts", "com.android.htccontacts.ViewCallHistory");
		IntentPattern.PatternBuilder patternbuilder1 = itagfactory.getPatternBuilder(intent14);
		patternbuilder1.antiPattern(true);
		linkedlist8.add(patternbuilder1.create());
		linkedlist.add(itagfactory.create("contacts", linkedlist8));
		LinkedList linkedlist9 = new LinkedList();
		Intent intent15 = new Intent();
		intent15.setAction("android.intent.action.VIEW");
		intent15.setData(Uri.parse("http://spb.com"));
		linkedlist9.add(itagfactory.getPatternBuilder(intent15).create());
		linkedlist.add(itagfactory.create("browser", linkedlist9));
		LinkedList linkedlist10 = new LinkedList();
		Intent intent16 = new Intent();
		intent16.setAction("android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS");
		linkedlist10.add(itagfactory.getPatternBuilder(intent16).create());
		Intent intent17 = new Intent();
		intent17.setAction("android.settings.MANAGE_APPLICATIONS_SETTINGS");
		linkedlist10.add(itagfactory.getPatternBuilder(intent17).create());
		linkedlist.add(itagfactory.create("settings_applications", linkedlist10));
		LinkedList linkedlist11 = new LinkedList();
		Intent intent18 = new Intent();
		intent18.setClassName("com.google.android.voicesearch", "");
		linkedlist11.add(itagfactory.getPatternBuilder(intent18).create());
		linkedlist.add(itagfactory.create("voice_search", linkedlist11));
		LinkedList linkedlist12 = new LinkedList();
		Intent intent19 = new Intent();
		intent19.setAction("android.media.action.STILL_IMAGE_CAMERA");
		linkedlist12.add(itagfactory.getPatternBuilder(intent19).create());
		Intent intent20 = new Intent();
		intent20.setClassName("com.sonyericsson.android.camera", "");
		linkedlist12.add(itagfactory.getPatternBuilder(intent20).create());
		Intent intent21 = new Intent();
		intent21.setClassName("com.android.camera", "com.android.camera.CameraEntry");
		linkedlist12.add(itagfactory.getPatternBuilder(intent21).create());
		Intent intent22 = new Intent();
		intent22.setClassName("com.android.camera", "com.android.camera.CameraLoading3D");
		linkedlist12.add(itagfactory.getPatternBuilder(intent22).create());
		linkedlist.add(itagfactory.create("camera", linkedlist12));
		LinkedList linkedlist13 = new LinkedList();
		Intent intent23 = new Intent();
		intent23.setAction("android.intent.action.DIAL");
		linkedlist13.add(itagfactory.getPatternBuilder(intent23).create());
		Intent intent24 = new Intent();
		intent24.setClassName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity");
		IntentPattern.PatternBuilder patternbuilder2 = itagfactory.getPatternBuilder(intent24);
		patternbuilder2.antiPattern(true);
		linkedlist13.add(patternbuilder2.create());
		Intent intent25 = new Intent();
		intent25.setClassName("com.android.contacts", "com.sec.android.app.contacts.ContactsEntryActivity");
		IntentPattern.PatternBuilder patternbuilder3 = itagfactory.getPatternBuilder(intent25);
		patternbuilder3.antiPattern(true);
		linkedlist13.add(patternbuilder3.create());
		linkedlist.add(itagfactory.create("phone", linkedlist13));
		LinkedList linkedlist14 = new LinkedList();
		Intent intent26 = new Intent();
		intent26.setAction("android.settings.BLUETOOTH_SETTINGS");
		linkedlist14.add(itagfactory.getPatternBuilder(intent26).create());
		linkedlist.add(itagfactory.create("settings_bluetooth", linkedlist14));
		LinkedList linkedlist15 = new LinkedList();
		Intent intent27 = new Intent();
		intent27.setClassName("com.google.android.apps.maps", "com.google.android.maps.driveabout.app.DestinationActivity");
		IntentPattern.PatternBuilder patternbuilder4 = itagfactory.getPatternBuilder(intent27);
		patternbuilder4.matchPackage(false);
		linkedlist15.add(patternbuilder4.create());
		linkedlist.add(itagfactory.create("navigation", linkedlist15));
		LinkedList linkedlist16 = new LinkedList();
		Intent intent28 = new Intent();
		intent28.setClassName("com.google.android.apps.maps", "com.google.android.maps.PlacesActivity");
		IntentPattern.PatternBuilder patternbuilder5 = itagfactory.getPatternBuilder(intent28);
		patternbuilder5.matchPackage(false);
		linkedlist16.add(patternbuilder5.create());
		linkedlist.add(itagfactory.create("places", linkedlist16));
		LinkedList linkedlist17 = new LinkedList();
		Intent intent29 = new Intent();
		intent29.setAction("android.intent.action.VIEW");
		intent29.setData(Uri.parse("twitter-android-app://"));
		linkedlist17.add(itagfactory.getPatternBuilder(intent29).create());
		linkedlist.add(itagfactory.create("twitter", linkedlist17));
		LinkedList linkedlist18 = new LinkedList();
		Intent intent30 = new Intent();
		intent30.setClassName("com.google.android.googlequicksearchbox", "");
		linkedlist18.add(itagfactory.getPatternBuilder(intent30).create());
		linkedlist.add(itagfactory.create("search", linkedlist18));
		LinkedList linkedlist19 = new LinkedList();
		Intent intent31 = new Intent();
		intent31.setAction("android.intent.action.VIEW");
		intent31.setType("vnd.android-dir/mms-sms");
		linkedlist19.add(itagfactory.getPatternBuilder(intent31).create());
		linkedlist.add(itagfactory.create("sms_new", linkedlist19));
		LinkedList linkedlist20 = new LinkedList();
		Intent intent32 = new Intent();
		intent32.setClassName("com.android.htccontacts", "com.android.htccontacts.ViewCallHistory");
		linkedlist20.add(itagfactory.getPatternBuilder(intent32).create());
		Intent intent33 = new Intent();
		intent33.setAction("android.intent.action.VIEW");
		intent33.setType("vnd.android.cursor.dir/calls");
		linkedlist20.add(itagfactory.getPatternBuilder(intent33).create());
		Intent intent34 = new Intent();
		intent34.setClassName("com.android.contacts", "com.android.contacts.DialtactsActivity");
		IntentPattern.PatternBuilder patternbuilder6 = itagfactory.getPatternBuilder(intent34);
		patternbuilder6.antiPattern(true);
		linkedlist20.add(patternbuilder6.create());
		Intent intent35 = new Intent();
		intent35.setClassName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity");
		IntentPattern.PatternBuilder patternbuilder7 = itagfactory.getPatternBuilder(intent35);
		patternbuilder7.antiPattern(true);
		linkedlist20.add(patternbuilder7.create());
		linkedlist.add(itagfactory.create("calllog", linkedlist20));
		LinkedList linkedlist21 = new LinkedList();
		Intent intent36 = new Intent();
		intent36.setClassName("com.google.android.email", "");
		linkedlist21.add(itagfactory.getPatternBuilder(intent36).create());
		Intent intent37 = new Intent();
		intent37.setClassName("com.android.email", "");
		linkedlist21.add(itagfactory.getPatternBuilder(intent37).create());
		Intent intent38 = new Intent();
		intent38.setClassName("com.htc.android.mail", "");
		linkedlist21.add(itagfactory.getPatternBuilder(intent38).create());
		Intent intent39 = new Intent();
		intent39.setClassName("com.lge.email", "");
		linkedlist21.add(itagfactory.getPatternBuilder(intent39).create());
		Intent intent40 = new Intent();
		intent40.setClassName("com.motorola.blur.email", "");
		linkedlist21.add(itagfactory.getPatternBuilder(intent40).create());
		linkedlist.add(itagfactory.create("email", linkedlist21));
		LinkedList linkedlist22 = new LinkedList();
		Intent intent41 = new Intent();
		intent41.setAction("android.settings.SETTINGS");
		linkedlist22.add(itagfactory.getPatternBuilder(intent41).create());
		linkedlist.add(itagfactory.create("settings", linkedlist22));
		LinkedList linkedlist23 = new LinkedList();
		Intent intent42 = new Intent();
		intent42.setAction("android.intent.action.VIEW");
		intent42.setDataAndType(Uri.parse("content://com.android.calendar"), "time/epoch");
		linkedlist23.add(itagfactory.getPatternBuilder(intent42).create());
		Intent intent43 = new Intent();
		intent43.setAction("android.intent.action.EDIT");
		intent43.setType("vnd.android.cursor.item/event");
		IntentPattern.PatternBuilder patternbuilder8 = itagfactory.getPatternBuilder(intent43);
		patternbuilder8.matchPackage(true);
		linkedlist23.add(patternbuilder8.create());
		linkedlist.add(itagfactory.create("calendar", linkedlist23));
		LinkedList linkedlist24 = new LinkedList();
		Intent intent44 = new Intent();
		intent44.setClassName("com.android.phone", "com.android.phone.Settings");
		linkedlist24.add(itagfactory.getPatternBuilder(intent44).create());
		linkedlist.add(itagfactory.create("settings_operator", linkedlist24));
		LinkedList linkedlist25 = new LinkedList();
		Intent intent45 = new Intent();
		intent45.setAction("android.intent.action.MAIN");
		intent45.setType("vnd.android-dir/mms-sms");
		linkedlist25.add(itagfactory.getPatternBuilder(intent45).create());
		linkedlist.add(itagfactory.create("sms", linkedlist25));
		LinkedList linkedlist26 = new LinkedList();
		Intent intent46 = new Intent();
		intent46.setAction("android.intent.action.PICK");
		intent46.setDataAndType(Uri.parse("content://com.android.contacts/contacts"), "vnd.android.cursor.dir/contact");
		linkedlist26.add(itagfactory.getPatternBuilder(intent46).create());
		Intent intent47 = new Intent();
		intent47.setAction("android.intent.action.PICK");
		intent47.setDataAndType(Uri.parse("content://contacts/people"), "vnd.android.cursor.dir/person");
		linkedlist26.add(itagfactory.getPatternBuilder(intent47).create());
		linkedlist.add(itagfactory.create("contacts_pick", linkedlist26));
		LinkedList linkedlist27 = new LinkedList();
		Intent intent48 = new Intent();
		intent48.setClassName("com.google.android.gm", "");
		linkedlist27.add(itagfactory.getPatternBuilder(intent48).create());
		linkedlist.add(itagfactory.create("gmail", linkedlist27));
		LinkedList linkedlist28 = new LinkedList();
		Intent intent49 = new Intent();
		intent49.setAction("android.settings.SOUND_SETTINGS");
		linkedlist28.add(itagfactory.getPatternBuilder(intent49).create());
		linkedlist.add(itagfactory.create("settings_sound", linkedlist28));
		LinkedList linkedlist29 = new LinkedList();
		Intent intent50 = new Intent();
		intent50.setAction("android.intent.action.VIEW");
		intent50.setData(Uri.parse("facebook://"));
		IntentPattern.PatternBuilder patternbuilder9 = itagfactory.getPatternBuilder(intent50);
		patternbuilder9.matchPackage(true);
		linkedlist29.add(patternbuilder9.create());
		linkedlist.add(itagfactory.create("facebook", linkedlist29));
		LinkedList linkedlist30 = new LinkedList();
		Intent intent51 = new Intent();
		intent51.setAction("android.intent.action.SEND");
		intent51.setPackage("com.google.android.email");
		intent51.setType("*/*");
		linkedlist30.add(itagfactory.getPatternBuilder(intent51).create());
		Intent intent52 = new Intent();
		intent52.setAction("android.intent.action.SEND");
		intent52.setPackage("com.htc.android.mail");
		intent52.setType("*/*");
		linkedlist30.add(itagfactory.getPatternBuilder(intent52).create());
		Intent intent53 = new Intent();
		intent53.setAction("android.intent.action.SEND");
		intent53.setPackage("com.android.email");
		intent53.setType("*/*");
		linkedlist30.add(itagfactory.getPatternBuilder(intent53).create());
		linkedlist.add(itagfactory.create("email_new", linkedlist30));
		LinkedList linkedlist31 = new LinkedList();
		Intent intent54 = new Intent();
		intent54.setAction("android.intent.action.MAIN");
		intent54.addCategory("android.intent.category.LAUNCHER");
		intent54.setClassName("com.sonyericsson.android.mediascape", "com.sonyericsson.android.mediascape.activity.MceActivity");
		linkedlist31.add(itagfactory.getPatternBuilder(intent54).create());
		Intent intent55 = new Intent();
		intent55.setAction("android.intent.action.MAIN");
		intent55.addCategory("android.intent.category.LAUNCHER");
		intent55.setClassName("com.sonyericsson.gallery", "com.sonyericsson.gallery.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent55).create());
		Intent intent56 = new Intent();
		intent56.setAction("android.intent.action.MAIN");
		intent56.addCategory("android.intent.category.LAUNCHER");
		intent56.setClassName("com.google.android.gallery3d", "com.cooliris.media.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent56).create());
		Intent intent57 = new Intent();
		intent57.setAction("android.intent.action.MAIN");
		intent57.addCategory("android.intent.category.LAUNCHER");
		intent57.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent57).create());
		Intent intent58 = new Intent();
		intent58.setAction("android.intent.action.MAIN");
		intent58.addCategory("android.intent.category.LAUNCHER");
		intent58.setClassName("com.android.gallery3d", "com.android.gallery3d.app.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent58).create());
		Intent intent59 = new Intent();
		intent59.setAction("android.intent.action.MAIN");
		intent59.addCategory("android.intent.category.LAUNCHER");
		intent59.setClassName("com.cooliris.media", "com.cooliris.media.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent59).create());
		Intent intent60 = new Intent();
		intent60.setAction("android.intent.action.MAIN");
		intent60.addCategory("android.intent.category.LAUNCHER");
		intent60.setClassName("com.htc.album", "com.htc.album.AlbumTabSwitchActivity");
		linkedlist31.add(itagfactory.getPatternBuilder(intent60).create());
		Intent intent61 = new Intent();
		intent61.setAction("android.intent.action.MAIN");
		intent61.addCategory("android.intent.category.LAUNCHER");
		intent61.setClassName("com.motorola.gallery", "com.motorola.gallery.TopScreen");
		linkedlist31.add(itagfactory.getPatternBuilder(intent61).create());
		Intent intent62 = new Intent();
		intent62.setClassName("com.htc.album", "com.htc.album.AlbumMain.ActivityMainCarousel");
		linkedlist31.add(itagfactory.getPatternBuilder(intent62).create());
		Intent intent63 = new Intent();
		intent63.setClassName("com.htc.album", "com.htc.album.AlbumMain.ActivityMainDropList");
		linkedlist31.add(itagfactory.getPatternBuilder(intent63).create());
		Intent intent64 = new Intent();
		intent64.setClassName("com.sonyericsson.camera", "com.sonyericsson.album.grid.GridActivity");
		linkedlist31.add(itagfactory.getPatternBuilder(intent64).create());
		Intent intent65 = new Intent();
		intent65.setClassName("com.motorola.blurgallery", "com.motorola.cgallery.Dashboard");
		linkedlist31.add(itagfactory.getPatternBuilder(intent65).create());
		Intent intent66 = new Intent();
		intent66.setClassName("com.android.gallery", "com.android.gallery.ui.MainActivity");
		linkedlist31.add(itagfactory.getPatternBuilder(intent66).create());
		Intent intent67 = new Intent();
		intent67.setAction("android.intent.action.MAIN");
		intent67.addCategory("android.intent.category.LAUNCHER");
		intent67.setClassName("com.android.sec.gallery3d", "com.android.sec.gallery3d.app.Gallery");
		linkedlist31.add(itagfactory.getPatternBuilder(intent67).create());
		linkedlist.add(itagfactory.create("gallery", linkedlist31));
		LinkedList linkedlist32 = new LinkedList();
		Intent intent68 = new Intent();
		intent68.setClassName("com.google.android.apps.maps", "com.google.android.maps.LatitudeActivity");
		linkedlist32.add(itagfactory.getPatternBuilder(intent68).create());
		linkedlist.add(itagfactory.create("latitude", linkedlist32));
		LinkedList linkedlist33 = new LinkedList();
		Intent intent69 = new Intent();
		intent69.setAction("android.intent.action.EDIT");
		intent69.setData(Uri.parse("vnd.android.cursor.item/event"));
		linkedlist33.add(itagfactory.getPatternBuilder(intent69).create());
		linkedlist.add(itagfactory.create("calendar_new", linkedlist33));
		LinkedList linkedlist34 = new LinkedList();
		Intent intent70 = new Intent();
		intent70.setAction("android.settings.WIFI_SETTINGS");
		linkedlist34.add(itagfactory.getPatternBuilder(intent70).create());
		linkedlist.add(itagfactory.create("settings_wifi", linkedlist34));
		LinkedList linkedlist35 = new LinkedList();
		Intent intent71 = new Intent();
		intent71.setAction("android.media.action.VIDEO_CAMERA");
		linkedlist35.add(itagfactory.getPatternBuilder(intent71).create());
		Intent intent72 = new Intent();
		intent72.setClassName("com.sonyericsson.android.camera", "com.sonyericsson.android.camera.CameraActivity");
		IntentPattern.PatternBuilder patternbuilder10 = itagfactory.getPatternBuilder(intent72);
		patternbuilder10.antiPattern(true);
		linkedlist35.add(patternbuilder10.create());
		Intent intent73 = new Intent();
		intent73.setClassName("com.android.camera", "com.android.camera.CamcorderEntry");
		linkedlist35.add(itagfactory.getPatternBuilder(intent73).create());
		linkedlist.add(itagfactory.create("camera_video", linkedlist35));
		LinkedList linkedlist36 = new LinkedList();
		Intent intent74 = new Intent();
		intent74.setClassName("com.softspb.time", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent74).create());
		Intent intent75 = new Intent();
		intent75.setClassName("com.google.android.deskclock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent75).create());
		Intent intent76 = new Intent();
		intent76.setClassName("com.android.deskclock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent76).create());
		Intent intent77 = new Intent();
		intent77.setClassName("com.android.alarmclock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent77).create());
		Intent intent78 = new Intent();
		intent78.setClassName("com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl");
		linkedlist36.add(itagfactory.getPatternBuilder(intent78).create());
		Intent intent79 = new Intent();
		intent79.setClassName("com.sec.android.app.clockpackage", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent79).create());
		Intent intent80 = new Intent();
		intent80.setClassName("com.motorola.blur.alarmclock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent80).create());
		Intent intent81 = new Intent();
		intent81.setClassName("com.sonyericsson.alarm", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent81).create());
		Intent intent82 = new Intent();
		intent82.setClassName("zte.com.cn.alarmclock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent82).create());
		Intent intent83 = new Intent();
		intent83.setClassName("com.lge.clock", "");
		linkedlist36.add(itagfactory.getPatternBuilder(intent83).create());
		linkedlist.add(itagfactory.create("alarm", linkedlist36));
		LinkedList linkedlist37 = new LinkedList();
		Intent intent84 = new Intent();
		intent84.setAction("android.intent.action.PICK");
		intent84.addCategory("android.intent.category.OPENABLE");
		intent84.setType("vnd.android.cursor.dir/audio");
		IntentPattern.PatternBuilder patternbuilder11 = itagfactory.getPatternBuilder(intent84);
		patternbuilder11.matchPackage(true);
		linkedlist37.add(patternbuilder11.create());
		Intent intent85 = new Intent();
		intent85.setClassName("com.sec.android.app.music", "om.sec.android.app.music.list.activity.MpMainTabActivity");
		linkedlist37.add(itagfactory.getPatternBuilder(intent85).create());
		Intent intent86 = new Intent();
		intent86.setClassName("com.sonyericsson.music", "com.sonyericsson.music.PlayerActivity");
		linkedlist37.add(itagfactory.getPatternBuilder(intent86).create());
		linkedlist.add(itagfactory.create("media", linkedlist37));
		LinkedList linkedlist38 = new LinkedList();
		Intent intent87 = new Intent();
		intent87.setAction("android.intent.action.INSERT");
		intent87.setData(Uri.parse("content://com.android.contacts/contacts"));
		linkedlist38.add(itagfactory.getPatternBuilder(intent87).create());
		linkedlist.add(itagfactory.create("contacts_new", linkedlist38));
		LinkedList linkedlist39 = new LinkedList();
		Intent intent88 = new Intent();
		intent88.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		linkedlist39.add(itagfactory.getPatternBuilder(intent88).create());
		Intent intent89 = new Intent();
		intent89.setAction("android.intent.action.VIEW");
		intent89.setData(Uri.parse("geo://"));
		IntentPattern.PatternBuilder patternbuilder12 = itagfactory.getPatternBuilder(intent89);
		patternbuilder12.matchPackage(false);
		linkedlist39.add(patternbuilder12.create());
		linkedlist.add(itagfactory.create("maps", linkedlist39));
		return linkedlist;
	}
}