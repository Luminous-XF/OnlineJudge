package cn.edu.nsu.onlinejudge.common;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * 敏感词过滤
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符号
    private static String REPLACEMENT = "***";

    // 关键词结束的标识
    private boolean isKeywordEnd = false;

    // 根节点
    private TrieNode rootNode = new TrieNode();


    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }


        } catch (IOException e) {
            logger.error("加载敏感词信息失败: " + e.getMessage());
        }
    }


    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 当前Node指向下一层Node
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
       if (StringUtils.isBlank(text)) return null;

       TrieNode tempNode = rootNode;
       int begin = 0, position = 0;
       StringBuilder res = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    res.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode =  tempNode.getSubNode(c);
            if (tempNode == null) {
                res.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                res.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }

        res.append(text.substring(begin));

        return res.toString();
    }


    /**
     * 前缀树
     */
    private class TrieNode {

        private Map<Character, TrieNode> subNodes = new HashMap<>();


        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        /**
         * 获取子节点
         *
         * @param c
         * @return
         */
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        /**
         * 添加子节点
         *
         * @return
         */
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }

    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlpha(c) && (c < 0x2E80 || c > 0x9FFF);
    }
}
