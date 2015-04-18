/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.bsvrz.sys.funclib.datenkatalog;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.*;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.Objects;

public final class IsDataEqual extends TypeSafeMatcher<Data> {

    private final Data expectedValue;

    public IsDataEqual(final Data equalArg) {
        expectedValue = equalArg;
    }

    @Factory
    public static Matcher<Data> dataEqualsTo(final Data operand) {
        return new IsDataEqual(operand);
    }

    @Override
    protected boolean matchesSafely(final Data item) {
        boolean result = true;
        for (Data expected : expectedValue) {
            final Data actual = item.getItem(expected.getName());

            final AttributeType type = expected.getAttributeType();
            if (type instanceof StringAttributeType) {
                if (expected.isArray())
                    result = Arrays.equals(expected.asTextArray().getTextArray(), actual.asTextArray().getTextArray());
                else
                    result = expected.asTextValue().getText().equals(actual.asTextValue().getText());
            } else if (type instanceof TimeAttributeType) {
                if (expected.isArray())
                    result = Arrays.equals(expected.asTimeArray().getMillisArray(), actual.asTimeArray().getMillisArray());
                else
                    result = expected.asTimeValue().getMillis() == actual.asTimeValue().getMillis();
            } else if (type instanceof IntegerAttributeType) {
                if (expected.isArray())
                    result = Arrays.equals(expected.asUnscaledArray().getLongArray(), actual.asUnscaledArray().getLongArray());
                else
                    result = expected.asUnscaledValue().longValue() == actual.asUnscaledValue().longValue();
            } else if (type instanceof DoubleAttributeType) {
                if (expected.isArray())
                    result = Arrays.equals(expected.asScaledArray().getDoubleArray(), actual.asScaledArray().getDoubleArray());
                else
                    result = expected.asScaledValue().doubleValue() == actual.asScaledValue().doubleValue();
            } else if (type instanceof ReferenceAttributeType) {
                if (expected.isArray())
                    result = Arrays.equals(expected.asReferenceArray().getSystemObjectArray(), actual.asReferenceArray().getSystemObjectArray());
                else
                    result = Objects.equals(expected.asReferenceValue().getSystemObject(), actual.asReferenceValue().getSystemObject());
            } else if (type instanceof AttributeListDefinition) {
                result = dataEqualsTo(expected).matches(actual);
            } else {
                throw new IllegalStateException("unreachable code");
            }

            if (!result)
                break;
        }
        return result;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendValue(expectedValue);
    }

}
