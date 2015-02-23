import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegExpBuilder {
    private StringBuilder _literal;
    private Boolean _ignoreCase;
    private Boolean _multiLine;
    private HashSet<Character> _specialCharactersInsideCharacterClass;
    private HashSet<Character> _specialCharactersOutsideCharacterClass;
    private StringBuilder _escapedString;
    private int _min;
    private int _max;
    private String _of;
    private Boolean _ofAny;
    private int _ofGroup;
    private String _from;
    private String _notFrom;
    private String _like;
    private String _either;
    private Boolean _reluctant;
    private Boolean _capture;

    public RegExpBuilder() {
        _literal = new StringBuilder();
        _specialCharactersInsideCharacterClass = new HashSet<>(Arrays.asList(new Character[] { '^', '-', ']' }));
        _specialCharactersOutsideCharacterClass = new HashSet<>(Arrays.asList(new Character[] { '.', '^', '$', '*', '+', '?', '(', ')', '[', '{' }));
        _escapedString = new StringBuilder();
        _clear();
    }

    private void _clear() {
        _ignoreCase = false;
        _multiLine = false;
        _min = -1;
        _max = -1;
        _of = "";
        _ofAny = false;
        _ofGroup = -1;
        _from = "";
        _notFrom = "";
        _like = "";
        _either = "";
        _reluctant = false;
        _capture = false;
    }

    private void _flushState() {
        if (!Objects.equals(_of, "") || _ofAny || _ofGroup > 0 || !Objects.equals(_from, "") || !Objects.equals(_notFrom, "") || !Objects.equals(_like, "")) {
            String captureLiteral = _capture ? "" : "?:";
            String quantityLiteral = getQuantityLiteral();
            String characterLiteral = getCharacterLiteral();
            String reluctantLiteral = _reluctant ? "?" : "";
            _literal.append("(" + captureLiteral + "(?:" + characterLiteral + ")" + quantityLiteral + reluctantLiteral + ")");
            _clear();
        }
    }

    private String getQuantityLiteral() {
        if (_min != -1) {
            if (_max != -1) {
                return "{" + _min + "," + _max + "}";
            }
            return "{" + _min + ",}";
        }
        return "{0," + _max + "}";
    }

    private String getCharacterLiteral() {
        if (!Objects.equals(_of, "")) {
            return _of;
        }
        if (_ofAny) {
            return ".";
        }
        if (_ofGroup > 0) {
            return "\\" + _ofGroup;
        }
        if (!Objects.equals(_from, "")) {
            return "[" + _from + "]";
        }
        if (!Objects.equals(_notFrom, "")) {
            return "[^" + _notFrom + "]";
        }
        if (!Objects.equals(_like, "")) {
            return _like;
        }
        return "";
    }

    public String getLiteral() {
        _flushState();
        return _literal.toString();
    }

    public Pattern getRegExp() {
        _flushState();
        int flags = 0;
        if (_ignoreCase) {
            flags = flags | Pattern.CASE_INSENSITIVE;
        }
        if (_multiLine) {
            flags = flags | Pattern.MULTILINE;
        }
        return Pattern.compile(_literal.toString(), flags);
    }

    public RegExpBuilder ignoreCase() {
        _ignoreCase = true;
        return this;
    }

    public RegExpBuilder multiLine() {
        _multiLine = true;
        return this;
    }

    public RegExpBuilder startOfInput() {
        _literal.append("(?:^)");
        return this;
    }

    public RegExpBuilder startOfLine() {
        multiLine();
        return startOfInput();
    }

    public RegExpBuilder endOfInput() {
        _flushState();
        _literal.append("(?:$)");
        return this;
    }

    public RegExpBuilder endOfLine() {
        multiLine();
        return endOfInput();
    }

    public RegExpBuilder either(RegExpBuilder r) {
        _flushState();
        _either = r.getLiteral();
        return this;
    }

    public RegExpBuilder either(String s) {
        return this.either(new RegExpBuilder().exactly(1).of(s));
    }

    public RegExpBuilder or(RegExpBuilder r) {
        String either = _either;
        String or = r.getLiteral();
        if (Objects.equals(either, "")) {
            _literal.deleteCharAt(_literal.length() - 1);
            _literal.append("|(?:" + or + "))");
        } else {
            _literal.append("(?:(?:" + either + ")|(?:" + or + "))");
        }
        _clear();
        return this;
    }

    public RegExpBuilder or(String s) {
        return this.or(new RegExpBuilder().exactly(1).of(s));
    }

    public RegExpBuilder exactly(int n) {
        _flushState();
        _min = n;
        _max = n;
        return this;
    }

    public RegExpBuilder min(int n) {
        _flushState();
        _min = n;
        return this;
    }

    public RegExpBuilder max(int n) {
        _flushState();
        _max = n;
        return this;
    }

    public RegExpBuilder of(String s) {
        _of = _escapeOutsideCharacterClass(s);
        return this;
    }

    public RegExpBuilder ofAny() {
        _ofAny = true;
        return this;
    }

    public RegExpBuilder ofGroup(int n) {
        _ofGroup = n;
        return this;
    }

    public RegExpBuilder from(char[] s) {
        _from = _escapeInsideCharacterClass(new String(s));
        return this;
    }

    public RegExpBuilder notFrom(char[] s) {
        _notFrom = _escapeInsideCharacterClass(new String(s));
        return this;
    }

    public RegExpBuilder like(RegExpBuilder r) {
        _like = r.getLiteral();
        return this;
    }

    public RegExpBuilder reluctantly() {
        _reluctant = true;
        return this;
    }

    public RegExpBuilder ahead(RegExpBuilder r) {
        _flushState();
        _literal.append("(?=" + r.getLiteral() + ")");
        return this;
    }

    public RegExpBuilder notAhead(RegExpBuilder r) {
        _flushState();
        _literal.append("(?!" + r.getLiteral() + ")");
        return this;
    }

    public RegExpBuilder asGroup() {
        _capture = true;
        return this;
    }

    public RegExpBuilder then(String s) {
        return exactly(1).of(s);
    }

    public RegExpBuilder find(String s) {
        return then(s);
    }

    public RegExpBuilder some(char[] s) {
        return min(1).from(s);
    }

    public RegExpBuilder maybeSome(char[] s) {
        return min(0).from(s);
    }

    public RegExpBuilder maybe(String s) {
        return max(1).of(s);
    }

    public RegExpBuilder something() {
        return min(1).ofAny();
    }

    public RegExpBuilder somethingBut(String s) {
        if (s.length() == 1) {
            return exactly(1).notFrom(s.toCharArray());
        }
        notAhead(new RegExpBuilder().exactly(1).of(s));
        return min(1).ofAny();
    }

    public RegExpBuilder anything() {
        return min(0).ofAny();
    }

    public RegExpBuilder anythingBut(String s) {
        if (s.length() == 1) {
            return max(1).notFrom(s.toCharArray());
        }
        notAhead(new RegExpBuilder().exactly(1).of(s));
        return min(0).ofAny();
    }

    public RegExpBuilder any() {
        return exactly(1).ofAny();
    }

    public RegExpBuilder lineBreak() {
        return either("\r\n").or("\r").or("\n");
    }

    public RegExpBuilder lineBreaks() {
        return like(new RegExpBuilder().lineBreak());
    }

    public RegExpBuilder whitespace() {
        if (_min == -1 && _max == -1) {
            return exactly(1).of("\\s");
        }
        _like = "\\s";
        return this;
    }

    public RegExpBuilder notWhitespace() {
        if (_min == -1 && _max == -1) {
            return exactly(1).of("\\S");
        }
        _like = "\\S";
        return this;
    }

    public RegExpBuilder tab() {
        return exactly(1).of("\t");
    }

    public RegExpBuilder tabs() {
        return like(new RegExpBuilder().tab());
    }

    public RegExpBuilder digit() {
        return exactly(1).of("\\d");
    }

    public RegExpBuilder notDigit() {
        return exactly(1).of("\\D");
    }

    public RegExpBuilder digits() {
        return like(new RegExpBuilder().digit());
    }

    public RegExpBuilder notDigits() {
        return like(new RegExpBuilder().notDigit());
    }

    public RegExpBuilder letter() {
        exactly(1);
        _from = "A-Za-z";
        return this;
    }

    public RegExpBuilder notLetter() {
        exactly(1);
        _notFrom = "A-Za-z";
        return this;
    }

    public RegExpBuilder letters() {
        _from = "A-Za-z";
        return this;
    }

    public RegExpBuilder notLetters() {
        _notFrom = "A-Za-z";
        return this;
    }

    public RegExpBuilder lowerCaseLetter() {
        exactly(1);
        _from = "a-z";
        return this;
    }

    public RegExpBuilder lowerCaseLetters() {
        _from = "a-z";
        return this;
    }

    public RegExpBuilder upperCaseLetter() {
        exactly(1);
        _from = "A-Z";
        return this;
    }

    public RegExpBuilder upperCaseLetters() {
        _from = "A-Z";
        return this;
    }

    public RegExpBuilder append(RegExpBuilder r) {
        exactly(1);
        _like = r.getLiteral();
        return this;
    }

    public RegExpBuilder optional(RegExpBuilder r) {
        max(1);
        _like = r.getLiteral();
        return this;
    }

    private String _escapeInsideCharacterClass(String s) {
        return _escapeSpecialCharacters(s, _specialCharactersInsideCharacterClass);
    }

    private String _escapeOutsideCharacterClass(String s) {
        return _escapeSpecialCharacters(s, _specialCharactersOutsideCharacterClass);
    }

    private String _escapeSpecialCharacters(String s, HashSet<Character> specialCharacters) {
        _escapedString = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char character = s.charAt(i);
            if (specialCharacters.contains(character)) {
                _escapedString.append("\\" + character);
            } else {
                _escapedString.append(character);
            }
        }
        return _escapedString.toString();
    }
}
