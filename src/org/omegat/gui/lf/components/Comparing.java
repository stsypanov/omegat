package org.omegat.gui.lf.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:20
 */
public class Comparing {
    private Comparing() { }

    public static <T> boolean equal(@Nullable T arg1, @Nullable T arg2){
        if (arg1 == arg2) return true;
        if (arg1 == null || arg2 == null){
            return false;
        }
        if (arg1 instanceof Object[] && arg2 instanceof Object[]){
            Object[] arr1 = (Object[])arg1;
            Object[] arr2 = (Object[])arg2;
            return Arrays.equals(arr1, arr2);
        }
        if (arg1 instanceof CharSequence && arg2 instanceof CharSequence) {
            return equal((CharSequence)arg1, (CharSequence)arg2, true);
        }
        return arg1.equals(arg2);
    }

    public static <T> boolean equal(@Nullable T[] arr1, @Nullable T[] arr2){
        if (arr1 == null || arr2 == null){
            return Arrays.equals(arr1, arr2);
        }
        return Arrays.equals(arr1, arr2);
    }

    public static boolean equal(CharSequence s1, CharSequence s2) {
        return equal(s1, s2, true);
    }

    public static boolean equal(@Nullable String arg1, @Nullable String arg2) {
        return arg1 == null ? arg2 == null : arg1.equals(arg2);
    }

    public static boolean equal(@Nullable CharSequence s1, @Nullable CharSequence s2, boolean caseSensitive) {
        if (s1 == s2) return true;
        if (s1 == null || s2 == null) return false;

        // Algorithm from String.regionMatches()

        if (s1.length() != s2.length()) return false;
        int to = 0;
        int po = 0;
        int len = s1.length();

        while (len-- > 0) {
            char c1 = s1.charAt(to++);
            char c2 = s2.charAt(po++);
            if (c1 == c2) {
                continue;
            }
            if (!caseSensitive && StringUtilRt.charsEqualIgnoreCase(c1, c2)) continue;
            return false;
        }

        return true;
    }

    public static boolean equal(@Nullable String arg1, @Nullable String arg2, boolean caseSensitive){
        if (arg1 == null || arg2 == null){
            return arg1.equals(arg2);
        }
        else{
            return caseSensitive ? arg1.equals(arg2) : arg1.equalsIgnoreCase(arg2);
        }
    }

    public static boolean strEqual(@Nullable String arg1, @Nullable String arg2){
        return strEqual(arg1, arg2, true);
    }

    public static boolean strEqual(@Nullable String arg1, @Nullable String arg2, boolean caseSensitive){
        return equal(arg1 == null ? "" : arg1, arg2 == null ? "" : arg2, caseSensitive);
    }

    public static <T> boolean haveEqualElements(@NotNull Collection<T> a, @NotNull Collection<T> b) {
        if (a.size() != b.size()) {
            return false;
        }

        Set<T> aSet = new HashSet<>(a);
        for (T t : b) {
            if (!aSet.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean haveEqualElements(@Nullable T[] a, @Nullable T[] b) {
        if (a == null || b == null) {
            return Arrays.equals(a, b);
        }

        if (a.length != b.length) {
            return false;
        }

        Set<T> aSet = new HashSet<>(Arrays.asList(a));
        for (T t : b) {
            if (!aSet.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public static int hashcode(@Nullable Object obj) { return obj == null ? 0 : obj.hashCode(); }
    public static int hashcode(Object obj1, Object obj2) { return hashcode(obj1) ^ hashcode(obj2); }

    public static int compare(byte o1, byte o2) {
        return o1 < o2 ? -1 : o1 == o2 ? 0 : 1;
    }

    public static int compare(boolean o1, boolean o2) {
        return o1 == o2 ? 0 : o1 ? 1 : -1;
    }

    public static int compare(int o1, int o2) {
        return o1 < o2 ? -1 : o1 == o2 ? 0 : 1;
    }

    public static int compare(long o1, long o2) {
        return o1 < o2 ? -1 : o1 == o2 ? 0 : 1;
    }
    public static int compare(double o1, double o2) {
        return o1 < o2 ? -1 : o1 == o2 ? 0 : 1;
    }

    public static int compare(@Nullable byte[] o1, @Nullable byte[] o2) {
        if (Arrays.equals(o1, o2)) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;

        if (o1.length > o2.length) return 1;
        if (o1.length < o2.length) return -1;

        for (int i = 0; i < o1.length; i++) {
            if (o1[i] > o2[i]) return 1;
            else if (o1[i] < o2[i]) return -1;
        }
        return 0;
    }

    public static <T extends Comparable<T>> int compare(@Nullable final T o1, @Nullable final T o2) {
        if (o1 == null) return o2 == null ? 0 : -1;
        if (o2 == null) return 1;
        return o1.compareTo(o2);
    }

    public static <T> int compare(@Nullable final T o1, @Nullable final T o2, @NotNull final Comparator<T> notNullComparator) {
        if (o1 == null) return o2 == null ? 0 : -1;
        if (o2 == null) return 1;
        return notNullComparator.compare(o1, o2);
    }
}
