package xyz.cofe.delphi.tsys.tm.json.jakson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlatStr {
    String value();
}
