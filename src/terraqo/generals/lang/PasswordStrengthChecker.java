/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.util.Set;

/**
 * 密码强度校验器
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public final class PasswordStrengthChecker {
    /**
     * 最小长度
     */
    private int minLength = 6;
    /**
     * 最大长度
     */
    private int maxLength = 20;
    /**
     * 允许大写字符
     */
    private boolean includeUpperLetter = true;
    /**
     * 允许小写字符
     */
    private boolean includeLowerLetter = true;
    /**
     * 允许顺序字符
     */
    private boolean includeDigitCharacter = true;
    /**
     * 允许特殊字符
     */
    private boolean includeSpecialCharacter = true;
    /**
     * 允许顺序字符
     */
    private boolean includeSequenceCharacter = false;

    public Result check(String password, Set<String> history) {
        char[] chars = password.toCharArray();
        boolean passLength = chars.length >= minLength && chars.length <= maxLength;
        boolean passHistory = history == null || !history.contains(password);
        boolean passDigit = !includeDigitCharacter, passSpecial = !includeSpecialCharacter;
        boolean passUpperLetter = !includeUpperLetter, passLowerLetter = !includeLowerLetter;
        char last = '\0';

        int sequence = 0;
        for (char c : chars) {
            if (last != '\0' && Math.abs(c - last) == 1) {
                sequence++;
            }
            last = c;
            if (!passDigit && Character.isDigit(c)) {
                if (includeDigitCharacter) {
                    passDigit = true;
                }
            } else if ((!passUpperLetter || !passLowerLetter) && Character.isLetter(c)) {
                if (!passUpperLetter && Character.isUpperCase(c)) {
                    if (includeUpperLetter) {
                        passUpperLetter = true;
                    }
                }
                if (!passLowerLetter && Character.isLowerCase(c)) {
                    if (includeLowerLetter) {
                        passLowerLetter = true;
                    }
                }
            } else if (!passSpecial && (c >= 33 && c <= 47 || c >= 58 && c <= 64 || c >= 91 && c <= 96 || c >= 123 && c <= 126)) {
                if (includeSpecialCharacter) {
                    passSpecial = true;
                }
            }
        }
        boolean passSequence = Math.floorDiv(sequence * 100, chars.length) < 60;
        return new Result(passLength, passUpperLetter && passLowerLetter, passDigit, passSpecial, passSequence, passHistory);
    }

    public int getMinLength() {
        return minLength;
    }

    public PasswordStrengthChecker setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public PasswordStrengthChecker setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public boolean isIncludeUpperLetter() {
        return includeUpperLetter;
    }

    public PasswordStrengthChecker setIncludeUpperLetter(boolean includeUpperLetter) {
        this.includeUpperLetter = includeUpperLetter;
        return this;
    }

    public boolean isIncludeLowerLetter() {
        return includeLowerLetter;
    }

    public PasswordStrengthChecker setIncludeLowerLetter(boolean includeLowerLetter) {
        this.includeLowerLetter = includeLowerLetter;
        return this;
    }

    public boolean isIncludeDigitCharacter() {
        return includeDigitCharacter;
    }

    public PasswordStrengthChecker setIncludeDigitCharacter(boolean includeDigitCharacter) {
        this.includeDigitCharacter = includeDigitCharacter;
        return this;
    }

    public boolean isIncludeSpecialCharacter() {
        return includeSpecialCharacter;
    }

    public PasswordStrengthChecker setIncludeSpecialCharacter(boolean includeSpecialCharacter) {
        this.includeSpecialCharacter = includeSpecialCharacter;
        return this;
    }

    public boolean isIncludeSequenceCharacter() {
        return includeSequenceCharacter;
    }

    public PasswordStrengthChecker setIncludeSequenceCharacter(boolean includeSequenceCharacter) {
        this.includeSequenceCharacter = includeSequenceCharacter;
        return this;
    }

    @Override
    public String toString() {
        return "PasswordStrengthChecker{" +
                "minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", includeUpperLetter=" + includeUpperLetter +
                ", includeLowerLetter=" + includeLowerLetter +
                ", includeDigitCharacter=" + includeDigitCharacter +
                ", includeSpecialCharacter=" + includeSpecialCharacter +
                ", includeSequenceCharacter=" + includeSequenceCharacter +
                '}';
    }

    public static final class Result {
        /**
         * 是否符合长度
         */
        private final boolean passLength;
        /**
         * 是否符合字符
         */
        private final boolean passLetter;
        /**
         * 是否符合数字
         */
        private final boolean passDigit;
        /**
         * 是否符合数字
         */
        private final boolean passSpecial;
        /**
         * 是否符合序列
         */
        private final boolean passSequence;
        /**
         * 是否符合历史
         */
        private final boolean passHistory;

        private Result(boolean passLength, boolean passLetter, boolean passDigit, boolean passSpecial, boolean passSequence, boolean passHistory) {
            this.passLength = passLength;
            this.passLetter = passLetter;
            this.passDigit = passDigit;
            this.passSpecial = passSpecial;
            this.passSequence = passSequence;
            this.passHistory = passHistory;
        }

        public boolean isPassLength() {
            return passLength;
        }

        public boolean isPassLetter() {
            return passLetter;
        }

        public boolean isPassDigit() {
            return passDigit;
        }

        public boolean isPassSpecial() {
            return passSpecial;
        }

        public boolean isPassSequence() {
            return passSequence;
        }

        public boolean isPassHistory() {
            return passHistory;
        }
    }
}
