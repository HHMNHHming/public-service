package com.gwm.avp.util;

public class Common {
    public static class User {
        public enum Rs {
            DISABLE(0), ENABLE(1);
            private int i;

            Rs(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class Resource {
        public enum Type {
            MENU(0), RESOURCE(1);
            private int i;

            Type(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum isParent {
            CHILD(0), PARENT(1);
            private int i;

            isParent(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public static final String RESOURCE_ROLES_MAP = "AUTH:RESOURCE_ROLES_MAP";
        public static final String RESOURCE_INIT_FLAG= "AUTH:RESOURCE_INIT_FLAG";
        public enum initFlag{
            IDLE(0),INITIALIZING(1);
            private  int i;
            initFlag(int i){
                this.i = i;
            }
            public int toValue() {
                return i;
            }
        }
    }

    public static class Task {
        public enum Status {
            CREATED(0), CLOSEBETA(1), GRAYSCALEBETA(2), TESTED(3), RELEASE(6), INVALID(7);
            private int i;

            private Status(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum ReleaseStatus {
            ALL(0), PART(1);
            private int i;

            ReleaseStatus(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum SwitchStatus {
            DISABLEALL(0), ENABLEALL(1), DISABLEPART(2), ENABLEPART(3);
            private int i;

            SwitchStatus(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum CloseBetaStatus {
            STOP(0), RUN(1), SUCCESS(2), FAILL(3);
            private int i;

            private CloseBetaStatus(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum GrayscaleBetaStatus {
            STOP(0), RUN(1), SUCCESS(2), FAILL(3);
            private int i;

            private GrayscaleBetaStatus(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum GrayscaleBetaAttrStatus {
            NONE(0), SUCCESS(1), FAILL(2);
            private int i;

            private GrayscaleBetaAttrStatus(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class Strategy {
        public enum CollectMode {
            FEEDBACK(2), SHADOW(3);
            private int i;

            CollectMode(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class TaskRelease {
        public enum Status {
            DISABLE(0), ENABLE(1);
            private int i;

            private Status(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class TaskSwitch {
        public enum Status {
            DISABLE(0), ENABLE(1);
            private int i;

            private Status(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    /**
     * 下发列表
     */
    public static class SendOut {
        public enum Status {
            CREATED(0), INIT(1), RUNNING(2), END(3), ERROR(-1);
            private int i;

            private Status(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class SendDetails {
        public enum Status {
            INIT(0), SUCCESS(1), ERROR(-1), CREATEFAILL(-2);
            private int i;

            Status(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }

        public enum Exc {
            DEL(0), ADD(1);
            private int i;

            Exc(int i) {
                this.i = i;
            }

            public int toValue() {
                return i;
            }
        }
    }

    public static class PostBackData {
        public enum Status {
            DELETED("DELETED"), INIT("INIT"), ANALYZING("ANALYZING"), ANALYZED("ANALYZED"), DECODING("DECODING"), DECODED("DECODED"),DONE("DONE"),ERROR("ERROR");
            private String i;

            Status(String i) {
                this.i = i;
            }

            public String toValue() {
                return i;
            }
        }

        public enum TaskType {
            NORMAL(0), CLOSEBETA(1), GRAYTEST(2);
            private int i;

            TaskType(int type) {
                i = type;
            }

            public int toValue() {
                return i;
            }
        }

        public enum faultCode {
            FEEDBACK("202"), SHADOW("301"), DEBUG("363"), PERIODIC("periodic_data_cache");
            private String i;

            faultCode(String i) {
                this.i = i;
            }

            public String toValue() {
                return i;
            }
        }
    }

    public static class VehicleSend{
        public enum Status{
            INIT(0),GOTVINS(1),RESETEDVIN(2),SUCCESS(3),ERROR(-1);
            private int status;
            private Status(int status){
                this.status = status;
            }
            public int toValue() {
                return status;
            }
        }
    }
    public static class VehicleExp{
        public enum Status{
            INIT(0),WAITING(1),RUNNING(2),SUCCESS(3),ERROR(-1);
            private int status;
            private Status(int status){
                this.status = status;
            }
            public int toValue() {
                return status;
            }
        }
    }
}
