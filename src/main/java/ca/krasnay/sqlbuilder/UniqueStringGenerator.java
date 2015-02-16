package ca.krasnay.sqlbuilder;

import java.security.SecureRandom;

/**
 * Generator of unique strings, suitable to use as unique IDs. The resulting
 * strings are Base62-encoded, using the digits 0-9 and the letters a-z and A-Z.
 *
 * <p>You should choose a string length that is reasonably unlikely to collide
 * with an existing value; however, note that longer strings bloat the size
 * of database indexes, possibly slowing your data access. You can estimate
 * your chance of collision using the <a href="http://en.wikipedia.org/wiki/Birthday_problem">Birthday Problem</a>.
 * The chance of collision is approximately <code>n^2 / (2 * 62^c)</code>, where <code>n</code>
 * is the number of rows and <code>c</code> is the length of the string. Using
 * this calculation, we see that with a 12 character string and a billion samples
 * there is only about a 0.015% chance of collision, while with an 8 character string
 * and 100,000 samples there is about a 0.002% chance of collision. Note however
 * that the chance of collision increases with the square of the number of samples,
 * so an 8 character string with a million samples increases the odds of
 * collision to 0.2%.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class UniqueStringGenerator implements Supplier<String> {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private int length;

    public UniqueStringGenerator(int length) {
        this.length = length;
    }

    @Override
    public String get() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int n = BASE62.length();
        for (int i = 0; i < length; i++) {
            sb.append(BASE62.charAt(random.nextInt(n)));
        }
        return sb.toString();
    }

}
