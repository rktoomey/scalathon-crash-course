package prasinous;

/**
 * Modified from a simply dreadful example available at from the "House of Break" spin meisters at:
 * http://download.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
 */
public class SwitchMonth {

    public static void main(String[] args) {

        int month = 8;

        System.out.println(findMonth(month));
    }

    private static String findMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Invalid month";

        }
    }
}