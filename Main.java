package com.company;

import sun.awt.SunToolkit;

import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;

public class Main {
    static int count_nuls(String x) {
        int n = 0;
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '0') n++;
        }
        return n;
    }

    static long from_bin_to_dec(String x) {
        long n = 0; // переводим из двоичной в десятичную
        for (int i = x.length() - 1; i > 0; i--) {
            n += Math.pow(2 * Integer.parseInt("" + x.charAt(x.length() - i - 1)), i);
        }
        if (x.charAt(x.length() - 1) == '1') n++; // последнее я вынес отдельно так как 0^0 тоже для него равен 1
        return n;
    }

    static String from_dec_to_bin(long n) {
        StringBuilder ans = new StringBuilder();
        while (n != 0) {
            ans.insert(0, n % 2);
            n = n / 2;
        }
        return ans.toString();
    }

    static String negative(String x) {
        StringBuilder x1 = new StringBuilder(); // создаем ивертированную строку (меняем 0 на 1 и наоборот)
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1') x1.append('0');
            else x1.append('1');
        }
        long n = from_bin_to_dec(x1.toString()); // переводим из двоичной в десятичную
        n++; // увеличиваем число на 1
        x = from_dec_to_bin(n); // переводим из десятичной в двоичную
        x = String.format("%0" + x1.length() + "d", Long.parseLong(x));
        // дополняем ответ до изначальной длины (вдруг мы при переводе строк в числа потеряли ведущие нолики)
        return x;
    }

    static String to_len(int len, char ds) { // создает строку длины len из символов ds
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; i++) {
            s.append(ds);
        }
        return s.toString();
    }

    static String convert_float(double b, int maxl) {
        String bin = ".";
        for (int i = 0; i < maxl; i++) {
            b = b * 2 - (int) b * 2;
            bin = bin + (int) b;
            if (b == 1.0) {
                break;
            }
        }
        return bin;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String m1 = scanner.next();
        double nn = Double.parseDouble(m1);
        if (nn == (double) 1 / 0) {
            System.out.println("Ваше число слишком большое и не влазит даже в double");
            return; // и сразу ломаем прогу
        }
        String[] m = m1.split("\\.");
        // проверка на корректность входных данных
        if (m.length > 2) {
            System.out.println("Возможно, Вы ввели что-то некорректно. Во введенном числе должна быть максимум одна точка.");
            return; // и сразу ломаем прогу
        }
        if (m.length == 1 || count_nuls(m[1]) == m[1].length()) {
            // мы выводим byte, short, int и long лишь если наше число изначально целое - нет дробной части или она нулевая
            long n;
            try { // ещё одна проверка на корректность и одновременное преобразование целой части в число
                n = Long.parseLong(m[0]);
            } catch (NumberFormatException e) {
                System.out.println("Такое чувство что вы ввели число некорректно :)");
                return; // тоже ломаем прогу
            }

            boolean f = false;
            if (n < 0) {
                f = true;
                n = -n; // если наше число отрицательное, то пока что будем делать вид что оно положительное, а потом инвертируем
            }
            String ans = from_dec_to_bin(n);
            char dop_symbol;
            if (f) {
                ans = negative(ans); // если число было отрицательным, переделываем его бинарную запись
                dop_symbol = '1'; // символ, которым будем дополнять ответ до нужной длины
            } else {
                dop_symbol = '0';
            }
            String s;
            int x = ans.length();
            if (8 >= x) {
                s = to_len(8 - x, dop_symbol) + ans; // дополняем до нужной длины
                System.out.println("byte: " + s.replaceAll("(.{8})", "$1 ")); // выводим, красиво расставляя пробелы
            }
            if (16 >= x) {
                s = to_len(16 - x, dop_symbol) + ans;
                System.out.println("short: " + s.replaceAll("(.{8})", "$1 "));
            }
            if (32 >= x) {
                s = to_len(32 - x, dop_symbol) + ans;
                System.out.println("int: " + s.replaceAll("(.{8})", "$1 "));
            }
            if (64 >= x) {
                s = to_len(64 - x, dop_symbol) + ans;
                System.out.println("long: " + s.replaceAll("(.{8})", "$1 "));
            }
        }

        // мы вывели все целые формы, если они были и теперь осталось сделать лишь дробные
        String ans = "";
        if (m[0].charAt(0) == '-') ans += "1 "; //  определяем первый бит ответа
        else ans += "0 ";
        long whole;
        try { // ещё одна проверка на корректность и одновременное преобразование целой части в число
            whole = Long.parseLong(m[0]);
        } catch (NumberFormatException e) {
            System.out.println("Такое чувство что вы ввели число некорректно :)");
            return; // тоже ломаем прогу
        }
        if (whole < 0) whole = -whole;
        String bin_whole = from_dec_to_bin(whole);
        double fractional;
        if (m.length == 1) fractional = 0.0;
        else {
            try { // преобразование дробной части в число
                fractional = Double.parseDouble("0." + m[1]);
            } catch (NumberFormatException e) {
                System.out.println("Такое чувство что вы ввели число некорректно :)");
                return; // тоже ломаем прогу
            }
        }
        // все комменты к коду для float будут в коде для double, т.к. они идентичны
        if (Double.toString(fractional).equals(Float.toString((float) fractional))) {
            String ans1 = ans;
            String s1 = "", s2 = "";
            s2 = convert_float(fractional, 23 - bin_whole.length() + 1);
            s1 += bin_whole + s2;
            String[] ss = s1.split("\\.");
            int por = s1.length() - 2 - ss[1].length() + 127;
            String porr = from_dec_to_bin(por);
            ans1 += to_len(8 - porr.length(), '0') + porr + " ";
            s2 = ss[0].replaceFirst("1", "") + ss[1];
            ans1 += s2 + to_len(23 - s2.length(), '0');
            System.out.println("float: " + ans1);
        }
        String s1 = "", s2 = "";
        s2 = convert_float(fractional, 52 - bin_whole.length()); // всякая куча второстепенных переменных
        s1 += bin_whole + s2; // бинарная запись числа
        String[] ss = s1.split("\\.");
        int por = s1.length() - 2 - ss[1].length() + 1023; // получаем смещенный порядок
        String porr = from_dec_to_bin(por);
        ans += to_len(11 - porr.length(), '0') + porr + " "; // формируем ответ из всех частей
        s2 = ss[0].replaceFirst("1", "") + ss[1];
        ans += s2 + to_len(52 - s2.length(), '0');
        System.out.println("double: " + ans);
    }
}