package cn.edu.nsu.onlinejudge.common.vo;

import cn.edu.nsu.onlinejudge.entity.Message;
import cn.edu.nsu.onlinejudge.entity.User;

import java.util.List;

public class LettersVO {

    private String targetUserNickname;

    private List<Letter> letters;

    public static class Letter {
        Message letter;

        private String userNickname;
        private String headerUrl;

        private int userId;

        public Message getLetter() {
            return letter;
        }

        public void setLetter(Message letter) {
            this.letter = letter;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

        public String getHeaderUrl() {
            return headerUrl;
        }

        public void setHeaderUrl(String headerUrl) {
            this.headerUrl = headerUrl;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "Letter{" +
                    "letter=" + letter +
                    ", userNickname='" + userNickname + '\'' +
                    ", headerUrl='" + headerUrl + '\'' +
                    ", userId=" + userId +
                    '}';
        }
    }


    public String getTargetUserNickname() {
        return targetUserNickname;
    }

    public void setTargetUserNickname(String targetUserNickname) {
        this.targetUserNickname = targetUserNickname;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    @Override
    public String toString() {
        return "LettersVO{" +
                "targetUserNickname='" + targetUserNickname + '\'' +
                ", letters=" + letters +
                '}';
    }
}
