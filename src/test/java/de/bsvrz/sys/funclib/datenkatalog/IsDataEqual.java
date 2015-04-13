/*
 * Funktionsbibliothek Datenkatalog
 * Copyright (c) 2014, Falko Schumann <http://www.muspellheim.de>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.bsvrz.sys.funclib.datenkatalog;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.*;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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
                result = expected.asTextValue().getText().equals(actual.asTextValue().getText());
            } else if (type instanceof TimeAttributeType) {
                result = expected.asTimeValue().getMillis() == actual.asTimeValue().getMillis();
            } else if (type instanceof IntegerAttributeType) {
                result = expected.asUnscaledValue().longValue() == actual.asUnscaledValue().longValue();
            } else if (type instanceof DoubleAttributeType) {
                result = expected.asScaledValue().doubleValue() == actual.asScaledValue().doubleValue();
            } else if (type instanceof ReferenceAttributeType) {
                final SystemObject expectedObject = expected.asReferenceValue().getSystemObject();
                final SystemObject actuaObject = actual.asReferenceValue().getSystemObject();
                result = (expectedObject == actuaObject) || (expectedObject.equals(actuaObject));
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
