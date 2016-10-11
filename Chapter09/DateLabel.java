import java.sql.Timestamp;

/**
 * DateLabel provides some basic methods
 * for formatting Date and Timestamp objects.
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class DateLabel {

    private static final long One_Day_In_Milliseconds = 86400000;

    /**
     * This date label represents "Today".
     */
    public static final String DATE_LABEL_TODAY = "Today";

    /**
     * This date label represents "Yesterday".
     */
    public static final String DATE_LABEL_YESTERDAY = "Yesterday";

    /**
     * This date label represents "This Month".
     */
    public static final String DATE_LABEL_THIS_MONTH = "This Month";

    /**
     * This date label represents "Older" (older than a month).
     */
    public static final String DATE_LABEL_OLDER = "Older";

    /**
     * This date label represents "none" (when
     * timestmamp is null/undefined).
     */
    public static final String DATE_LABEL_NONE = "";


    /**
     * Get the current timestamp.
     * @return the current timestamp.
     */
    public static java.sql.Timestamp getTimestamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

    /**
     * Get the Date Label.
     * @param ts the timestamp you want to get a data label
     * @param now the timestamp you want to compare to
     * @return the date label for a given timestamp.
     */
    public static String getDateLabel(java.sql.Timestamp ts,
                                      java.sql.Timestamp now) {
        if (ts == null) {
            return DATE_LABEL_NONE;
        }

        if (now == null) {
            return DATE_LABEL_NONE;
        }

        long tsTime = ts.getTime();
        long nowTime = now.getTime();
        long quotient = (nowTime - tsTime)/One_Day_In_Milliseconds;

        if (quotient < 1) {
            return DATE_LABEL_TODAY;
        }
        else if (quotient < 2) {
            return DATE_LABEL_YESTERDAY;
        }
        else if (quotient < 30) {
            return DATE_LABEL_THIS_MONTH;
        }
        else {
            return DATE_LABEL_OLDER;
        }
    }

    public static void main(String[] args) {
        java.sql.Timestamp now = getTimestamp();

        java.sql.Timestamp ts1 = getTimestamp();
        System.out.println(getDateLabel(ts1, now));
        System.out.println(ts1.toString());
        System.out.println("-------------");

        // timestamp in format yyyy-mm-dd hh:mm:ss.fffffffff
        java.sql.Timestamp ts22 =
            java.sql.Timestamp.valueOf("2005-04-06 09:01:10");
        System.out.println(getDateLabel(ts22, now));
        System.out.println(ts22.toString());
        System.out.println("-------------");

        java.sql.Timestamp ts2 =
            java.sql.Timestamp.valueOf("2005-03-26 10:10:10");
        System.out.println(getDateLabel(ts2, now));
        System.out.println(ts2.toString());
        System.out.println("-------------");

        java.sql.Timestamp ts3 =
            java.sql.Timestamp.valueOf("2004-07-18 10:10:10");
        System.out.println(getDateLabel(ts3, now));
        System.out.println(ts3.toString());
        System.out.println("-------------");

        java.sql.Timestamp ts4 =
            java.sql.Timestamp.valueOf("2004-06-20 10:10:10");
        System.out.println(getDateLabel(ts4, now));
        System.out.println(ts4.toString());
        System.out.println("-------------");
    }
}

