package com.aqwas.androidtest.model;

import java.util.List;

public class UserModel {

    /**
     * jwt : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlNDMwMDgzNzE2ZDNiMDAxMTU4OTY4ZiIsImlhdCI6MTU4MTQ0OTM0NywiZXhwIjoxNTg0MDQxMzQ3fQ.1fA199PxoW4m6vcIuJdTayq6HvEYGi7S1goGAWnLH70
     * user : {"confirmed":true,"blocked":false,"_id":"5e430083716d3b001158968f","username":"ahmed","email":"ahmedelsayed_1993@yahoo.com","id":"5e2dd1a8eb21020069cbff15"},"id":"5e430083716d3b001158968f"}
     */

    private String jwt;
    private UserBean user;
    private List<MessageBean> message;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class UserBean {
        /**
         * confirmed : true
         * blocked : false
         * _id : 5e430083716d3b001158968f
         * username : ahmed
         * email : ahmedelsayed_1993@yahoo.com
         * id : 5e430083716d3b001158968f
         */

        private boolean confirmed;
        private boolean blocked;
        private String _id;
        private String username;
        private String email;
        private String id;

        public UserBean(String username, String email, String id) {
            this.username = username;
            this.email = email;
            this.id = id;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }

    public static class MessageBean {
        private List<MessagesBean> messages;

        public List<MessagesBean> getMessages() {
            return messages;
        }

        public void setMessages(List<MessagesBean> messages) {
            this.messages = messages;
        }

        public static class MessagesBean {
            /**
             * id : Auth.form.error.username.taken
             * message : Username already taken
             */

            private String id;
            private String message;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
