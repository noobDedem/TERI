package id.fantasticfive.teri.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import id.fantasticfive.teri.R;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

/**
 * Created by Demas on 11/13/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    public static final String NAMA_MATKUL = "namaMatkul";
    public static final String RUANG_JAM = "ruangJam";
    public static final String ID = "id";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String matakuliah = intent.getStringExtra(NAMA_MATKUL);
        String ruangJam = intent.getStringExtra(RUANG_JAM);
        int notifId = intent.getIntExtra(ID, 0);

        showAlarmNotification(context, matakuliah, ruangJam, notifId);
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId){
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setPriority(PRIORITY_MAX);

        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Log.e("REPEAT",time);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = 0;
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, requestCode, intent, 0);
        Intent downloadServiceIntent = new Intent(context, ServiceNotification.class);
        context.startService(downloadServiceIntent);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }

    public void setAlarmUjian(Context context, String type, String date, String time, String matkul, String ruang, int notifId){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NAMA_MATKUL, matkul);
        intent.putExtra(RUANG_JAM, ruang + "   " + time);
        intent.putExtra(ID, notifId);

        Log.e("ONE TIME",date+" "+time);
        String dateArray[] = date.split("-");
        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, notifId, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(context, "Alarm ujian " + date + " Jam " + time, Toast.LENGTH_SHORT).show();
    }

    public void setAlarm(Context context, String type, String date, String time, String matkul, String ruang, int notifId){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NAMA_MATKUL, matkul);
        intent.putExtra(RUANG_JAM, ruang + "   " + time);
        intent.putExtra(ID, notifId);

        Log.d("Listener", "waktu" + date + time);
        String timeArray[] = time.split(":");
        int hari = convertHari(date);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, hari);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, notifId, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY*7, pendingIntent);
        Toast.makeText(context, "Repeating hari " + date + " Jam " + time, Toast.LENGTH_SHORT).show();
    }


    public void cancelAlarm(Context context, int notifId){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
//        int requestCode = type.equalsIgnoreCase(TYPE_KULIAH) ? NOTIF_ID_ONETIME : NOTIF_ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public int convertHari (String hari) {
        if (hari.equals("Senin")) {
            return Calendar.MONDAY;
        } else if (hari.equals("Selasa")) {
            return Calendar.TUESDAY;
        } else if (hari.equals("Rabu")) {
            return Calendar.WEDNESDAY;
        } else if (hari.equals("Kamis")) {
            return Calendar.THURSDAY;
        } else if (hari.equals("Jumat")) {
            return Calendar.FRIDAY;
        }
        return 0;
    }
}
