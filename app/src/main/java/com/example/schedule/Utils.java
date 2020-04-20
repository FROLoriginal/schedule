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

    public static class Time {

        public static final int CURRENT_TIME_MORE = 0;
        public static final int CURRENT_TIME_BETWEEN = 1;
        public static final int CURRENT_TIME_LESS = 2;

        public static int isCurrentTimeBetween(String from, String to, int dayOfWeek, Calendar currentTime) {

            int firstHalfFrom = Integer.valueOf(from.split(":")[0]);
            int secondHalfFrom = Integer.valueOf(from.split(":")[1]);
            int firstHalfTo = Integer.valueOf(to.split(":")[0]);
            int secondHalfTo = Integer.valueOf(to.split(":")[1]);

            Calendar firstTime = Calendar.getInstance();
            Calendar secondTime = Calendar.getInstance();

            firstTime.set(firstTime.get(Calendar.YEAR), firstTime.get(Calendar.MONTH),
                    convertDayOfWeekToUS(dayOfWeek + 1), firstHalfFrom, secondHalfFrom);

            secondTime.set(secondTime.get(Calendar.YEAR), secondTime.get(Calendar.MONTH),
                    convertDayOfWeekToUS(dayOfWeek + 1), firstHalfTo, secondHalfTo);

            currentTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DAY_OF_WEEK), currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE));

            long current = currentTime.getTimeInMillis();
            long second = secondTime.getTimeInMillis();

            if (current >= firstTime.getTimeInMillis() && current <= second) {
                return CURRENT_TIME_BETWEEN;
            } else if (current > second) {
                return CURRENT_TIME_MORE;
            } else return CURRENT_TIME_LESS;

        }

        public static int getDayOfWeekWhereFirstDayOfWeekIsMonday(int day) {

            return day == 1 ? 7 : day - 1;

        }

        private static int convertDayOfWeekToUS(int day) {

            return day == 7 ? 1 : day + 1;

        }
    }
}
