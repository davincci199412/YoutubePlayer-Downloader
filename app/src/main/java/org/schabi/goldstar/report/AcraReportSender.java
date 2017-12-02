package org.schabi.goldstar.report;

import android.content.Context;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.schabi.goldstar.R;


public class AcraReportSender implements ReportSender {

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        ErrorActivity.reportError(context, report,
                ErrorActivity.ErrorInfo.make(ErrorActivity.UI_ERROR,"none",
                        "App crash, UI failure", R.string.app_ui_crash));
    }
}
