import java.util.Scanner;
import java.lang.Math;

public class Main {
    static int count_of_zeros(String x) { // считает число нулей в строке
        int n = 0;
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '0') n++;
        }
        return n;
    }

    static String to_len(int len, char ds) { // создает строку длины len из символов ds
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; i++) {
            s.append(ds);
        }
        return s.toString();
    }

    static long from_bin_to_dec(String x) { // переводит из двоичной в десятичную
        long n = 0;
        for (int i = x.length() - 1; i > 0; i--) {
            n += Math.pow(2 * Long.parseLong("" + x.charAt(x.length() - i - 1)), i);
        }
        if (x.charAt(x.length() - 1) == '1') n++; // последний символ я вынес отдельно так как 0^0 для java тоже равен 1
        return n;
    }

    static String from_dec_to_bin(long n) { // переводит целое число из десятичной в двоичную
        StringBuilder ans = new StringBuilder();
        while (n != 0) {
            ans.insert(0, n % 2);
            n = n / 2;
        }
        return ans.toString();
    }

    static String float_from_dec_to_bin(double b, int count) { // переводит дробное число из десятичной в двоичную
        StringBuilder ans = new StringBuilder(".");
        for (int i = 0; i < count; i++) {
            b = b * 2 - (int) b * 2;
            ans.append((int) b);
            if (b == 1.0) {
                break;
            }
        }
        if (ans.toString().equals(".")) ans.append("0");
        return ans.toString();
    }

    static String negative(String x) { // превращает бинарную запись числа в бинарную запись его отрицательного варианта
        StringBuilder x1 = new StringBuilder(); // создаем ивертированную строку (меняем 0 на 1 и наоборот)
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1') x1.append('0');
            else x1.append('1');
        }
        long n = from_bin_to_dec(x1.toString()); // переводим из двоичной в десятичную
        n++; // увеличиваем число на 1
        x = from_dec_to_bin(n); // переводим из десятичной в двоичную
        try {
            x = String.format("%0" + x1.length() + "d", Long.parseLong(x));
        } catch (NumberFormatException e) {
            System.out.println("Что-то пошло не так"); // такое происходит при числе, равному минимальному Integer или Long
            System.exit(0);
        }
        // дополняем ответ до изначальной длины (вдруг мы при переводе строк в числа потеряли ведущие нолики)
        return x;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next().replaceAll(",", "\\.");
        double nn;
        try { // тест на корректность ввода
            nn = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            System.out.println("Вы ввели что-то неправильно");
            return; // конец
        }
        if (Double.isInfinite(nn)) {
            System.out.println("Ваше число слишком большое по модулю и не влазит даже в double");
            return; // ломаем прогу
        }

        String[] m = s.split("\\.");
        // проверка на корректность входных данных
        if (m.length > 2) {
            System.out.println("Возможно, Вы ввели что-то некорректно. Во введенном числе должна быть максимум одна точка.");
            return; // ломаем прогу
        }
        if (m.length == 1 || count_of_zeros(m[1]) == m[1].length()) {
            // мы выводим byte, short, int и long лишь если наше число изначально целое - нет дробной части или она нулевая
            long n;
            try {
                n = Long.parseLong(m[0]);
            } catch (NumberFormatException e) {
                System.out.println("Ваше число слишком уж велико");
                return;
            }
            boolean f = false;
            if (n < 0) {
                n = -n; // если наше число отрицательное, то пока что будем делать вид что оно положительное, а потом инвертируем
                f = true; // флажок нам покажет, что число было отрицательным
            }
            String ans = from_dec_to_bin(n);
            char dop_symbol; // символ, которым будем дополнять ответ до нужной длины
            if (f) {
                ans = negative(ans); // если число было отрицательным, переделываем его бинарную запись
                dop_symbol = '1';
            } else {
                dop_symbol = '0';
            }
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
        if (m[0].charAt(0) == '-')
            ans += "1 "; //  определяем первый бит ответа
        else
            ans += "0 ";
        long whole;
        try { // ещё одна проверка на корректность и одновременное преобразование целой части в число
            whole = Long.parseLong(m[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ваше число слишком уж велико");
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
            String bin_float = float_from_dec_to_bin(fractional, 23 - bin_whole.length() + 1);
            String num = bin_whole + bin_float;

            String[] ss = num.split("\\.");
            int por = num.length() - 2 - ss[1].length() + 127;
            String porr = from_dec_to_bin(por);
            ans1 += to_len(8 - porr.length(), '0') + porr + " ";

            s = ss[0].replaceFirst("1", "") + ss[1];
            ans1 += s + to_len(23 - s.length(), '0');

            System.out.println("float: " + ans1);
        }
        String bin_float = float_from_dec_to_bin(fractional, 52 - bin_whole.length()); // бинарная запись дробной части
        String num = bin_whole + bin_float; // бинарная запись числа

        String[] ss = num.split("\\.");
        int por = num.length() - 2 - ss[1].length() + 1023; // получаем смещенный порядок
        String porr = from_dec_to_bin(por);
        ans += to_len(11 - porr.length(), '0') + porr + " "; // добавляет к ответу порядок

        s = ss[0].replaceFirst("1", "") + ss[1];
        ans += s + to_len(52 - s.length(), '0'); // добавляет к ответу мантиссу

        System.out.println("double: " + ans);
    }
}
