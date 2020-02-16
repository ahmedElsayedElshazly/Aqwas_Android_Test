package com.aqwas.androidtest.model;

import java.io.Serializable;
import java.util.List;

public class TasksModel implements Serializable {
    /**
     * id : string
     * Title : string
     * Description : string
     * DueDate : string
     * isDone : false
     * Priority : None
     * owner : {"id":"string","username":"string","email":"string","provider":"string","password":"string","resetPasswordToken":"string","confirmed":true,"blocked":true,"role":"string","notes":["string"],"tasks":["string"]}
     */

    private String id;
    private String Title;
    private String Description;
    private String DueDate;
    private boolean isDone;
    private String Priority;
    private OwnerBean owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String DueDate) {
        this.DueDate = DueDate;
    }

    public boolean isIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String Priority) {
        this.Priority = Priority;
    }

    public OwnerBean getOwner() {
        return owner;
    }

    public void setOwner(OwnerBean owner) {
        this.owner = owner;
    }

    public static class OwnerBean implements Serializable{
        /**
         * id : string
         * username : string
         * email : string
         * provider : string
         * password : string
         * resetPasswordToken : string
         * confirmed : true
         * blocked : true
         * role : string
         * notes : ["string"]
         * tasks : ["string"]
         */

        private String id;
        private String username;
        private String email;
        private String provider;
        private String password;
        private String resetPasswordToken;
        private boolean confirmed;
        private boolean blocked;
        private String role;
        private List<String> notes;
        private List<String> tasks;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getResetPasswordToken() {
            return resetPasswordToken;
        }

        public void setResetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<String> getNotes() {
            return notes;
        }

        public void setNotes(List<String> notes) {
            this.notes = notes;
        }

        public List<String> getTasks() {
            return tasks;
        }

        public void setTasks(List<String> tasks) {
            this.tasks = tasks;
        }
    }


}
