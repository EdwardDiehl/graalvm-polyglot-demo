package org.example;

import java.util.Set;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

class Main {
    public static void main(String[] args) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        Value array = polyglot.eval("js", "(%s)(10, 50)".formatted(embeddedJavaScript));
        System.out.println("(JS) Array: %s".formatted(array));

        Value doubledArray = polyglot.eval("ruby", "%s.call([%s])".formatted(embeddedRuby, array.as(Set.class)));
        System.out.println("(Ruby) Doubled array: %s".formatted(doubledArray));

        int fifthElement = doubledArray.getArrayElement(4).asInt();
        System.out.println("Fifth element of the doubled array: %s".formatted(fifthElement));
    }

    private final static String embeddedJavaScript =
            """
                    (length, max) =>
                      [...new Array(length)].map(() => Math.round(Math.random() * max))
                    """;

    private final static String embeddedRuby =
            """
                    -> (numbers) { numbers.map { |number| number * 2 } }
                    """;
}
