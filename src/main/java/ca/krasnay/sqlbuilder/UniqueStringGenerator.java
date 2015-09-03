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
 * <p>To avoid an embarrassing accident, we maintain a blacklist of offensive
 * words, and avoid generating a sequence that contains one of these
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class UniqueStringGenerator implements Supplier<String> {

    public static final String BASE36 = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private String[] blacklist = "anal,anus,ass,boob,butt,clit,cock,cum,cunt,dick,fuck,gay,nigg,poon,poop,porn,pube,sex,shit,smut,tit,twat,vag".split(",");

    private String alphabet;

    private int length;

    public UniqueStringGenerator(int length) {
        this(BASE62, length);
    }

    public UniqueStringGenerator(String alphabet, int length) {
        this.alphabet = alphabet;
        this.length = length;
    }

    boolean isOffensive(String s) {

        String normalized = s.toLowerCase()
                .replace("0", "o")
                .replace("1", "i")
                .replace("5", "s")
                .replace("v", "u");

        for (String badWord : blacklist) {
            if (normalized.contains(badWord)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String get() {
        while (true) {
            String s = generate();
            if (!isOffensive(s)) {
                return s;
            }
        }
    }

    private String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int n = alphabet.length();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(n)));
        }
        return sb.toString();
    }

}
