package com.supersection.trading.utility;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneNumberUtils {

    public static String formatPhoneNumber(String rawNumber, String region) throws NumberParseException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber number = phoneUtil.parse(rawNumber, region);
        if (phoneUtil.isValidNumber(number)) {
            return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

}