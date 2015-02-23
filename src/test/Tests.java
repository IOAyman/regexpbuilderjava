import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class Tests {

    @Test
    public void startOfLine() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(1).of("p")
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(!regex.matcher("qp").matches());
    }

    @Test
    public void endOfLine() {
        Pattern regex = new RegExpBuilder()
                .exactly(1).of("p")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(!regex.matcher("pq").matches());
    }

    @Test
    public void eitherOr() {
        RegExpBuilder p1 = new RegExpBuilder().exactly(1).of("p");
        RegExpBuilder p2 = new RegExpBuilder().exactly(2).of("q");
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .either(p1)
                .or(p2)
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(regex.matcher("qq").matches());
        assertTrue(!regex.matcher("pqq").matches());
        assertTrue(!regex.matcher("qqp").matches());
    }

    @Test
    public void orChain() {
        RegExpBuilder p1 = new RegExpBuilder().exactly(1).of("p");
        RegExpBuilder p2 = new RegExpBuilder().exactly(1).of("q");
        RegExpBuilder p3 = new RegExpBuilder().exactly(1).of("r");
        Pattern regex = new RegExpBuilder()
                .either(p1)
                .or(p2)
                .or(p3)
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(regex.matcher("q").matches());
        assertTrue(regex.matcher("r").matches());
        assertTrue(!regex.matcher("s").matches());
    }

    @Test
    public void orString() {
        Pattern regex = new RegExpBuilder()
                .either("p")
                .or("q")
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(regex.matcher("q").matches());
        assertTrue(!regex.matcher("r").matches());
    }

    @Test
    public void exactly() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(3).of("p")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("ppp").matches());
        assertTrue(!regex.matcher("pp").matches());
        assertTrue(!regex.matcher("pppp").matches());
    }

    @Test
    public void min() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .min(2).of("p")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("pp").matches());
        assertTrue(regex.matcher("ppp").matches());
        assertTrue(regex.matcher("ppppppp").matches());
        assertTrue(!regex.matcher("p").matches());
    }

    @Test
    public void max() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .max(3).of("p")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("p").matches());
        assertTrue(regex.matcher("pp").matches());
        assertTrue(regex.matcher("ppp").matches());
        assertTrue(!regex.matcher("pppp").matches());
        assertTrue(!regex.matcher("pppppppp").matches());
    }

    @Test
    public void minMax() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .min(3).max(7).of("p")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("ppp").matches());
        assertTrue(regex.matcher("ppppp").matches());
        assertTrue(regex.matcher("ppppppp").matches());
        assertTrue(!regex.matcher("pp").matches());
        assertTrue(!regex.matcher("p").matches());
        assertTrue(!regex.matcher("pppppppp").matches());
        assertTrue(!regex.matcher("pppppppppppp").matches());
    }

    @Test
    public void of() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(2).of("p p p ")
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("p p p p p p ").matches());
        assertTrue(!regex.matcher("p p p p pp").matches());
    }

    @Test
    public void ofAny() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(3).ofAny()
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("pqr").matches());
    }

    @Test
    public void ofGroup() {
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(3).of("p").asGroup()
                .exactly(1).of("q")
                .exactly(1).ofGroup(1)
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("pppqppp").matches());
    }

    @Test
    public void from() {
        char[] someLetters = { 'p', 'q', 'r' };
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(3).from(someLetters)
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("ppp").matches());
        assertTrue(regex.matcher("qqq").matches());
        assertTrue(regex.matcher("ppq").matches());
        assertTrue(regex.matcher("rqp").matches());
        assertTrue(!regex.matcher("pyy").matches());
    }

    @Test
    public void notFrom() {
        char[] someLetters = { 'p', 'q', 'r' };
        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(3).notFrom(someLetters)
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("lmn").matches());
        assertTrue(!regex.matcher("mnq").matches());
    }

    @Test
    public void like() {
        RegExpBuilder r = new RegExpBuilder()
                .min(1).of("p")
                .min(2).of("q");

        Pattern regex = new RegExpBuilder()
                .startOfLine()
                .exactly(2).like(r)
                .endOfLine()
                .getRegExp();

        assertTrue(regex.matcher("pqqpqq").matches());
        assertTrue(!regex.matcher("qppqpp").matches());
    }

    @Test
    public void reluctantly() {
        Pattern regex = new RegExpBuilder()
                .exactly(2).of("p")
                .min(2).ofAny().reluctantly()
                .exactly(2).of("p")
                .getRegExp();
        Matcher m = regex.matcher("pprrrrpprrpp");
        m.find();

        assertTrue(m.end() == 8);
    }

    @Test
    public void ahead() {
        RegExpBuilder r = new RegExpBuilder()
                .exactly(1).of("lang");
        Pattern regex = new RegExpBuilder()
                .exactly(1).of("dart")
                .ahead(r)
                .getRegExp();
        Matcher m = regex.matcher("dartlang");
        m.find();

        assertTrue(m.group().equals("dart"));
        assertTrue(!regex.matcher("dartpqr").matches());
    }

    @Test
    public void notAhead() {
        RegExpBuilder r = new RegExpBuilder()
                .exactly(1).of("pqr");
        Pattern regex = new RegExpBuilder()
                .exactly(1).of("dart")
                .notAhead(r)
                .getRegExp();

        assertTrue(regex.matcher("dartlang").find());
        assertTrue(!regex.matcher("dartpqr").matches());
    }

    @Test
    public void asGroup() {
        Pattern regex = new RegExpBuilder()
                .min(1).max(3).of("p")
                .exactly(1).of("dart").asGroup()
                .exactly(1).from(new char[] { 'p', 'q', 'r' })
                .getRegExp();
        Matcher m = regex.matcher("pdartq");
        m.find();

        assertTrue(m.group(1).equals("dart"));
    }
}
