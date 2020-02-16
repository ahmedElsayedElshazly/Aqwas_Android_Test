package com.aqwas.androidtest.model;

import java.io.Serializable;

public class NotesModel implements Serializable {

    /**
     * _id : 5e2de3d1a8eec80010c16797
     * Title : string
     * body : string
     * createdAt : 2020-01-26T19:09:05.930Z
     * updatedAt : 2020-01-26T20:25:09.361Z
     * __v : 0
     * owner : {"confirmed":true,"blocked":false,"_id":"5e2de282a8eec80010c16796","username":"ghazi","email":"ghazi@test1.com","provider":"local","createdAt":"2020-01-26T19:03:30.844Z","updatedAt":"2020-01-26T19:03:30.865Z","__v":0,"role":"5e2dd1a8eb21020069cbff15","id":"5e2de282a8eec80010c16796"}
     * id : 5e2de3d1a8eec80010c16797
     */

    private String _id;
    private String Title;
    private String body;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private OwnerBean owner;
    private String id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public OwnerBean getOwner() {
        return owner;
    }

    public void setOwner(OwnerBean owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class OwnerBean implements Serializable{
        /**
         * confirmed : true
         * blocked : false
         * _id : 5e2de282a8eec80010c16796
         * username : ghazi
         * email : ghazi@test1.com
         * provider : local
         * createdAt : 2020-01-26T19:03:30.844Z
         * updatedAt : 2020-01-26T19:03:30.865Z
         * __v : 0
         * role : 5e2dd1a8eb21020069cbff15
         * id : 5e2de282a8eec80010c16796
         */

        private boolean confirmed;
        private boolean blocked;
        private String _id;
        private String username;
        private String email;
        private String provider;
        private String createdAt;
        private String updatedAt;
        private int __v;
        private String role;
        private String id;

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

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
