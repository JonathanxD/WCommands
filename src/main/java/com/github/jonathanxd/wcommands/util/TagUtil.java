/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.wcommands.util;

import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.util.reflection.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TagUtil {

    public static boolean allOpenAllClose(String text) {
        return countQuote(text);
    }

    public static boolean allOpenAllClose(String text, char[] tags) {
        return countTag(text, tags);
    }

    public static boolean allTagsOk(String text, char[] tags) {
        return process(text, tags).tags.isEmpty();
    }

    public static boolean countQuote(String text) {

        int singleQuotes = 0;
        int doubleQuotes = 0;

        boolean lastCharDefined = false;
        char lastChar = ' ';

        for (int x = 0; x < text.length(); ++x) {
            char currentChar = text.charAt(x);

            if (!lastCharDefined) {
                lastChar = currentChar;
                lastCharDefined = true;
            }

            if (lastChar != '\\') {
                if (currentChar == '\'') {
                    ++singleQuotes;
                } else if (currentChar == '"') {
                    ++doubleQuotes;
                }
            }
        }

        double finalSingle = singleQuotes / 2;
        double finalDouble = doubleQuotes / 2;

        if (finalSingle != 0) {
            return finalSingle == Math.floor(finalSingle);
        }

        if (finalDouble != 0) {
            return finalDouble == Math.floor(finalDouble);
        }

        return false;
    }

    public static boolean countTag(String text, char[] tag) {

        int[] tagsCount = new int[tag.length];

        Arrays.fill(tagsCount, 0);

        boolean lastCharDefined = false;
        char lastChar = ' ';

        for (int x = 0; x < text.length(); ++x) {
            char currentChar = text.charAt(x);

            if (!lastCharDefined) {
                lastChar = currentChar;
            }

            if (!lastCharDefined || lastChar != '\\') {
                int found = find(tag, currentChar);

                if (found >= 0) {
                    if (tag[found] != tag[found + 1]) {
                        tagsCount[found] = tagsCount[found] + 1;
                    } else {
                        if (tagsCount[found] > tagsCount[found + 1]) {
                            tagsCount[found + 1] = tagsCount[found + 1] + 1;
                        } else {
                            tagsCount[found] = tagsCount[found] + 1;
                        }
                    }
                }
            }

            if (!lastCharDefined)
                lastCharDefined = true;

            lastChar = currentChar;
        }

        try {
            for (int x = 0; x < tagsCount.length; ++x) {
                if (tagsCount[x] != tagsCount[++x]) {
                    return false;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("You need to specify open and close tags (like char[]{'[', ']'})!", e);
        }

        return true;
    }

    private static Tag build(char[] tags, int index, State state) {

        if (state.toBoolean()) {
            return new Tag(tags[0], tags[1], index, -1, null);
        } else {
            return new Tag(tags[0], tags[1], -1, index, null);
        }
    }

    public static TagsData process(String str, char[] tags) {
        char[][] openClose = new char[tags.length / 2][2];


        for (int tagPos = 0, x = 0; x < openClose.length; ++x) {
            openClose[x][0] = tags[tagPos];
            openClose[x][1] = tags[++tagPos];
            ++tagPos;
        }

        TagsData tagsData = new TagsData();

        char[] charArray = str.toCharArray();

        char lastChar = ' ';
        boolean lastCharDefined = false;

        for (int x = 0; x < charArray.length; ++x) {
            char current = charArray[x];

            if (!lastCharDefined || lastChar != '\\') {
                Data find = findTag(openClose, current, tagsData);

                if (find != null) {
                    if (find.state == State.CLOSE) {
                        tagsData.up(build(openClose[find.position], x, find.state), charArray);
                    } else {
                        tagsData.up(build(openClose[find.position], x, find.state), charArray);
                    }
                }
            }

            lastCharDefined = true;
            lastChar = current;
        }

        return tagsData;
    }

    private static String select(char[] chars, int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int x = start; x < end; ++x) {
            stringBuilder.append(chars[x]);
        }

        return stringBuilder.toString();
    }

    private static Data findTag(char[][] tags, char c, TagsData tagsData) {

        for (int x = 0; x < tags.length; ++x) {
            char[] tag = tags[x];

            if (tag[0] == c) {
                if (tag[0] == tag[1]) {
                    State state = tagsData.lastStateFor(tag[0], tag[1]);
                    if (state == State.OPEN) {
                        return new Data(x, State.CLOSE);
                    } else {
                        return new Data(x, State.OPEN);
                    }
                } else {
                    return new Data(x, State.OPEN);
                }

            } else if (tag[1] == c) {
                return new Data(x, State.CLOSE);
            }
        }

        return null;

    }

    private static int find(char[] chars, char c) {
        char current;
        for (int x = 0; x < chars.length; ++x) {
            current = chars[x];
            if (current == c)
                return x;
        }

        return -1;
    }

    public static List<String> getSplited(String text, TagsData tagsData) {
        return getSplited(text, tagsData, ' ', false);
    }

    public static List<String> getSplited(String text, TagsData tagsData, char alternative, boolean useAlternative) {
        char[] chars = text.toCharArray();

        List<String> strList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        int skip = -1;

        for (int x = 0; x < chars.length; ++x) {
            if (skip > -1) {
                --skip;
                continue;
            }

            Tag t = tagsData.findForOpenIndex(x);

            if (t != null) {
                if (sb.length() > 0) {
                    strList.add(sb.toString());
                    sb.setLength(0);
                }

                strList.add(t.getTextBetween());

                skip = t.getCloseIndex() - 1;
            } else if (useAlternative && chars[x] == alternative) {
                strList.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(chars[x]);
            }

        }

        if (sb.length() > 0) {
            strList.add(sb.toString());
        }

        return strList;
    }

    public enum State {
        OPEN, CLOSE;

        private boolean toBoolean() {
            return this == OPEN;
        }
    }

    private static final class Data {
        private final int position;
        private final State state;

        private Data(int position, State state) {
            this.position = position;
            this.state = state;
        }
    }

    public static class Tag extends Element {

        private final char openTag;
        private final char closeTag;
        private final int openIndex;
        private final int closeIndex;
        private final String textBetween;

        private Tag(char openTag, char closeTag, int openIndex, int closeIndex, String textBetween) {
            super(textBetween);
            this.openTag = openTag;
            this.closeTag = closeTag;
            this.openIndex = openIndex;
            this.closeIndex = closeIndex;
            this.textBetween = textBetween;
        }

        public char getOpenTag() {
            return openTag;
        }

        public char getCloseTag() {
            return closeTag;
        }

        public int getOpenIndex() {
            return openIndex;
        }

        public int getCloseIndex() {
            return closeIndex;
        }

        private int getId() {
            return Objects.hash(openTag, closeTag);
        }

        public String getTextBetween() {
            return textBetween;
        }

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }

    public static final class TagsData {


        List<Tag> closedTags = new ArrayList<>();
        Deque<Tag> tags = new LinkedList<>();

        private void up(Tag tag, char[] chars) {

            if (tags.isEmpty() || tags.getLast().getId() != tag.getId()) {
                tags.addLast(tag);
            } else {
                Tag last = tags.pollLast();
                String text = select(chars, last.getOpenIndex() + 1, tag.getCloseIndex());
                closedTags.add(new Tag(last.getOpenTag(), last.getCloseTag(), last.getOpenIndex(), tag.getCloseIndex(), text));
            }
        }

        public Tag findForOpenIndex(int index) {
            for (Tag tag : closedTags) {
                if (tag.getOpenIndex() == index) {
                    return tag;
                }
            }
            return null;
        }

        public Tag findForOpen(char open) {
            for (Tag tag : closedTags) {
                if (tag.getOpenTag() == open) {
                    return tag;
                }
            }
            return null;
        }

        public Tag findForClose(char close) {
            for (Tag tag : closedTags) {
                if (tag.getCloseTag() == close) {
                    return tag;
                }
            }
            return null;
        }


        public Node<Tag, State> findForUnknown(char unknown) {
            for (Tag tag : closedTags) {


                boolean st = tag.getOpenTag() == unknown;

                if (st || tag.getCloseTag() == unknown) {
                    return new Node<Tag, State>(tag, st ? State.OPEN : State.CLOSE);
                }
            }
            return null;
        }

        public List<Tag> getClosedTags() {
            return closedTags;
        }

        public Deque<Tag> getTags() {
            return tags;
        }

        private State lastStateFor(char open, char close) {
            Tag tag = new Tag(open, close, -1, -1, null);

            Iterator<Tag> desc = tags.descendingIterator();

            while (desc.hasNext()) {
                Tag tagInDeque = desc.next();
                if (tagInDeque.getId() == tag.getId()) {
                    return tagInDeque.getCloseIndex() <= -1 ? State.OPEN : State.CLOSE;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return "[notClosed=" + tags + ", closed=" + closedTags + "]";
        }
    }

    private static class Element {
        private final String data;

        private Element(String data) {
            this.data = data;
        }
    }

}
