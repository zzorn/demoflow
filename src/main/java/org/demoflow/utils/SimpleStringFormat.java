package org.demoflow.utils;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Simple Format instance that just returns the edited string unchanged.
 */
public class SimpleStringFormat extends Format {
    @Override public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return toAppendTo.append(obj.toString());
    }

    @Override public Object parseObject(String source, ParsePosition pos) {
        pos.setIndex(source.length());
        return source.toString();
    }
}
