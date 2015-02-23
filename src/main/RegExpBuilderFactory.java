public class RegExpBuilderFactory {

    public RegExpBuilder ignoreCase() {
        return new RegExpBuilder().ignoreCase();
    }

    public RegExpBuilder multiLine() {
        return new RegExpBuilder().multiLine();
    }

    public RegExpBuilder startOfInput() {
        return new RegExpBuilder().startOfInput();
    }

    public RegExpBuilder startOfLine() {
        return new RegExpBuilder().startOfLine();
    }

    public RegExpBuilder endOfInput() {
        return new RegExpBuilder().endOfInput();
    }

    public RegExpBuilder endOfLine() {
        return new RegExpBuilder().endOfLine();
    }

    public RegExpBuilder either(RegExpBuilder r) {
        return new RegExpBuilder().either(r);
    }

    public RegExpBuilder either(String s) {
        return new RegExpBuilder().either(s);
    }

    public RegExpBuilder exactly(int n) {
        return new RegExpBuilder().exactly(n);
    }

    public RegExpBuilder min(int n) {
        return new RegExpBuilder().min(n);
    }

    public RegExpBuilder max(int n) {
        return new RegExpBuilder().max(n);
    }

    public RegExpBuilder ahead(RegExpBuilder r) {
        return new RegExpBuilder().ahead(r);
    }

    public RegExpBuilder notAhead(RegExpBuilder r) {
        return new RegExpBuilder().notAhead(r);
    }

    public RegExpBuilder then(String s) {
        return new RegExpBuilder().then(s);
    }

    public RegExpBuilder find(String s) {
        return new RegExpBuilder().find(s);
    }

    public RegExpBuilder some(char[] s) {
        return new RegExpBuilder().some(s);
    }

    public RegExpBuilder maybeSome(char[] s) {
        return new RegExpBuilder().maybeSome(s);
    }

    public RegExpBuilder maybe(String s) {
        return new RegExpBuilder().maybe(s);
    }

    public RegExpBuilder something() {
        return new RegExpBuilder().something();
    }

    public RegExpBuilder somethingBut(String s) {
        return new RegExpBuilder().somethingBut(s);
    }

    public RegExpBuilder anything() {
        return new RegExpBuilder().anything();
    }

    public RegExpBuilder anythingBut(String s) {
        return new RegExpBuilder().anythingBut(s);
    }

    public RegExpBuilder any() {
        return new RegExpBuilder().any();
    }

    public RegExpBuilder lineBreak() {
        return new RegExpBuilder().lineBreak();
    }

    public RegExpBuilder whitespace() {
        return new RegExpBuilder().whitespace();
    }

    public RegExpBuilder notWhitespace() {
        return new RegExpBuilder().notWhitespace();
    }

    public RegExpBuilder tab() {
        return new RegExpBuilder().tab();
    }

    public RegExpBuilder digit() {
        return new RegExpBuilder().digit();
    }

    public RegExpBuilder notDigit() {
        return new RegExpBuilder().notDigit();
    }

    public RegExpBuilder letter() {
        return new RegExpBuilder().letter();
    }

    public RegExpBuilder notLetter() {
        return new RegExpBuilder().notLetter();
    }

    public RegExpBuilder lowerCaseLetter() {
        return new RegExpBuilder().lowerCaseLetter();
    }

    public RegExpBuilder upperCaseLetter() {
        return new RegExpBuilder().upperCaseLetter();
    }

    public RegExpBuilder append(RegExpBuilder r) {
        return new RegExpBuilder().append(r);
    }

    public RegExpBuilder optional(RegExpBuilder r) {
        return new RegExpBuilder().optional(r);
    }
}
