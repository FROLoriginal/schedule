package com.example.schedule;

import java.util.Calendar;

public class Utils {

    public static final int OTHER = 0;
    public static final int SEMINAR = 1;
    public static final int LABORATORY_WORK = 2;
    public static final int LECTURE = 3;

    private static final String sem = "Пр";
    private static final String lab = "Лаб";
    private static final String lect = "Лекц";

    public static int typeOfSubject(String subjectName) {

        final String sem = "Пр";
        final String lab = "Лаб";
        final String lect = "Лекц";

        String[] s = subjectName.split("\\.");
        if (s.length > 0) {
            switch (s[0]) {

                case sem:
                    return SEMINAR;
                case lab:
                    return LABORATORY_WORK;
                case lect:
                    return LECTURE;
                default:
                    return OTHER;
            }
        } else return OTHER;
    }

    public static String deleteTypeOfSubjectPart(String subjectName) {

        String[] s = subjectName.split("\\.");
        if (s.length > 0) {
            switch (s[0]) {

                case sem:
                case lab:
                case lect:
                    return s[1].trim();
                default:
                    return subjectName;
            }
        }
        return subjectName;
    }

    public static String toUpperCaseFirstLetter(String input) throws NullPointerException {
        char firstLetter = input.charAt(0);
        input = input.substring(1);
        return Character.toUpperCase(firstLetter) + input;
    }

    public static class Time {

        public static final int LESSON_IS_NOT_EXISTS = -1;
        public static final int LESSON_IS_OVER = 0;
        public static final int LESSON_IS_NOT_OVER = 1;
        public static final int LESSON_WILL_START = 2;

        public static int lessonStatus(String from, String to, int dayOfWeek) {
            try {
                int firstHalfFrom = Integer.valueOf(from.split(":")[0]);
                int secondHalfFrom = Integer.valueOf(from.split(":")[1]);
                int firstHalfTo = Integer.valueOf(to.split(":")[0]);
                int secondHalfTo = Integer.valueOf(to.split(":")[1]);

                Calendar firstTime = Calendar.getInstance();
                Calendar secondTime = Calendar.getInstance();

                firstTime.set(Calendar.DAY_OF_WEEK, convertEUDayOfWeekToUS(dayOfWeek + 1) - 1);
                firstTime.set(Calendar.HOUR_OF_DAY, firstHalfFrom);
                firstTime.set(Calendar.MINUTE, secondHalfFrom);

                secondTime.set(Calendar.DAY_OF_WEEK, convertEUDayOfWeekToUS(dayOfWeek + 1) - 1);
                secondTime.set(Calendar.HOUR_OF_DAY, firstHalfTo);
                secondTime.set(Calendar.MINUTE, secondHalfTo);

                long current = Calendar.getInstance().getTimeInMillis();
                long second = secondTime.getTimeInMillis();

                if (current >= firstTime.getTimeInMillis() && current <= second) {
                    return LESSON_IS_NOT_OVER;
                } else if (current > second) {
                    return LESSON_IS_OVER;
                } else return LESSON_WILL_START;
            } catch (NullPointerException ex) {
                return LESSON_IS_NOT_EXISTS;
            }
        }

        public static int convertUSDayOfWeekToEU(int day) {

            return day == 1 ? 7 : day - 1;
        }

        public static int convertEUDayOfWeekToUS(int day) {

            return day == 7 ? 1 : day + 1;
        }

        /*
        Timeline looks like

        (12:00)x1 - - - - - - - - (13:20)x2
                        (12:30)y1 - - - - - - - - - (14:00)y2
         */
        public static boolean isTimeIntersect(String t1_from, String t1_to,
                                              String t2_from, String t2_to) {

            int[] t1_from_ = stringArrToInt(t1_from.split(":"));
            int[] t1_to_ = stringArrToInt(t1_to.split(":"));
            int[] t2_from_ = stringArrToInt(t2_from.split(":"));
            int[] t2_to_ = stringArrToInt(t2_to.split(":"));

            int x1 = t1_from_[0] * 60 + t1_from_[1];
            int x2 = t1_to_[0] * 60 + t1_to_[1];
            int y1 = t2_from_[0] * 60 + t2_from_[1];
            int y2 = t2_to_[0] * 60 + t2_to_[1];

            return (x1 < y1 && y1 < x2 ||
                    x1 < y2 && y2 < x2 ||
                    y1 < x1 && x1 < y2 ||
                    y1 < x2 && x2 < y2);

        }

        public static int timeToMinutes(String time){
            String[] arr = time.split(":");
            return Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]);

        }

        private static int[] stringArrToInt(String[] arr) {

            int[] result = new int[arr.length];

            for (int i = 0; i < result.length; i++) {
                result[i] = Integer.parseInt(arr[i]);
            }
            return result;

        }

    }
}
