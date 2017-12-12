package in.co.job4you;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", // will not be used
        mailTo = "basantjavajob@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.error_crash)

public class MyApplication extends Application
{
    MyApplication application_File = null;
    public MyApplication()
    {
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        application_File = this;
        ACRA.init(this);
        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy poli = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(poli);
        }
    }
}
