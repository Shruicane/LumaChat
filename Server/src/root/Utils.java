package root;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
