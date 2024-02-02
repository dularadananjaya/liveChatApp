package lk.ijse.livechatRoom.regExPatterns;

import lombok.Getter;

import java.util.regex.Pattern;

public class RegExpatterns {
    @Getter
    public static final Pattern validName = Pattern.compile("\\b[A-Z][a-z]*( [A-Z][a-z]*)*\\b");
    @Getter
    public static final Pattern validDescription = Pattern.compile("\\b[a-z.A-Z]+(?: [a-zA-Z]+)*\\b");
    @Getter
    public static final Pattern validPassword = Pattern.compile("(.*?[0-9]){4,}");
}
