Android Location Tracking
===

**update**: I realized several people forked this recently. This is an old version. New and optimized one could be found in my other repo: [Tracking map on Android](https://github.com/yyl/Tracking-map-on-android).

This application is used to track user's location regularly and store them into a database in the background. It will work even if user quits it. It has a turn on/off function for user to stop it.

In this application I use `Sevice`, `AlarmManager`, `Fragment`, `SQLiteOpenHelper`, `Content Provider` and `LoaderManager` classess. It runs on SDK 10 (2.3.3) and up. The `Fragment` class requires support package, which could be downloaded using Android SDK manager.

I build it as a research tool; feel free to fork or use it.
