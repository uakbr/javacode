package com.openspaceservices.user.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Vishal Manval
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CommonUtil {

    private static MessageDigest md;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
    private static String secretKey = "OptimEyes2020";
    private static String salt = "12345678";

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNullAndEmpty(String data) {
        return (data == null || "".equals(data.trim()));
    }

    public static boolean isListNotNullAndEmpty(List list) {
        return (list != null && !list.isEmpty());
    }

    public static <Source, Destination> List<Destination> copyListBeanProperty(Iterable<Source> sourceList, Class Destiniation) throws InstantiationException, IllegalAccessException {
        List<Destination> list = new ArrayList<Destination>();
        for (Source source : sourceList) {
            list.add((Destination) copyBeanProperties(source, Destiniation));
        }
        return list;
    }

    public static <Source, Destination> Set<Destination> copySetBeanProperty(Iterable<Source> sourceSet, Class Destiniation) throws InstantiationException, IllegalAccessException {
        Set<Destination> set = new HashSet<Destination>();
        for (Source source : sourceSet) {
            set.add((Destination) copyBeanProperties(source, Destiniation));
        }
        return set;
    }

    public static <Source, Destination> Destination copyBeanProperties(Source source, Class Destination) throws InstantiationException, IllegalAccessException {
        Destination destination = (Destination) Destination.newInstance();
        BeanUtils.copyProperties(source, destination);
        return destination;
    }

    public static <Source, Destination> void copyProperties(Source source, Destination destination) throws Exception {
        for (Method method : source.getClass().getDeclaredMethods()) {
            try {
                String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    methodName = methodName.replaceFirst("get", "set");
                    Object value = method.invoke(source, null);
                    if (value != null) {
                        Method method2 = destination.getClass().getMethod(methodName, value.getClass());
                        method2.invoke(destination, value);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * @param pass
     * @return
     */
    public static String encryptPassword(String pass) {
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;

    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static Timestamp getStartDayTimestamp(String date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern(pattern);
            java.util.Date date2 = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date2);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            return new Timestamp(calendar.getTimeInMillis());
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static Timestamp getEndDayTimestamp(String date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern(pattern);
            java.util.Date date2 = dateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date2);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return new Timestamp(calendar.getTimeInMillis());
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static Timestamp toTimeStamp(String date, String pattern) {
        try {
            dateFormat.applyPattern(pattern);
            return new Timestamp(dateFormat.parse(date).getTime());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String toDateStringFormat(Timestamp date, String pattern) {
        if (null != date) {
            dateFormat.applyPattern(pattern);
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String toDateStringFormat(Date date, String pattern) {
        if (null != date) {
            dateFormat.applyPattern(pattern);
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

}
