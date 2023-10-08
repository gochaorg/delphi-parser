package xyz.cofe.delphi.tsys.json.jakson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlatStr {
    String value();
}
